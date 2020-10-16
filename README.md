# Репозиторий для домашних работ по курсу "Java Developer. Professional."

Группа 2020-09


#### Студент:

Александр Тумаш

---



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

HW-02. DIY ArrayList.

В задании разбирается устройство коллекции ArrayList.
На основе полученного опыта создана собственная коллекция,
реализующая часть методов интерфейса List.
Для проверки полученного результата использованы методы класса Colletions:
- Collections.addAll(Collection<? super T> c, T... elements)
- Collections.copy(List<? super T> dest, List<? extends T> src)
- Collections.sort(List<T> list, Comparator<? super T> c)

Работа указанных методов продемонстрирована в класе Launch.