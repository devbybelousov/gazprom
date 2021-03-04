# REST-сервис для подачи заявок по получению доступа к информационной системе
_REST-сервис для подачи заявок по получению доступа к информационной системе_ — это интерфейс, который позволяет управлять опросами с помощью http-запросов к специальному серверу. 
Синтаксис запросов и тип возвращаемых ими данных строго определены на стороне самого сервиса.
Например, для получения информации о пользователе необходимо составить запрос такого вида:
```
http://localhost:8080/api/user/info?id=2
```
Сборка и запуск
----------------------------------
```
mvn clean istall
```

База данных
------------------------------------
Изменить поля в application.properties
```
spring.datasource.url = jdbc:mysql://[host]:[port]/gazprom_data?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useLegacyDatetimeCode=false&characterEncoding=utf8
spring.datasource.username = [username]
spring.datasource.password = [password]
```
Запросы:
-----------------------------------
### api/auth/login (POST)
Авторизация пользователя
* Параметры: 
    * **userName** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
* Результат: 
    * **accessToken** (_строка_) - токен для доступа
    * **tokenType** (_строка_) - тип токена
    * **userId** (_положительное число_) - идентификатор пользователя
    * **role** (_строка_) - роль пользователя

### api/user (POST)
Создание пользователя
* Параметры: 
    * **userName** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
    * **name** (_строка_) - имя пользователя
    * **lastName** (_строка_) - фамилия пользователя
    * **middleName** (_строка_) - отчество пользователя
    * **email** (_строка_) - почта пользователя
    * **departmentId** (_положительное число_) - идентификатор подразделения
    * **role** (_положительное число_) - номер роли
        * 1 - супер админ
        * 2 - пользователь
        * 3 - владелец
        * 4 - администратор
        * 5 - резервный администратор
* Результат: После успешного выполнения возвращает 1.

### api/user/exists (GET)
Проверка пользователя в системе
* Параметры:
    * **userName** (_строка_) - логин пользователя
* Результат: После успешного выполнения возвращает 1.

### api/user/info (GET)
Получение информации о пользователе
* Параметры: 
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: 
   * **userId** (_положительное число_) - идентификатор пользователя
   * **userName** (_строка_) - логин пользователя
   * **password** (_строка_) - пароль пользователя
   * **email** (_строка_) - почта пользователя
   * **name** (_строка_) - имя пользователя
   * **lastName** (_строка_) - фамилия пользователя
   * **middleName** (_строка_) - отчество пользователя
   * **department** (_строка_) - подразделение
   
### api/user (GET)
Получение списка пользователей
* Результат: массив объектов, описывающих пользователей

### api/user (PUT)
 Редактирование пользователя
* Параметры: 
    * **userName** (_строка_) - логин пользователя
    * **password** (_строка_) - пароль пользователя
    * **name** (_строка_) - имя пользователя
    * **lastName** (_строка_) - фамилия пользователя
    * **middleName** (_строка_) - отчество пользователя
    * **email** (_строка_) - почта пользователя
    * **departmentId** (_положительное число_) - идентификатор подразделения
    * **role** (_положительное число_) - номер роли
        * 1 - супер админ
        * 2 - пользователь
        * 3 - владелец
        * 4 - администратор
        * 5 - резервный администратор
* Результат: После успешного выполнения возвращает 1.

### api/user (DELETE)
Удаление пользователя
* Параметры:
    * **id** (_положительное число_) - идентификатор пользователя
* Результат: После успешного выполнения возвращает 1.

### api/request (GET)
Получение списка заявок пользователя
* Параметры:
    * **userId** (_положительное число_) -  идентификатор пользователя
    * **filter** (_целое число_) - номер фильтра
        * -1 - без фильтра
        * 1 - фильтр по статусу
        * 2 - фильтр по дате
        * 3 - фильтр по системе
    * **status** (_строка_) - статус заявки, при filter = 1
    * **date** (_строка_) - дата формат гггг-мм-дд, при filter = 2
    * **systemId** (_положительное число_) - идентификатор информационной систем, при filter = 3
* Результат: массив объектов, описывающих заявки

### api/request/info (GET)
Получить информацию о заявке 
* Параметры:
    * **id** (_положительное число_) - идентификатор заявки
