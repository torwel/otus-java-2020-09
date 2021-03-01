package ru.otus.torwel.h13.appcontainer;

import org.reflections.Reflections;
import ru.otus.torwel.h13.appcontainer.api.AppComponent;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainer;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
        for (Class<?> initialConfig : initialConfigs) {
            processConfig(initialConfig);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> markedMethods = getComponentMarkedMethods(configClass);
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
                throw new ProcessConfigReflectiveOperationException(e.getMessage(), e);
            }
        }
    }

    // Составляет список методов, аннотированных @AppComponent
    private List<Method> getComponentMarkedMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .collect(Collectors.toList());
    }

    private Object createComponent(Object configInstance, Method method) throws IllegalAccessException, InvocationTargetException {
        Class<?>[] types = method.getParameterTypes();
        Object[] inputParams = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            inputParams[i] = getAppComponent(types[i]);
        }
        return method.invoke(configInstance, inputParams);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
