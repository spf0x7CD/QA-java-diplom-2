# Тесты API

*   Тестирование API сайта https://stellarburgers.nomoreparties.site.
*   Документация к API https://code.s3.yandex.net/qa-automation-engineer/java/cheatsheets/paid-track/diplom/api-documentation.pdf

## Задание

### Создание пользователя

*   Создать уникального пользователя
*   Создать пользователя, который уже зарегистрирован
*   Создать пользователя и не заполнить одно из обязательных полей

### Логин пользователя

*   Логин под существующим пользователем
*   Логин с неверным логином и паролем

### Изменение данных пользователя

*   С авторизацией
	+   Изменить любое поле
*   Без авторизации
	+   Изменить любое поле
	+   Система должна вернуть ошибку

### Создание заказа

*   С авторизацией
*   Без авторизации
*   С ингредиентами
*   Без ингредиентов
*   С неверным хешем ингредиентов

### Получение заказов конкретного пользователя

*   Авторизованный пользователь
*   Неавторизованный пользователь

## Используемые библиотеки

*   **JUnit**: для написания и запуска тестов
*   **Allure**: для генерации отчетов о тестировании
*   **Rest-Assured**: для тестирования RESTful API
*   **Gson**: для работы с JSON-данными
*   **Project Lombok**: для автоматической генерации кода (геттеров, сеттеров, конструкторов)
*   **JavaFaker**: для генерации фейковых данных для тестирования
