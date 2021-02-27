package ru.otus.torwel.h13.appcontainer;

import org.reflections.Reflections;
import ru.otus.torwel.h13.appcontainer.api.AppComponent;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainer;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        List<Class<?>> initialConfigs = Arrays.asList(initialConfigClass);
        processConfigs(initialConfigs);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        List<Class<?>> initialConfigs = new ArrayList<>(configClasses);
        processConfigs(initialConfigs);
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object obj : appComponents) {
            if (componentClass.isAssignableFrom(obj.getClass())) {
                return (C)obj;
            }
        }
        throw new IllegalStateException(String.format("Does not contained the requested component %s", componentClass.getName()));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object obj = appComponentsByName.get(componentName);
        if (obj != null) {
            return (C)obj;
        }
        throw new IllegalStateException(String.format("Does not contained the requested component %s", componentName));
    }

    private void processConfigs(List<Class<?>> initialConfigs) {
        initialConfigs.sort(Comparator.comparingInt(o -> o.getAnnotation(AppComponentsContainerConfig.class).order()));
        initialConfigs.forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        ArrayList<Method> markedMethods = getComponentMarkedMethods(configClass);
        if (!markedMethods.isEmpty()) {
            markedMethods.sort(Comparator.comparingInt(o -> o.getAnnotation(AppComponent.class).order()));
            try {
                Object configInstance = configClass.cast(configClass.getConstructor().newInstance());
                for (var method : markedMethods) {
                    Object component = createComponent(configInstance, method);
                    appComponents.add(component);
                    appComponentsByName.put(method.getName(), component);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    }

    // Составляет список методов, аннотированных @AppComponent
    private ArrayList<Method> getComponentMarkedMethods(Class<?> configClass) {
        ArrayList<Method> methods = new ArrayList<>();
        for (var method : configClass.getMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private Object createComponent(Object configInstance, Method method) throws IllegalAccessException, InvocationTargetException {
        Parameter[] params = method.getParameters();
        Object[] inputParams = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            Class<?> type = params[i].getType();
            inputParams[i] = getAppComponent(type);
        }
        return method.invoke(configInstance, inputParams);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
