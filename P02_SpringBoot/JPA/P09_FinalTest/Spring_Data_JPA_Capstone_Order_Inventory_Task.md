# Spring Data JPA Capstone Task – Order \& Inventory Management System

## 1\. Bài toán

Xây dựng một **Order \& Inventory Management backend** bằng Spring Boot + PostgreSQL + Spring Data JPA.

Hệ thống cho phép:

```text
Customer
→ tạo Order
→ Order chứa nhiều OrderItem
→ mỗi OrderItem tham chiếu Product
→ kiểm tra và trừ stock
→ quản lý trạng thái Order
```

Domain chính:

```text
Customer
   |
   | 1 - N
   v
Order
   |
   | 1 - N
   v
OrderItem
   |
   | N - 1
   v
Product
```

`Product` có các thông tin quan trọng như:

```text
stock
price
version
```

để xử lý các tình huống concurrent ordering.

\---



# 2.1. Đề xuất Database Schema / Entity Fields

Phần này chỉ đưa ra **các entity/table và field cần có** để dễ thiết kế database và API.

Relationship annotation như:

```text
@OneToMany
@ManyToOne
@OneToOne
mappedBy
@JoinColumn
Owning Side
```

**không được thiết kế sẵn ở đây**. Người implement phải tự xác định dựa trên business requirement.

## Customer

```text
customers
---------
id
full\\\_name
email
phone
status
created\\\_at
updated\\\_at
```

Gợi ý datatype:

```text
id          BIGINT
full\\\_name   VARCHAR
email       VARCHAR
phone       VARCHAR
status      VARCHAR / ENUM mapping
created\\\_at  TIMESTAMP
updated\\\_at  TIMESTAMP
```

Business notes:

```text
email phải unique
status có thể là ACTIVE / INACTIVE
```

\---

## Product

```text
products
--------
id
name
description
price
stock
status
version
created\\\_at
updated\\\_at
```

Gợi ý datatype:

```text
id            BIGINT
product\\\_code  VARCHAR
name          VARCHAR
description   TEXT
price         DECIMAL
stock         INTEGER
status        VARCHAR / ENUM mapping
version       BIGINT
created\\\_at    TIMESTAMP
updated\\\_at    TIMESTAMP
```

Business notes:

```text
product\\\_code phải unique
price >= 0
stock >= 0
version dùng cho Optimistic Locking
```

\---

## Order

Nên tránh đặt table name là `order` vì `ORDER` là keyword SQL.

Có thể dùng:

```text
orders
------
id
customer\\\_id
status
total\\\_amount
created\\\_at
updated\\\_at
```

Gợi ý datatype:

```text
id            BIGINT
order\\\_code    VARCHAR
customer\\\_id   BIGINT
status        VARCHAR / ENUM mapping
total\\\_amount  DECIMAL
created\\\_at    TIMESTAMP
updated\\\_at    TIMESTAMP
```

Business notes:

```text
order\\\_code phải unique
customer\\\_id tham chiếu Customer
status có thể là PENDING / CONFIRMED / CANCELLED / COMPLETED
total\\\_amount là tổng giá trị Order
```

\---

## OrderItem

```text
order\\\_items
-----------
id
order\\\_id
product\\\_id
quantity
unit\\\_price
subtotal
created\\\_at
updated\\\_at
```

Gợi ý datatype:

```text
id          BIGINT
order\\\_id    BIGINT
product\\\_id  BIGINT
quantity    INTEGER
unit\\\_price  DECIMAL
subtotal    DECIMAL
created\\\_at  TIMESTAMP
updated\\\_at  TIMESTAMP
```

Business notes:

```text
quantity > 0
order\\\_id tham chiếu Order
product\\\_id tham chiếu Product

unit\\\_price nên lưu giá Product tại thời điểm đặt hàng,
không phụ thuộc vào việc Product thay đổi giá sau này.

subtotal có thể được tính:
quantity \\\* unit\\\_price
```

\---

# 2.2. Database Diagram

Logical database diagram:

