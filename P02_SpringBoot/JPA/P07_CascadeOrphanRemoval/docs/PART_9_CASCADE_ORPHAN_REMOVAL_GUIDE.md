# Part 9 - Cascade & Orphan Removal Laboratory

| Checklist | Concept | File | Field / Method / Endpoint |
| --- | --- | --- | --- |
| 9.1 | Cascade | `persistdemo/entity/PersistOrder.java` | `items`; `POST /api/part9/persist/orders` |
| 9.2 | PERSIST | `persistdemo/service/PersistDemoService.java` | `persistParentOnly` |
| 9.3 | MERGE | `mergedemo/service/MergeDemoService.java` | `mergeDetachedGraph` |
| 9.4 | REMOVE | `removedemo/service/RemoveDemoService.java` | `removeParent`; `DELETE /api/part9/remove/orders/{parentId}` |
| 9.5 | ALL | `alldemo/entity/AllOrder.java` | `items`; `POST /api/part9/all/orders` |
| 9.6 | Parent -> Child | `persistdemo/entity/PersistOrder.java` | cascade is configured on `PersistOrder.items` only |
| 9.7 | ALL warning | this guide and `alldemo/entity/AllOrder.java` | "Choose by lifecycle" section |
| 9.8 | orphanRemoval | `orphandemo/entity/OrphanOrder.java` | `removeItem`; `DELETE /api/part9/orphan/orders/{parentId}/items/{childId}` |
| 9.9 | REMOVE vs orphanRemoval | `controller/CascadePracticeController.java` | `compareRemoveAndOrphan`; `GET /api/part9/remove-vs-orphan` |
| 9.10 | Delete Parent behavior | `nocascadedemo/service/NoCascadeDemoService.java`, `removedemo/service/RemoveDemoService.java` | `attemptParentDelete`, `removeParent` |
| 9.11 | DB Cascade vs JPA Cascade | `schema.sql`, `dbcascadedemo/DatabaseCascadeDemoService.java` | `directSqlDelete`; `POST /api/part9/database-cascade/orders` |

All Java paths in the table are relative to
`src/main/java/com/example/P07_CascadeOrphanRemoval/`. Every scenario has its own entity pair and tables, so one cascade configuration cannot hide another.

## 9.1 Cascade is operation propagation

Cascade is not a relationship mapping. `@OneToMany` describes a relationship; its `cascade` member decides whether particular entity lifecycle operations propagate.

```text
Cascade PERSIST

Parent
  |
persist()
  v
Child
```

In the persist demo, the service calls only `entityManager.persist(order)`. `CascadeType.PERSIST` on `PersistOrder.items` makes Hibernate persist its items too. SQL logging should show one parent INSERT followed by child INSERTs.

Parent and Child are contextual lifecycle terms here, not annotations or fixed JPA entity types. The entity from which a configured operation propagates acts as Parent; the receiving related entity acts as Child. `Order -> OrderItem` is natural because an item normally belongs to the order lifecycle.

Relationship direction is not cascade direction. A bidirectional mapping lets Java navigate both ways, but cascade propagates only from the side where it is configured. Here it is configured on `Order.items`, so an Order operation can reach OrderItems. Persisting an OrderItem does not persist its Order.

Putting cascade on `OrderItem.order` would reverse that direction. Configuring `ALL` on both sides is technically possible but dangerous:

```text
delete one OrderItem
-> REMOVE cascades to Order
-> Order REMOVE cascades to every OrderItem
```

Bidirectional relationship does not require bidirectional cascade.

## 9.2 CascadeType.PERSIST

`PersistDemoService.persistParentOnly` builds an Order with items through `addItem`, calls only `persist(order)`, flushes, and returns generated parent/child IDs. It never persists an item explicitly.

The helper keeps both Java references consistent:

```java
items.add(item);
item.setOrder(this);
```

Helper methods synchronize the Java object graph. They are separate from cascade behavior and perform no persistence by themselves.

## 9.3 CascadeType.MERGE

`MergeDemoService.mergeDetachedGraph` deliberately performs this lifecycle:

