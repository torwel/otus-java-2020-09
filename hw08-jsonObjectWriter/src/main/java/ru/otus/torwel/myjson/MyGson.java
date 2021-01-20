package ru.otus.torwel.myjson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MyGson {

    private StringBuilder jsonSB;
    private Object object;

    /**
     * Метод сериализует переданный объект в строку формата JSON.
     *
     * @param objectForSerialize объект для сериализации.
     * @return строка JSON формата.
     */
    public String toJson(Object objectForSerialize) {
        if (objectForSerialize == null) {
            return "null";
        }
        jsonSB = new StringBuilder();
        object = objectForSerialize;

        try {
            if (isNumberType(object.getClass())) {
                jsonSB.append(object);
            } else if (isTextType(object.getClass())) {
                jsonSB.append('"').append(object).append('"');
            } else if (object.getClass().isArray()) {
                arrayToJson();
            } else if (Collection.class.isAssignableFrom(object.getClass())) {
                listToJson();
            } else {
                objectToJson();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return jsonSB.toString();
    }


    /**
     * Метод преобразует объект и записывает результат в JSON-строку.
     *
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void objectToJson() throws IllegalAccessException {
        jsonSB.append('{');
        for (Field field : object.getClass().getDeclaredFields()) {
            Class<?> typeField = field.getType();
            if (typeField.isArray()) {
                arrayToJson(field);
            } else if (Collection.class.isAssignableFrom(typeField)) {
                listToJson(field);
            } else if (isTextType(typeField)) {
                textToJson(field);
            } else if (isNumberType(typeField)) {
                numberToJson(field);
            }
        }
        if(jsonSB.charAt(jsonSB.length() - 1) == ',') {
            jsonSB.deleteCharAt(jsonSB.length() - 1);
        }
        jsonSB.append('}');
    }

    private void numberToJson(Field field) throws IllegalAccessException {
        simpleDataToJson(field, false);
    }

    private void textToJson(Field field) throws IllegalAccessException {
        simpleDataToJson(field, true);
    }

    /**
     * Метод создает строку из поля примитивного типа или обертки примитивов.
     * Также может принимать более сложные объекты. Но при этом вставляет в
     * результирующую строку в качестве значения то, что вернет метод
     * {@code toString()} объекта.
     * Результирующая строка имеет формат {@code "fieldName":"textValue"} для
     * текстовых полей или {@code "fieldName":numberValue} для числовых значений.
     *
     * @param field ссылка на поле, которое будет преобразовано.
     * @param isTextData если true, то формат строки текстовый, иначе - числовой.
     * @throws IllegalAccessException если поле недоступно для чтения.
     */
    private void simpleDataToJson(Field field, boolean isTextData) throws IllegalAccessException {
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            jsonSB.append('"').append(field.getName()).append("\":");
            if (isTextData) {
                jsonSB.append('"').append(valObject).append('"');
            } else {
                jsonSB.append(valObject);
            }
            jsonSB.append(',');
        }
    }

    /**
     * Метод преобразует объект в JSON-строку.
     * Тип объекта должен  соответствовать java.util.Collection.
     */
    private void listToJson() {
        Collection<?> collection = (Collection<?>) object;
        Iterator<?> iterator = collection.iterator();
        Class<?> typeElements = null;
        if (iterator.hasNext()) {
            typeElements = iterator.next().getClass();
        }
        Object[] array = collection.toArray();

        if (typeElements == null) {
            jsonSB.append("[]");
        } else {
            arrayToJson(typeElements, array);
        }
    }

    /**
     * Метод преобразует поле {@code field} объекта.
     * Тип поля должен соответствовать java.util.Collection.
     * Результат записывается в JSON-строку
     *
     * @param field поле должно быть типа java.util.Collection
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void listToJson(Field field) throws IllegalAccessException {
        ParameterizedType paramType = (ParameterizedType) field.getGenericType();
        Class<?> typeElements = (Class<?>) paramType.getActualTypeArguments()[0];
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            Object[] array = ((List<?>) valObject).toArray();
            arrayToJson(field.getName(), typeElements, array);
        }
    }

    /**
     * Метод преобразует объект в JSON-строку.
     * Объект должен быть массивом.
     */
    private void arrayToJson() {
        Object[] array = unpackArray(object);
        if (array.length > 0) {
            Class<?> typeElements = array[0].getClass();
            arrayToJson(typeElements, array);
        } else {
            jsonSB.append("[]");
        }
    }

    /**
     * Метод преобразует поле {@code field} объекта.
     * Поле должно быть массивом.
     * Результат записывается в JSON-строку
     *
     * @param field поле должно быть типа java.util.Collection
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void arrayToJson(Field field) throws IllegalAccessException {
        Class<?> typeElements = field.getType().componentType();
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            Object[] array = unpackArray(valObject);
            arrayToJson(field.getName(), typeElements, array);
        }
    }

    /**
     * Метод формирует строку из имени поля и данных в виде массива {@code array}.
     * Результирующая строка имеет формат {@code  "fieldName":["e1","e2",etc]} для
     * текстовых полей или {@code "fieldName":[e1,e2,etc]} для числовых значений.
     *
     * @param fieldName имя поля, которое будет указано в результирующей строке
     * @param typeElements тип элементов массива
     * @param array массив для обработки
     */
    private void arrayToJson(String fieldName, Class<?> typeElements, Object[] array) {
        jsonSB.append('"').append(fieldName).append("\":");
        arrayToJson(typeElements, array);
        jsonSB.append(',');
    }

    /**
     * Метод формирует и записывает JSON-строку из массива.
     * В зависимости от типа данных в массиве, формат строки текстовый или числовой.
     */
    private void arrayToJson(Class<?> typeElements, Object[] array) {
        jsonSB.append('[');
        if (isTextType(typeElements)) {
            for (Object o : array) {
                jsonSB.append('"').append(o).append('"');
                jsonSB.append(',');
            }
        } else {
            for (Object o : array) {
                jsonSB.append(o);
                jsonSB.append(',');
            }
        }
        jsonSB.deleteCharAt(jsonSB.length() - 1);
        jsonSB.append(']');
    }

    /**
     * Метод приводит тип переданного объекта к типу массива {@code Object[]}.
     * Если передан массив примитивных типов, то происходит упаковка элементов
     * в классы-обертки.
     *
     * @param value переменная типа Object, содержащая массив
     * @return переменную типа Object[]
     */
    private Object[] unpackArray(Object value) {
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

    private boolean isTextType(Class<?> clazz) {
        return clazz.equals(char.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(String.class);
    }

    private boolean isNumberType(Class<?> clazz) {
        return clazz.equals(byte.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(short.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(int.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(long.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(float.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(double.class) ||
                clazz.equals(Double.class);
    }
}
