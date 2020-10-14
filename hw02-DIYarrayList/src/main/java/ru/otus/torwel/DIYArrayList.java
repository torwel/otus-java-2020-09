package ru.otus.torwel;

import java.util.*;

public class DIYArrayList<T> implements List<T> {

    /**
     * Размер по умолчанию массива для элементов списка.
     */
    private final int DEF_CAPACITY = 10;

    /**
     * Массив для хранения элементов списка.
     */
    private Object[] elements;

    /**
     * Текущий размер массива для элементов списка.
     */
    private int size;

    /**
     * Создание пустого списка размером по умолчанию.
     */
    public DIYArrayList() {
        elements = new Object[DEF_CAPACITY];
    }

    /**
     * Наибольший размер массива. Параметр используется при
     * увеличении размера массива элементов списка.
     * На некоторых ВМ превышение этого размера может вызвать
     * {@code OutOfMemoryError: Requested array size exceeds VM limit}.
     */
    public static final int MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    /**
     * Создание пустого списка заданого размера.
     * @param  initialCapacity  размер создаваемого списка
     * @throws IllegalArgumentException если заданный размер отрицательный
     */
    public DIYArrayList(int initialCapacity) {
        if (initialCapacity >= 0) {
            elements = new Object[initialCapacity];
        }
        else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }


    /**
     * Возвращает текущий размер списка.
     * @return количество элементов в списке
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверяет, является ли список пустым.
     * @return true если список пустой, иначе false
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {

        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    /**
     *
     */
    private void add(T e, Object[] elements, int s) {
        if (s == elements.length) {
            elements = grow();
        }
        elements[s] = e;
        size++;
    }

    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     * @throws OutOfMemoryError if minCapacity is less than zero
     */
    private Object[] grow(int minCapacity) {
        int oldCapacity = elements.length;
        if (oldCapacity > 0) {
            int newCapacity = newLength(oldCapacity,
                    minCapacity - oldCapacity,
                    oldCapacity >> 1);
            return elements = Arrays.copyOf(elements, newCapacity);
        } else {
            return elements = new Object[Math.max(DEF_CAPACITY, minCapacity)];
        }
    }

    private Object[] grow() {
        return grow(size + 1);
    }

    /**
     * Calculates a new array length given an array's current length, a preferred
     * growth value, and a minimum growth value.  If the preferred growth value
     * is less than the minimum growth value, the minimum growth value is used in
     * its place.  If the sum of the current length and the preferred growth
     * value does not exceed {@link #MAX_ARRAY_LENGTH}, that sum is returned.
     * If the sum of the current length and the minimum growth value does not
     * exceed {@code MAX_ARRAY_LENGTH}, then {@code MAX_ARRAY_LENGTH} is returned.
     * If the sum does not overflow an int, then {@code Integer.MAX_VALUE} is
     * returned.  Otherwise, {@code OutOfMemoryError} is thrown.
     *
     * @param oldLength   current length of the array (must be non negative)
     * @param minGrowth   minimum required growth of the array length (must be
     *                    positive)
     * @param prefGrowth  preferred growth of the array length (ignored, if less
     *                    then {@code minGrowth})
     * @return the new length of the array
     * @throws OutOfMemoryError if increasing {@code oldLength} by
     *                    {@code minGrowth} overflows.
     */
    private int newLength(int oldLength, int minGrowth, int prefGrowth) {
        // assert oldLength >= 0
        // assert minGrowth > 0

        int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
        if (newLength - MAX_ARRAY_LENGTH <= 0) {
            return newLength;
        }
        return hugeLength(oldLength, minGrowth);
    }

    private int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError("Required array length too large");
        }
        if (minLength <= MAX_ARRAY_LENGTH) {
            return MAX_ARRAY_LENGTH;
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }
}
