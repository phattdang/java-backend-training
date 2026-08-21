# P05 Rest API Demo

Mini REST API demo cho resource `User`, dung de thuc hanh REST API fundamentals. Demo nay khong dung database, JPA hay Security. Du lieu duoc luu tam trong `ConcurrentHashMap`.

Base URL:

```text
http://localhost:8080/api/users
```

Headers nen dung khi test bang Postman:

```text
Accept: application/json
Content-Type: application/json
```

`Content-Type` can cho request co JSON body nhu `POST`, `PUT`, `PATCH`. `Accept` cho server biet client muon nhan JSON response.

## Endpoints

| Method | URI | Body | Status thanh cong | REST concept |
| --- | --- | --- | --- | --- |
| GET | `/api/users?status=ACTIVE&page=0&size=10` | Khong | `200 OK` | Collection resource, query parameter, safe, idempotent |
| GET | `/api/users/{id}` | Khong | `200 OK` | Single resource, path parameter, safe, idempotent |
| POST | `/api/users` | Full create JSON | `201 Created` | Tao resource moi, non-idempotent |
| PUT | `/api/users/{id}` | Full update JSON | `200 OK` | Thay the/cap nhat toan bo resource, idempotent |
| PATCH | `/api/users/{id}` | Partial update JSON | `200 OK` | Cap nhat mot phan resource |
| DELETE | `/api/users/{id}` | Khong | `204 No Content` | Xoa resource, khong can response body |

Loi demo:

| Case | Status |
| --- | --- |
| User khong ton tai | `404 Not Found` |
| `status`, `page`, `size` hoac body khong hop le | `400 Bad Request` |

## Postman Requests

### 1. Get all users

```http
GET http://localhost:8080/api/users
Accept: application/json
```

### 2. Get users with query parameters

```http
GET http://localhost:8080/api/users?status=ACTIVE&page=0&size=10
Accept: application/json
```

### 3. Get one user by path parameter

```http
GET http://localhost:8080/api/users/1
Accept: application/json
```

### 4. Create user with POST

Moi lan gui request nay se tao user moi voi id moi, nen POST minh hoa non-idempotent.

```http
POST http://localhost:8080/api/users
Accept: application/json
Content-Type: application/json

{
  "name": "Pham Hoang Nam",
  "email": "nam@example.com",
  "status": "ACTIVE"
}
```

### 5. Full update user with PUT

Gui lai cung mot body nhieu lan se cho cung mot trang thai resource, nen PUT minh hoa idempotent.

```http
PUT http://localhost:8080/api/users/1
Accept: application/json
Content-Type: application/json

{
  "name": "Nguyen Van An Updated",
  "email": "an.updated@example.com",
  "status": "ACTIVE"
}
```

### 6. Partial update user with PATCH

Chi gui field can thay doi.

```http
PATCH http://localhost:8080/api/users/1
Accept: application/json
Content-Type: application/json

{
  "status": "BLOCKED"
}
```

### 7. Delete user

```http
DELETE http://localhost:8080/api/users/1
Accept: application/json
```

### 8. Test 404 Not Found

```http
GET http://localhost:8080/api/users/999
Accept: application/json
```

### 9. Test 400 Bad Request

```http
GET http://localhost:8080/api/users?status=WRONG&page=0&size=10
Accept: application/json
```

### 10. Test 400 with invalid create body

```http
POST http://localhost:8080/api/users
Accept: application/json
Content-Type: application/json

{
  "name": "",
  "email": "missing-name@example.com",
  "status": "ACTIVE"
}
```