```text
+----------------------+
|      customers       |
+----------------------+
| id                   |
| full\\\_name            |
| email                |
| phone                |
| status               |
| created\\\_at           |
| updated\\\_at           |
+----------+-----------+
           |
           | customer\\\_id
           |
           v
+----------------------+
|        orders        |
+----------------------+
| id                   |
| order\\\_code           |
| customer\\\_id          |
| status               |
| total\\\_amount         |
| created\\\_at           |
| updated\\\_at           |
+----------+-----------+
           |
           | order\\\_id
           |
           v
+----------------------+
|     order\\\_items      |
+----------------------+
| id                   |
| order\\\_id             |
| product\\\_id           |
| quantity             |
| unit\\\_price           |
| subtotal             |
| created\\\_at           |
| updated\\\_at           |
+----------+-----------+
           |
           | product\\\_id
           |
           v
+----------------------+
|       products       |
+----------------------+
| id                   |
| product\\\_code         |
| name                 |
| description          |
| price                |
| stock                |
| status               |
| version              |
| created\\\_at           |
| updated\\\_at           |
+----------------------+
```

Có thể nhìn ngắn gọn thành:

```text
Customer
   |
   | customer\\\_id
   v
Order
   |
   | order\\\_id
   v
OrderItem
   |
   | product\\\_id
   v
Product
```

Lưu ý:

```text
Diagram trên chỉ mô tả foreign-key direction ở database level.

Người implement vẫn phải tự quyết định:

- Entity nào chứa field reference
- Entity nào chứa collection
- @ManyToOne đặt ở đâu
- @OneToMany đặt ở đâu
- mappedBy dùng field nào
- Owning Side là phía nào
- Cascade nào phù hợp
- orphanRemoval có nên dùng hay không
- Fetch Strategy nên là gì
```

\---

# 2.3. Suggested Enum Values

Có thể dùng các enum sau để bài tập có thêm phần Enum Mapping.

## CustomerStatus

```text
ACTIVE
INACTIVE
```

## ProductStatus

```text
ACTIVE
INACTIVE
OUT\\\_OF\\\_STOCK
```

## OrderStatus

```text
PENDING
CONFIRMED
CANCELLED
COMPLETED
```

Nên sử dụng:

```java
@Enumerated(EnumType.STRING)
```

để giá trị DB dễ đọc và không phụ thuộc thứ tự enum.

\---

## 2\. Luồng nghiệp vụ tạo Order

Client gửi request:

```json
{
  "customerId": 1,
  "items": \\\[
    {
      "productId": 10,
      "quantity": 2
    },
    {
      "productId": 20,
      "quantity": 1
    }
  ]
}
```

Backend phải thực hiện flow:

```text
Validate Customer
↓
Load Products
↓
Check stock
↓
Create Order
↓
Create OrderItems
↓
Decrease Product stock
↓
Persist Order graph
↓
Commit
```

Nếu bất kỳ Product nào không đủ stock:

```text
ROLLBACK toàn bộ transaction
```

Không được xảy ra tình trạng:

```text
Order đã lưu
nhưng stock chưa update
```

hoặc:

```text
stock đã bị trừ
nhưng Order không được tạo
```

\---

# 3\. JPA Mapping Requirements

Entity model phải thể hiện đúng các relationship:

```text
Customer 1-N Order
Order 1-N OrderItem
OrderItem N-1 Product
```

Phải xác định rõ:

```text
Owning Side
Inverse Side
@JoinColumn
mappedBy
Foreign Key holder
```

Ví dụ:

```text
orders.customer\\\_id
order\\\_items.order\\\_id
order\\\_items.product\\\_id
```

Relationship `Order → OrderItem` phải có helper method phù hợp:

```java
order.addItem(...)
order.removeItem(...)
```

Helper method phải đồng bộ hai phía của object graph nếu mapping là bidirectional.

Không được nhầm helper method với persistence/cascade behavior.

\---

# 4\. Entity Lifecycle \& Persistence Context

Hệ thống phải có experiment hoặc test chứng minh được:

```text
Transient
Managed
Detached
Removed
```

Phải hiểu và quan sát được:

```text
Persistence Context
First-level cache
Dirty Checking
flush vs commit
persist vs merge
EntityManager.contains(...)
```

Ví dụ cần chứng minh được:

