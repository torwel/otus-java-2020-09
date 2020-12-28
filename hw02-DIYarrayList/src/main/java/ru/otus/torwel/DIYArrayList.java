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
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Удаляет все элементы списка
     */
    @Override
    public void clear() {
        modCount++;
        final Object[] es = elements;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    /**
     * Возвращает элемент с определенной позиции списка.
     *
     * @param  index индекс возвращаемого элемента
     * @return с указанной позиции списка'
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return getElement(index);
    }

    @SuppressWarnings("unchecked")
    private T getElement(int index) {
        return (T) elements[index];
    }

    /**
     * Заменяет элемент в списке.
     * @param index индекс заменяемого элемента
     * @param element новый элемент, который должен быть установлен
     * @return старый элемент
     */
    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        T oldValue = getElement(index);
        elements[index] = element;
        return oldValue;
    }

    /**
     * Добавляет элемент в конец списка.
     * @param element добавляемый элемент
     * @return true если колеекция изменена в результате вызова
     */
    @Override
    public boolean add(T element) {
        modCount++;
        if (size == elements.length) {
            elements = grow();
        }
        elements[size] = element;
        size++;
        return true;
    }

    /**
     * Вставляет переданный элемент на определенную позицию списка.
     * При этом часть списка от текущей позиции до конца сдвигается вправо.
     *
     * @param index индекс позиции, куда устанавливается новый элемент
     * @param element добавляемый элемент списка
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, T element) {
        rangeCheck(index);
        modCount++;
        final int s = size;
        Object[] els  = this.elements;
        if (s == els.length)
            els = grow();
        System.arraycopy(els, index, els, index + 1, s - index);
        els[index] = element;
        size = s + 1;
    }

    /**
     * Увеличивает емкость массива элементов так, чтобы обеспечить размещение
     * минимального количества элементов, передаваемого аргументом.
     *
     * @param minCapacity желаемое минимальное изменение емкости
     * @throws OutOfMemoryError если minCapacity отрицательно
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
     * Вычисляет новую длину массива для элементов списка на основе значений
     * текущей длины, предпочитаемого прироста и минимального прироста.
     * из предпочитаемого и минимального прироста выбирается максимальное
     * значение и используется для вычисления.
     *
     * @param oldLength   значение текущей длины массива (не отрицательное)
     * @param minGrowth   минимально необходимый прирост длины (положительное)
     * @param prefGrowth  предпочтительный прирост длины (игнорируется если
     *                    меньше {@code minGrowth})
     * @return значение новой длины массива
     * @throws OutOfMemoryError если увеличение {@code oldLength} на значение
     *                    {@code minGrowth} превышает Integer.MAX_VALUE.
     */
    private int newLength(int oldLength, int minGrowth, int prefGrowth) {

        int newLength = Math.max(minGrowth, prefGrowth) + oldLength;
        if (newLength - MAX_ARRAY_LENGTH <= 0) {
            return newLength;
        }
        return hugeLength(oldLength, minGrowth);
    }

    private int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) {
            throw new OutOfMemoryError("Required array length too large");
        }
        if (minLength <= MAX_ARRAY_LENGTH) {
            return MAX_ARRAY_LENGTH;
        }
        return Integer.MAX_VALUE;
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
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
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
        rangeCheck(index);
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
        throw new UnsupportedOperationException();
    }

    /**
     * Проверка вхождения индекса в допустимые пределы.
     */
    private void rangeCheck(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
    }

    /**
     * Возвращает строку с перечислением элементов списка
     */
    public String toString(){
        if (size == 0)
            return super.toString();
        StringBuilder str = new StringBuilder("[");
        for(int i = 0; ; i++) {
            str.append(elements[i].toString());
            if (i == size - 1)
                return str.append("]").toString();
            str.append(", ");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void sort(Comparator<? super T> c) {
        final int expectedModCount = modCount;
        Arrays.sort((T[]) elements, 0, size, c);
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        modCount++;
    }
}
