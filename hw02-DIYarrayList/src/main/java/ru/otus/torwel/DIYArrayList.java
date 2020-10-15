package ru.otus.torwel;

import java.util.*;
import java.util.function.Consumer;

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
     * Переменная необходима для определения конкурирующих модификаций
     * массива элементов.
     */
    private int modCount = 0;

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

/*
Определен ниже
    @Override
    public Iterator<T> iterator() {
        return null;
    }
*/

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
//        TODO: Во всех модифицирующих методах должно быть изменение modCount++;
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

    /**
     * Удаляет определенный индексом элемент. Если справа есть элементы,
     * то они сдвигаются влево на одну позицию.
     *
     * @param index индекс удаляемого элемета
     * @return элемент, который был удален из списка
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public T remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elements;

        @SuppressWarnings("unchecked")
        T oldValue = (T) es[index];

        modCount++;
        final int newSize;
        if ((newSize = size - 1) > index)
            System.arraycopy(es, index + 1, es, index, newSize - index);
        es[size = newSize] = null;

        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    /**
     * Возвращает итератор элементов списка, указывающий на переданный
     * в качестве параметра индекс. Метод {@link ListIterator#next next}
     * вернет элемент с позиции index.
     * Метод {@link ListIterator#previous previous} вернет элемент
     * с позиции index - 1.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public ListIterator<T> listIterator(int index) {
        rangeCheckForAdd(index);
        return new DIYArrayList.ListItr(index);
    }

    /**
     * Возвращает итератор элементов списка.
     */
    public ListIterator<T> listIterator() {
        return new DIYArrayList.ListItr(0);
    }

    /**
     * Возвращает итератор элементов списка.
     */
    public Iterator<T> iterator() {
        return new DIYArrayList.Itr();
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        // prevent creating a synthetic constructor
        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = DIYArrayList.this.elements;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (T) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                DIYArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

/*
        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            final int size = DIYArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elements;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }
*/

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    private class ListItr extends Itr implements ListIterator<T> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public T previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = DIYArrayList.this.elements;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (T) elementData[lastRet = i];
        }

        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                DIYArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T e) {
            checkForComodification();

            try {
                int i = cursor;
                DIYArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    /**
     * A version of rangeCheck used by add and addAll.
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    @SuppressWarnings("unchecked")
    static <T> T elementAt(Object[] es, int index) {
        return (T) es[index];
    }

}
