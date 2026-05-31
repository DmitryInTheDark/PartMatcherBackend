PartMatcher — техническая документация (сводка для дипломной работы)

1. Архитектура

- Технологии: Spring Boot 4, Java 21, Spring Security (JWT), Spring Web, Spring Data JPA, H2/Postgres (JPA), WebSocket (STOMP), springdoc-openapi.
- Уровни: контроллеры (REST + WebSocket), сервисы (бизнес-логика), репозитории (JPA), сущности (JPA), DTO + mapper'ы, конфигурация безопасности и WebSocket.
- Паттерны: layered architecture, DTO mapping через мапперы, JWT stateless-аутентификация.

2. ER-диаграмма (текст)

- users (User)
  - id PK
  - name
  - email (unique, индекс)
  - password
  - roles (user_roles через ElementCollection)
  - favoriteParts (many-to-many -> parts via user_favorites)

- vehicles (Vehicle)
  - id PK
  - vin (unique, индекс)
  - brand, model, year, engine, bodyType
  - compatibleParts (many-to-many -> parts via vehicle_parts)

- parts (Part)
  - id PK
  - article (unique, индекс)
  - name, manufacturer, description, price, imageUrl, category
  - compatibleVehicles (many-to-many)
  - analogs (one-to-many -> AnalogPart.originalPart)

- analog_parts (AnalogPart)
  - id PK
  - original_part_id FK -> parts.id
  - analog_part_id FK -> parts.id

- support_chat_messages (SupportChatMessage)
  - id PK
  - sender_id FK -> users.id
  - recipient_id FK -> users.id
  - content, sentAt, read

- search_history (SearchHistory)
  - id PK
  - user_id FK -> users.id
  - query, searchedAt

- vin_history (VinHistory)
  - id PK
  - user_id FK -> users.id
  - vin, searchedAt

3. Индексы и уникальные ограничения (итог)

- User.email: колонка помечена unique; добавлен явный индекс `idx_users_email`.
- Vehicle.vin: колонка помечена unique; добавлен явный индекс `idx_vehicles_vin`.
- Part.article: колонка помечена unique; добавлен явный индекс `idx_parts_article`.
- Уникальные ограничения определены через `@Column(unique = true)` — это создаёт уникальные индексы.

4. REST API (краткий список)

- /api/auth/register [POST] — регистрация (UserRegistrationDto)
- /api/auth/login [POST] — вход (AuthRequestDto)

- /api/vehicles/vin/{vin} [GET] — поиск автомобиля по VIN и совместимые детали
- /api/vehicles/vin/{vin}/parts [GET] — получить детали по VIN
- /api/vehicles [POST] (ADMIN) — создать автомобиль
- /api/vehicles/{id} [PUT] (ADMIN) — обновить автомобиль
- /api/vehicles/{id} [DELETE] (ADMIN) — удалить автомобиль
- /api/vehicles/{vehicleId}/parts/{partId} [POST] (ADMIN) — связать деталь с автомобилем
- /api/vehicles/{vehicleId}/parts/{partId} [DELETE] (ADMIN) — удалить связь

- /api/parts/search [GET] — поиск деталей
- /api/parts/{id}/analogs [GET] — получить аналоги детали
- /api/parts/vehicle/{vin} [GET] — получить детали по VIN
- /api/parts [POST] (ADMIN) — создать деталь
- /api/parts/{id} [PUT] (ADMIN) — обновить деталь
- /api/parts/{id} [DELETE] (ADMIN) — удалить деталь

- /api/user/me [GET] — профиль текущего пользователя
- /api/user/favorites [GET] — избранные детали
- /api/user/favorites/{partId} [POST] — добавить в избранное
- /api/user/favorites/{partId} [DELETE] — удалить из избранного
- /api/user/history/search [GET] — история поисковых запросов
- /api/user/history/vin [GET] — история VIN-поисков

- /api/support/chat/history?counterpartyId={id} [GET] — история переписки с другим пользователем (доступно только для аутентифицированных)

- /api/admin/statistics [GET] (ADMIN) — системная статистика

