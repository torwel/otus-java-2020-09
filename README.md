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
- [HW-08. JSON object writer.](#hw08)
- [HW-09. DIY ORM.](#hw09)
- [HW-10. Hibernate.](#hw10)
- [HW-11. DIY Cache Engine.](#hw11)
- [HW-12. Web-server.](#hw12)
- [HW-13. DIY IoC Container.](#hw13)
- [HW-14. Spring MVC.](#hw14)

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

<a name="hw08"></a>
### HW-08. JSON object writer.

#### Постановка задачи

Создать свой JSON object writer.  
Цель: Научиться сериализовывать объект в json, попрактиковаться в разборе
структуры объекта.

Необходимо поддержать:
- примитивные типы и Wrapper-ы (Integer, Float и т.д.);
- строки;
- массивы примитивных типов;
- коллекции (interface Collection).


#### Реализация

Класс `MyGson` содержит логику обработки передаваемых объектов и формирования
строки в JSON формате. Для создания строки необходимо передать сериализуемый
обект в качестве параметра методу класса `toJson(Object objectForSerialize)`
Метод работает только с полями типов, указанных в задании. Также, дополнительно,
реализована работа с массивами обернутых примитивов и строк.


#### Как использовать

1. Класс `Demo` содержит небольшую демонстрацию работы MyGson в сравнении с
библиотекой сериализации GSON от Google.
2. В модуле присутствует класс с юнит-тестом `MyGsonTest`, который проводит 
ряд тестов работы класса `MyGson` с различными входными объектами.

---

<a name="hw09"></a>
### HW-09. DIY ORM.

#### Постановка задачи

Создать свой ORM для существующего приложения.  
Цель: Научиться работать с JDBC.
На практике освоить многоуровневую архитектуру приложения.  
Работа должна использовать базу данных в docker-контейнере.


#### Реализация

Реализованы интерфейсы
- `class JdbcMapperImpl<T> implements JdbcMapper<T>`;
- `class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>`;
- `class EntitySQLMetaDataImpl implements EntitySQLMetaData`.

Их реализации встроены в существующие слои приложения без их изменения.

Также создан проверочный класс `Account` и соответствующие ему классы:
- `class DbServiceAccountImpl implements DbServiceAccount`
- `class AccountDaoJdbc implements AccountDao`


#### Как использовать

Для проверки работы ORM с классом `Client` в методе `HomeWork.main()` предложен
блок кода. Он оставлен без изменений и добавлена проверка работы обновления
записей в БД.

Далее, в методе `HomeWork.main()` идет код, демонстрирующий работу созданного
ORM с классом Account.

Порядок запуска:
1. Запустить docker.
2. Выполнить метод `HomeWork.main()`.

В директории `hw09-orm/docker` файлы с инструкциями запуска docker'а:
- `runDb.src` - скрипт для Centos.
- `winCommands.txt` - команды для терминала Windows.

Если необходимо проверить работу с новым классом, необходимо создать класс
`NewClass`, реализовать предназначенные для него интерфейсы `DBServiceNewClass` и
`NewClassDaoJdbc`. А также описать таблицу для него в файле:
`hw09-orm/src/main/resources/db/migration/V1__initial_schema.sql`.

---

<a name="hw10"></a>
### HW-10. Hibernate.


#### Постановка задачи

Цель: На практике освоить основы Hibernate.

Понять как аннотации Hibernate влияют на формирование sql-запросов.

Работа должна использовать базу данных в docker-контейнере.


#### Реализация

За основу взят пакет `ru.otus.torwel.core` из предыдущего ДЗ (Самодельный ORM).
В пакете `ru.otus.torwel.hibernate` реализован функционал сохранения и чтения
объекта `Client` через Hibernate.
Конфигурация Hibernate вынесена в файл `hw10-ormHibernate/src/main
/resources/hibernate.cfg.xml`.

В класс `Client` добавлены поля типа `AddressDataSet` и `List<PhoneDataSet> phones`.
Все три класса размечены таким образом, чтобы при сохранении/чтении объека `Client`
каскадно сохранялись/читались вложенные объекты.


#### Как использовать

Работа реализованного пакета продемонстрирована в тесте `DbServiceClientImplTest.java`.


---

<a name="hw11"></a>
### HW-11. DIY Cache Engine.


#### Постановка задачи

Цель: Научится применять `WeakHashMap`, понять базовый принцип организации
кеширования.


#### Реализация

Реализован класс `MyCache` из вебинара. Использован класс `WeakHashMap`
для хранения значений.

Добавлено кэширование в класс `DBService` из задания про Hibernate ORM.


#### Как использовать

Должен быть запущен Docker с PostgreSQL. Команды запуска и остановки в директории
`hw11-DIYCacheEngine/docker`.

Для демонстрации выполненного задания в `Demo.main(...)` запускается два метода:
- `testClientLoadingWithCache(DbServiceClientImpl)` - для оценки скорости загрузки
  объекта с помощью кэша и без;
- `testCacheClearing(DbServiceClientImpl)` - для проверки очистки кэша при недостатке
  памяти. В параметрах VM необходимо указать -Xmx15g.


---

<a name="hw12"></a>
### HW-12. Web-server.

#### Постановка задачи

Цель: Научиться создавать серверный и пользовательский http-интерфейсы.
Научиться встраивать web-сервер в уже готовое приложение.

Встроить web-сервер в приложение из ДЗ про Hibernate ORM.
Сделать стартовую страницу, на которой админ должен аутентифицироваться.
Сделать админскую страницу для работы с пользователями.

На этой странице должны быть доступны следующие функции:
- создать пользователя;
- получить список пользователей.


#### Как использовать

Должен быть запущен docker-контейнер с Postgresql. 
Инструкции здесь: `hw12-webServer/docker`.

Web-сервер запускается методом `WebServerWithBasicSecurityDemo.main(...)`.
Стартовая страница: `http://localhost:8080`.

В работе используется Basic-аутентификация. Пользователи хранятся в БД.
Логин и пароль первого пользователя `user1:user1`.


---

<a name="hw13"></a>
### HW-13. DIY IoC Container.

#### Постановка задачи

Цель: В процессе создания своего контекста понять как работает основная часть
Spring framework.

Обязательная часть:
- Скачать заготовку приложения тренажера таблицы умножения из репозитория с
  примерами.
- В классе `AppComponentsContainerImpl` реализовать обработку полученной в
  конструкторе конфигурации, основываясь на разметке аннотациями из пакета
  `appcontainer`. Так же необходимо реализовать методы `getAppComponent`.
- В итоге должно получиться работающее приложение. Менять можно только
  класс `AppComponentsContainerImpl`.

Дополнительное задание (можно не делать):
- Разделить AppConfig на несколько классов и распределить по ним создание
  компонентов. В `AppComponentsContainerImpl` добавить конструктор, который
  обрабатывает несколько классов-конфигураций.

Дополнительное задание (можно не делать):
- В `AppComponentsContainerImpl` добавить конструктор, который принимает на
  вход имя пакета, и обрабатывает все имеющиеся там классы-конфигурации.


#### Реализация

Выполнены все три задания. Вся работа содержится в классе
`AppComponentsContainerImpl`.


#### Как использовать

Все варианты использования включены в `App.main(String[] args)`.


---

<a name="hw14"></a>
### HW-14. Spring MVC.


#### Постановка задачи

Цель: Научиться создавать war-пакеты и запускать их в TomCat.
Научиться пользоваться Thymeleaf.

- Собрать war для приложения из ДЗ про Web Server
- Создавать основные классы приложения, как Spring beans (Кэш, Dao, DBService)
- Настройку зависимостей выполнить с помощью Java/Annotation based конфигурации
- Для обработки запросов использовать @Controller и/или @RestController
- В качестве движка шаблонов использовать Thymeleaf
- Запустить веб приложение во внешнем веб сервере


#### Как использовать

Установить TomCat. Выполнить сборку модуля.
Файл `hw14-springMVC\build\libs\tor.war` скопировать в директорию
`webapps` веб-сервера TomCat.

Должен быть запущен docker-контейнер с Postgresql.
Инструкции здесь: `hw12-webServer/docker`.

Запустить веб-сервер `bin\startup.bat`.

Приложение будет доступно по адресу: `http://localhost:8080/tor/`.


---

