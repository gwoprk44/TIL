# JPA 자바 ORM 표준 프로그래밍

## 목차
- [JPA 시작하기](#jpa-시작하기)
    - [Dialect](#dialect-방언)
    - [애플리케이션 개발](#애플리케이션-개발)
    - [주의할 점](#주의-할-점)
    - [JPQL](#jpql)
- [영속성 관리](#영속성-관리)
    - [영속성 컨텍스트](#영속성-컨텍스트)
    - [플러시](#플러시)
    - [준영속 상태](준영속-상태)
- [Entity 매핑]()
    - [객체와 테이블 매핑](#객체와-테이블-매핑)
    - [데이터베이스 스키마 자동 생성](#데이터베이스-스키마-자동-생성)
    - [필드와 칼럼 매핑](#필드와-컬럼-매핑)
    - [기본 키 매핑](#기본-키-매핑)
    - [실전 예제](#실전-예제)

# JPA 시작하기

## Dialect; 방언

![alt text](/assets/방언.png)

- JPA는 특정 DB에 종속되지 않는다.
- 각각의 DB는 제공하는 SQL 문법이 조금씩 다른데 이를 방언이라 한다.

## 애플리케이션 개발

#### JPA 구동방식
![alt text](/assets/구동방식.png)

JPA의 `Persistence` 클래스가 `META-INF/persistence.xml` 설정 파일을 읽어서 `EntityManagerFactory`라는 클래스를 생성한다. 여기서 필요할 때마다 `EntityManager`를 만든다.

### 회원 생성

```java
public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory는 애플리케이션 로딩 시점에 딱 하나만 만들어야 한다.
        // persistence.xml에서 설정했던 'persistence-unit name' 값을 넘긴다.
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");

        // EntityManager를 꺼낸다.
        // EntityManager는 트랜잭션마다 만들어줘야 한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // JPA의 모든 작업은 트랜잭션 내에서 해야 적용된다.
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            // 실제 동작 코드를 작성한다.
            Member member = new Member();
            member.setId(1L);
            member.setName("helloA");

            // 저장한다.
            entityManager.persist(member);

            // 정상적이면 커밋한다.
            tx.commit();
        } catch (Exception e) {
            // 실패하면 롤백한다.
            tx.rollback();
        } finally {
            // EntityManager가 내부적으로 데이터베이스 커넥션을 물고 동작하기 때문에 쓰고 나서 꼭 닫아줘야 한다.
            entityManager.close();
        }

        // 전체 애플리케이션이 끝나면 팩토리까지 닫아준다.
        entityManagerFactory.close();
    }
}
```

### 회원 수정
```java
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            // 수정할 대상을 가져온다.
            Member findMember = entityManager.find(Member.class, 1L);

            System.out.println("id: " + findMember.getId());
            System.out.println("name: " + findMember.getName());

            findMember.setName("helloJPA");

            // 수정한 객체를 따로 저장하지 않아도 된다. 
            // 데이터를 JPA를 통해 가져오면 변경 여부를 트랜잭션 커밋 시점에
            // 다 체크해서 바뀐 내용에 대해 업데이트 쿼리를 만들어 날린다.

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
```

## 주의 할 점

- EntityManagerFactory
    - 애플리케이션 실행 시점에 하나만 생성해서 애플리케이션 전체에 걸쳐 공유한다.
- EntityManager
    - 요청이 왔을 때 썼다가 끝나면 버리는 사이클을 반복한다.
    - 그래서 절대 스레드 간에 공유해서는 안된다. 사용하고 바로 버려야 한다.
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
    - 트랜잭션을 실제 지정해본 경험이 없어도 DB 자체적으로 트랜잭션을 사용하기 때문에 적용해왔을 것이다.

## JPQL
```java
public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        try {
            // 일반적인 쿼리처럼 생겼지만 JPA는 테이블이 아니라
            // 객체를 대상으로 쿼리를 짜기 때문에
            // 여기서 Member는 테이블이 아니라 객체를 가리킨다.
            List<Member> result = entityManager
                    .createQuery("select m from Member as m", Member.class)
                    // 페이지네이션을 할 수도 있다. 아래는 1번부터 10개 가져온다.
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            for (Member member : result) {
                System.out.println("name: " + member.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            entityManager.close();
        }

        entityManagerFactory.close();
    }
}
```
- JPA를 사용하면 Entity 객체를 중심으로 개발하게 된다.
- 검색 또한 테이블이 아닌 Entity 객체를 대상으로 한다.
    - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능하다.
- 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요하다.
    - 하지만 쿼리를 쓰면 SQL에 종속된다.
- 이때 객체를 대상으로 검색할 수 있게 하는 기술이 JPQL이다.

---


# 영속성 관리
![영속성관리](/assets/영속성관리.png)

- 요청마다 엔티티매니저팩토리를 통해 엔티티 매니저 생성
- 엔티티매니저는 내부적으로 DB커넥션을 통해 DB를 사용

## 영속성 컨텍스트

- 엔티티를 영구 저장하는 환경
- 논리적인 개념이라 눈에 보이지 않는다.
- 엔티티 매니저를 통해 접근한다.

### 엔티티 생명주기
- 비영속
    - new/transient
    - 객체를 생성한 최초 상태
    - 영속성 컨텍스트의 관리를 받지 않는다.
- 영속
    - managed
    - `persist()`한 상태
    - 영속성 컨텍스트에서 관리된다.
- 준영속
    - detached
    - 영속성 컨텍스트에 저장되었다가 분리된 상태
    - 영속성 컨텍스트와 아무 관계가 아닌 상태이다.
- 삭제
    - removed
    - DB에서 데이터를 삭제한 상태

### 영속성 컨텍스트의 이점

#### 1차 캐시
- 사실상 영속성 컨텍스트와 동일한 의미이다.

![1차캐시](/assets/1차캐시1.png)
- member를 생성하면 비영속 상태.
- 영속화를 하면 PK가 키, 엔티티 값이 되어 1차 캐시에 저장된다.
- 1차캐시에 저장을 하면 조회 시 DB대신 1차 캐시를 뒤져 반환한다.
- DB에는 존재하지만 1차 캐시에 존재하지 않는 값은 1차 캐시에 저장한뒤 반환한다.
    - 이후 동일 값의 조회 요청이 들어오면 1차 캐시에서 값을 반환한다.
- 성능상 이점은 크게 존재하지 않는다.

#### 동일성 보장
- 자바 컬렉션에서 꺼낸 데이터가 레퍼런스가 같듯이 JPA도 이와 같은 동일성을 보장한다.
- 반복 가능한읽기 등급의 트랜잭션 격리 수준을 제공한다.

#### 트랜잭션을 지원하는 쓰기 지연

![쓰기지연](/assets/쓰기지연.png)

- `persist()`시 바로 쿼리를 날리지 않는다.
- JPA는 쿼리를 쌓아놨다가 트랜잭션을 커밋하는 순간에 DB에 쿼리를 날린다.

![쓰기지연2](/assets/쓰기지연2.png)

![쓰기지연3](/assets/쓰기지연3.png)

```java
public class JpaMain {

    public static void main(String[] args) {
        
        ...

        Member member1 = new Member(150L, "A");
        Member member2 = new Member(160L, "B");

        // 쓰기 지연 SQL 저장소에 저장된다.
        entityManager.persist(member1);
        entityManager.persist(member2);
        System.out.println("------------");

        // 실제 쿼리가 날아간다.
        tx.commit();
    }
}
```

#### 변경 감지(더티 체킹)
![](/assets/영속성컨텍스트.png)
- JPA는 자바 컬렉션처럼 값을 다루는게 목적이다.
    - 컬렉션에서 꺼낸 값을 변경했다고 컬렉션에 다시 집어넣지 않는다.
    - JPA도 마찬가지로 변경후에 `persist()`사용을 안해도 된다.
- 이러한것이 가능한 이유는 영속성 컨텍스트덕분이다.
    - 1차 캐시에 스냅샷이라는것이 존재하는데 이 스냅샷은 최초로 영속성 컨텍스트에 들어온 상태를 저장해 둔 것이다.
    - 커밋하는 시점에 내부적으로  `flush()`되면서 엔티티와 스냅샷을 비교하여 변화한게 있으면 `update`쿼리를 생성한다.
    - 쿼리를 쓰기 지연 sql 저장소에 반영한 뒤 DB에 날린다.

## 플러시
- 영속성 컨텍스트의 변경 내용을 DB에 반영
    - 변경 감지
    - 수정된 엔티티를 쓰기 지연 SQL 저장소에 등록
    - 쿼리를 DB에 전송
- 커밋시 플러시는 자동 발생한다.

### 영속성 컨텍스트를 플러시 하는 방법
- 직접 호출
    - `em.flush()`이용
- 자동 호출
    - 트랜잭션 커밋
    - JPQL 쿼리 실행

```java
public class JpaMain {

    public static void main(String[] args) {
        
        ...

        Member member = new Member(200L, "A");
        entityManager.persist(member);

        entityManager.flush();
        System.out.println("-----");

        tx.commit();
    }
}
```
- 쿼리를 미리 반영하고 싶다면 커밋 전에 `flush()`를 이용하여 호출 가능하다.
- 이를 사용해도 1차 캐시는 유지된다.
    - 1차 캐시와는 상관없이 지연 SQL 저장소에 쌓인 쿼리나 변경 감지 내용이 DB에 날라가기 때문.
- JPQL 실행시에는 자동으로 무조건 `flush()`를 날린다.

## 준영속 상태
- 영속
    - 영속성 컨텍스트에서 관리되는 상태
    - insert뿐만 아니라 조회시 1차 캐시에 없어서 1차 캐시에 올리는 상태도 포함.
- 준영속
    - 영속 상태의 엔티티가 영속성 컨텍스트에서 분리되는 상태
    - `detach()`를 실행하면 트랜잭션을 커밋해도 영향을 받지 않는다.
    - 영속성 컨텍스트가 더 이상 관리하지 않는 상태가 된다.
    - 즉, 영속성 컨텍스트가 제공하는 기능을 사용할 수 없다.

### 준영속 상태로 만드는 법
- em.detach(entity)
    - 특정 엔티티만 준영속 상태로 전환한다.
- em.clear()
    - 영속성 컨텍스트를 통으로 지운다.
    - 1차 캐시등이 사라졌기 때문에 같은 데이터를 조회해도 다시 db에서 가져온다.
- em.close()
    - 영속성 컨텍스트를 종료한다.

---

# Entity 매핑

## 객체와 테이블 매핑

### @Entity

JPA가 관리하는 객체이다. JPA를 사용하여 테이블과 매핑할 클래스는 이 어노테이션을 반드시 사용해야 한다.
- 기본 생성자를 필수로 구현해야 한다.
    - 파라미터가 없는 public or protected 생성자
- final, enum, interface, inner 클래스에 사용 불가하다.
- DB에 저장할 필드에는 final 사용이 불가하다.

#### name 속성
- jpa에 사용할 엔티티 이름을 지정한다.
- 기본값은 클래스명이다.
- 같은 이름이 존재하는것이 아니면 가급적 기본값을 사용하자.

### @Table
Entity와 매핑할 테이블을 지정한다.

#### name 속성
- 매핑할 테이블 이름을 지정한다.
- 실제 쿼리 역시 name에 지정한 테이블로 날아간다.
- 엔티티 이름을 기본값으로 사용한다.

#### catalog 속성
- db catalog를 매핑한다.

#### schema 속성
- 데이터베이스 schema 매핑.

- uniqueConstraints(DDL) 속성
DDL 생성 시에 유니크 제약 조건 생성.

## 데이터베이스 스키마 자동 생성
- JPA는 애플리케이션 실행 시점에 DDL을 자동 생성하게 할 수 있다.
- 데이터베이스 방언을 활용해 DB에 맞는 DDL을 생성한다.
- 미리 테이블을 만들지 않아도 되므로 객체 중심의 개발을 할 수 있는 장점이 있다.
- 운영에선 쓰면 안되고 개발 단계에서 쓰면 좋다.
- 각 SQL 방언에 맞춰 생성해준다.

![](/assets/스키마.png)

### 주의사항
- 운영 서버에서는 절대 create, create-drop, update를 사용하지말자.
- 로컬 서버를 제외한 서버는 직접 스크립트를 짜서 반영하도록 하자.

## DDL 생성 기능

### 제약 조건 추가

`@Column(nullable = false, length = 10)`

### 유니크 제약 조건 추가

`@Table(uniqueConstraints = {@UniqueConstraint( name = "NAME_AGE_UNIQUE", columnNames = {"NAME", "AGE"} )})`

- 위 조건들은 DB에만 영향을 미친다.
- DDL 생성에만 관여.

## 필드와 컬럼 매핑

```java
@Entity
public class Member {

    @Id
    private Long id;

    // DB의 칼럼 명을 따로 명시할 수 있다.
    @Column(name = "name")
    private String username;

    private Integer age;

    // DB에는 enum 타입이 없어서 이 애너테이션을 달아줘야 한다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 날짜 타입은 @Temporal을 달아준다.
    // DB는 DATE, TIME, TIMESTAMP로 나뉘기 때문에 정보를 줘야 한다.
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // varchar를 넘어서는 큰 컨텐츠를 넣고 싶을 때 사용한다.
    // String 타입이면 DB에서 clob으로 생성된다.
    @Lob
    private String description;

    // getter, setter
}
```

![](/assets/필드.png)


### @Column

![](/assets/컬럼.png)

### @Enumerated

![](/assets/인엄.png)

- 자바 enum 타입 매핑시 사용.
- `ORDINAL`은 사용하지 말자.

### @Temporal

![](/assets/템퍼럴.png)
- 날짜 타입을 매핑할 때 사용한다.
- 최신 하이버네이트 사용시 생략가능하게 지원해준다.

### @Lob

- db blob타입과 clob타입을 매핑한다.
- 지정할 수 잇는 속성은 없으며 문자면 clob 나머지는 blob로 매핑한다.

### @Transient
- 필드 매핑을 사용하지 않을 때 사용한다.
- db에 저장이나 조회가 되지 않는다.
- 주로 메모리 상에서 임시로 어떤 값을 저장할 때 사용한다.

## 기본 키 매핑

### 직접 할당
- `@Id`만 사용하는 방식이다.

### 자동 생성
- `@GeneratedValue`를 추가한 방식이다.

#### IDENTITY

- 기본 키 생성을 DB에 위임한다.
- DB에 insert SQL을 실행하여야 ID 값을 알 수 있다.
- JPA는 identity 전략에서만 `em.persist()`로 호출하자 마자 바로 insert 쿼리를 날린다.
    - 영속성 컨텍스트에서 관리되려면 PK값이 필요하기 때문.

#### SEQUENCE
- DB에 시퀀스 오브젝트를 생성하여 이 값을 통해 세팅한다.
- JPA는 전략이 sequence면 id값을 DB에서 먼저 가져온다.
    - 이와 동시에 다음 시퀀스 값으로 이동.
    - 이 때문에 `persist()`시점에 id를 조회 가능하다.
- 이 상태로 영속성 컨텍스트에 남아있다가 실제 커밋 시점에 insert 쿼리를 날린다.

하지만 이렇게 매번 db와 통신하는것이 서버에 부담을 줄 수 있기 때문에 `allocationSize`를 사용할 수 있다.
- 기본값 50개를 DB에서 가져와 메모리 상에 일단 준비해놓고 거기서 사용한다.
    - 여러 데이터를 이어서 persist() 하면서 next value를 call 하지 않는다.
- 다 쓰면 다시 call 해서 그 다음 50개를 가져온다.
- 여러 웹 서버가 있어도 동시성 문제 없이 다양한 이슈가 해결된다.

#### Table
- 키 생성 전용 테이블을 만들어 db 시퀀스를 흉내내는 전략이다.
- 모든 데이터베이스에 적용 가능하나 테이블이 따로 생기다보니 성능에 안좋은 영향.

### 권장하는 식별자 전략
- 기본키 제약 조건
    - Not null
    - 유일성
    - 불변성
- 변하면 안되는 조건이 미래까지 지켜질만한 자연키를 찾기가 어렵다.
- 예제에서 살펴봤듯이 대리키(대체키)를 사용하는것이 좋다.
    - Long 타입
    - 시퀀스, UUID 등의 대체키
    - 키 생성 전략 조합(회사 관례 등)

## 실전 예제

### 테이블 설계

![](/assets/테이블설계.png)

### 엔티티 설계와 매핑

![](/assets/엔티티설계.png)

테이블과 엔티티를 똑같이 설계하여 코드를 작성하였다.

이렇게 됐을때의 문제점은 다음과 같다.

- 주문한 회원을 찾기위해
    - `order`를 조회하고
    - `memberId`를 찾아
    - 다시 `member`를 조회해야 한다.
- 객체는 참조를 통해 쭉쭉쭉 찾아가야 하지만 이러한 설계는 식별자를 통해 찾아가므로 흐름이 끊겨버린다. 이러한 설계는 객체지향스럽지 않다.

### 데이터 중심 설계의 문제점
- 테이블의 외래키를 그대로 찾아온다.
- 객체 그래프 탐색이 불가능하다.
- 참조가 없으므로 앞서만든 UML도 오류라고 볼 수 있다.
    - 엔티티 매핑 다이어그램을 보면 id만 존재할 뿐 실제로 참조는 다 끊기는 모습이다.

이러한 문제점을 연관관계 매핑을 통해 해결이 가능하다.

---