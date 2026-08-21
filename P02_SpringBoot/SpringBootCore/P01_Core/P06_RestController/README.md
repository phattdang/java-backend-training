# P06 Rest Controller Demo

Mini REST controller demo cho resource `User`. Bai nay tap trung vao annotation va response handling cua Spring MVC REST Controller, khong dung database, JPA hay Security.

Base URL:

```text
http://localhost:8080
```

Headers nen dung:

```text
Accept: application/json
Content-Type: application/json
X-Request-Id: demo-001
```

`Content-Type` dung cho request co body JSON. `Accept` yeu cau response JSON. `X-Request-Id` la custom request header va API se tra lai header nay trong response neu client gui len.

## File Chinh

| File | Vai tro |
| --- | --- |
| `controller/UserController.java` | Demo `@RestController`, mapping, params, headers, consumes, produces, `ResponseEntity` |
| `service/UserService.java` | Business logic don gian va in-memory storage bang `ConcurrentHashMap` |
| `domain/User.java` | Internal object, khong expose truc tiep ra API |
| `domain/UserStatus.java` | Enum status: `ACTIVE`, `INACTIVE`, `BLOCKED` |
| `dto/CreateUserRequest.java` | Request DTO cho POST |
| `dto/UpdateUserRequest.java` | Request DTO cho PUT |
| `dto/PatchUserRequest.java` | Request DTO cho PATCH |
| `dto/UserResponse.java` | Response DTO tra ve cho client |

## Endpoint Mapping

| Method | URI | Minh hoa |
| --- | --- | --- |
| GET | `/api/users?status=ACTIVE&page=0&size=10` | `@RequestParam`, `ResponseEntity`, custom response header |
| GET | `/api/members?status=ACTIVE&page=0&size=10` | Mapping nhieu URL vao cung logic voi `/api/users` |
| GET | `/api/users/{id}` | `@PathVariable`, `produces = application/json`, `404 Not Found` |
| GET | `/api/users/{id}/direct` | Return object truc tiep, Spring tu serialize thanh JSON |
| POST | `/api/users` | `@RequestBody`, `consumes = application/json`, `201 Created`, `Location` header |
| PUT | `/api/users/{id}` | Full update DTO, idempotent |
| PATCH | `/api/users/{id}` | Partial update DTO |
| DELETE | `/api/users/{id}` | `204 No Content`, khong co body |

## Request Mau Cho Postman

### 1. GET collection with query params and request header

```http
GET http://localhost:8080/api/users?status=ACTIVE&page=0&size=10
Accept: application/json
X-Request-Id: demo-001
```

### 2. GET same logic through /api/members

```http
GET http://localhost:8080/api/members?status=ACTIVE&page=0&size=10
Accept: application/json
X-Request-Id: demo-002
```

### 3. GET by id with path variable

```http
GET http://localhost:8080/api/users/1
Accept: application/json
X-Request-Id: demo-003
```

### 4. GET direct object return

```http
GET http://localhost:8080/api/users/1/direct
Accept: application/json
```

### 5. POST create user

Response thanh cong se co `201 Created`, JSON body va `Location: /api/users/{newId}`.

```http
POST http://localhost:8080/api/users
Accept: application/json
Content-Type: application/json
X-Request-Id: demo-004

{
  "name": "Pham Hoang Nam",
  "email": "nam@example.com",
  "status": "ACTIVE"
}
```

### 6. PUT full update user

```http
PUT http://localhost:8080/api/users/1
Accept: application/json
Content-Type: application/json
X-Request-Id: demo-005

{
  "name": "Nguyen Van An Updated",
  "email": "an.updated@example.com",
  "status": "ACTIVE"
}
```

### 7. PATCH partial update user

```http
PATCH http://localhost:8080/api/users/1
Accept: application/json
Content-Type: application/json
X-Request-Id: demo-006

{
  "status": "BLOCKED"
}
```

### 8. DELETE user

```http
DELETE http://localhost:8080/api/users/1
Accept: application/json
X-Request-Id: demo-007
```

### 9. Test 404 Not Found

```http
GET http://localhost:8080/api/users/999
Accept: application/json
```

### 10. Test 400 Bad Request

```http
GET http://localhost:8080/api/users?status=WRONG&page=0&size=10
Accept: application/json
```