```text
find Entity
↓
Entity = Managed
↓
modify field
↓
không gọi save lần hai
↓
Dirty Checking
↓
UPDATE khi flush/commit
```

Ngoài ra cần giải thích được:

```text
save(new entity)
→ liên quan tới persist

save(existing/detached entity)
→ có thể liên quan tới merge
```

\---

# 5\. Repository Requirements

Sử dụng:

```text
JpaRepository
```

cho các repository chính.

Phải có các thao tác CRUD cơ bản:

```text
save
findById
findAll
existsById
delete
```

Không được viết thủ công EntityManager cho mọi CRUD operation nếu Spring Data JPA đã cung cấp abstraction phù hợp.

\---

# 6\. Query Requirements

Hệ thống phải có ví dụ thực tế cho:

## Derived Query

Ví dụ:

```text
findByCustomerId(...)
findByStatus(...)
findByProductNameContaining(...)
```

## JPQL

Phải có ít nhất một custom JPQL query dùng:

```java
@Query
```

và sử dụng:

```text
Entity name
Entity field
Named parameter
@Param
```

## Native Query

Phải có ít nhất một query:

```java
@Query(
    value = "...",
    nativeQuery = true
)
```

Native Query nên được dùng cho một use case có lý do hợp lý, không chỉ để demo cú pháp.

\---

# 7\. Pagination \& Sorting

Danh sách Order phải hỗ trợ:

```text
Pagination
Sorting
```

Ví dụ:

```text
GET /orders?page=0\\\&size=20\\\&sort=createdAt,desc
```

Phải sử dụng Spring Data pagination abstraction phù hợp.

Không được load toàn bộ dữ liệu rồi tự cắt list trong memory.

\---

# 8\. Fetching Requirements

Relationship nên sử dụng `LAZY` phù hợp.

Phải demo được:

```text
FetchType.LAZY
FetchType.EAGER concept
Proxy / Lazy Loading
LazyInitializationException
```

Phải hiểu rằng:

```text
findAll()
!=
load toàn bộ relationship
```

Và:

```text
LAZY
→ relationship chỉ được load khi thực sự cần
```

\---

# 9\. N+1 Requirement

Phải cố tình tạo một use case gây N+1.

Ví dụ:

```text
load Orders
↓
loop Orders
↓
access OrderItems
↓
access Product
```

SQL log phải thể hiện pattern:

```text
1 root query
+
N additional relationship queries
```

Sau đó phải fix cùng logical use case bằng:

```text
JOIN FETCH
@EntityGraph
DTO Projection
```

Phải so sánh SQL trước và sau.

Không được giải quyết bằng cách chuyển toàn bộ relationship sang `EAGER`.

\---

# 10\. DTO \& Projection

Không trả Entity trực tiếp qua REST API.

Phải sử dụng DTO cho request/response.

Phải có ít nhất một DTO Projection hoặc Projection query.

Ví dụ:

```text
OrderSummary
-------------
orderId
orderCode
customerName
status
totalAmount
createdAt
```

Use case chỉ cần một số field thì không được bắt buộc load full Entity graph nếu không cần thiết.

Phải hiểu:

```text
DTO / Projection
→ không phải Managed Entity
→ không có Dirty Checking
```

\---

# 11\. Transaction Requirements

Business flow `createOrder()` phải chạy trong một transaction.

Ví dụ:

```java
@Transactional
public void createOrder(...) {
    ...
}
```

Transaction phải bao gồm:

```text
load Customer
load Products
check stock
create Order
create OrderItems
decrease stock
persist data
```

Phải demo được:

```text
Commit
Rollback
Dirty Checking trong Transaction
Multiple Repository operations trong cùng Transaction
```

\---

# 12\. Rollback Experiment

Tạo một test hoặc API cố tình:

```text
save Order
↓
decrease Product stock
↓
throw RuntimeException
```

Kết quả phải chứng minh:

```text
Order rollback
Stock rollback
```

Phải có thêm experiment cho:

```text
Checked Exception
```

để quan sát:

```text
default behavior
vs
rollbackFor
```

\---

# 13\. Transaction Proxy / Self Invocation

Phải hiểu và document được:

