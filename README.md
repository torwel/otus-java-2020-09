# Репозиторий для домашних работ по курсу "Java Developer. Professional."

Группа 2020-09


#### Студент:

Александр Тумаш

---

### Содержание

- [HW-01. Gradle.](#hw01)
- [HW-02. DIY ArrayList.](#hw02)
- [HW-03. Reflection and Annotation.](#hw03)
- [HW-04. Garbage collection.](#hw04)
- [HW-05. Automatic Logging.](#hw05)
- [HW-06. ATM emulator.](#hw06)
- [HW-07. Design patterns.](#hw07)

---

<a name="hw01"></a>

### HW-01. Gradle.

Создан github-репозиторий для выполнения домашних заданий.
Настроена система управления зависимостями и сборки проекта Gradle.
Получены навыки работы с ней.

Запуск сборки проекта и проверка работоспособности
приложения выполняется командами в консоли:
```
 gradlew.bat build
 java -jar ./hw01-gradle/build/libs/gradleHelloWorld-0.1.jar
``` 
---

<a name="hw02"></a>

### HW-02. DIY ArrayList.

В задании разбирается устройство коллекции ArrayList.
На основе полученного опыта создана собственная коллекция,
реализующая часть методов интерфейса List.
Для проверки полученного результата использованы методы класса Colletions:
- Collections.addAll(Collection<? super T> c, T... elements)
- Collections.copy(List<? super T> dest, List<? extends T> src)
- Collections.sort(List<T> list, Comparator<? super T> c)

Работа указанных методов продемонстрирована в класе Launch.

---

<a name="hw03"></a>

### HW-03. Reflection and Annotation.

В задании создан тестовый фреймворк `TestFr`. При этом использованы механизмы
Java Reflection и работа с собственными аннотациями.

Состав модуля `hw03-reflection`:
- Пакет `ru.otus.torwel.testfr` содержит логику тестирования.
- В классе `TestThis` создается описание тестов с помощью аннотированных методов.
- Класс `DemoClass` - тестируемый демо класс.
- Класс `Launch` содержит main-метод для демонстрации работы фреймворка.

---

<a name="hw04"></a>

### HW-04. Garbage collection.

В задании создано простое приложение, позволяющее сравнить различные сборщики
мусора JVM. С его помощью произведено сравнение четырех GC:
- SerialGC
- ParallelGC
- G1GC
- ZGC

Запуск приложения.
В модуле `hw04-GC` в классе `Launch` неоьходимо выполнить метод main.

Подробное описание программы тестирования, ее настройки, а также результаты
проведенных тестов и выводы содержатся в файле [conclusions.md](./CONCLUSIONS.md).

---

<a name="hw05"></a>

### HW-05. Automatic Logging.

#### Постановка задачи

У нас есть класс `Car`, который содержит основную логику. Необходимо
реализовать логирование работы его методов с помощью аннотации `@Log`.


#### Реализация

Для решения поставленной задачи используем динамический прокси объекта `Car`.

Так как класс `java.lang.reflect.Proxy` работает только с методами,
объявленными в интерфейсах, необходимо создать специальный интерфейс.
В нем объявим и отметим аннотацией `@Log` методы, которые будем логировать.
Это и будет основным механизмом, с помощью которого мы можем включать и отключать
логирование.

Создаем класс `Ioc`. Его вложенный класс-обработчик `CarInvocationHandler`
будет содержать оригинальный объект `Car`. В методе `invoke` вложенного класса
реализуем логику логирования на основе аннотации `@Log`, а затем вызываем
соответствующий метод оригинального объекта.

Метод-фабрика `CarLogging createCar()` класса `Ioc` создает оригинальный объект
и его обработчик, а затем возвращает прокси-объект.


#### Как использовать

В пакете создан интерфейс `CarLogging`. В нем необходимо объявить и отметить
аннотацией `@Log` методы, которые будем логировать.

Класс `Car` реализует указанный интерфейс. Работа механизма логирования
продемонстрирована в методе `Launch.main(String...)`.

---

<a name="hw06"></a>

### HW-06. ATM emulator.

#### Постановка задачи

Написать эмулятор банкомата. Должны быть реализованы следующие возможности:
- Прием банкнот разных номиналов
- Выдача запрошенной суммы минимальным количеством банкнот
- Выдача суммы остатка денежных средств банкомата

#### Реализация

Для реализации логики работы АТМ введены необходимые понятия и созданы
соответствующие интерфейсы и классы:
1. Денежный номинал. Класс-перечисление `CurrencyDignity` Содержит все допустимые
   значения номиналов для нашей абстрактной валюты. Реализован в виде
   перечисления. Добавлять новые или убирать ненужные номиналы следует
   в этом классе.
2. Банкнота. Интерфейс `Banknote`. Класс `BanknoteImpl`. Каждый объект данного
   класса сопоставляется с реальной банкнотой. Их мы будем выдавать и принимать.
3. Денежная кассета. Интерфейс `Cassette`. Класс `CassetteImpl`. Кассета должна
   содержать только банкноты одинакового номинала. Класс реализует всю
   необходимую логику для обеспечения этого требования при приеме и выдаче
   банкнот.
4. Банкомат. Интерфейс `ATM`. Класс `ATMImpl`. Интерфейс содержит методы, которые
   выполняют поставленные задачи. Объект класса агрегирует в себе объекты
   кассет с банкнотами необходимого номинала. Все операции с банкоматом возможно
   совершить работая только с объектом, реализующим интерфейс ATM.

#### Как использовать

В модуле содержится тестовый класс `ATMTest` с методом `commonTest()`.
Данный тест демонстрирует работу эмулятора АТМ.

---

<a name="hw07"></a>

### HW-07. Design patterns.

#### Постановка задачи

Реализовать TODO, описанные в классе `Demo`:
1. Класс `ru.otus.torwel.model.Message`. Добавить поля field11 - field13.
   Для field13 используйте класс `ObjectForMessage`.
2. Сделать процессор, который поменяет местами значения field11 и field12.
3. Сделать процессор, который будет выбрасывать исключение в четную секунду.
   Секунда должна определяться во время выполнения.
   Сделать тест с гарантированным результатом.
4. Сделать `Listener` для ведения истории: старое сообщение - новое.
   Подумайте, как сделать, чтобы сообщения не портились.


#### Реализация

1. Внесены необходимые исправления в класс `ru.otus.torwel.model.Message`.
2. Создан класс `ru.otus.torwel.processor.homework.ProcessorSwapF11F12`.
   Реализован метод `process(Message message)`, меняющий значения полей
   местами.
3. Создан класс `ru.otus.torwel.processor.homework.ProcessorEvenSecondsPhobia` и
   интерфейс `TimeProvider`. Создан тестовый класс
   `ru.otus.torwel.processor.homework.ProcessorEvenSecondsPhobiaTest`
   с проверкой результатов выполнения метода `process(Message msg)`.
4. Создан класс `ru.otus.torwel.listener.homework.ListenerHistory`. 
   Его работа демонстрируется в методе `HomeWork.main(args)`.



#### Как использовать

Общая демонстрация выполненных заданий - метод `HomeWork.main(args)`.

Для процессора, выбрасывающего исключение при запуске в четную секунду,
создан отдельный тест `ru.otus.torwel.processor.homework.ProcessorEvenSecondsPhobiaTest`.

---
