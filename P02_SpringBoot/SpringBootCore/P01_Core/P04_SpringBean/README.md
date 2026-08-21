# P04 Spring Bean Demo

Mini project nay dung de thuc hanh Spring Bean va ApplicationContext. App mac dinh phai chay duoc; cac case loi duoc de trong comment tai `errorcase/ErrorCaseExamples.java`.

## Chay app

Can JDK 21 va `JAVA_HOME` tro den thu muc JDK.

```powershell
.\mvnw.cmd spring-boot:run
```

## Endpoint de test

- `GET /`: danh sach endpoint demo.
- `GET /order`: demo `@Repository`, `@Service`, constructor injection, `@Configuration` va `@Bean`.
- `GET /payment`: demo inject qua interface, `@Primary`, `@Qualifier`.
- `GET /scope/singleton`: singleton bean co cung instance.
- `GET /scope/prototype`: prototype bean tao instance khac nhau moi lan lay tu `ObjectProvider`.
- `GET /scope/request-session`: request scope doi theo moi request, session scope giu trong cung HTTP session.
- `GET /lifecycle`: xem trang thai sau `@PostConstruct`; khi stop app xem log `@PreDestroy`.
- `GET /lazy`: lan dau goi endpoint moi tao `HeavyService` do co `@Lazy`.

## File minh hoa

- `component/TrainingMessageProvider.java`: `@Component` va bean name.
- `controller/BeanDemoController.java`: `@RestController`, constructor injection, API test bean.
- `service/OrderService.java`: `@Service`, constructor injection, inject dependency qua interface.
- `service/MomoPaymentService.java`: implementation `PaymentService` duoc chon mac dinh bang `@Primary`.
- `service/PaypalPaymentService.java`: implementation duoc chon ro bang `@Qualifier`.
- `repository/OrderRepository.java`: `@Repository`.
- `config/AppConfig.java`: `@Configuration` va tao bean bang `@Bean("simpleAuditFormatter")`.
- `config/bean/AuditFormatter.java`: class thuong khong annotation, duoc dua vao container bang `@Bean`.
- `scope/SingletonBean.java`: singleton scope mac dinh.
- `scope/PrototypeBean.java`: prototype scope.
- `scope/RequestBean.java`: request scope.
- `scope/SessionBean.java`: session scope.
- `lifecycle/CacheService.java`: `@PostConstruct` va `@PreDestroy`.
- `lazy/HeavyService.java`: lazy initialization.
- `errorcase/ErrorCaseExamples.java`: vi du loi de bat rieng khi hoc.

## Goi y test case loi

Khong nen de cac case loi bat mac dinh vi app se khong start. Khi muon hoc loi:

1. Mo `errorcase/ErrorCaseExamples.java`.
2. Copy mot case ra class rieng trong package dang scan.
3. Chay app va doc stacktrace.
4. Hoan tac case do truoc khi test case khac.
