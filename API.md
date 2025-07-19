## **Описание эндпоинтов**

## **1\. Аутентификация и авторизация**

| Метод | Путь	                          | Описание	                 | Доступ |
|-------|---------------------------------|------------------------------|--------|
| POST  | /api/auth/login	              | Вход по email/password	     | Все    |
| POST  | /api/auth/refresh	              | Обновление JWT-токена	     | Все    |
| GET   | /oauth2/authorization/{provider}|	OAuth2-логин (Яндекс, Google)| Все    |
| POST  | /api/auth/validate-email	      | Проверка корпоративной почты | Все    |

## **2\. Управление пользователями (User)**

| Метод  | Путь	               | Описание	                             | Доступ |
|--------|---------------------|-----------------------------------------|--------|
| GET    | /api/users/me	   | Данные текущего пользователя	         | Все    |
| GET    | /api/users	       | Список всех пользователей (с пагинацией)| ADMIN  |
| POST   | /api/users	       | Создание пользователя                   | ADMIN  |
| PUT    | /api/users/{userId} | Обновление пользователя                 | ADMIN  |
| DELETE | /api/users/{userId} | Деактивация пользователя                | ADMIN  |

## **3\. Роли (Role)**

| Метод | Путь	              | Описание	      | Доступ |
|-------|---------------------|-------------------|--------|
| GET   | /api/roles	      | Список всех ролей |	ADMIN  |
| POST  | /api/roles	      | Создание роли	  | ADMIN  |
| PUT   | /api/roles/{roleId} |	Обновление роли	  | ADMIN  |

## **4\. Группы пользователей (UserGroup)**

| Метод  | Путь	                               | Описание	                      | Доступ |
|--------|-------------------------------------|----------------------------------|--------|
| GET	 | /api/groups	                       | Список групп	                  | ADMIN  |
| POST	 | /api/groups	                       | Создание группы	              | ADMIN  |
| PUT	 | /api/groups/{groupId}	           | Обновление группы	              | ADMIN  |
| POST	 | /api/groups/{groupId}/users         | Добавление пользователя в группу | ADMIN  |
| DELETE | /api/groups/{groupId}/users/{userId}| Удаление пользователя из группы  | ADMIN  |

## **5\. Отчёты (Report)**

| Метод  | Путь	                           | Описание	                 | Доступ         |
|--------|---------------------------------|-----------------------------|----------------|
| GET	 | /api/reports	                   | Список доступных отчётов	 | USER           |
| POST	 | /api/reports	                   | Создание отчёта             | ANALYST, ADMIN |
| GET	 | /api/reports/{reportId}	       | Получение метаданных отчёта | USER           |
| PUT	 | /api/reports/{reportId}	       | Обновление отчёта	         | ANALYST, ADMIN |
| DELETE | /api/reports/{reportId}	       | Удаление отчёта             | ADMIN          |
| POST	 | /api/reports/{reportId}/execute | Запуск отчёта	             | USER           |
| GET	 | /api/reports/{reportId}/history | История изменений отчёта    | ANALYST, ADMIN |

## **6\. Параметры отчётов (ReportParameter)**

| Метод | Путь	                                       | Описание	          | Доступ         |
|-------|----------------------------------------------|----------------------|----------------|
| GET	| /api/reports/{reportId}/parameters	       | Параметры отчёта     | USER           |
| POST	| /api/reports/{reportId}/parameters	       | Добавление параметра | ANALYST, ADMIN |
| PUT	| /api/reports/{reportId}/parameters/{paramId} | Обновление параметра | ANALYST, ADMIN |

## **7\. Коллекции (Collection)**

| Метод  | Путь	                                             | Описание	                    | Доступ         |
|--------|---------------------------------------------------|------------------------------|----------------|
| GET	 | /api/collections                                  | Список коллекций	            | USER           |
| POST	 | /api/collections	                                 | Создание коллекции	        | ANALYST, ADMIN |
| PUT	 | /api/collections/{collectionId}	                 | Обновление коллекции	        | ANALYST, ADMIN |
| POST	 | /api/collections/{collectionId}/reports	         | Добавление отчёта в коллекцию| ANALYST, ADMIN |
| DELETE | /api/collections/{collectionId}/reports/{reportId}| Удаление отчёта из коллекции | ANALYST, ADMIN |

## **8\. Доступ к коллекциям (CollectionAccess)**

| Метод  | Путь	                                            | Описание	                              | Доступ |
|--------|--------------------------------------------------|-----------------------------------------|--------|
| POST	 | /api/collections/{collectionId}/access           | Назначение доступа (группе/пользователю)|	ADMIN  |
| DELETE | /api/collections/{collectionId}/access/{accessId}| Отзыв доступа	                          | ADMIN  |

## **9\. Подключения к БД (DatabaseConnection)**

| Метод | Путь	                               | Описание	            | Доступ |
|-------|--------------------------------------|------------------------|--------|
| GET   | /api/connections                     | Список подключений     | ADMIN  |
| POST  | /api/connections                     | Создание подключения	| ADMIN  |
| PUT   | /api/connections/{connectionId}      | Обновление подключения | ADMIN  |
| POST  | /api/connections/{connectionId}/test | Тест подключения       | ADMIN  |

## **10\. Очередь запросов**

| Метод | Путь	            | Описание	                      | Доступ |
|-------|-------------------|---------------------------------|--------|
| GET   | /api/queue        | Текущие задачи                  | ADMIN  |
| GET   | /api/queue/config | Получение конфигурации очереди  | ADMIN  |
| PUT   | /api/queue/config | Обновление конфигурации очереди | ADMIN  |

## **11\. Запросы на создание отчётов**

| Метод | Путь	                                  | Описание	              | Доступ         |
|-------|-----------------------------------------|---------------------------|----------------|
| POST  | /api/report-requests                    | Создание запроса	      | USER           |
| PUT   | /api/report-requests/{requestId}/status | Изменение статуса запроса | ANALYST, ADMIN |

## **Security Matrix**

| Роль    | Доступные эндпоинты                                  |
|---------|------------------------------------------------------|
| USER    | /api/reports, /api/collections, /api/report-requests |
| ANALYST | Все USER + создание/редактирование отчётов           |
| ADMIN   | Полный доступ                                        |