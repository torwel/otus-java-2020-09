package ru.otus.torwel.myjson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class MyGson {

    /**
     * Метод сериализует переданный объект в строку формата JSON.
     *
     * @param object объект для сериализации.
     * @return строка JSON формата.
     */
    public String toJson(Object object) {
        StringBuilder sb = new StringBuilder();
        if (object != null) {
            for (Field field : object.getClass().getDeclaredFields()) {
                Class<?> typeField = field.getType();
                try {
                    if (typeField.isArray()) {
                        convertArrayData(sb, object, field);
                    } else if (Collection.class.isAssignableFrom(typeField)) {
                        convertListData(sb, object, field);
                    } else if (isTextClass(typeField)) {
                        convertTextData(sb, object, field);
                    } else if (isNumberClass(typeField)) {
                        convertNumberData(sb, object, field);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (sb.length() == 0) {
                sb.append("{}");
            } else {
                sb.insert(0, '{');
                sb.replace(sb.length() - 1, sb.length(), "}");
            }
        } else {
            sb.append("null");
        }
        return sb.toString();
    }

    private void convertNumberData(StringBuilder sb, Object object, Field field) throws IllegalAccessException {
        convertSimpleData(sb, object, field, false);
    }

    private void convertTextData(StringBuilder sb, Object object, Field field) throws IllegalAccessException {
        convertSimpleData(sb, object, field, true);
    }

    /**
     * Метод создает строку из поля примитивного типа или обертки примитивов.
     * Также может принимать более сложные объекты. Но при этом вставляет в
     * результирующую строку в качестве значения то, что вернет метод
     * {@code toString()} объекта.
     * Результирующая строка имеет формат {@code  "fieldName":"textValue"} для
     * текстовых полей или {@code "fieldName":numberValue} для числовых значений.
     *
     * @param sb переменная StringBuilder, в нее будет сохранена получаемая строка
     * @param object объект, с исходным полем для преобразования
     * @param field ссылка на поле, которое будет преобразовано
     * @param isTextData если true, то
     * @throws IllegalAccessException если поле недоступно для чтения
     */
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

    /**
     * Метод создает строку из поля типа java.util.Collection.
     *
     * @param sb переменная StringBuilder, в нее будет сохранена получаемая строка
     * @param object объект, с исходным полем для преобразования
     * @param field ссылка на поле, которое будет преобразовано
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void convertListData(StringBuilder sb,  Object object, Field field) throws IllegalAccessException {
        ParameterizedType paramType = (ParameterizedType) field.getGenericType();
        Class<?> typeElements = (Class<?>) paramType.getActualTypeArguments()[0];
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            Object[] array = ((List<?>) valObject).toArray();
            convertCompositeData(sb, field, typeElements, array);
        }
    }

    /**
     * Метод создает строку из поля типа массив.
     *
     * @param sb переменная StringBuilder, в нее будет сохранена получаемая строка
     * @param object объект, с исходным полем для преобразования
     * @param field ссылка на поле, которое будет преобразовано
     * @throws IllegalAccessException если поле недоступно для чтения
     */
    private void convertArrayData(StringBuilder sb,  Object object, Field field) throws IllegalAccessException {
        Class<?> typeElements = field.getType().componentType();
        field.setAccessible(true);
        Object valObject = field.get(object);
        if (valObject != null) {
            Object[] array = unpackArray(valObject);
            convertCompositeData(sb, field, typeElements, array);
        }
    }

    /**
     * Метод создает строку из переданного массива.
     * Результирующая строка имеет формат {@code  "fieldName":["e1","e2",etc]} для
     * текстовых полей или {@code "fieldName":[e1,e2,etc]} для числовых значений.
     *
     * @param sb переменная StringBuilder, в нее будет сохранена получаемая строка
     * @param field ссылка на поле, имя которого будет указано в результирующей строке
     * @param typeElements тип элементов массива
     * @param array массив для обработки
     */
    private void convertCompositeData(StringBuilder sb, Field field, Class<?> typeElements, Object[] array) {
        sb.append('"').append(field.getName()).append("\":[");
        if (isTextClass(typeElements)) {
            for (Object o : array) {
                sb.append('"').append(o).append('"');
                sb.append(',');
            }
        } else {
            for (Object o : array) {
                sb.append(o);
                sb.append(',');
            }
        }
        sb.replace(sb.length() - 1, sb.length(), "]");
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

    private boolean isTextClass(Class<?> clazz) {
        return clazz.equals(char.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(String.class);
    }

    private boolean isNumberClass(Class<?> clazz) {
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