```text
@Transactional
→ hoạt động thông qua Spring proxy
```

Phải có ít nhất một ví dụ hoặc test giải thích self-invocation:

```java
this.transactionalMethod();
```

có thể bypass transaction proxy.

Không cần xây architecture phức tạp chỉ để demo, nhưng phải giải thích được lý do.

\---

# 14\. Cascade \& Orphan Removal

`OrderItem` được xem là lifecycle child của `Order`.

Phải demo:

```text
CascadeType.PERSIST
CascadeType.REMOVE
orphanRemoval = true
```

Cần phân biệt:

```text
Cascade REMOVE
→ Parent bị delete
→ Child bị delete

orphanRemoval
→ Child bị remove khỏi Parent relationship
→ Child bị delete
```

Không được dùng:

```java
CascadeType.ALL
```

bừa bãi trên relationship như:

```text
OrderItem → Product
```

vì `Product` có lifecycle độc lập.

\---

# 15\. Auditing

Thêm JPA Auditing cho các entity phù hợp.

Ví dụ:

```text
createdAt
updatedAt
```

Phải kiểm tra dữ liệu auditing thực tế trong PostgreSQL.

\---

# 16\. Constraint Requirements

Các dữ liệu phù hợp phải có unique constraint.

Ví dụ:

```text
orderCode
productCode
customerEmail
```

Phải thử duplicate data và quan sát exception từ database/JPA layer.

Ngoài unique constraint, cần đảm bảo foreign key mapping đúng.

\---

# 17\. Optimistic Locking

`Product` phải hỗ trợ Optimistic Locking bằng:

```java
@Version
private Long version;
```

Phải demo flow:

```text
Product:
stock = 1
version = 5

T1 đọc version=5
T2 đọc version=5

T1 update
→ version=6
→ success

T2 update bằng version=5
→ conflict
→ Optimistic Lock exception
```

Phải hiểu:

```text
version là field của Entity
nhưng đại diện version của cả row
```

Version tăng trên chính record hiện tại, không tạo row mới.

\---

# 18\. Pessimistic Locking

Tạo repository method:

```java
@Lock(LockModeType.PESSIMISTIC\\\_WRITE)
```

Ví dụ:

```java
findByIdForUpdate(...)
```

Use case phải demo:

```text
T1
→ SELECT Product FOR UPDATE
→ lock row

T2
→ gọi cùng read-for-update
→ WAIT

T1
→ update
→ commit
→ unlock

T2
→ đọc state mới nhất
→ tiếp tục
```

Phải so sánh:

```text
Optimistic
→ không lock trước
→ conflict lúc update

Pessimistic
→ lock ngay từ lúc read-for-update
→ transaction sau phải chờ
```

\---

# 19\. Lock không thay thế Transaction

Phải giải thích được:

```text
Transaction
→ atomic operation
→ commit / rollback

Lock
→ xử lý concurrent conflict
```

Lock không được coi là giải pháp thay thế `@Transactional`.

\---

# 20\. SQL Logging

Bật Hibernate SQL logging.

