package ru.otus.torwel.myjson;

import com.google.gson.Gson;

public class Demo {

    private AnyObject anyObject = new AnyObject();
    private String description = "Demo. JSON object writer.";

    public static void main(String[] args) {
        Gson gson = new Gson();
        MyGson myGson = new MyGson();

        Demo demoObject = new Demo();

        String json = gson.toJson(demoObject);
        System.out.println("Gson:   " + json);

        String myJson = myGson.toJson(demoObject);
        System.out.println("myGson: " + myJson);

        Demo deserializedTestObject = gson.fromJson(myJson, demoObject.getClass());
        System.out.println("Expected object: " + demoObject);
        System.out.println("myGson object:   " + deserializedTestObject);
    }
}
