package ru.otus.torwel.myjson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class MyGson {

    public String toJson(AnyObject object) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Field field : object.getClass().getDeclaredFields()) {
            Class<?> typeField = field.getType();
            try {
                if (typeField.isArray()) {
                    convertArrayData(sb, object, field);
                } else if (Collection.class.isAssignableFrom(typeField)) {
                    convertListData(sb, object, field);
                } else if (typeField.equals(char.class) ||
                        typeField.equals(String.class) ||
                        typeField.equals(Character.class)) {
                    convertTextData(sb, object, field);
                } else {
                    convertNumberData(sb, object, field);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.replace(sb.length()-1, sb.length(), "}");
        return sb.toString();
    }

    private void convertNumberData(StringBuilder sb, Object object, Field field) throws IllegalAccessException {
        convertSimpleData(sb, object, field, false);
    }

    private void convertTextData(StringBuilder sb, Object object, Field field) throws IllegalAccessException {
        convertSimpleData(sb, object, field, true);
    }

    private void convertSimpleData(StringBuilder sb, Object object, Field field, boolean isTextData) throws IllegalAccessException {
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            sb.append('"').append(field.getName()).append("\":");
            if (isTextData) {
                sb.append('"').append(valObject).append('"');
            } else {
                sb.append(valObject);
            }
            sb.append(',');
        }
    }

    private void convertListData(StringBuilder sb,  Object object, Field field) throws IllegalAccessException {

//        todo: null при отсутствующем array не должен ронять прогу.
//         просто ничего не должно добавляться в json-строку по этому полю

        sb.append('"').append(field.getName()).append("\":[");

        ParameterizedType paramType = (ParameterizedType) field.getGenericType();
        Class<?> typeElements = (Class<?>) paramType.getActualTypeArguments()[0];

        field.setAccessible(true);
        List list = (List) field.get(object);
        if (typeElements.equals(String.class) || typeElements.equals(Character.class)) {
            for (int i = 0; i < list.size(); i++) {
                sb.append('"').append(list.get(i)).append('"');
                sb.append(',');
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i));
                sb.append(',');
            }
        }

        sb.replace(sb.length()-1, sb.length(), "]");
        sb.append(',');
    }

    /**
     * Метод создает строку из поля типа массив.
     * @param sb переменная StringBuilder, в нее будет сохранена получаемая строка
     * @param object объект, с исходным полем для преобразования
     * @param field ссылка на поле, которое будет преобразовано
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void convertArrayData(StringBuilder sb,  Object object, Field field) throws IllegalAccessException {

//        todo: null при отсутствующем array не должен ронять прогу.
//         просто ничего не должно добавляться в json-строку по этому полю

        sb.append('"').append(field.getName()).append("\":[");
        Class<?> typeElements = field.getType().componentType();
        field.setAccessible(true);
        Object[] array = unpackArray(field.get(object));
        if (typeElements.equals(char.class) ||
                typeElements.equals(String.class) ||
                typeElements.equals(Character.class)) {
            for (int i = 0; i < array.length; i++) {
                sb.append('"').append(array[i]).append('"');
                sb.append(',');
            }
        } else {
            for (int i = 0; i < array.length; i++) {
                sb.append(array[i]);
                sb.append(',');
            }
        }
        sb.replace(sb.length()-1, sb.length(), "]");
        sb.append(',');
}

    /**
     * Метод приводит тип переданного объекта к типу массива {@code Object[]}.
     * Если передан массив примитивных типов, то происходит упаковка элементов
     * в классы-обертки.
     *
     * @param value переменная типа Object, содержащая массив
     * @return переменную типа Object[]
     * @throws NullPointerException если передан null
     * @throws IllegalArgumentException если передан не массив
     */
    private Object[] unpackArray(Object value) {
        if(value == null) {
            throw new NullPointerException();
        }
        if(value.getClass().isArray()) {
            if(value instanceof Object[]) {
                return (Object[])value;
            }
            else { // box primitive arrays
                final Object[] boxedArray = new Object[Array.getLength(value)];
                for(int i=0; i < boxedArray.length; i++) {
                    boxedArray[i] = Array.get(value, i); // automatic boxing
                }
                return boxedArray;
            }
        }
        else {
            throw new IllegalArgumentException("Not an array");
        }
    }
}