* Результат:
    * **id** (_положительное число_) - идентификатор заявки
    * **status** (_строка_) - активный статус заявки
    * **fillingDate** (_строка_) - дата создания заявки в формате дд.мм.гггг
    * **expiryDate** (_строка_) - дата принятия заявки в формате дд.мм.гггг
    * **informationSystem** (_объект_) - объект, описывающий информационную систему
    
### api/request (POST)
Создание новой заявки
* Параметры:
    * **usersId** (_массив положительных чисел_) - список идентификаторов пользователей
    * **privilegesId** (_массив положительных чисел_) - список идентификаторов привилегий
    * **systemId** (_положительное число_) - идентификатор информационной системы
* Результат: После успешного выполнения возвращает 1.

### api/request/admin
Получить заявки администратора
* Параметры:
    * **id** (_положительное число_) - идентификатор администратора
* Результат: массив объектов, описывающих заявки

### api/request/owner
Получить заявки владельца
* Параметры:
    * **id** (_положительное число_) - идентификатор владельца
* Результат: массив объектов, описывающих заявки

### api/request/check/owner
Принять/отклонить заявку со стороны владельца
* Параметры:
    * **id** (_положительное число_) - идентификатор заявки
    * **userId** (_положительное число_) - идентификатор владельца
    * **reason** (_строка_) - причина отклонения заявки*
* Результат: После успешного выполнения возвращает 1
*Необязательное поле 

### api/request/check/admin
Принять/отклонить заявку со стороны администратора
* Параметры:
    * **id** (_положительное число_) - идентификатор заявки
    * **userId** (_положительное число_) - идентификатор администратора
    * **reason** (_строка_) - причина отклонения заявки*
* Результат: После успешного выполнения возвращает 1
*Необязательное поле

### api/request (DELETE)
Удаление заявки
* Параметры:
    * **id** (_положительное число_) - идентификатор заявки
* Результат: После успешного выполнения возвращает 1.

### api/unit (GET)
Получение всех отделов
* Результат: массив объектов, описывающих отделы

### api/unit (POST)
Создание отдела
* Параметры:
    * **title** (_строка_) - название отдела
* Результат: После успешного выполнения возвращает 1.

### api/unit (DELETE)
Удаление отдела
* Параметры:
    * **id** (_положительное число_) - идентификатор отдела
* Результат: После успешного выполнения возвращает 1.

### api/department (GET)
Получение всех подразделений по отделу
* Параметры:
    * **unitId** (_положительное число_) - идентификатор отдела
* Результат: массив объектов, описывающих подразделений

### api/department (POST)
Создание подразделения
* Параметры:
    * **title** (_строка_) - название подразделения
    * **unitId** (_положительное число_) - идентификатор отдела
* Результат: После успешного выполнения возвращает 1.

### api/department (DELETE)
Удаление подразделения
* Параметры:
    * **id** (_положительное число_) - идентификатор подразделения
* Результат: После успешного выполнения возвращает 1.

### api/system (GET)
Получение всех информационных систем
* Результат: массив объектов, описывающих информационные системы

### api/system/info (GET)
Получение информации о системе
* Параметры:
    * **id** (_положительное число_) - идентификатор информационной системы
* Результат: объект описывающий информационную систему

### api/system (POST)
Создание информационной системы
* Параметры:
    * **title** (_строка_) - название информационной системы
    * **ownerId** (_положительное число_) - идентификатор владельца системы
    * **primaryAdminId** (_положительное число_) - идентификатор главного администратора системы
    * **backupAdminId** (_положительное число_) - идентификатор резервного администратора системы
    * **privilegesId** (_массив положительных чисел_) - идентификатор отдела
* Результат: После успешного выполнения возвращает 1.

### api/system (DELETE)
Удаление информационной системы
* Параметры:
    * **id** (_положительное число_) - идентификатор информационной системы
* Результат: После успешного выполнения возвращает 1.

### api/privilege (POST)
Создание привилегии
* Параметры:
    * **title** (_строка_) - название привилегии
    * **description** (_строка_) - описание привилегии
* Результат: После успешного выполнения возвращает 1.

### api/privilege (DELETE)
Удаление привилегии
* Параметры:
    * **id** (_положительное число_) - идентификатор привилегии
* Результат: После успешного выполнения возвращает 1.
