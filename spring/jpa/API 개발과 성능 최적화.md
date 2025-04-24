

# 스프링 부트와 JPA 활용 - API 개발과 성능 최적화

## 목차
- [스프링 부트와 JPA 활용 - API 개발과 성능 최적화](#스프링-부트와-jpa-활용---api-개발과-성능-최적화)
  - [목차](#목차)
  - [API 개발 기본](#api-개발-기본)
    - [회원 등록 api](#회원-등록-api)
      - [saveMemberV1](#savememberv1)
      - [문제점](#문제점)
      - [saveMemberV2](#savememberv2)
    - [회원 수정 API](#회원-수정-api)
      - [오류 정정](#오류-정정)
    - [회원 조회 API](#회원-조회-api)
      - [엔티티 외부 노출](#엔티티-외부-노출)
  - [지연 로딩과 성능 최적화](#지연-로딩과-성능-최적화)
    - [엔티티 직접 노출](#엔티티-직접-노출)
      - [무한 루프](#무한-루프)
      - [@JsonIgnore](#jsonignore)
      - [지연 로딩 객체](#지연-로딩-객체)
      - [정리](#정리)
    - [엔티티를 DTO로 반환](#엔티티를-dto로-반환)
      - [Fetch join 최적화](#fetch-join-최적화)
    - [JPA에서 DTO 직접 조회](#jpa에서-dto-직접-조회)
      - [정리](#정리-1)
      - [쿼리 방식 권장 순서](#쿼리-방식-권장-순서)
  - [컬렉션 조회 최적화](#컬렉션-조회-최적화)
    - [엔티티 직접 노출](#엔티티-직접-노출-1)
    - [Entity를 DTO로 변환: Fetch join](#entity를-dto로-변환-fetch-join)
      - [fetch join 최적화](#fetch-join-최적화-1)
      - [DB의 distinct](#db의-distinct)
      - [JPA의 distinct](#jpa의-distinct)
      - [컬렉션의 fetch join](#컬렉션의-fetch-join)
    - [Entity를 DTO로 변환: 페이징과 한계 돌파](#entity를-dto로-변환-페이징과-한계-돌파)
      - [해결 방법](#해결-방법)
      - [Before](#before)
      - [After](#after)
      - [비교](#비교)
      - [장점](#장점)
      - [default\_batch\_fetch\_size](#default_batch_fetch_size)
      - [적정값](#적정값)
    - [DTO 직접 조회](#dto-직접-조회)
      - [N+1](#n1)
    - [DTO 직접 조회: 컬렉션 조회 최적화](#dto-직접-조회-컬렉션-조회-최적화)
      - [IN절](#in절)
    - [DTO 직접 조회: 플랫 데이터 최적화](#dto-직접-조회-플랫-데이터-최적화)
      - [Join](#join)
      - [중복 제거](#중복-제거)
      - [단점](#단점)
    - [정리](#정리-2)
      - [엔티티 조회 후 DTO 변환](#엔티티-조회-후-dto-변환)
      - [DTO로 직접 조회](#dto로-직접-조회)
    - [권장 순서](#권장-순서)
    - [참고](#참고)


## API 개발 기본

### 회원 등록 api

- 템플릿 뷰를 반환하는 컨트롤러와 api 방식의 컨트롤러의 패키지를 분리한다.
  - 둘은 예외 처리를 하는 방식이 다르다.
  - ex. api 방식은 json 형식으로 응답을 보낸다.

#### saveMemberV1

MemberApiController
```java
// @RestController = @Controller + @ResponseBody
// @ResponseBody는 데이터를 json이나 xml로 보낼 때 사용한다.
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    // @RequestBody가 json 데이터를 객체로 바꿔준다.
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
```

Member
```java
@Entity
@Getter
@Setter
public class Member {

  ...

    // validation을 위한 애너테이션
    @NotEmpty
    private String name;

}
```

- @RestController
  - @Controller + @ResponseBody
  - @ResponseBody는 데이터를 json이나 xml로 보낼 때 사용한다.
- @RequestBody
  - json으로 온 바디를 Member 객체로 변환한다.
- @Valid
  - validation을 체크해준다.

#### 문제점

- 프레젠테이션 레이어(컨트롤러)의 validation을 위한 로직이 엔티티에 들어가있다.
  - 어떤 API에서는 validation이 필요하지 않을 수 있다.
  - 엔티티의 필드명을 바꾸면 API 스펙 자체가 바뀌어버린다.
  - 수 많은 엔티티 필드 중에 무슨 값이 들어올지 모른다.
  - 실무에서는 한 엔티티에 대해 API가 다양하게 만들어진다.
    - 하나의 엔티티에 각각의 API를 위한 모든 요구사항을 담기 어렵다.

#### saveMemberV2
```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {

        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
```
- API 요청 스펙에 맞춰 별도의 DTO를 파라미터로 받는다.
  - Entity와 프레젠테이션 계층 로직을 분리할 수 있다.
  - Entity와 API 스펙을 명확하게 분리한다.
  - Entity가 변해도 API 스펙이 변하지 않는다.
  - 실무에서는 API 스펙에 Entity를 노출하면 안된다.

### 회원 수정 API
```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        // 트랜잭션이 끝난 후 다시 커리해서 가져온다.
        Member member = memberService.findOne(id);

        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
```
- update할 때 사용했던 member 대신 새로 쿼리해서 member 값을 가져온다.
  - 혹은 update 했던 값의 id 정도는 가져와서 다시 쿼리한다.
  - 기존 member를 그대로 사용하면 update에서 커맨드와 쿼리가 같이 있는 모양이 되기 때문이다.
  - 커맨드와 쿼리를 분리하는 연습을 하면 유지보수가 쉬워진다.
- 커맨드
  - update를 하기 위한 변경성 메서드

```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        // 변경 감지를 사용한다.
        member.setName(name);
    }
}
```
- 변경 감지에 의해 영속 상태의 member는 @Transactional이 끝나는 시점에 flush() 하고 DB 트랜잭션을 커밋한다.

#### 오류 정정
- 회원 정보를 부분 업데이트 하지만 PUT을 사용하고 있다.
  - PUT
    - 전체 업데이트
  - PATCH, POST
    - 부분 업데이트

### 회원 조회 API
```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
}
```
- Entity를 그대로 반환하면 Entity 내용이 외부에 다 노출된다.
- 화면에 뿌리기 위한 프레젠테이션 로직이 Entity에 추가되면 안된다.
- 특정 필드에 @JsonIgnore를 넣을 수도 있지만 다양한 api가 Entity를 사용하기 때문에 사용하면 안된다.
- Entity가 변경되면 API 스펙이 바뀌어버린다.

#### 엔티티 외부 노출
- Entity 대신에 API 스펙에 맞는 별도의 DTO를 노출해야 한다.
  - 어떤 API는 name 필드가 필요하지만, 어떤 API는 name 필드가 필요 없을 수 있다.
```java
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> members = memberService.findMembers();

        List<MemberDto> collect = members.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
```
- 내가 노출할 데이터만 별도의 DTO로 만든다.
  - Result로 한 번 감싸줘서 향후 필요한 필드를 추가할 수 있게 한다.
  - 감싸주지 않으면 바로 json 배열 타입으로 나가면서 수정에 대한 유연성이 떨어진다.

```java
@Data
@AllArgsConstructor
static class Result<T> {
    private T data;
    private Integer count;
}
```
- 수정 사항 발생 시 위와 같이 수정한다.

## 지연 로딩과 성능 최적화

### 엔티티 직접 노출
- 주문, 배송 정보, 회원의 조회 api 개발
- 지연로딩으로 성능문제를 해결한다.

#### 무한 루프
```java
/**
 * Order 조회
 * Order - Member는 ManyToOne
 * Order - Delivery는 OneToOne
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        return all;
    }
}
```
- 위와 같이 코드를 작성하면 무한 루프가 발생한다.

```java
public class Order {

  ...

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
```
```java
public class Member {
    
    ...

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
```
- Order와 Member가 서로를 참조하고 있기 때문이다.
  - Order에 Member가 있어서 들어가보면 Member에 Order가 있고...
- jackson이 무한 루프를 돌면서 계속 객체를 만든다.

#### @JsonIgnore
```java
public class Member {
    
    ...

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
```
- 양방향이 걸리는 곳은 모두 한쪽에 애노테이션을 사용해 이를 방지한다.

#### 지연 로딩 객체
```java
public class Order {

    ...

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
```
- member는 지연 로딩으로 가져오게 되어있다.
- DB에서 가져올 때는 order 데이터만 가져온다.
- member에는 프록시 객체를 생성해서 넣어둔다.
  - 이때 쓰는 라이브러리가 예외에 나온 bytebuddy다.
- jackson 라이브러리가 member를 변환하려는 순간, 프록시 객체를 json으로 어떻게 생성할지 몰라 에러를 낸 것이다.

#### 정리
- Entity를 API 응답 형태로 외부에 노출하면 안된다.
  - DTO로 변환해서 반환하자.
- 지연 로딩을 피하려고 즉시 로딩을 설정하면 안된다.
  - 연관 관계가 필요없는 경우에도 항상 모든 데이터를 조회하면서 성능 문제가 발생한다.
    - 연관된 데이터를 다 조회하면서 N+1 문제가 터진다.
  - 즉시 로딩으로 설정하면 성능 튜닝이 매우 어려워진다.
  - 항상 지연 로딩을 기본으로 하자.
  - 성능 최적화가 필요하면 fetch join을 사용하자.

### 엔티티를 DTO로 반환
```java
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream().map(SimpleOrderDto::new).collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        // DTO가 Entity에 의존하는 것은 문제가 되지 않는다.
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            // lazy
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            // lazy
            address = order.getDelivery().getAddress();
        }
    }
}
```
- 두 번째 order에 대한 member와 delivery도 다시 조회한다.
  1. 맨 처음에 order를 조회하면서 SQL이 1번 실행된다.
  2. 결과 데이터가 2개가 나온다.
  3. map(SimpleOrderDto::new)에서 2번 루프를 돈다.
  4. 처음 돌 때 해당 order에 대한 member와 delivery 쿼리를 날린다.
  5. 두 번째 돌 때 해당 order에 대한 member와 delivery 쿼리를 날린다.
- order 조회 1번 + member 지연 로딩 N번 + delivery 지연 로딩 N번으로 1 + N + N번이 실행된다.
- order의 결과가 4개라면 최악의 경우 주문 1번 + 회원 4번 + 배송 4번이 실행된다.
  - 최악이라고 한 이유는 같은 회원에 대한 정보를 조회한다면 영속성 컨텍스트에 존재하므로 쿼리가 나가지 않기 때문이다.
- EAGER로 바꿔도 예측이 안되는 복잡한 쿼리들이 나간다.

#### Fetch join 최적화
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .getResultList();
    }
}
```
```java
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream().map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }
}
```
- 쿼리 한 방으로 order, member, delivery를 한 번에 조인해서 가져온다.
  - 연관 관계에 있는 값을 프록시 대신 실제 값으로 다 채워서 가져온다.
- fetch join으로 member, delivery는 이미 조회된 상태이므로 지연 로딩 하지 않는다.
- 실무에서 fetch join을 적극적으로 사용하는 것이 좋다.

### JPA에서 DTO 직접 조회
```JAVA
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }
}
```
```JAVA
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                        // 원하는 필드만 DTO에 정의해서 가져올 수 있다.
                        "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.status, o.orderDate, d.address)"
                                + " from Order o"
                                + " join o.member m"
                                + " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
```
```JAVA
@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, OrderStatus orderStatus, LocalDateTime orderDate, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.address = address;
    }
}
```

#### 정리
- 대부분의 성능은 where의 조건이 index를 안 타는 상황이나 join에서 먹기 때문에 select 필드를 줄인다고 성능이 대폭 개선되진 않는다.
  - select 필드가 진짜 많은데 실시간으로 사용하는 유저가 많다면 그때 고려해보자.
- 만약 사용하게 된다면 별도의 패키지에 OrderSimpleQueryRepository처럼 쿼리용 repository를 따로 파는 게 좋다.
  - Entity를 위한 repository단에 DTO가 사용되면 애매하기 때문이다.

#### 쿼리 방식 권장 순서
1. 우선 Entity를 DTO로 변환한다.
2. 필요하면 fetch join으로 성능을 최적화 한다.
   - 대부분의 이슈가 여기서 해결된다.
3. 그래도 안되면 DTO로 직접 조회한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template으로 직접 SQL을 쓰는 것이다.

## 컬렉션 조회 최적화

### 엔티티 직접 노출
- 컬렉션인 1:N 관계를 조회하고 최적화한다.
  - 1:N join시 데이터가 뻥튀기되어 최적화하기 힘들다.

```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            
            // orderItem의 Item을 초기화 한다.
            orderItems.forEach(o -> o.getItem().getName()); // Lazy 강제 초기화
        }

        return all;
    }
}
```
- Order - OrderItem,OrderItem - order 관계를 가져온다.
- 지연 로딩으로 설정한 연관 관계는 강제로 초기화 한다.
  - `hibernate5Module`는 지연 로딩 필드를 null로 출력한다.
  - 양방향 관계는 한 쪽에 `JsonIgnore`를 꼭 붙여준다.
  - 엔티티를 직접 노출하기 때문에 이 방법은 지양하는것이 좋다.

### Entity를 DTO로 변환: Fetch join

```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getItem().getPrice();
            count = orderItem.getCount();
        }
    }
}
```
- 필드에서도 엔티티를 그대로 노출하면 안된다.
  - OrderDto에는 OrderItem이 아니라 OrderItemDto 형태로 존재하여야 한다.
  - 단순 값 타입은 변화할 일이 없으므로 바로 사용해도 무관하다.
- 지연 로딩 쿼리 횟수
  - order
    - 1번
  - member, address, orderItem
    - order 결과 개수만큼
  - item
    - orderItem 결과 개수만큼
  - 다만, 같은 Entity가 영속성 컨텍스트에 있다면 지연 로딩이더라도 SQL을 실행하지 않는다.

#### fetch join 최적화
- 쿼리가 많이 나가는 문제를 패치 조인을 통해 해결할것이다.
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {
    public List<Order> findAllWithItem() {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
```
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }
}
```
```json
[
  {
    "orderId": 4,
    "name": "userA",
    "orderDate": "2022-05-01T12:07:06.612872",
    "orderStatus": "ORDER",
    "address": {
      "city": "서울",
      "street": "1",
      "zipcode": "1111"
    },
    "orderItems": [
      {
        "itemName": "JPA1 BOOK",
        "orderPrice": 10000,
        "count": 1
      },
      {
        "itemName": "JPA2 BOOK",
        "orderPrice": 20000,
        "count": 2
      }
    ]
  },
  {
    "orderId": 4,
    "name": "userA",
    "orderDate": "2022-05-01T12:07:06.612872",
    "orderStatus": "ORDER",
    "address": {
      "city": "서울",
      "street": "1",
      "zipcode": "1111"
    },
    "orderItems": [
      {
        "itemName": "JPA1 BOOK",
        "orderPrice": 10000,
        "count": 1
      },
      {
        "itemName": "JPA2 BOOK",
        "orderPrice": 20000,
        "count": 2
      }
    ]
  },
  {
    "orderId": 11,
    "name": "userB",
    "orderDate": "2022-05-01T12:07:06.643625",
    "orderStatus": "ORDER",
    "address": {
      "city": "진주",
      "street": "2",
      "zipcode": "2222"
    },
    "orderItems": [
      {
        "itemName": "SPRING1 BOOK",
        "orderPrice": 20000,
        "count": 3
      },
      {
        "itemName": "SPRING2 BOOK",
        "orderPrice": 40000,
        "count": 4
      }
    ]
  },
  {
    "orderId": 11,
    "name": "userB",
    "orderDate": "2022-05-01T12:07:06.643625",
    "orderStatus": "ORDER",
    "address": {
      "city": "진주",
      "street": "2",
      "zipcode": "2222"
    },
    "orderItems": [
      {
        "itemName": "SPRING1 BOOK",
        "orderPrice": 20000,
        "count": 3
      },
      {
        "itemName": "SPRING2 BOOK",
        "orderPrice": 40000,
        "count": 4
      }
    ]
  }
]
```
하지만 order 결과가 2개, orderItem 4개를 조인하면 order가 4개로 뻥튀기되는 문제가 발생한다.

![](/assets/뻥튀기.png)
- order와 order를 조인하면 중복된 결과가 나온다.
- order는 2개지만 order_item에는 각 order_id에 해당하는 주문이 2개씩 총 4개가 존재하기 때문.
  - 즉, order_item 개수만큼 뻥튀기 된다.
- 뻥튀기된 값은 레퍼런스마저 같다.

```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {
    public List<Order> findAllWithItem() {
        return em.createQuery(
                        "select distinct o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
```
```json
[
  {
    "orderId": 4,
    "name": "userA",
    "orderDate": "2022-05-01T12:06:19.629212",
    "orderStatus": "ORDER",
    "address": {
      "city": "서울",
      "street": "1",
      "zipcode": "1111"
    },
    "orderItems": [
      {
        "itemName": "JPA1 BOOK",
        "orderPrice": 10000,
        "count": 1
      },
      {
        "itemName": "JPA2 BOOK",
        "orderPrice": 20000,
        "count": 2
      }
    ]
  },
  {
    "orderId": 11,
    "name": "userB",
    "orderDate": "2022-05-01T12:06:19.664464",
    "orderStatus": "ORDER",
    "address": {
      "city": "진주",
      "street": "2",
      "zipcode": "2222"
    },
    "orderItems": [
      {
        "itemName": "SPRING1 BOOK",
        "orderPrice": 20000,
        "count": 3
      },
      {
        "itemName": "SPRING2 BOOK",
        "orderPrice": 40000,
        "count": 4
      }
    ]
  }
]
```
- 컬렉션의 데이터 뻥튀기를 막기위해 `distinct`를 사용한다.

#### DB의 distinct
- 한 줄이 완전히 같아야 제거
  - 몇몇 상황에서는 중복 데이터의 모든 칼럼 데이터가 같지 않아 제거되지 않는다.
  - ex. order는 겹치지만 order_item 값은 겹치지 않아 제거x

#### JPA의 distinct
- SQL에 distinct를 추가해서 실제 distinct 쿼리가 나간다.
  - DB상에서는 distinct를 붙이나 안 붙이나 제거되지 않고 들어온다.
- 애플리케이션 상에서 다시 한 번 중복을 거른다.
  - 조회 결과에 같은 Entity가 조회되면 즉, 레퍼런스가 같은 중복 데이터가 있으면 날린다.
- 페이징이 불가능하다는 단점이 있다.
  - 페이징을 설정해도 limit, offset 쿼리가 나가지 않는다.
  - order가 중복 2개씩 총 4개가 있는데 페이징으로 order 1을 건너뛰어도 그 다음 페이지에 중복인 order 1이 다시 들어가서 이상해진다.
    - 우리가 원하는 건 order 2개인데 order_item 기준으로 페이징 된다.
  - 컬렉션 fetch join에서 페이징을 사용하면 모든 데이터를 DB에서 일단 읽어온 뒤, 메모리에서 페이징하면서 OOM이 발생할 수 있다.

#### 컬렉션의 fetch join
- 컬렉션 패치 조인은 1개만 사용 가능하다.
- 2개 이상의 컬렉션에서는 사용할 수 없다.
  - 1대 다의 다가 되면서 다 * 다가 되므로 데이터가 뻥튀기되면서 부정확하게 조회된다.

### Entity를 DTO로 변환: 페이징과 한계 돌파
- 컬렉션을 fetch join 하면 페이징이 불가능하다.
  - 1:N 조인이 발생하므로 데이터가 에측하지 못한 방향으로 뻥튀기된다.
  - 1:N에서 1을 기준으로 페이징 하는게 목적인데 데이터는 N을 기준으로 row가 생성된다.
  - 즉, order를 페이징하고 싶은데 orderItem이 기준이 되어버린다.
- 하이버네이트는 모든 DB 데이터를 읽어온 뒤 메모리에서 페이징하는 위험한 상황이 벌어질 수 있다.

#### 해결 방법
- OneToOne, ManyToOne 관계를 모두 fetch join 한다.
  - ToOne 관계는 row 수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.- ex) order의 member, delivery
- 컬렉션은 지연 로딩으로 조회한다.
  - fetch join은 사용하지 않는다.
  - ex) order의 orderItem
- 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size와 @BatchSize를 적용한다.
  - 컬렉션이나 프록시 객체를 설정한 size만큼 in 쿼리로 조회한다.
  - hibernate.default_batch_fetch_size
    - 글로벌로 설정할 때 사용
  - @BatchSize
    - 개별로 최적화 할 때 사용

#### Before
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        // ToOne 관계는 fetch join으로 가져온다.
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
```
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
```
```sql
select order0_.order_id       as order_id1_6_0_,
       member1_.member_id     as member_i1_4_1_,
       delivery2_.delivery_id as delivery1_2_2_,
       order0_.delivery_id    as delivery4_6_0_,
       order0_.member_id      as member_i5_6_0_,
       order0_.order_date     as order_da2_6_0_,
       order0_.status         as status3_6_0_,
       member1_.city          as city2_4_1_,
       member1_.street        as street3_4_1_,
       member1_.zipcode       as zipcode4_4_1_,
       member1_.name          as name5_4_1_,
       delivery2_.city        as city2_2_2_,
       delivery2_.street      as street3_2_2_,
       delivery2_.zipcode     as zipcode4_2_2_,
       delivery2_.status      as status5_2_2_
from orders order0_
         inner join
     member member1_ on order0_.member_id = member1_.member_id
         inner join
     delivery delivery2_ on order0_.delivery_id = delivery2_.delivery_id

select orderitems0_.order_id      as order_id5_5_0_,
       orderitems0_.order_item_id as order_it1_5_0_,
       orderitems0_.order_item_id as order_it1_5_1_,
       orderitems0_.count         as count2_5_1_,
       orderitems0_.item_id       as item_id4_5_1_,
       orderitems0_.order_id      as order_id5_5_1_,
       orderitems0_.order_price   as order_pr3_5_1_
from order_item orderitems0_
where orderitems0_.order_id = ?

select item0_.item_id        as item_id2_3_0_,
       item0_.name           as name3_3_0_,
       item0_.price          as price4_3_0_,
       item0_.stock_quantity as stock_qu5_3_0_,
       item0_.artist         as artist6_3_0_,
       item0_.etc            as etc7_3_0_,
       item0_.author         as author8_3_0_,
       item0_.isbn           as isbn9_3_0_,
       item0_.actor          as actor10_3_0_,
       item0_.director       as directo11_3_0_,
       item0_.dtype          as dtype1_3_0_
from item item0_
where item0_.item_id = ?

select item0_.item_id        as item_id2_3_0_,
       item0_.name           as name3_3_0_,
       item0_.price          as price4_3_0_,
       item0_.stock_quantity as stock_qu5_3_0_,
       item0_.artist         as artist6_3_0_,
       item0_.etc            as etc7_3_0_,
       item0_.author         as author8_3_0_,
       item0_.isbn           as isbn9_3_0_,
       item0_.actor          as actor10_3_0_,
       item0_.director       as directo11_3_0_,
       item0_.dtype          as dtype1_3_0_
from item item0_
where item0_.item_id = ?
    ...반복
```
- 위 로그는 order 결과 하나 당 나가는 쿼리다.
  - order 조회 후 order_item을 쿼리한다.
  - order_item에 item이 2개 있으므로 다시 2번 쿼리 한다.
- 다른 order에 대해서도 똑같이 order_item 1번, item 2번 쿼리한다.
- order가 100개라면 더 많은 쿼리가 나가게 될 것이다.

#### After
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream().map(OrderDto::new).collect(Collectors.toList());
    }
}
```
```java
@Repository
public class OrderRepository {

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", Order.class)
                // 페이징을 적용한다.
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
```
```yaml
spring:
  jpa:
    properties:
      hibernate:
        # 미리 in 절로 땡겨 올 데이터 개수
        default_batch_fetch_size: 1000
```
```sql
select order0_.order_id       as order_id1_6_0_,
       member1_.member_id     as member_i1_4_1_,
       delivery2_.delivery_id as delivery1_2_2_,
       order0_.delivery_id    as delivery4_6_0_,
       order0_.member_id      as member_i5_6_0_,
       order0_.order_date     as order_da2_6_0_,
       order0_.status         as status3_6_0_,
       member1_.city          as city2_4_1_,
       member1_.street        as street3_4_1_,
       member1_.zipcode       as zipcode4_4_1_,
       member1_.name          as name5_4_1_,
       delivery2_.city        as city2_2_2_,
       delivery2_.street      as street3_2_2_,
       delivery2_.zipcode     as zipcode4_2_2_,
       delivery2_.status      as status5_2_2_
from orders order0_
         inner join
     member member1_ on order0_.member_id = member1_.member_id
         inner join
     --     페이징이 적용된다.
         delivery delivery2_ on order0_.delivery_id = delivery2_.delivery_id limit ?
offset ?

select orderitems0_.order_id      as order_id5_5_1_,
       orderitems0_.order_item_id as order_it1_5_1_,
       orderitems0_.order_item_id as order_it1_5_0_,
       orderitems0_.count         as count2_5_0_,
       orderitems0_.item_id       as item_id4_5_0_,
       orderitems0_.order_id      as order_id5_5_0_,
       orderitems0_.order_price   as order_pr3_5_0_
from order_item orderitems0_
-- in 절로 땡겨온다.
where orderitems0_.order_id in (
                                ?, ?
    )

select item0_.item_id        as item_id2_3_0_,
       item0_.name           as name3_3_0_,
       item0_.price          as price4_3_0_,
       item0_.stock_quantity as stock_qu5_3_0_,
       item0_.artist         as artist6_3_0_,
       item0_.etc            as etc7_3_0_,
       item0_.author         as author8_3_0_,
       item0_.isbn           as isbn9_3_0_,
       item0_.actor          as actor10_3_0_,
       item0_.director       as directo11_3_0_,
       item0_.dtype          as dtype1_3_0_
from item item0_
-- in 절로 땡겨온다.
where item0_.item_id in (
                         ?, ?
    )
```
- 페이징이 잘 적용되었다.
- default_batch_fetch_size
  - order 2개와 그 아래의 데이터를 가져오는 데 쿼리가 3개만 나갔다.
  - 이전에는 order 마다 item 쿼리 2개씩 총 4개가 나갔는데 확 줄었다.
  - pk 기준으로 in 절을 날리기 때문에 쿼리 최적화로 빠르게 가져온다.
  - fetch_size를 100으로 정했는데 데이터가 1000개면 쿼리는 10개가 나간다.

#### 비교
![](/assets/v3.png)
- v3
  - 한방 쿼리로 모든걸 가져온다.
  - 컬렉션 때문에 중복 데이터가 많아 성능상 문제가 있다.

![](/assets/v3.1.png)
- v3.1
  - 중복 없이 최적화가 이루어진다.
  - 데이터를 몇 천개씩 퍼올릴때 사용하면 좋다.

#### 장점
- 쿼리 호출 수가 1+N에서 1+1로 최적화된다.
- fetch join과 비교해 쿼리 호출 수가 약간 증가하지만 중복이 제거되어 DB 데이터 전송량이 감소한다.
- 컬렉션 fetch join은 페이징이 불가능하지만 이 방법은 페이징이 가능하다.
  - ToOne 관계는 fetch join 해도 페이징에 영향을 주지 않는다.
  - 따라서 ToOne 관계는 fetch join으로 쿼리 수를 줄이고, 나머지는 default_batch_fetch_size로 최적화 한다.

#### default_batch_fetch_size
- 적어놓은 개수만큼 미리 땡겨온다.
  - in 절은 PK를 가지고 빠른 속도로 조회해온다.
- 설정 값은 in 쿼리에 들어갈 조건의 개수와 같다.
  - 데이터가 1000개이고 100으로 설정해놨다면 쿼리가 10번 나간다.

실무에서 웬만하면 이 설정을 켜두고 있는 게 좋다.

#### 적정값
- 1000개 이상은 부하로 오류가 발생하므로 사용하지 않는다.
  - DB에 따라 in절 파라미터를 1000으로 제한하기도 한다.
- 100~1000 사이를 권장한다.
  - 값이 적으면 부하를 낮추는 대신 잘라가면서 가니까 속도가 느리다.

```java
orders.stream().map(OrderDto::new).collect(Collectors.toList());
```
- 애플리케이션은 100이든 1000이든 결국 loop를 돌면서 전체 데이터를 로딩한다.
- 따라서 WAS 입장에서는 메모리 사용량이 같다.
- 1000이 쿼리를 덜 날려도 되니 성능상 가장 좋지만, DB와 애플리케이션 모두가 순간 부하를 견딜 수 있는 값으로 한다.

### DTO 직접 조회
- 화면이나 api에 fit한 용도로만 사용하는 쿼리는 패키지를 따로 파서 구현한다.
- 화면과 관련된 로직과 중요 핵심 비즈니스 로직 구분이 가능해진다.

```java
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    // 컬렉션은 별도의 메서드로 조회한다.
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
}
```
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }
}
```
```java
@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    // 컬렉션은 생성자에서 제외한다.
    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```
```java
@Data
public class OrderItemQueryDto {
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
```
- 쿼리는 루트(order)에서 한번, 컬렉션에서(order_item)에서 N번 실행된다.
- ToOne 관계를 먼저 조회하고 ToMany 단계는 별도로 처리한다.
  - ToOne은 join해도 데이터 row 수가 증가하지 않는다.
    - row가 증가하지 않기 때문에 join으로 최적화하기 쉬워 한 번에 조회한다.
  - ToMany는 join 하면 row 수가 증가한다.
    - 최적화하기 어려우므로 findOrderItems()같은 별도의 메서드로 조회한다.

#### N+1
```java
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        // query 1번 -> 결과는 N개
        List<OrderQueryDto> result = findOrders();

        // query N번
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }
}
```
- forEach를 돌며 N+1을 초래한다.

### DTO 직접 조회: 컬렉션 조회 최적화

#### IN절
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

}
```
```java
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    public List<OrderQueryDto> findAllByDto_optimization() {
        // 일단 루트 쿼리로 ToOne 코드를 한 방에 가져온다.
        List<OrderQueryDto> result = findOrders();

        // orderId만 따로 뽑은 다음 쿼리 파라미터로 바로 넘겨버린다.
        List<Long> orderIds = result.stream().map(OrderQueryDto::getOrderId).collect(Collectors.toList());

        // 쿼리는 한 번만 날린다.
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                // in절로 여러 orderId에 대한 order_item을 한 번에 가져온다.
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // 메모리상에서 map으로 변환 후 값을 매칭해서 가져온다.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }
}
```
- in절로 여러 orderId에 대한 쿼리를 한 번에 날린 뒤, 메모리 상에서 값을 매칭해 가져온다.
  - map을 사용해 매칭했기 때문에 성능이 O(1)로 향상되었다.
- 쿼리는 총 2번 나간다.
  - 루트 쿼리 뒤에 orderItem을 가져오는 쿼리가 한 번만 실행된다.
  - ToOne 관계를 먼저 조회하고 여기서 얻은 식별자 orderId로 ToMany 관계인 OrderItem을 한 번에 조회한다.

### DTO 직접 조회: 플랫 데이터 최적화
- 한방으로 쿼리를 날려보도록 하자.

#### Join

```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats;
    }
}
```
```java
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    public List<OrderFlatDto> findAllByDto_flat() {
        // 모든 데이터를 join 한다.
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
```
```java
@Data
public class OrderFlatDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private Address address;
    private OrderStatus orderStatus;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.address = address;
        this.orderStatus = orderStatus;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
```
- 이렇게 모든 데이터를 join 해서 조회하면 중복 데이터를 반환한다.
- order가 아니라 orderItem이 기준이 되면서 페이징이 불가하다.
- 하지만 쿼리를 한방만 날리는 장점이 있다.

#### 중복 제거
```java
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                // orderId 기준으로 묶는다.
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }
}
```
```java
@Data
// groupingBy할 때 묶는 기준이 뭔지 알려줘야 한다.
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, List<OrderItemQueryDto> orderItems) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderItems = orderItems;
    }
}
```
- API 스펙을 OrderQueryDto로 맞춰야 한다면 노가다로 중복을 제거해 변환할 수 있다.
- @EqualsAndHashCode(of = "orderId")

#### 단점
- 쿼리는 한 번이지만 join으로 인해 DB에서 애플리케이션에 전달하는 데이터가 중복된다.
  - 상황에 따라 V5보다 더 느릴 수도 있다.
- 애플리케이션에서 해야 할 추가 작업이 크다.
  - 분해하는 추가 작업이 필요하다.
- 페이징이 불가능하다.
  - 데이터가 중복되기 때문에 정확한 페이징 결과가 나오지 않는다.
  - 2개만 페이지 하면 중복 데이터 2개만 나오게 된다.

### 정리

#### 엔티티 조회 후 DTO 변환

V1
- 엔티티를 조회하여 직접 반환한다.
- 엔티티가 변화면 API 스펙도 변화하기 때문에 DTO로 변환하는것을 권장.

V2
- 엔티티 조회 후 DTO로 변환.
- 여러 테이블을 join할 때 성능이 나오지 않는다.

V3
- 페치 조인으로 쿼리 수를 최적화 한다.

V3.1
- 컬렉션은 fetch join 시 페이징이 불가능하다.
- 실무에서는 페이징을 할 일이 많으므로 다음과 같이 구현한다.
  - ToOne 관계는 fetch join으로 쿼리 수를 최적화한다.
  - 컬렉션은 fetch join 대신 지연 로딩을 유지한다.
    - fetch join을 하면 페이징이 안되기 때문이다.
  - hibernate.default_batch_fetch_size, @BatchSize로 최적화한다.
    - 지연 로딩을 하면 N+1 문제가 나타나므로 이 옵션으로 해결한다.

#### DTO로 직접 조회

V4
- JPA에서 DTO를 직접 조회한다.
- 코드가 단순하다.
- 컬렉션이 아니라 특정 주문 한건만 조회한다면 이 방식으로 성능이 잘 나온다.

V5
- 컬렉션 조회를 최적화한다.
  - in 절을 활용해 메모리 상에서 map으로 값을 매칭한다.
- 여러 주문을 한 번에 조회하는 경우에는 이 방식을 사용한다.
  - Order 데이터가 1000건일 때 V4는 쿼리가 1 + 1000번 실행된다.
    - 1은 Order를 조회한 쿼리 수, 1000은 조회된 Order의 row 수다.
  - V5를 쓰면 쿼리가 총 1 + 1만 실행된다.
- 쿼리가 2번 나가지만(네트워크를 2번 타지만) 딱 정규화된 데이터를 보낸다.
- DTO를 직접 조회해야할 때는 거의 V5 방식을 많이 사용한다.
- 코드가 복잡하다.

V6
- 플랫 데이터로 최적화한다.
  - join 결과를 그대로 조회한 후 애플리케이션에서 원하는 모양으로 직접 변환한다.
- 쿼리 한 번으로 조회해서 좋아 보이지만 Order 기준으로 페이징이 불가하다.
- 실무에서는 이 정도로 데이터가 많으면 페이징이 꼭 필요하므로 선택하기 어렵다.
- 데이터가 많으면 중복 전송이 증가해서 V5와의 성능 차이도 미비하다.
  - 데이터가 뻥튀기 되어서 네트워크 전송량이 많아지기 때문에 그걸 따지면 V5가 더 좋을 수도 있다.

### 권장 순서
1. Entity 조회 후 DTO로 변환하는 방식을 우선 고려한다.
  - fetch join으로 쿼리 수를 최적화한다.
  - 컬렉션을 최적화한다.
    - 페이징이 필요하다면 hibernate.default_batch_fetch_size, @BatchSize로 최적화한다.
    - 페이징이 필요없다면 fetch join을 사용한다.
2. Entity 조회 방식으로 해결이 안되면 DTO 조회 방식을 사용한다.
3. DTO 조회 방식으로 해결이 안되면 NativeSQL이나 스프링 JdbcTemplate을 사용한다.

### 참고
- Entity 조회 방식은 코드를 거의 수정하지 않는다.
  - fetch join, hibernate.default_batch_fetch_size, @BatchSize 등 옵션만 약간 변경해서 다양한 성능 최적화를 시도할 수 있다.
- DTO를 직접 조회하는 방식은 성능을 최적화하거나 성능 최적화 방식을 변경할 때 많은 코드를 변경해야 한다.
- Entity 조회 방식으로 대부분의 애플리케이션이 해결된다.
- 해결이 안되는 문제는 정말 트래픽이 많은 것이므로 캐시 등 다른 방법을 생각해봐야 한다.
  - 참고로 Entity는 영속성 컨텍스트에서 관리되는 상태가 있기 때문에 캐싱하면 안된다.
  - Entity를 DTO로 변환한 값을 캐시해야 한다.
- Entity 조회 방식은 JPA가 많은 부분을 최적화해주기 때문에 단순한 코드를 유지하면서 성능을 최적화할 수 있다.
- DTO 조회 방식은 SQL을 직접 다루는 것과 유사하기 때문에 성능 최적화와 코드 복잡도 사이에서 줄타기를 해야한다.


