package ru.otus.torwel;

import java.util.Arrays;

public class Launch {

    public static void main(String[] args) {
        Object[] elements = {1, 2, 3};
        System.out.println( "Elements count: " + elements.length);
        System.out.println( Arrays.toString(elements) );

        DIYArrayList<Integer> poligon = new DIYArrayList<>();
        System.out.println(poligon);
        poligon.add(1);
        System.out.println(poligon.get(0));

    }

}
