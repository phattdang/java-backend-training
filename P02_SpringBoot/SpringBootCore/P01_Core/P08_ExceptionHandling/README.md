# P08 Exception Handling Demo

Mini demo nay tap trung vao Validation + Exception Handling trong Spring Boot REST API. Khong dung database, JPA hay Security.

Base URL:

```text
http://localhost:8080
```

## File Chinh

| File | Vai tro |
| --- | --- |
| `controller/UserController.java` | REST endpoints de tao user, lay user, va tao loi bat ngo |
| `service/UserService.java` | In-memory business logic, throw custom exception |
| `exception/UserNotFoundException.java` | Custom exception cho case user khong ton tai |
| `exception/EmailAlreadyExistsException.java` | Custom exception cho case email trung |
| `exception/GlobalExceptionHandler.java` | `@RestControllerAdvice` gom exception ve mot noi |
| `dto/ErrorResponse.java` | JSON error response thong nhat: `status`, `code`, `message`, `timestamp` |
| `dto/CreateUserRequest.java` | Request DTO co `@NotBlank`, `@Email`, `@Min` |
| `dto/UserResponse.java` | Response DTO cho user |

## Error Response Format

Moi loi deu tra ve JSON theo format:

```json
{
  "status": 404,
  "code": "USER_NOT_FOUND",
  "message": "User not found with id: 99",
  "timestamp": "2026-08-21T14:45:00.123"
}
```

## Request Flow

Request thanh cong:

```text
Client
-> Controller
-> Service
-> UserResponse
-> Jackson serialize Java object thanh JSON
-> HTTP response
```

Custom exception flow:

```text
Client
-> Controller
-> Service
-> throw UserNotFoundException hoac EmailAlreadyExistsException
-> GlobalExceptionHandler
-> @ExceptionHandler
-> ResponseEntity<ErrorResponse>
-> Jackson serialize ErrorResponse thanh JSON
-> HTTP response
```

Validation exception flow:

```text
Client
-> Controller method duoc goi voi @Valid @RequestBody
-> Spring validate CreateUserRequest
-> fail validation
-> throw MethodArgumentNotValidException
-> GlobalExceptionHandler.handleValidationError()
-> gom tat ca field errors vao message
-> 400 Bad Request
```

Unexpected exception flow:

```text
Client
-> Controller
-> Service
-> throw IllegalStateException
-> GlobalExceptionHandler.handleUnexpectedError()
-> 500 Internal Server Error
```

## Request Mau Cho Postman

### 1. GET user ton tai

Seed data co san user id `1`.

```http
GET http://localhost:8080/users/1
Accept: application/json
```

### 2. GET user khong ton tai -> 404

```http
GET http://localhost:8080/users/99
Accept: application/json
```

Expected:

```json
{
  "status": 404,
  "code": "USER_NOT_FOUND",
  "message": "User not found with id: 99",
  "timestamp": "..."
}
```

### 3. POST tao user thanh cong -> 201

```http
POST http://localhost:8080/users
Accept: application/json
Content-Type: application/json

{
  "fullName": "Phat Dang",
  "email": "phat@example.com",
  "age": 24
}
```

### 4. POST email trung -> 409

Email `existing@example.com` da duoc seed san trong service.

```http
POST http://localhost:8080/users
Accept: application/json
Content-Type: application/json

{
  "fullName": "Another User",
  "email": "existing@example.com",
  "age": 25
}
```

Expected:

```json
{
  "status": 409,
  "code": "EMAIL_ALREADY_EXISTS",
  "message": "Email already exists: existing@example.com",
  "timestamp": "..."
}
```

### 5. POST DTO sai nhieu field -> 400

Request nay sai 3 field: `fullName` blank, `email` sai format, `age` nho hon 18.

```http
POST http://localhost:8080/users
Accept: application/json
Content-Type: application/json

{
  "fullName": "",
  "email": "not-an-email",
  "age": 12
}
```

Expected message gom nhieu field errors trong mot response:

```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "fullName: fullName must not be blank; email: email must be valid; age: age must be at least 18",
  "timestamp": "..."
}
```

### 6. RuntimeException bat ngo -> 500

```http
GET http://localhost:8080/users/unexpected-error
Accept: application/json
```

Expected:

```json
{
  "status": 500,
  "code": "INTERNAL_SERVER_ERROR",
  "message": "Unexpected error: Unexpected demo error from service layer",
  "timestamp": "..."
}
```