(Полная документация доступна через Swagger UI — springdoc-openapi) — OpenAPI конфиг в OpenApiConfig.

5. WebSocket endpoints

- STOMP endpoint: /ws-support (SockJS fallback)
- Application destination prefix: /app
- Broker destinations: /topic
- WebSocket mapping:
  - /app/support/message — отправка сообщения поддержки
  - подписка: /topic/support — рассылка сообщений чата

WebSocket подключение требует JWT — см. WebSocketJwtChannelInterceptor (проверяет заголовок Authorization / authToken при STOMP CONNECT).

6. JWT authentication flow (кратко)

- Клиент получает JWT при логине (/api/auth/login).
- Для REST-запросов: передавать header `Authorization: Bearer <token>`.
- Для WebSocket/STOMP: при CONNECT передать native header `Authorization: Bearer <token>` (или `authToken`).
- Сервер валидирует JWT в `JwtAuthenticationFilter` (REST) и в `WebSocketJwtChannelInterceptor` (WebSocket), затем устанавливает `Authentication` в SecurityContext или в STOMP accessor.

7. Алгоритм интеллектуального поиска (реализация)

- Запрос нормализуется (нижний регистр, Unicode-normalization, удаление диакритики).
- Токенизируется по пробелам, удаляются пустые токены.
- Для каждой детали рассчитывается взвешенная релевантность по полям:
  - name (вес 100)
  - article (вес 90)
  - manufacturer (вес 70)
  - category (вес 50)
- Правила подсчёта для токена и поля:
  - точное совпадение => полный вес
  - contains => 85% веса
  - иначе считается Levenshtein-сходство (1 - distance/maxLen); при схожести >= 0.35 начисляется weight * similarity * 0.9
- Список результатов сортируется по итоговой оценке (desc), фильтруются элементы с нулевым счётом.
- Защита от NPE: поля нормализуются с учётом null -> пустая строка.

8. Роли и права доступа

- USER — обычный пользователь: доступ к профилю, избранному, истории, отправка сообщений через WebSocket/REST в чат поддержки.
- SUPPORT — агент поддержки: доступ к `/api/support/**` и просмотру переписки.
- ADMIN — полный доступ к административным операциям: `/api/admin/**`, создание/удаление/редактирование сущностей (Vehicle, Part).

9. Что исправлено/уделано в кодовой базе (резюме изменений в рамках аудита)

- Исправлен потенциальный NullPointerException в `PartService.normalize` — теперь метод безопасно обрабатывает null.
- Добавлены явные DB-индексы на `users.email`, `vehicles.vin`, `parts.article`.
- DTO-валидация усилена: пароль — `@Size(min=6,max=100)` (Auth/UserRegistration), VIN — `@Size(11,17)` (VehicleCreate), цена деталей — `@Positive`.
- Добавлены `@Operation` аннотации (OpenAPI) к контроллерным методам, которые ранее не документировались, чтобы Swagger показывал все endpoint'ы.
- Добавлен/расширен `DataInitializer` для сидирования демонстрационных данных (минимум: 3 пользователя, 20 автомобилей, 100 деталей, 20 аналогов).

10. Файлы изменений

- Изменены: `PartService.java`, `User.java`, `Vehicle.java`, `Part.java`, несколько DTO (AuthRequestDto, UserRegistrationDto, VehicleCreateDto, PartCreateDto, PartUpdateDto), контроллеры (`PartController`, `UserController`, `SupportChatController`, `VehicleController`), `DataInitializer.java`.

11. Как запустить демонстрацию

- Просто запустите приложение (например, `./mvnw spring-boot:run`) — при первом старте если БД пуста, `DataInitializer` создаст демо-данные.
- Откройте Swagger UI: `/swagger-ui.html` или `/swagger-ui/index.html` (springdoc-openapi автоматическая конфигурация).

---

Документ подготовлен для включения в дипломную записку: при необходимости могу сгенерировать отдельный PDF / LaTeX-фрагмент и более развёрнутые ER-диаграммы (PlantUML).