# Репозиторий для домашних работ по курсу "Java Developer. Professional."

Группа 2020-09


#### Студент:

Александр Тумаш

---

#### Содержание

- [HW-01. Gradle.](#hw01)
- [HW-02. DIY ArrayList.](#hw02)
- [HW-03. Reflection and Annotation.](#hw03)
- [HW-04. Garbage collection.](#hw04)

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