```text
explicit setup persist -> flush -> clear
find parent and initialize children -> clear
modify detached parent and child
merge(detached parent)
```

Setup persists each object explicitly because the mapping contains MERGE, not PERSIST. The response proves:

- the original parent and child are not managed before merge;
- the instance returned by `merge` is managed;
- the detached argument remains a different instance;
- child modifications are copied because MERGE propagates from the parent.

Always continue working with the object returned by `merge`.

## 9.4 CascadeType.REMOVE

The remove demo creates its rows explicitly because REMOVE does not imply PERSIST. Deleting the managed parent then propagates REMOVE to all items in the collection.

```text
Cascade REMOVE

Parent deleted
v
Child A deleted
Child B deleted
Child C deleted
v
Parent row deleted
```

SQL normally deletes child rows before the parent row to satisfy the foreign key. One parent with a large collection can therefore cause many child deletions.

## 9.5 CascadeType.ALL

`AllOrder.items` uses `CascadeType.ALL`. JPA `ALL` includes:

```text
PERSIST, MERGE, REMOVE, REFRESH, DETACH
```

The endpoint demonstrates the convenient PERSIST part by saving an entire lifecycle-owned graph through the parent. It does not pretend to test every included operation.

## 9.6 Direction: Parent -> Child

The main direction is `Order -> OrderItem`. No item-side association declares cascade. Navigability in both directions is independent from lifecycle propagation in one direction.

## 9.7 Choose ALL by lifecycle, not cardinality

Good lifecycle fit:

```text
Order
`-- OrderItem
```

An OrderItem normally has no independent business lifecycle, so `ALL` can be reasonable.

Poor lifecycle fit:

```text
Department
`-- Employee
```

Employees may need reassignment or a nullable department when a department is reorganized. Blind `ALL` could delete real employees when the department is deleted.

Do not choose cascade based only on relationship cardinality. Choose it from lifecycle ownership and business rules.

## 9.8 orphanRemoval = true

The orphan demo starts with at least three items. It explicitly persists setup rows, then calls only `order.removeItem(itemB)`:

```text
Parent
|-- Child A
|-- Child B
`-- Child C

remove Child B from relationship

Parent
|-- Child A
`-- Child C

Child B
-> DELETE from DB
```

No repository or `EntityManager.remove(itemB)` call is made. At flush, Hibernate recognizes that the child was removed from the parent collection and deletes it. The parent remains.

## 9.9 Cascade REMOVE vs orphanRemoval

| Setting | Trigger | Result |
| --- | --- | --- |
| `CascadeType.REMOVE` | Parent is removed | REMOVE propagates to associated children |
| `orphanRemoval = true` | Child is removed from the parent relationship | That abandoned child is deleted |

Memory rule:

```text
Cascade REMOVE
-> Parent dies -> Child dies

orphanRemoval
-> Child is abandoned by Parent -> Child dies
```

The two DELETE endpoints are intentionally separate: `/remove/orders/{parentId}` deletes a whole graph, while `/orphan/orders/{parentId}/items/{childId}` deletes one unlinked child.

## 9.10 Deleting a Parent

Case A - no REMOVE cascade: `NoCascadeOrder.items` has no cascade. Its children use a non-null foreign key. The delete endpoint attempts and flushes the parent DELETE inside a dedicated transaction, catches the expected FK failure outside that transaction, then returns an explanation rather than crashing the application.

Case B - REMOVE cascade: the remove endpoint deletes every associated item, then the parent, and reports counts after a flush/clear.

Case C - children must survive: for a Department/Employee rule, first reassign employees or make the FK nullable and set it to null, then delete the department. Cascade REMOVE is a lifecycle/business decision, not a default cleanup feature.

## 9.11 JPA Cascade vs Database Cascade

```text
JPA Cascade

Application
v
EntityManager / Hibernate
v
Hibernate performs child operations
v
Database
```

JPA cascade runs when the lifecycle operation is handled through JPA/Hibernate. A DELETE typed in DBeaver does not trigger JPA cascade.

