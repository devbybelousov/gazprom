# Информационная система для подачи заявок
Запросы:
-----------------------------------
**Авторизация пользователя (POST)**: _api/auth/signin_
* входные данные: userName, password; 
* выходные данные: accessToken, tokenType, userId

**Добавление нового пользователя (POST)**: _api/admin/create/user_
* входные данные: userName, password, name, lastName, middleName, roleId, departmentId; 
* выходные данные: ok

**Получение информации пользователя (GET)**: _api/user/info_
* входные данные: id;
* выходные данные: userId, userName, email, name, lastName, middleName, department
* пример: api/user/info?id=1

**Получение всех заявок пользователя (GET)**: _api/user/all/user/request_
* входные данные: id
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* пример: api/user/all/user/request?id=1

**Создание новой заявки (POST)**: _api/user/add/request_
* входные данные: userId(список), fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* выходные данные: ok

**Получение активных заявок конкретного пользователя (GET)**: _api/user/active/request_
* входные данные: id
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* пример: api/user/active/request?userId=1

**Получить все отделы (GET)**: _api/user/all/unit_
* входные данные: нет
* выходные данные: data(список - id, title)
* пример: api/user/unit

**Получить все должности (GET)**: _api/admin/all/role_
* входные данные:  нет
* выходные данные: data(список - id, role)
* пример: api/admin/role?publicKey=...

**Получить все подразделения (GET)**: _api/user/all/department_
* входные данные:  нет
* выходные данные: data(список - id, title)
* пример: api/user/department

**Получить все информационные системы (GET)**: _api/user/all/system_
* входные данные:  нет
* выходные данные: data(список - id, title)
* пример: api/user/system

**Изменить статус заявки (GET)**: _api/admin/update/status_
* входные данные: requestId, status*
* выходные данные: ok
* пример: api/admin/update/status?requestId=1&status=STATUS_ENABLE

**Добавить новую информационную систему (POST)**: _api/admin/add/system_
* входные данные: message(title)
* выходные данные: ok

**Добавить новый отдел (POST)**: _api/admin/add/unit_
* входные данные: message(title)
* выходные данные: ok

**Добавить новое подразделение (POST)**: _api/admin/add/department_
* входные данные: message(title, unitId)
* выходные данные: ok

**Получение всех активных заявок (GET)**: _api/user/all/request/active_
* входные данные: publicKey(клиента),
* выходные данные: message(список, где id, 
user(id, userName, name, lastName, middleName, role, department),system, validity, status, date)
* пример: api/admin/all/request?publicKey=...

_*status - нужно отправить одно из (STATUS_ENABLE, STATUS_DISABLE, STATUS_REFUSED, STATUS_SHIPPED)_

Подключение к БД:
-----------------------------------
1. Database -> + -> Data Source -> MySQL\
user: **root**\
password: **1234**
2. Запустить файл _createData_;
3. Запустить файл _createTable_;
4. Запустить файл _insertData_;\
Предупреждение! Не забудь добавить новую БД в настройки Data Source\
Database: _gazprom_data_