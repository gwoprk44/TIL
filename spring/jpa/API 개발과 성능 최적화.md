# 스프링 부트와 JPA 활용 - API 개발과 성능 최적화

## 목차


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