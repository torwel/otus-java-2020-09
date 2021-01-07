package ru.otus.torwel.myjson;

import com.google.gson.Gson;

import java.util.Arrays;

public class Demo {

/*
    Напишите свой json object writer (object to JSON string) аналогичный gson на основе javax.json.

    Gson это делает так:
    Gson gson = new Gson();
    AnyObject obj = new AnyObject(22, "test", 10);
    String json = gson.toJson(obj);

    Сделайте так:
    MyGson myGson = new MyGson();
    AnyObject obj = new AnyObject(22, "test", 10);
    String myJson = myGson.toJson(obj);

    Должно получиться:
    AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
    System.out.println(obj.equals(obj2));

*/

    public static void main(String[] args) {

        Gson gson = new Gson();


        MyGson myGson = new MyGson();
        AnyObject obj = new AnyObject();
//        obj.setByteType((byte)20);
//        obj.setByteWrapped((byte)21);
//        obj.setShortType((short)300);
//        obj.setShortWrapped((short)301);
//        obj.setIntType(4000);
//        obj.setIntWrapped(4001);
//        obj.setLongType(5_000L);
//        obj.setLongWrapped(5_001L);
//        obj.setFloatType(0.70f);
//        obj.setFloatWrapped(0.71f);
//        obj.setDoubleType(10_000.0);
//        obj.setDoubleWrapped(10_000.1);
        obj.setCharType('A');
//        obj.setCharWrapped('W');
//        obj.setStringType("String_Type");
        obj.setCharArray(new char[]{'a', 'b', 'c'});
        obj.setIntArray(new int[]{10, 20, 30});
        obj.setListDoubles(Arrays.asList(5.0, 50.0, 500.0));
        obj.setListCharacters(Arrays.asList('Q', 'W', 'E'));

        String json = gson.toJson(obj);
        System.out.println("json:   " + json);

        String myJson = myGson.toJson(obj);
        System.out.println("myJson: " + myJson);

        AnyObject obj2 = gson.fromJson(myJson, AnyObject.class);
        System.out.println("JSON strings equals: " + json.equals(myJson));
        System.out.println("Deserialized object is the same: " + obj.equals(obj2));

    }
}
