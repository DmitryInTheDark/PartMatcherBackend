# PartMatcher Backend API Contract

## Общие правила
- Базовый URL: `http://<host>:8080`
- JWT заголовок: `Authorization: Bearer <token>`
- Все endpoint’ы, кроме `/api/auth/**`, требуют JWT.
- Ответы об ошибках возвращаются в виде:
  ```json
  {
    "message": "Текст ошибки",
    "success": false
  }
  ```

---

# AUTH API

## 1. POST /api/auth/register
- JWT: не требуется
- Роль: не требуется
- Request DTO: `UserRegistrationDto`
- Response DTO: `AuthResponseDto`
- Пример запроса:
  ```json
  {
    "name": "Demo User",
    "email": "user@partmatcher.local",
    "password": "password"
  }
  ```
- Пример ответа:
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 3,
      "name": "Demo User",
      "email": "user@partmatcher.local",
      "roles": ["USER"]
    }
  }
  ```
- Возможные ошибки:
  - `400` — валидация полей, сообщение об ошибке
  - `400` — бизнес-ошибка, если email уже существует
  - `500` — внутренняя ошибка сервера

## 2. POST /api/auth/login
- JWT: не требуется
- Роль: не требуется
- Request DTO: `AuthRequestDto`
- Response DTO: `AuthResponseDto`
- Пример запроса:
  ```json
  {
    "email": "user@partmatcher.local",
    "password": "password"
  }
  ```
- Пример ответа:
  ```json
  {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 3,
      "name": "Demo User",
      "email": "user@partmatcher.local",
      "roles": ["USER"]
    }
  }
  ```
- Возможные ошибки:
  - `400` — неверный формат данных
  - `401` — неверные учетные данные
  - `500` — внутренняя ошибка сервера

---

# USER API

## 3. GET /api/user/me
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `UserDto`
- Пример ответа:
  ```json
  {
    "id": 3,
    "name": "Demo User",
    "email": "user@partmatcher.local",
    "roles": ["USER"]
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — пользователь не найден
  - `500` — внутренняя ошибка сервера

## 4. GET /api/user/favorites
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<PartDto>`
- Пример ответа:
  ```json
  [
    {
      "id": 10,
      "article": "ART000010",
      "name": "Part 10",
      "manufacturer": "Bosch",
      "description": "Demo description for part 10",
      "price": 19.0,
      "imageUrl": null,
      "category": "Filters"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `500` — внутренняя ошибка сервера

## 5. POST /api/user/favorites/{partId}
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример: `POST /api/user/favorites/10`
- Пример ответа:
  ```json
  {
    "message": "Деталь добавлена в избранное",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — деталь не найдена
  - `500` — внутренняя ошибка сервера

## 6. DELETE /api/user/favorites/{partId}
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример: `DELETE /api/user/favorites/10`
- Пример ответа:
  ```json
  {
    "message": "Деталь удалена из избранного",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — избранная деталь не найдена
  - `500` — внутренняя ошибка сервера

## 7. GET /api/user/history/search
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<SearchHistoryDto>`
- Пример ответа:
  ```json
  [
    {
      "id": 5,
      "query": "Bosch filter",
      "searchedAt": "2026-05-31T12:34:56"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `500` — внутренняя ошибка сервера

## 8. GET /api/user/history/vin
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<VinHistoryDto>`
- Пример ответа:
  ```json
  [
    {
      "id": 3,
      "vin": "JT123456789012345",
      "searchedAt": "2026-05-31T12:35:10"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `500` — внутренняя ошибка сервера

---

# VEHICLE API

## 9. GET /api/vehicles/vin/{vin}
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `VehicleSearchResultDto`
- Пример: `GET /api/vehicles/vin/JT123456789012345`
- Пример ответа:
  ```json
  {
    "vehicle": {
      "id": 1,
      "vin": "JT123456789012345",
      "brand": "Toyota",
      "model": "Camry",
      "year": 2020,
      "engine": "2.5L 4-Cylinder",
      "bodyType": "Sedan"
    },
    "compatibleParts": [
      {
        "id": 1,
        "article": "T-OF-001",
        "name": "Toyota Oil Filter",
        "manufacturer": "Toyota",
        "description": "Оригинальный масляный фильтр для Toyota Camry",
        "price": 18.90,
        "imageUrl": "https://example.com/images/toyota-oil-filter.jpg",
        "category": "Filter"
      }
    ]
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — автомобиль не найден
  - `500` — внутренняя ошибка сервера

## 10. GET /api/vehicles/vin/{vin}/parts
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<PartDto>`
- Пример: `GET /api/vehicles/vin/JT123456789012345/parts`
- Пример ответа:
  ```json
  [
    {
      "id": 1,
      "article": "T-OF-001",
      "name": "Toyota Oil Filter",
      "manufacturer": "Toyota",
      "description": "Оригинальный масляный фильтр для Toyota Camry",
      "price": 18.90,
      "imageUrl": "https://example.com/images/toyota-oil-filter.jpg",
      "category": "Filter"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — автомобиль не найден
  - `500` — внутренняя ошибка сервера

## 11. POST /api/vehicles
- JWT: требуется
- Роль: ADMIN
- Request DTO: `VehicleCreateDto`
- Response DTO: `VehicleDto`
- Пример запроса:
  ```json
  {
    "vin": "JT999999999999999",
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023,
    "engine": "1.8L",
    "bodyType": "Sedan"
  }
  ```
- Пример ответа:
  ```json
  {
    "id": 21,
    "vin": "JT999999999999999",
    "brand": "Toyota",
    "model": "Corolla",
    "year": 2023,
    "engine": "1.8L",
    "bodyType": "Sedan"
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `400` — invalid VIN / missing fields
  - `500` — внутренняя ошибка сервера

## 12. PUT /api/vehicles/{id}
- JWT: требуется
- Роль: ADMIN
- Request DTO: `VehicleUpdateDto`
- Response DTO: `VehicleDto`
- Пример запроса:
  ```json
  {
    "brand": "Toyota",
    "model": "Camry",
    "year": 2021,
    "engine": "2.5L",
    "bodyType": "Sedan"
  }
  ```
- Пример ответа:
  ```json
  {
    "id": 1,
    "vin": "JT123456789012345",
    "brand": "Toyota",
    "model": "Camry",
    "year": 2021,
    "engine": "2.5L",
    "bodyType": "Sedan"
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — автомобиль не найден
  - `400` — validation error
  - `500` — внутренняя ошибка сервера

## 13. DELETE /api/vehicles/{id}
- JWT: требуется
- Роль: ADMIN
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример ответа:
  ```json
  {
    "message": "Автомобиль удален",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — автомобиль не найден
  - `500` — внутренняя ошибка сервера

## 14. POST /api/vehicles/{vehicleId}/parts/{partId}
- JWT: требуется
- Роль: ADMIN
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример: `POST /api/vehicles/1/parts/10`
- Пример ответа:
  ```json
  {
    "message": "Деталь связана с автомобилем",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — автомобиль или деталь не найдены
  - `500` — внутренняя ошибка сервера

## 15. DELETE /api/vehicles/{vehicleId}/parts/{partId}
- JWT: требуется
- Роль: ADMIN
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример: `DELETE /api/vehicles/1/parts/10`
- Пример ответа:
  ```json
  {
    "message": "Связь между автомобилем и деталью удалена",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — автомобиль или деталь не найдены
  - `500` — внутренняя ошибка сервера

---

# PART API

## 16. GET /api/parts/search
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: query param `query` (string)
- Response DTO: `List<PartDto>`
- Пример: `GET /api/parts/search?query=Bosch%20filter`
- Пример ответа:
  ```json
  [
    {
      "id": 2,
      "article": "B-OF-020",
      "name": "Bosch Oil Filter",
      "manufacturer": "Bosch",
      "description": "Аналоговый масляный фильтр Bosch",
      "price": 14.50,
      "imageUrl": "https://example.com/images/bosch-oil-filter.jpg",
      "category": "Filter"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `400` — пустой query
  - `500` — внутренняя ошибка сервера

## 17. GET /api/parts/{id}/analogs
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<AnalogPartDto>`
- Пример: `GET /api/parts/1/analogs`
- Пример ответа:
  ```json
  [
    {
      "id": 1,
      "originalPart": {
        "id": 1,
        "article": "T-OF-001",
        "name": "Toyota Oil Filter",
        "manufacturer": "Toyota",
        "description": "Оригинальный масляный фильтр для Toyota Camry",
        "price": 18.90,
        "imageUrl": "https://example.com/images/toyota-oil-filter.jpg",
        "category": "Filter"
      },
      "analogPart": {
        "id": 2,
        "article": "B-OF-020",
        "name": "Bosch Oil Filter",
        "manufacturer": "Bosch",
        "description": "Аналоговый масляный фильтр Bosch",
        "price": 14.50,
        "imageUrl": "https://example.com/images/bosch-oil-filter.jpg",
        "category": "Filter"
      }
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — деталь не найдена
  - `500` — внутренняя ошибка сервера

## 18. GET /api/parts/vehicle/{vin}
- JWT: требуется
- Роль: любой аутентифицированный
- Request DTO: none
- Response DTO: `List<PartDto>`
- Пример: `GET /api/parts/vehicle/JT123456789012345`
- Пример ответа:
  ```json
  [
    {
      "id": 1,
      "article": "T-OF-001",
      "name": "Toyota Oil Filter",
      "manufacturer": "Toyota",
      "description": "Оригинальный масляный фильтр для Toyota Camry",
      "price": 18.90,
      "imageUrl": "https://example.com/images/toyota-oil-filter.jpg",
      "category": "Filter"
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `404` — автомобиль по VIN не найден
  - `500` — внутренняя ошибка сервера

## 19. POST /api/parts
- JWT: требуется
- Роль: ADMIN
- Request DTO: `PartCreateDto`
- Response DTO: `PartDto`
- Пример запроса:
  ```json
  {
    "article": "ART000101",
    "name": "Demo Part",
    "manufacturer": "Valeo",
    "description": "Test part",
    "price": 23.50,
    "imageUrl": "https://example.com/images/demo.jpg",
    "category": "Engine"
  }
  ```
- Пример ответа:
  ```json
  {
    "id": 101,
    "article": "ART000101",
    "name": "Demo Part",
    "manufacturer": "Valeo",
    "description": "Test part",
    "price": 23.50,
    "imageUrl": "https://example.com/images/demo.jpg",
    "category": "Engine"
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `400` — validation error
  - `400` — деталь с таким артикулом уже существует
  - `500` — внутренняя ошибка сервера

## 20. PUT /api/parts/{id}
- JWT: требуется
- Роль: ADMIN
- Request DTO: `PartUpdateDto`
- Response DTO: `PartDto`
- Пример запроса:
  ```json
  {
    "name": "Bosch Oil Filter Updated",
    "manufacturer": "Bosch",
    "description": "Updated filter",
    "price": 15.20,
    "imageUrl": "https://example.com/images/bosch-oil-filter-updated.jpg",
    "category": "Filter"
  }
  ```
- Пример ответа:
  ```json
  {
    "id": 2,
    "article": "B-OF-020",
    "name": "Bosch Oil Filter Updated",
    "manufacturer": "Bosch",
    "description": "Updated filter",
    "price": 15.20,
    "imageUrl": "https://example.com/images/bosch-oil-filter-updated.jpg",
    "category": "Filter"
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — деталь не найдена
  - `400` — validation error
  - `500` — внутренняя ошибка сервера

## 21. DELETE /api/parts/{id}
- JWT: требуется
- Роль: ADMIN
- Request DTO: none
- Response DTO: `ApiResponseDto`
- Пример ответа:
  ```json
  {
    "message": "Деталь удалена",
    "success": true
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `404` — деталь не найдена
  - `500` — внутренняя ошибка сервера

---

# SUPPORT API

## 22. GET /api/support/chat/history?counterpartyId={id}
- JWT: требуется
- Роль: SUPPORT, ADMIN, USER
- Request DTO: none
- Response DTO: `List<ChatMessageDto>`
- Пример: `GET /api/support/chat/history?counterpartyId=2`
- Пример ответа:
  ```json
  [
    {
      "id": 10,
      "senderId": 3,
      "recipientId": 2,
      "content": "Здравствуйте, нужна помощь с запчастью",
      "sentAt": "2026-05-31T12:40:00",
      "read": false
    },
    {
      "id": 11,
      "senderId": 2,
      "recipientId": 3,
      "content": "Добрый день, чем могу помочь?",
      "sentAt": "2026-05-31T12:41:00",
      "read": false
    }
  ]
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли SUPPORT/ADMIN/USER
  - `400` — missing counterpartyId
  - `500` — внутренняя ошибка сервера

---

# ADMIN API

## 23. GET /api/admin/statistics
- JWT: требуется
- Роль: ADMIN
- Request DTO: none
- Response DTO: `AdminStatisticsDto`
- Пример ответа:
  ```json
  {
    "totalUsers": 3,
    "totalVehicles": 20,
    "totalParts": 100,
    "totalVinSearches": 5,
    "totalPartSearches": 12,
    "totalChatMessages": 8
  }
  ```
- Возможные ошибки:
  - `401` — неавторизован
  - `403` — нет роли ADMIN
  - `500` — внутренняя ошибка сервера

---

# WebSocket API

## Endpoint подключения
- `ws://<host>:8080/ws-support`
- SockJS поддерживается
- Префикс приложения: `/app`
- Брокер: `/topic`

## STOMP destinations
- От клиента: `/app/support/message`
- Клиент подписывается на: `/topic/support`

## Формат CONNECT
- Заголовок:
  - `Authorization: Bearer <token>`
- Альтернатива:
  - `authToken: Bearer <token>`
- Пример STOMP frame:
  ```text
  CONNECT
  accept-version:1.2
  host:localhost
  authorization:Bearer <token>
  ```

## Формат сообщений
- Тело JSON соответствует `ChatMessageDto`
- Обязательные поля:
  - `recipientId`
  - `content`
- Пример:
  ```json
  {
    "recipientId": 2,
    "content": "Здравствуйте, нужна помощь с запчастью"
  }
  ```

## Формат ответов
- Сервер отвечает на `/topic/support`
- Тело JSON соответствует `ChatMessageDto`
- Пример:
  ```json
  {
    "id": 11,
    "senderId": 3,
    "recipientId": 2,
    "content": "Здравствуйте, нужна помощь с запчастью",
    "sentAt": "2026-05-31T12:45:00",
    "read": false
  }
  ```

## Формат ошибок
- При неверном CONNECT / отсутствующем токене соединение разрывается с ошибкой STOMP.
- Ошибки возвращаются в виде STOMP ERROR frame.
- В приложении нет формализованного JSON-ошибочного payload-а для WS.

---

# DTO-спецификация

## AuthRequestDto
- `email`: String, non-null, email
- `password`: String, non-null, min 6, max 100

## UserRegistrationDto
- `name`: String, non-null
- `email`: String, non-null, email
- `password`: String, non-null, min 6, max 100

## AuthResponseDto
- `accessToken`: String, nullable? нет для успешного ответа
- `user`: `UserDto`, nullable? нет для успешного ответа

## UserDto
- `id`: Long, non-null
- `name`: String, non-null
- `email`: String, non-null
- `roles`: Set<String>, non-null

## VehicleCreateDto
- `vin`: String, non-null, min 11, max 17
- `brand`: String, non-null
- `model`: String, non-null
- `year`: Integer, non-null
- `engine`: String, non-null
- `bodyType`: String, non-null

## VehicleUpdateDto
- `brand`: String, non-null
- `model`: String, non-null
- `year`: Integer, non-null
- `engine`: String, non-null
- `bodyType`: String, non-null

## VehicleDto
- `id`: Long, non-null
- `vin`: String, non-null
- `brand`: String, non-null
- `model`: String, non-null
- `year`: Integer, non-null
- `engine`: String, non-null
- `bodyType`: String, non-null

## VehicleSearchResultDto
- `vehicle`: `VehicleDto`, non-null
- `compatibleParts`: List<`PartDto`>, non-null

## PartCreateDto
- `article`: String, non-null
- `name`: String, non-null
- `manufacturer`: String, non-null
- `description`: String, nullable
- `price`: BigDecimal, non-null, positive
- `imageUrl`: String, nullable
- `category`: String, non-null

## PartUpdateDto
- `name`: String, non-null
- `manufacturer`: String, non-null
- `description`: String, nullable
- `price`: BigDecimal, non-null, positive
- `imageUrl`: String, nullable
- `category`: String, non-null

## PartDto
- `id`: Long, non-null
- `article`: String, non-null
- `name`: String, non-null
- `manufacturer`: String, non-null
- `description`: String, nullable
- `price`: BigDecimal, nullable
- `imageUrl`: String, nullable
- `category`: String, nullable

## ApiResponseDto
- `message`: String, non-null
- `success`: boolean, non-null

## SearchHistoryDto
- `id`: Long, non-null
- `query`: String, non-null
- `searchedAt`: LocalDateTime, non-null

## VinHistoryDto
- `id`: Long, non-null
- `vin`: String, non-null
- `searchedAt`: LocalDateTime, non-null

## ChatMessageDto
- `id`: Long, nullable on outgoing request
- `senderId`: Long, nullable on outgoing request
- `recipientId`: Long, non-null
- `content`: String, non-null
- `sentAt`: LocalDateTime, nullable on outgoing request
- `read`: boolean, non-null

## AnalogPartDto
- `id`: Long, non-null
- `originalPart`: `PartDto`, non-null
- `analogPart`: `PartDto`, non-null

## AdminStatisticsDto
- `totalUsers`: long, non-null
- `totalVehicles`: long, non-null
- `totalParts`: long, non-null
- `totalVinSearches`: long, non-null
- `totalPartSearches`: long, non-null
- `totalChatMessages`: long, non-null
