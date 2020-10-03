package ru.otus.torwel;

import com.google.common.collect.Lists;

import java.util.ArrayList;


/**
 *
 * To start the application:
 * gradlew.bat build
 * java -jar ./hw01-gradle/build/libs/gradleHelloWorld-0.1.jar
 *
 */

public class HelloOtus {

    public static void main(String[] args) {
        ArrayList<Integer> list;
        list = Lists.newArrayList(1, 2, 3, 4, 5);
        System.out.println(list);
    }
}