```text
Database Cascade

Application / DBeaver / Script / Other Service
v
Database
v
FK ON DELETE CASCADE
```

Database cascade is a database constraint rule. It applies no matter whether DELETE comes from JDBC, DBeaver, a script, a migration, an admin tool, or another application.

| Topic | JPA Cascade | Database Cascade |
| --- | --- | --- |
| Controlled by | Hibernate/JPA | Database |
| Configuration | Entity mapping | FK constraint |
| Works for direct SQL outside JPA | No | Yes |
| PERSIST/MERGE concepts | Yes | No |
| DELETE propagation | Yes | Yes, with DB FK rules |
| Main purpose | Entity lifecycle | Database integrity/rules |

```text
JPA Cascade
-> cascades entity lifecycle operations

Database Cascade
-> cascades database constraint operations
```

They are not alternatives where exactly one must always be selected. The system needs a clear responsibility/source of truth, and both should not be added blindly to every association. This lab focuses mainly on JPA cascade but also runtime-tests database cascade in isolated `db_orders` and `db_order_items` tables. `schema.sql` defines `ON DELETE CASCADE`; `DatabaseCascadeDemoService` uses direct JDBC SQL and verifies the child count becomes zero without loading any entity.

## Run the laboratory

Requirements: Java 21 and PostgreSQL. Create the database once:

```sql
CREATE DATABASE p07_jpa_cascade_practice;
```

Defaults are PostgreSQL user/password `postgres`/`postgres`. Override them with `JPA_CASCADE_DB_USERNAME` and `JPA_CASCADE_DB_PASSWORD`.

```powershell
$env:JAVA_HOME='C:\path\to\jdk-21'
.\mvnw.cmd spring-boot:run
```

The app listens on port `8088`. Use a unique `orderCode` for every creation request:

```json
{
  "orderCode": "PERSIST-001",
  "customerName": "Ada",
  "items": [
    {"productName": "Keyboard", "quantity": 1, "price": 89.90},
    {"productName": "Mouse", "quantity": 2, "price": 25.50},
    {"productName": "Cable", "quantity": 3, "price": 9.99}
  ]
}
```

### PERSIST

POST the JSON to `/api/part9/persist/orders`. Observe one explicit parent persist and all generated child IDs.

### MERGE

POST it with another code to `/api/part9/merge/orders`. The booleans should show detached arguments `false`, returned managed instances `true`, and `mergeReturnedSameInstance=false`.

### REMOVE

POST to `/api/part9/remove/orders`, copy `parentId`, then call `DELETE /api/part9/remove/orders/{parentId}`. The result should report no parent and zero children.

### orphanRemoval

POST at least three items to `/api/part9/orphan/orders`, copy `parentId` and one `childId`, then call `DELETE /api/part9/orphan/orders/{parentId}/items/{childId}`. The result should report that the parent exists, the removed child does not, and two children remain.

### No REMOVE cascade

POST to `/api/part9/no-cascade/orders`, then DELETE its parent URL. PostgreSQL should reject the parent row DELETE because referenced children remain; the endpoint returns that result cleanly.

### Database cascade

POST to `/api/part9/database-cascade/orders`. The service inserts with JDBC, directly deletes the parent row, and should return `childrenAfterDirectSqlDelete=0` due solely to the FK rule.

## Misconception checklist

1. Parent/Child are contextual lifecycle roles, not fixed annotations.
2. Cascade direction is determined by the side where cascade is configured.
3. A bidirectional relationship does not require bidirectional cascade.
4. `CascadeType.ALL` must not be added automatically.
5. `CascadeType.REMOVE` can delete every child in a collection.
6. `orphanRemoval` does not require deleting the parent.
7. Helper methods do not persist anything by themselves.
8. JPA cascade and database cascade operate at different layers.
9. Direct SQL does not trigger JPA cascade.
10. Database cascade works even when Hibernate is not involved.

This project deliberately stops at Part 9: it does not introduce fetching optimization, pagination, locking, auditing, caches, transaction propagation experiments, domain events, soft delete, or Hibernate-specific cascade types.
