# Информационная система для подачи заявок
Запросы:
-----------------------------------
**Авторизация пользователя (POST)**: _api/auth/signin_
* входные данные: userName, password; 
* выходные данные: accessToken, tokenType, userId, role

**Добавление нового пользователя (POST)**: _api/admin/create/user_
* входные данные: userName, password, name, lastName, middleName, departmentId, role (номер от 0 до 4); 
* выходные данные: ok
* замечание: номер роли: 0 - пользователь (ROLE_USER), 
1 - супер админ (ROLE_SUPER_ADMIN), 2 - владелец (ROLE_OWNER), 
3 - админ (ROLE_PRIMARY_ADMIN), 4 - резервный админ (ROLE_BACKUP_ADMIN)

**Получение информации пользователя (GET)**: _api/user/info_
* входные данные: userId;
* выходные данные: userId, userName, email, name, lastName, middleName, department
* пример: **api/user/info?userId=1**

**Получение информации всех пользователей (GET)**: _api/user/all_
* входные данные: нет;
* выходные данные: список(userId, userName, email, name, lastName, middleName, department)

**Получение всех заявок пользователя (GET)**: _api/request/all_
* входные данные: userId
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], idSystem
* пример: **api/user/all/user/request?userId=1**

**Создание новой заявки (POST)**: _api/request/add_
* входные данные: userId(список), privilegesId(список), idSystem
* выходные данные: ok

**Получение активных заявок конкретного пользователя (GET)**: _api/request/all/active_
* входные данные: userId
* выходные данные: список, где idRequest, status, history(список)[], 
users(cписок)[userId, userName, email, name, lastName, middleName, department],
fillingDate[day, month, year], expiryDate[day, month, year], privileges[id, title],  idSystem
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

**Проверить логин в системе(GET)**: _api/admin/exists/user_
* входные данные:  userName
* выходные данные: ошибка или 200
* пример: **api/admin/exists/user?userName=Oleg**

**Добавить новую систему(POST)**: _api/admin/system/add_
* входные данные:  title, ownerId, primaryAdminId, backupAdminId, privilegesId(список)
* выходные данные: ok

**Добавить новое подразделение(GET)**: _api/admin/department/add_
* входные данные:  title, unitId
* выходные данные: ok
* пример: **api/admin/department/add?title=Название&unitId=1**

**Добавить новую привилегию(GET)**: _api/admin/privilege/add_
* входные данные:  title, description
* выходные данные: ok
* пример: **api/admin/privilege/add?title=Название&description=Описание**

**Добавить новый отдел(GET)**: _api/admin/unit/add_
* входные данные:  title
* выходные данные: ok
* пример: **api/admin/unit/add?title=Название**

**Удалить систему(GET)**: _api/admin/system/delete_
* входные данные:  systemId
* выходные данные: ok
* пример: **api/admin/unit/add?systemId=1**

**Удалить подразделение(GET)**: _api/admin/department/delete_
* входные данные:  departmentId
* выходные данные: ok
* пример: **api/admin/department/add?departmentId=1**

**Удалить привилегию(GET)**: _api/admin/privilege/delete_
* входные данные:  privilegeId
* выходные данные: ok
* пример: **api/admin/privilege/add?privilegeId=1**

**Удалить отдел(GET)**: _api/admin/unit/delete_
* входные данные:  unitId
* выходные данные: ok
* пример: **api/admin/unit/add?unitId=1**

**Удалить пользователя(GET)**: _api/admin/user/delete_
* входные данные:  userId
* выходные данные: ok
* пример: **api/admin/user/add?userId=1**

**Удалить заявку(GET)**: _api/admin/request/delete_
* входные данные:  requestId
* выходные данные: ok
* пример: **api/admin/request/add?requestId=1**

**Принять заявку со стороны владельца(GET)**: _api/request/approval/owner_
* входные данные:  requestId, userId
* выходные данные: ok
* пример: **api/request/approval/owner?requestId=1&userId=1**

**Принять заявку со стороны администратора(GET)**: _api/request/approval/admin_
* входные данные:  requestId, userId
* выходные данные: ok
* пример: **api/request/approval/admin?requestId=1&userId=1**

**Отклонить заявку со стороны владельца(GET)**: _api/request/rejection/owner_
* входные данные:  requestId, userId, reason
* выходные данные: ok
* пример: **api/request/rejection/owner?requestId=1&userId=1&reason=Причина**

**Принять заявку со стороны администратора(GET)**: _api/request/rejection/admin_
* входные данные:  requestId, userId, reason
* выходные данные: ok
* пример: **api/request/rejection/admin?requestId=1&userId=1&reason=Причина**

**Получить заявки администратора(GET)**: _api/request/all/admin_
* входные данные: userId
* выходные данные: ok
* пример: **api/request/all/admin?&userId=1**

**Получить заявки владельца(GET)**: _api/request/all/owner_
* входные данные: userId
* выходные данные: ok
* пример: **api/request/all/owner?&userId=1**

**Обновить пароль пользователя(GET)**: _api/user/update/password_
* входные данные: userName, password
* выходные данные: ok
* пример: **api/user/update/password?&userName=Логин&password=qwerty**

**Обновить электронную почту пользователя(GET)**: _api/user/update/email_
* входные данные: userName, email
* выходные данные: ok
* пример: **api/user/update/email?&userName=Логин&email=info@example.com**

**Получить информацию о заявке(GET)**: _api/request/info_
* входные данные: requestId
* выходные данные: idRequest, status, users(список), privileges, filingDate, expiryDate, idSystem, system (Название)
* пример: **api/request/info?&requestId=1&**