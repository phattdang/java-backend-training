# Part 2 - Entity Mapping Basics

Start the application, then use the requests below against `http://localhost:8082`.

## Create a normal `User`

```http
POST /practice/users
Content-Type: application/json

{
  "fullName": "An Nguyen",
  "email": "an@example.com",
  "status": "ACTIVE",
  "role": "USER",
  "dateOfBirth": "2000-05-20",
  "active": true,
  "age": 26,
  "temporaryDisplayName": "Only visible in this POST response"
}
```

The response contains an `id` generated with `IDENTITY`. In PostgreSQL, `status` is stored as the text `ACTIVE`, while `role` is stored as ordinal `0`. `temporaryDisplayName` appears in this immediate response but is `null` after a later GET because `@Transient` prevents persistence. `readOnlyCode` remains `null` because `insertable = false` makes Hibernate omit it from INSERT SQL.

## Read users

```http
GET /practice/users
GET /practice/users/1
```

## Trigger constraints and validation

Send the normal request twice to demonstrate the database unique constraint on `email`. Change `fullName` to `null` to demonstrate request validation and the non-null mapping. The controller validates requests before persistence, so inspecting the generated schema or inserting SQL directly is the clearest way to observe the database `NOT NULL` constraint itself.

## Create a `SequenceUser`

```http
POST /practice/sequence-users
Content-Type: application/json

{
  "name": "Sequence example"
}
```

The returned `id` comes from `sequence_user_id_sequence`. `AutoUser` is an isolated code/schema example of `GenerationType.AUTO`; it intentionally has no repository or endpoint.

## Compare generated schema/data

Useful PostgreSQL queries:

```sql
select id, full_name, email, status, role, date_of_birth, created_at,
       active, age, read_only_code
from users;

select * from sequence_users;
select * from auto_users;
```

Changing the order of `UserRole` constants later would change what stored ordinal numbers mean. This is why `STRING` is usually safer for long-lived data.
