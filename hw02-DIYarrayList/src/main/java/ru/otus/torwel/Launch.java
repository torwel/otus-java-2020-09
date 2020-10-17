package ru.otus.torwel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Launch {

    public static void main(String[] args) {

        System.out.println("Проверка метода Collections.addAll(Collection<? super T> c, T... elements)");

        Integer[] vals = new Integer[25];
        for (int i = 0; i < vals.length; i++) {
            vals[i] = (int) (100 * Math.random());
        }
        List<Integer> list = new DIYArrayList<>();
        Collections.addAll(list, vals);
        System.out.println("Array: " + Arrays.toString(vals));
        System.out.println("List:  " + list);
        System.out.println("\n\n");

        System.out.println("Проверка метода Collections.copy(List<? super T> dest, List<? extends T> src)");
        List<Integer> listCopy = new DIYArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listCopy.add(0);
        }
        Collections.copy(listCopy, list);
        System.out.println("Copy:  " + listCopy);
        System.out.println("\n\n");

        System.out.println("Проверка метода Collections.sort(List<T> list, Comparator<? super T> c)");
        Collections.sort(listCopy);
        System.out.println("Sorted copy: " + listCopy);
        System.out.println("\n\n");

        List<Person> pers = new DIYArrayList<>();
        Launch launch = new Launch();
        pers.add(launch.new Person("Nikolay"));
        pers.add(launch.new Person("Anna"));
        pers.add(launch.new Person("Alexey"));
        pers.add(launch.new Person("Svetlana"));
        Collections.sort(pers, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        System.out.println(pers);

//        debugMethod();
    }

    private static void debugMethod() {

        DIYArrayList<Integer> poligon = new DIYArrayList<>();

        System.out.println("Call 'toString' method for empty list:");
        System.out.println(poligon);

        poligon.add(110);
        System.out.println("Get element 0: ");
        System.out.println(poligon.get(0));

        System.out.println("Adding two elements...");
        poligon.add(40);
        poligon.add(80);
        System.out.println("Call 'toString' method for list:");
        System.out.println(poligon);

        System.out.println("Adding elements to list by calling 'Collections.addAll'...");
        Collections.addAll(poligon, 30, 70, 100, 50, 20, 90, 60, 10);

        System.out.println("Call 'toString' method for list:");
        System.out.println(poligon);
        System.out.println("Size: " + poligon.size());

        System.out.println("Copying list...");
        DIYArrayList<Integer> copyPoligon = new DIYArrayList<>();
        for (int i = 0; i < poligon.size(); i++) {
            copyPoligon.add(0);
        }
        Collections.copy(copyPoligon, poligon);
        System.out.println("src[0] element changed to '8888'.");
        poligon.set(0, 8888);
        System.out.println("src: " + poligon);
        System.out.println("dst: " + copyPoligon);

        System.out.println("Deleting element number 5.");
        poligon.remove(4);
        System.out.println("poligon: " + poligon);

        System.out.println("Clearing poligon.");
        poligon.clear();
        System.out.println("poligon: " + poligon);
        System.out.println("size: " + poligon.size());


        System.out.println("Sorting copyPoligon:");
        Collections.sort(copyPoligon);
        System.out.println("poligon" + copyPoligon);

    }

    class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
