package ru.otus.torwel.myjson;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

    class MyGsonTest {

    Gson gson;
    MyGson myGson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        myGson = new MyGson();
    }

    @Test
    @DisplayName("Тест JSON: передан NULL.")
    void toJsonNullObject() {

        Object testObj = null;

        String expectedJson = gson.toJson(testObj);
        System.out.println("expected: " + expectedJson);

        String myJson = myGson.toJson(testObj);
        System.out.println("myJson:   " + myJson);

        assertEquals("null", myJson);
        assertEquals(expectedJson, myJson);
    }

    @Test
    @DisplayName("Тест JSON: Объект без полей.")
    void toJsonObjectWithoutFields() {

        Object testObj = new Object();

        String expectedJson = gson.toJson(testObj);
        System.out.println("expected: " + expectedJson);

        String myJson = myGson.toJson(testObj);
        System.out.println("myJson:   " + myJson);

        assertEquals(expectedJson, myJson);
    }

    @Test
    @DisplayName("Тест JSON: Объект с NULL полями.")
    void toJsonObjectWithNullFields() {

        TestObject testObject = new TestObject() ;

        // serialize objects
        String expectedJson = gson.toJson(testObject);
        System.out.println("expected string: " + expectedJson);

        String myJson = myGson.toJson(testObject);
        System.out.println("myJson string:   " + myJson);

        // deserializing json strings
        TestObject expectedObject = gson.fromJson(expectedJson, testObject.getClass());
        System.out.println("expected object: " + expectedObject);
        TestObject deserializedTestObject = gson.fromJson(myJson, testObject.getClass());
        System.out.println("myJson object:   " + deserializedTestObject);

        assertEquals(expectedJson, myJson);
    }

    @Test
    @DisplayName("Тест JSON: Объект AnyObject - все поля имеют значения по умолчанию.")
    void toJsonAllFieldsDefault() {
        AnyObject testObject = new AnyObject();

        // serialize object
        String expectedJson = gson.toJson(testObject);
        System.out.println("expected: " + expectedJson);

        String myJson = myGson.toJson(testObject);
        System.out.println("myJson:   " + myJson);

        // deserializing json string
        System.out.println("expected object: " + testObject);
        AnyObject deserializedTestObject = gson.fromJson(myJson, testObject.getClass());
        System.out.println("myJson object:   " + deserializedTestObject);

        assertEquals(testObject, deserializedTestObject);
    }

    @Test
    @DisplayName("Тест JSON: Объект AnyObject - всем полям присвоены значения.")
    void toJsonAllFieldsUsed() {
        AnyObject testObject = new AnyObject();

        testObject.setByteType((byte)20);
        testObject.setByteWrapped((byte)21);
        testObject.setShortType((short)300);
        testObject.setShortWrapped((short)301);
        testObject.setIntType(4000);
        testObject.setIntWrapped(4001);
        testObject.setLongType(5_000L);
        testObject.setLongWrapped(5_001L);
        testObject.setFloatType(0.70f);
        testObject.setFloatWrapped(0.71f);
        testObject.setDoubleType(10_000.0);
        testObject.setDoubleWrapped(10_000.1);
        testObject.setCharType('A');
        testObject.setCharWrapped('W');
        testObject.setStringType("String_Type");
        testObject.setCharArray(new char[]{'a', 'b', 'c'});
        testObject.setIntArray(new int[]{10, 20, 30});
        testObject.setStringArray(new String[]{"st1","st2","st3"});
        testObject.setListDoubles(Arrays.asList(5.0, 50.0, 500.0));
        testObject.setListCharacters(Arrays.asList('Q', 'W', 'E'));
        var lst = new ArrayList<String>();
        lst.add("text1");
        lst.add("text2");
        testObject.setListString(lst);

        // serialize object
        String expectedJson = gson.toJson(testObject);
        System.out.println("expected: " + expectedJson);

        String myJson = myGson.toJson(testObject);
        System.out.println("myJson:   " + myJson);

        // deserializing json string
        System.out.println("expected object: " + testObject);
        AnyObject deserializedTestObject = gson.fromJson(myJson, testObject.getClass());
        System.out.println("myJson object:   " + deserializedTestObject);

        assertEquals(testObject, deserializedTestObject);
    }

}