# P07 JSON Data Binding Demo

Mini demo nay tap trung vao cach Spring Boot dung Jackson de map JSON qua lai voi Java object.

Base URL:

```text
http://localhost:8080/api/json
```

Headers:

```text
Accept: application/json
Content-Type: application/json
```

## File Chinh

| File | Vai tro |
| --- | --- |
| `controller/JsonUserController.java` | Endpoint demo `@RequestBody`, return object, va `ObjectMapper` |
| `dto/CreateUserRequest.java` | Request record, demo `@JsonProperty("full_name")` |
| `dto/UserResponse.java` | Response record, demo `@JsonProperty` va `@JsonIgnore` |
| `dto/ManualJsonDemoResponse.java` | Response cho manual ObjectMapper demo |
| `dto/MappingErrorRequest.java` | DTO dung de test loi mapping |

## Khai Niem

Serialization la Java object -> JSON. Trong `POST /api/json/users`, controller return `UserResponse` truc tiep, Spring/Jackson serialize object nay thanh JSON response.

Deserialization la JSON -> Java object. Trong cac endpoint co `@RequestBody`, Spring/Jackson doc JSON request body va tao record DTO tu JSON.

`ObjectMapper` la core class cua Jackson. `writeValueAsString()` bien Java object thanh JSON string. `readValue()` doc JSON string va tao Java object.

Voi Spring Boot 4, Jackson mac dinh la Jackson 3 nen `ObjectMapper` dung package `tools.jackson.databind.ObjectMapper`. Dependency duoc khai bao ro trong `pom.xml` bang `spring-boot-starter-jackson`. Neu can khai bao exception cho code manual, dung `tools.jackson.core.JacksonException` thay vi `JsonProcessingException`.

`@JsonProperty("full_name")` noi voi Jackson rang JSON field `full_name` map vao Java component `fullName`.

`@JsonIgnore` an field khoi JSON. Demo nay tao `internalNote` trong `UserResponse`, nhung response JSON se khong co `internalNote`.

Record DTO duoc Jackson map bang canonical constructor cua record. Ten JSON property phai khop component name, hoac khop ten trong `@JsonProperty`.

## Request Mau Cho Postman

### 1. JSON to Java and Java to JSON

Request nay demo ca deserialization va serialization.

```http
POST http://localhost:8080/api/json/users
Accept: application/json
Content-Type: application/json

{
  "full_name": "Phat",
  "email": "phat@example.com",
  "birthday": "2000-05-20"
}
```

Response se co `full_name`, `email`, `birthday`, `createdAt`; khong co `internalNote` vi bi `@JsonIgnore`.

### 2. Manual ObjectMapper

```http
POST http://localhost:8080/api/json/manual
Accept: application/json
Content-Type: application/json

{
  "full_name": "Phat Manual",
  "email": "manual-input@example.com",
  "birthday": "1999-12-31"
}
```

Response gom:

| Field | Y nghia |
| --- | --- |
| `javaObjectToJson` | Ket qua `objectMapper.writeValueAsString()` |
| `jsonToJavaObject` | Ket qua `objectMapper.readValue()` |

### 3. Mapping error: birthday sai format

`LocalDate` mac dinh nhan format ISO `yyyy-MM-dd`. Format duoi day sai nen Jackson se fail khi deserialize.

```http
POST http://localhost:8080/api/json/users
Accept: application/json
Content-Type: application/json

{
  "full_name": "Wrong Date",
  "email": "wrong-date@example.com",
  "birthday": "20-05-2000"
}
```

### 4. Mapping error: number nhan string khong hop le

```http
POST http://localhost:8080/api/json/mapping-errors
Accept: application/json
Content-Type: application/json

{
  "id": "abc",
  "full_name": "Wrong Number",
  "birthday": "2000-05-20"
}
```

### 5. Mapping error: JSON syntax sai

Thieu dau phay sau `"Broken JSON"` nen Jackson khong parse duoc JSON.

```http
POST http://localhost:8080/api/json/users
Accept: application/json
Content-Type: application/json

{
  "full_name": "Broken JSON"
  "email": "broken@example.com",
  "birthday": "2000-05-20"
}
```