Ví dụ:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format\\\_sql=true
```

Nếu phù hợp với version hiện tại, bật bind parameter logging.

Trong quá trình demo phải đọc và giải thích được SQL Hibernate generate.

Không chỉ kiểm tra API response.

\---

# 21\. Database Verification

Sử dụng DBeaver để kiểm tra trực tiếp:

```text
tables
columns
primary keys
foreign keys
unique constraints
relationship data
version
audit fields
actual persisted values
```

Phải kiểm tra dữ liệu trước/sau các experiment như:

```text
transaction rollback
orphan removal
optimistic locking
pessimistic locking
N+1
```

\---

# 22\. API Requirements

Tối thiểu nên có các API:

## Customer

```text
POST /customers
GET  /customers/{id}
```

## Product

```text
POST  /products
GET   /products/{id}
GET   /products
PATCH /products/{id}
```

## Order

```text
POST /orders
GET  /orders/{id}
GET  /orders
```

Có thể bổ sung:

```text
DELETE /orders/{id}/items/{itemId}
```

để demo orphan removal.

Exact API path có thể thay đổi nếu thiết kế hợp lý hơn.

\---

# 23\. Trace Full Flow

Phải trace được một request hoàn chỉnh:

```text
API Request
↓
Controller
↓
Service
↓
@Transactional
↓
Repository
↓
JPA / EntityManager
↓
Hibernate
↓
Generated SQL
↓
PostgreSQL
```

Người implement phải giải thích được vai trò của từng layer.

\---

# 24\. Error Handling / Debugging

Phải có khả năng debug các lỗi phổ biến như:

```text
EntityNotFound
DataIntegrityViolation
Duplicate unique value
Foreign key violation
LazyInitializationException
Optimistic Lock conflict
Transaction rollback
Incorrect relationship mapping
```

Không cần xây global error framework quá phức tạp, nhưng lỗi phải được hiểu và xử lý rõ ràng.

\---

# Acceptance Criteria

Task được coi là hoàn thành khi demo được:

```text
1. Spring Boot kết nối PostgreSQL thành công.
2. Entity mapping đúng với schema.
3. Primary Key và Enum mapping hoạt động.
4. JpaRepository CRUD hoạt động.
5. Derived Query hoạt động.
6. JPQL Query hoạt động.
7. Native Query hoạt động.
8. Pagination + Sorting hoạt động.
9. Customer 1-N Order mapping đúng.
10. Order 1-N OrderItem mapping đúng.
11. OrderItem N-1 Product mapping đúng.
12. Owning Side / mappedBy / JoinColumn đúng.
13. Helper method relationship hoạt động.
14. LAZY loading được quan sát.
15. LazyInitializationException được reproduce hoặc giải thích bằng runnable test.
16. N+1 được reproduce.
17. N+1 được fix bằng JOIN FETCH.
18. N+1 được fix bằng @EntityGraph.
19. DTO Projection hoạt động.
20. Transaction boundary nằm ở Service phù hợp.
21. Dirty Checking được quan sát.
22. RuntimeException rollback toàn bộ transaction.
23. Checked Exception + rollbackFor được demo.
24. Cascade PERSIST hoạt động.
25. Cascade REMOVE hoạt động.
26. orphanRemoval hoạt động.
27. Auditing hoạt động.
28. Unique Constraint được enforce.
29. Optimistic Lock conflict được reproduce bằng @Version.
30. Pessimistic Lock được reproduce.
31. SQL generated được đọc và giải thích.
32. Dữ liệu được verify bằng DBeaver.
33. Không trả Entity trực tiếp qua REST API.
34. Full flow API → PostgreSQL được trace rõ ràng.
```

\---

# Mục tiêu cuối cùng

Sau khi hoàn thành task, người implement phải tự giải thích được:

```text
JPA vs Hibernate vs Spring Data JPA

Entity mapping

Persistence Context

Transient / Managed / Detached / Removed

Dirty Checking

persist / merge / save

Repository abstraction

Derived Query

JPQL

Native SQL

OneToMany / ManyToOne

Owning Side

mappedBy / JoinColumn

LAZY / EAGER

LazyInitializationException

N+1

Fetch Join

EntityGraph

DTO Projection

Cascade

orphanRemoval

Transaction boundary

Commit / Rollback

Runtime vs Checked Exception rollback

Transaction proxy / Self Invocation

Pagination / Sorting

Auditing

Unique Constraint

Optimistic Lock

Pessimistic Lock

Hibernate SQL generation

Database verification
```

\---

# Tóm tắt đề bài

> \\\*\\\*Xây dựng một Order \\\& Inventory Management backend bằng Spring Boot, PostgreSQL và Spring Data JPA. Hệ thống phải xử lý đầy đủ entity lifecycle, relationship mapping, CRUD/query, transaction, fetching, N+1, cascade, projection, auditing, database constraints và concurrent inventory update. Mọi behavior quan trọng phải được chứng minh bằng API/test, Hibernate SQL logs và dữ liệu PostgreSQL thực tế.\\\*\\\*

Mục tiêu của bài này không phải tạo một project CRUD đơn giản, mà là sử dụng một business case thực tế để tổng hợp và chứng minh toàn bộ kiến thức Spring Data JPA đã học.

