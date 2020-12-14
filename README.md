# Информационная система для подачи заявок
Запросы:
-----------------------------------
**Авторизация пользователя (POST)**: _api/auth/signin_
* входные данные: userName, password; 
* выходные данные: accessToken, tokenType, userId

**Добавление нового пользователя (POST)**: _api/auth/create/user_
* входные данные: userName, password, name, lastName, middleName, departmentId; 
* выходные данные: ok

**Получение информации пользователя (GET)**: _api/user/info_
* входные данные: userId;
* выходные данные: userId, userName, email, name, lastName, middleName, department
* пример: **api/user/info?userId=1**

**Получение всех заявок пользователя (GET)**: _api/request/all_
* входные данные: userId
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* пример: **api/user/all/user/request?userId=1**

**Создание новой заявки (POST)**: _api/request/add_
* входные данные: userId(список), fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* выходные данные: ok

**Получение активных заявок конкретного пользователя (GET)**: _api/request/all/active_
* входные данные: userId
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* пример: **api/user/active/request?userId=1**

**Получить все отделы (GET)**: _api/user/unit/all_
* входные данные: нет
* выходные данные: data(список - id, title)

**Получить все подразделения (GET)**: _api/user/department/all_
* входные данные:  unitId
* выходные данные: data(список - id, title)
* пример: **api/user/department/all?unitId=1**

**Получить все информационные системы (GET)**: _api/user/system/all_
* входные данные:  нет
* выходные данные: data(список - title, privileges([id, title, description], ownerId, primaryAdminId, backupAdminId)

**Получить все привилегии информационной системы (GET)**: _api/user/system/info_
* входные данные:  systemId
* выходные данные: title, privileges([id, title, description], ownerId, primaryAdminId, backupAdminId
* пример: **api/user/system/info?systemId=1**