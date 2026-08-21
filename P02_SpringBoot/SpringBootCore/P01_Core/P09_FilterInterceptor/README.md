# P09 Filter, Interceptor & Request Pipeline Demo

Mini demo nay giup nhin ro thu tu request di qua Servlet Filter, DispatcherServlet/Spring MVC, Interceptor, Controller, roi response quay nguoc lai. Khong dung database, JPA hay Spring Security.

Base URL:

```text
http://localhost:8080
```

## File Chinh

| File | Vai tro |
| --- | --- |
| `filter/LoggingFilter.java` | Servlet Filter, chay ben ngoai Spring MVC |
| `interceptor/LoggingInterceptor.java` | Spring MVC Interceptor, chay quanh Controller handler |
| `config/WebConfig.java` | Dang ky interceptor va global CORS config |
| `controller/DemoController.java` | Endpoint demo GET, POST, error, va public exclude |
| `dto/CreateDemoRequest.java` | JSON request body cho POST |
| `dto/DemoResponse.java` | JSON response DTO |

## Flow Can Nho

```text
CLIENT
  -> TOMCAT
  -> FILTER
  -> DISPATCHERSERVLET
  -> HANDLERMAPPING
  -> INTERCEPTOR.preHandle()
  -> HANDLERADAPTER
  -> ARGUMENT RESOLUTION
  -> CONTROLLER
  -> INTERCEPTOR.postHandle()
  -> RETURN VALUE HANDLING
  -> HTTP MESSAGE CONVERTER / JACKSON
  -> INTERCEPTOR.afterCompletion()
  -> FILTER
  -> TOMCAT
  -> CLIENT
```

Filter duoc goi truoc DispatcherServlet, nen no bao ben ngoai ca Spring MVC. `filterChain.doFilter(request, response)` la diem request di tiep vao phan con lai cua pipeline. Code nam sau `filterChain.doFilter(...)` chi chay khi response quay tro lai qua filter.

Interceptor duoc goi trong Spring MVC, sau khi request da vao DispatcherServlet va Spring da tim duoc handler. `preHandle()` chay truoc Controller. `postHandle()` chay sau Controller neu handler xu ly thanh cong. `afterCompletion()` chay cuoi request MVC, phu hop de cleanup/log, va van chay khi Controller nem exception.

Neu `preHandle()` return `false`, Spring MVC dung flow o do: Controller khong chay, `postHandle()` khong chay, response quay lai Filter. Trong demo nay header `X-Block: true` se bi chan bang `403`.

Global CORS config nam trong `WebConfig`, ap dung `/api/**`, cho origin `http://localhost:3000`, methods `GET, POST, PUT, PATCH, DELETE, OPTIONS`, va allowed headers `*`. Khong dung `@CrossOrigin` tren controller.

Spring Security sau nay cung dung Servlet Filter, nhung bai nay khong implement Security.

## Request Mau Va Log Mong Doi

### 1. Request GET binh thuong

```http
GET http://localhost:8080/api/demo/10?name=Phat
Accept: application/json
X-Request-Id: abc123
```

Log thu tu:

```text
FILTER BEFORE
FILTER request method=GET, uri=/api/demo/10
INTERCEPTOR preHandle
INTERCEPTOR handler=DemoController#getDemo
CONTROLLER GET
INTERCEPTOR postHandle
INTERCEPTOR afterCompletion
INTERCEPTOR mvcDurationMs=...
FILTER AFTER
FILTER response status=200, durationMs=...
```

### 2. POST co JSON body

```http
POST http://localhost:8080/api/demo
Accept: application/json
Content-Type: application/json

{
  "name": "Phat",
  "age": 22
}
```

Mental flow:

```text
Filter
-> DispatcherServlet
-> HandlerMapping
-> Interceptor preHandle
-> HandlerAdapter
-> Argument Resolution
-> @RequestBody
-> HttpMessageConverter
-> Jackson
-> Controller
-> Interceptor postHandle
-> Interceptor afterCompletion
-> Filter after
```

Log thu tu:

```text
FILTER BEFORE
INTERCEPTOR preHandle
INTERCEPTOR handler=DemoController#createDemo
CONTROLLER POST
INTERCEPTOR postHandle
INTERCEPTOR afterCompletion
FILTER AFTER
```

### 3. Interceptor chan request

```http
GET http://localhost:8080/api/demo/10?name=Phat
Accept: application/json
X-Block: true
```

Log thu tu:

```text
FILTER BEFORE
INTERCEPTOR preHandle
INTERCEPTOR handler=DemoController#getDemo
INTERCEPTOR preHandle -> X-Block=true, return false, controller will not run
FILTER AFTER
FILTER response status=403, durationMs=...
```

Khong co `CONTROLLER GET`.

### 4. Endpoint exclude khoi Interceptor

```http
GET http://localhost:8080/api/public/hello
Accept: application/json
```

Log thu tu:

```text
FILTER BEFORE
FILTER request method=GET, uri=/api/public/hello
CONTROLLER PUBLIC HELLO
FILTER AFTER
FILTER response status=200, durationMs=...
```

Khong co `INTERCEPTOR preHandle`, vi `WebConfig` exclude `/api/public/**`.

### 5. Controller throw exception

```http
GET http://localhost:8080/api/demo/error
Accept: application/json
```

Log thu tu:

```text
FILTER BEFORE
INTERCEPTOR preHandle
INTERCEPTOR handler=DemoController#throwError
CONTROLLER ERROR
INTERCEPTOR afterCompletion
INTERCEPTOR exception=RuntimeException
FILTER AFTER
FILTER response status=500, durationMs=...
```

`postHandle()` khong phai luc nao cung chay khi Controller nem exception. `afterCompletion()` la noi phu hop hon de log/cleanup cuoi request.

### 6. CORS preflight

```http
OPTIONS http://localhost:8080/api/demo
Origin: http://localhost:3000
Access-Control-Request-Method: POST
```

Response headers mong doi:

```text
Access-Control-Allow-Origin: http://localhost:3000
Access-Control-Allow-Methods: GET,POST,PUT,PATCH,DELETE,OPTIONS
```
