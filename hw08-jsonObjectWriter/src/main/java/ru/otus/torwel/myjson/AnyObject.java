package ru.otus.torwel.myjson;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnyObject {

    private byte byteType;
    private Byte byteWrapped;
    private short shortType;
    private Short shortWrapped;
    private int intType;
    private Integer intWrapped;
    private long longType;
    private Long longWrapped;
    private float floatType;
    private Float floatWrapped;
    private double doubleType;
    private Double doubleWrapped;
    private char charType;
    private Character charWrapped;
    private String stringType;
    private char[] charArray;
    private int[] intArray;
    private String[] stringArray;
    private List<Double> listDoubles;
    private List<Character> listCharacters;
    private List<String> listString;

    public int pubInt = 9000;

    public AnyObject() {
    }

    public void setByteType(byte byteType) {
        this.byteType = byteType;
    }

    public void setByteWrapped(Byte byteWrapped) {
        this.byteWrapped = byteWrapped;
    }

    public void setShortType(short shortType) {
        this.shortType = shortType;
    }

    public void setShortWrapped(Short shortWrapped) {
        this.shortWrapped = shortWrapped;
    }

    public void setIntType(int intType) {
        this.intType = intType;
    }

    public void setIntWrapped(Integer intWrapped) {
        this.intWrapped = intWrapped;
    }

    public void setLongType(long longType) {
        this.longType = longType;
    }

    public void setLongWrapped(Long longWrapped) {
        this.longWrapped = longWrapped;
    }

    public void setFloatType(float floatType) {
        this.floatType = floatType;
    }

    public void setFloatWrapped(Float floatWrapped) {
        this.floatWrapped = floatWrapped;
    }

    public void setDoubleType(double doubleType) {
        this.doubleType = doubleType;
    }

    public void setDoubleWrapped(Double doubleWrapped) {
        this.doubleWrapped = doubleWrapped;
    }

    public void setCharType(char charType) {
        this.charType = charType;
    }

    public void setCharWrapped(Character charWrapped) {
        this.charWrapped = charWrapped;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public void setPubInt(int pubInt) {
        this.pubInt = pubInt;
    }

    public void setCharArray(char[] charArray) {
        this.charArray = charArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public void setStringArray(String[] stringArray) {
        this.stringArray = stringArray;
    }

    public void setListDoubles(List<Double> listDoubles) {
        this.listDoubles = listDoubles;
    }

    public void setListCharacters(List<Character> listCharacters) {
        this.listCharacters = listCharacters;
    }

    public void setListString(List<String> listString) {
        this.listString = listString;
    }

    @Override
    public String toString() {
        return "AnyObject{" +
                "byteType=" + byteType +
                ", byteWrapped=" + byteWrapped +
                ", shortType=" + shortType +
                ", shortWrapped=" + shortWrapped +
                ", intType=" + intType +
                ", intWrapped=" + intWrapped +
                ", longType=" + longType +
                ", longWrapped=" + longWrapped +
                ", floatType=" + floatType +
                ", floatWrapped=" + floatWrapped +
                ", doubleType=" + doubleType +
                ", doubleWrapped=" + doubleWrapped +
                ", charType=" + charType +
                ", charWrapped=" + charWrapped +
                ", stringType='" + stringType + '\'' +
                ", charArray=" + Arrays.toString(charArray) +
                ", intArray=" + Arrays.toString(intArray) +
                ", stringArray=" + Arrays.toString(stringArray) +
                ", listDoubles=" + listDoubles +
                ", listCharacters=" + listCharacters +
                ", listString=" + listString +
                ", pubInt=" + pubInt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyObject)) return false;
        AnyObject anyObject = (AnyObject) o;
        return byteType == anyObject.byteType &&
                shortType == anyObject.shortType &&
                intType == anyObject.intType &&
                longType == anyObject.longType &&
                Float.compare(anyObject.floatType, floatType) == 0 &&
                Double.compare(anyObject.doubleType, doubleType) == 0 &&
                charType == anyObject.charType &&
                pubInt == anyObject.pubInt &&
                Objects.equals(byteWrapped, anyObject.byteWrapped) &&
                Objects.equals(shortWrapped, anyObject.shortWrapped) &&
                Objects.equals(intWrapped, anyObject.intWrapped) &&
                Objects.equals(longWrapped, anyObject.longWrapped) &&
                Objects.equals(floatWrapped, anyObject.floatWrapped) &&
                Objects.equals(doubleWrapped, anyObject.doubleWrapped) &&
                Objects.equals(charWrapped, anyObject.charWrapped) &&
                Objects.equals(stringType, anyObject.stringType) &&
                Arrays.equals(charArray, anyObject.charArray) &&
                Arrays.equals(intArray, anyObject.intArray) &&
                Arrays.equals(stringArray, anyObject.stringArray) &&
                Objects.equals(listDoubles, anyObject.listDoubles) &&
                Objects.equals(listCharacters, anyObject.listCharacters) &&
                Objects.equals(listString, anyObject.listString);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(byteType, byteWrapped,
                shortType, shortWrapped,
                intType, intWrapped,
                longType, longWrapped,
                floatType, floatWrapped,
                doubleType, doubleWrapped,
                charType, charWrapped,
                stringType,
                listDoubles,
                listCharacters,
                listString,
                pubInt);
        result = 31 * result + Arrays.hashCode(charArray);
        result = 31 * result + Arrays.hashCode(intArray);
        result = 31 * result + Arrays.hashCode(stringArray);
        return result;
    }
}
