# JPA 자바 ORM 표준 프로그래밍

## 목차
- [JPA 자바 ORM 표준 프로그래밍](#jpa-자바-orm-표준-프로그래밍)
  - [목차](#목차)
- [JPA 시작하기](#jpa-시작하기)
  - [Dialect; 방언](#dialect-방언)
  - [애플리케이션 개발](#애플리케이션-개발)
      - [JPA 구동방식](#jpa-구동방식)
    - [회원 생성](#회원-생성)
    - [회원 수정](#회원-수정)
  - [주의 할 점](#주의-할-점)
  - [JPQL](#jpql)
- [영속성 관리](#영속성-관리)
  - [영속성 컨텍스트](#영속성-컨텍스트)
    - [엔티티 생명주기](#엔티티-생명주기)
    - [영속성 컨텍스트의 이점](#영속성-컨텍스트의-이점)
      - [1차 캐시](#1차-캐시)
      - [동일성 보장](#동일성-보장)
      - [트랜잭션을 지원하는 쓰기 지연](#트랜잭션을-지원하는-쓰기-지연)
      - [변경 감지(더티 체킹)](#변경-감지더티-체킹)
  - [플러시](#플러시)
    - [영속성 컨텍스트를 플러시 하는 방법](#영속성-컨텍스트를-플러시-하는-방법)
  - [준영속 상태](#준영속-상태)
    - [준영속 상태로 만드는 법](#준영속-상태로-만드는-법)
- [Entity 매핑](#entity-매핑)
  - [객체와 테이블 매핑](#객체와-테이블-매핑)
    - [@Entity](#entity)
      - [name 속성](#name-속성)
    - [@Table](#table)
      - [name 속성](#name-속성-1)
      - [catalog 속성](#catalog-속성)
      - [schema 속성](#schema-속성)
  - [데이터베이스 스키마 자동 생성](#데이터베이스-스키마-자동-생성)
    - [주의사항](#주의사항)
  - [DDL 생성 기능](#ddl-생성-기능)
    - [제약 조건 추가](#제약-조건-추가)
    - [유니크 제약 조건 추가](#유니크-제약-조건-추가)
  - [필드와 컬럼 매핑](#필드와-컬럼-매핑)
    - [@Column](#column)
    - [@Enumerated](#enumerated)
    - [@Temporal](#temporal)
    - [@Lob](#lob)
    - [@Transient](#transient)
  - [기본 키 매핑](#기본-키-매핑)
    - [직접 할당](#직접-할당)
    - [자동 생성](#자동-생성)
      - [IDENTITY](#identity)
      - [SEQUENCE](#sequence)
      - [Table](#table-1)
    - [권장하는 식별자 전략](#권장하는-식별자-전략)
  - [실전 예제-1](#실전-예제-1)
    - [테이블 설계](#테이블-설계)
    - [엔티티 설계와 매핑](#엔티티-설계와-매핑)
    - [데이터 중심 설계의 문제점](#데이터-중심-설계의-문제점)
- [연관 관계 매핑](#연관-관계-매핑)
  - [예제 시나리오](#예제-시나리오)
  - [단방향 연관 관계](#단방향-연관-관계)
      - [엔티티 정의](#엔티티-정의)
      - [엔티티 저장 및 조회](#엔티티-저장-및-조회)
    - [연관 관계 수정](#연관-관계-수정)
  - [양방향 연관 관계](#양방향-연관-관계)
      - [정의](#정의)
      - [Team](#team)
    - [연관 관계의 주인과 mappedBy](#연관-관계의-주인과-mappedby)
    - [연관 관계의 주인](#연관-관계의-주인)
    - [주인을 결정하는 기준](#주인을-결정하는-기준)
    - [Tip](#tip)
    - [정리](#정리)
  - [실전 예제-2](#실전-예제-2)
    - [단방향 연관관계 설정](#단방향-연관관계-설정)
    - [양방향 연관관계 설정](#양방향-연관관계-설정)
- [다양한 연관 관계 매핑](#다양한-연관-관계-매핑)
  - [다대일](#다대일)
    - [단방향](#단방향)
    - [양방향](#양방향)
  - [일대다](#일대다)
    - [단방향](#단방향-1)
    - [정리](#정리-1)
    - [양방향](#양방향-1)
  - [일대일](#일대일)
    - [주 테이블 외래키 단방향](#주-테이블-외래키-단방향)
      - [Member](#member)
      - [Locker](#locker)
    - [주 테이블 외래키 양방향](#주-테이블-외래키-양방향)
      - [Member](#member-1)
      - [Locker](#locker-1)
    - [대상 테이블 외래키 단방향](#대상-테이블-외래키-단방향)
    - [대상 테이블 외래키 양방향](#대상-테이블-외래키-양방향)
    - [정리](#정리-2)
      - [주 테이블에 외래키를 두는 방법](#주-테이블에-외래키를-두는-방법)
      - [대상 테이블에 외래키를 두는 방법](#대상-테이블에-외래키를-두는-방법)
  - [다대다](#다대다)
  - [실전 예제-3](#실전-예제-3)
    - [Entity](#entity-1)
    - [ERD](#erd)
    - [연관 관계 구현](#연관-관계-구현)
      - [Delivery](#delivery)
      - [Category](#category)
      - [Item](#item)
      - [Order](#order)
    - [@JoinColumn](#joincolumn)
    - [@ManyToOne](#manytoone)
    - [@OneToMany](#onetomany)
- [고급 매핑](#고급-매핑)
  - [상속 관계 매핑 정의](#상속-관계-매핑-정의)
  - [주요 애너테이션](#주요-애너테이션)
    - [@Inheritance](#inheritance)
    - [@DiscriminatorColumn](#discriminatorcolumn)
    - [@DiscriminatorValue](#discriminatorvalue)
  - [상속 관계 매핑](#상속-관계-매핑)
    - [조인 전략](#조인-전략)
      - [장점](#장점)
      - [단점](#단점)
    - [단일 테이블 전략](#단일-테이블-전략)
      - [장점](#장점-1)
      - [단점](#단점-1)
    - [구현 클래스 마다 단일 테이블 전략](#구현-클래스-마다-단일-테이블-전략)
      - [장점](#장점-2)
      - [단점](#단점-2)
    - [정리](#정리-3)
  - [매핑 정보 상속](#매핑-정보-상속)
    - [특징](#특징)
  - [실전 예제-4](#실전-예제-4)
      - [Item](#item-1)
- [프록시와 연관 관계](#프록시와-연관-관계)
  - [프록시](#프록시)
    - [프록시 기초](#프록시-기초)
    - [프록시 특징](#프록시-특징)
    - [프록시 객체의 초기화](#프록시-객체의-초기화)
    - [주의 사항](#주의-사항)
    - [준영속 상태의 프록시](#준영속-상태의-프록시)
    - [프록시 유틸리티 메서드](#프록시-유틸리티-메서드)
  - [즉시 로딩과 지연 로딩](#즉시-로딩과-지연-로딩)
    - [지연 로딩](#지연-로딩)
    - [즉시 로딩](#즉시-로딩)
    - [fetch join](#fetch-join)
  - [영속성 전이와 고아 객체](#영속성-전이와-고아-객체)
    - [영속성 전이](#영속성-전이)
    - [고아 객체 제거](#고아-객체-제거)
      - [orphanRemoval = true](#orphanremoval--true)
      - [CascadeType.REMOVE](#cascadetyperemove)
      - [`CascadeType.ALL`과 `orphanRemoval = true` 동시 사용](#cascadetypeall과-orphanremoval--true-동시-사용)
  - [실전 예제-5](#실전-예제-5)
    - [글로벌 fetch 전략 설정](#글로벌-fetch-전략-설정)



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

## 실전 예제-1

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



# 연관 관계 매핑

## 예제 시나리오
- 회원과 팀이 존재.
- 회원은 하나의 팀에만 소속 가능.
- 회원과 팀은 다대일 관계.
    - 하나의 팀에 여러 회원이 소속 가능하다.

![](/assets/연관관계.png)

## 단방향 연관 관계
![](/assets/단방향.png)
- 객체 지향적으로 모델링을 진행하면 Member에는 teamId가 아닌 Team의 참조값을 가지게 된다.

#### 엔티티 정의
```java
@Entity
public class Member {

    private Long id;
    @Column(name = "USERNAME")
    private String name;

    // 이제 FK 대신 객체를 참조하기 위해 삭제한다.
    //  @Column(name = "TEAM_ID")
    //  private Long teamId;

    // 하나의 팀에 여러 멤버가 소속될 수 있으므로
    // Member 입장에서는 many, Team 입장에서는 one
    @ManyToOne
    // join 하기 위한 FK를 명시해준다.
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
```

#### 엔티티 저장 및 조회
```java
public class JpaMain() {

    public static void main(String[] args) {
        // 팀 저장
        Team team = new Team();
        team.setName("TeamA");

        em.persist(team);

        // 회원 저장
        Member member = new Member();
        member.setName("member1");
        // id 대신 team 객체를 바로 넣어 주면 알아서 FK로 insert 한다.
        member.setTeam(team);

        em.persist(member);

        // 조회
        // member는 앞에서 이미 영속성 컨텍스트에 들어가 있어서
        // 1차 캐시에 저장이 되어있는 상태이기 때문에 조회 쿼리는 나가지 않는다.
        // 만약 쿼리를 직접 보고 싶다면
        // em.flush()   싱크를 맞추고
        // em.clear()   영속성 컨텍스트를 초기화 하면 findMember부터는 깔끔한 영속성 컨텍스트에서 다시 가져오면서 쿼리 로그를 볼 수 있다.
        Member findMember = em.find(Member.class, member.getId());

        // 이전에는 member.getTeamId()를 사용했는데 이제 바로 접근할 수 있다.
        Team findTeam = findMember.getTeam();

        tx.commit();
    }
}
```

![](/assets/단방향매핑.png)

- FK 대신 객체를 참조하며 연관 관계가 매핑.

### 연관 관계 수정

```java
public class JpaMain() {

    public static void main(String[] args) {
        
        ...

        // 만약 다른 팀으로 바꾸고 싶다면
        Team teamB = new Team();
        teamB.setName("TeamB");

        em.persist(teamB);

        // 그냥 다시 새로운 팀으로 넣어주면 된다.
        member.setTeam(teamB);

        // 그냥 값만 바꾸면 변경 감지해서 update 쿼리가 나간다.
    }
}
```
- 연관 관계 수정시 그냥 값을 바꿔 넣어주면 알아서 update 쿼리를 날린다.

## 양방향 연관 관계
![](/assets/양방향.png)

- 양방향 형태로 전환하여도 테이블 연관 관계는 변하지 않는다.
    - 테이블은 member에서 team을, team에서 member를 외래키로 join하여 자유롭게 조회 가능하다.
- 그러나 객체는 team에 `List<Member> members`를 넣어야 접근이 가능해진다.

#### 정의
```java
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USERNAME")
    private String name;
    private int age;

    @ManyToOne
    @Column(name = "TEAM_ID")
    private Team team;
}
```

#### Team
```java
@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // mappedBy는 Member에 있는 'team' 변수가 연결되어 있음을 의미한다.
    // 즉, 반대편에 무엇이 걸려있는지 알려준다.
    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>(); // 리스트는 초기화 하는 것이 관례다. add 할 때 NPE를 방지한다.

}
```

### 연관 관계의 주인과 mappedBy
![](/assets/연관관계설명.png)

- 객체는 연관관계가 2개이다.
    - 회원에서 팀
    - 팀에서 회원
    - 단방향이 두개 있는것과 같다.
- 테이블은 연관관계가 1개이다
    - 회원과 팀 사이의 양방향
    - member 테이블의 FK와 team의 PK를 조인하면 된다.
- 객체의 양방향 관계는 사실 양방향이 아니라 서로 다른 단방향 관계가 2개인 상태이다.
- 따라서 객체를 양방향으로 참조하려면 단방향 연관 관계를 2개 만들어야 한다.

### 연관 관계의 주인
![](/assets/연관관계주인.png)

- member를 새로운 team에 넣어주고싶다고 가정하였을 때 member에서 team 값을 변경할지 team에서 member 값을 변경할지 딜레마가 발생한다.
- db입장에서는 member의 FK인 team_id값만 변경되면 된다.
- 따라서 둘 중 하나에서 외래키를 관리해야 하는데 이를 연관관계의 주인을 설정하는 과정이라 한다.

- 연관 관계의 주인
    - 외래키 등록, 수정 등을 관리
    - mappedBy 속성을 사용하지 않는다.
- 주인이 아닌 쪽
    - 읽기만 가능하다.
    - mappedBy 속성을 사용하며 주인을 명시한다.

### 주인을 결정하는 기준

```java
@Entity
public class Member {
    ...

    // 외래키가 있는 곳을 연관 관계의 주인으로 정한다.
    // 연관 관계 주인인 team만 insert, update를 할 수 있다.
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;
}

@Entity
public class Team {
    ...

    // team에 의해 관리가 된다는 뜻이다. 즉, team이 연관 관계의 주인이다.
    // 따라서 읽기만 가능하다. 값을 넣어도 아무 일이 일어나지 않는다.
    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();
}
```
![](/assets/연관관계주인설명.png)
- 주인
    - 외래키가 있는 Many를 주인으로 정한다.
- 가짜 매핑
    - 주인이 반대편인 One쪽을 의미한다.

Member.team이 주인이 되면 외래키가 Member에 있기 때문에 쿼리를 한 방에 보낼 수 있다. Member 객체를 바꿨으니까 member 테이블에 업데이트 쿼리가 나가는구나 하고 직관적으로 이해가 된다.

반대로 Team.members가 주인이 되면, members를 바꿨을 때 내가 수정한 테이블인 team이 아니라 member 테이블에 쿼리가 나가야 한다. 나는 team 객체를 수정했는데 member 테이블이라는 엉뚱한 곳에 쿼리가 나가기 때문에 혼란스럽다.

### Tip
- 무조건 외래키가 있는곳을 주인으로 정하자.
- db입장에서는 외래키가 있는곳이 무조건 N이고 아닌 곳은 무조건 1이다.
- N쪽이 무조건 연관관계의 주인이 되도록 하자.
    - N쪽이 무조건 `ManyToOne`이 된다.

### 정리
- 웬만하면 단방향 매핑으로 해결하도록 하자.
    - 단방향 매핑으로도 이미 연관 관계 매핑은 완료된다.
    - 테이블 설계를 어느 정도 하면서 객체 설계를 진행하므로 테이블에서 파악된 FK로 단방향 매핑을 설계하자.
- 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색) 기능을 추가한 것 뿐이다.
    - 양방향을 사용하면 고려할 것만 늘어난다.
- 실무에서 사용시 JPQL등으로 역방향 참조가 필요할 경우에만 사용하도록 하자.
    - 단방향으로 매핑을 해놓고, 필요시에만 양방향 매핑을 활용
- 연관 관계의 주인
    - 비즈니스 로직 기준 X
    - 외래 키의 위치 기준 O

## 실전 예제-2

![](/assets/객체구조.png)
- 참조를 사용하도록 변경

![](/assets/테이블구조.png)

### 단방향 연관관계 설정
```java
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // Member 객체 자체를 참조하고 FK를 기준으로 join 한다.
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
```

```java
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    // 외래키를 관리하니까 OrderItem.order와 OrderItem.item이 연관 관계의 주인이 된다.
    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
```

### 양방향 연관관계 설정
```java
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    // 비즈니스적으로 가치가 있는 값은 양방향으로 설정한다.
    // OrderItem.order가 연관 관계의 주인이다.
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
```

```java
@Entity
@Table(name = "ORDERS")
public class Order {

    ...

    // Order와 OrderItem이 양방향 연관 관계이므로 연관 관계 편의 메서드를 구현한다.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
```

단방향을 그대로 유지하여도 상관이 없다. 즉, Order에 orderItem 참조를 굳이 추가하지 않아도 된다. 예제에서는 연습을 위해 사용하였다.



# 다양한 연관 관계 매핑

연관관계 매핑시 고려할 사항은 다음과 같다.
- 다중성
- 단방향, 양방향
- 연관 관계의 주인

## 다대일
- N쪽이 연관 관계의 주인이다.

### 단방향
![](/assets/다대일단방향.png)

- 회원이 N, 팀이1
    - N쪽이 외래키를 가진다.
- 외래키가 존재하는 곳에 참조를 걸고 연관 관계 매핑을 진행한다.

### 양방향

![](/assets/다대일양방향.png)

- 양방향 자체가 테이블에 영향을 미치지는 않는다.
- 연관관계의 주인이 외래 키를 관리하기 때문에 객체에 양방향 관계를 추가하면 된다.

## 일대다
- 1이 연관 관계의 주인이다. 실무에서 추천하지 않는 방법이다.

### 단방향

![](/assets/일대다단방향.png)
- team을 중심으로 member를 관리한다.
- db입장에서는 무조건 N쪽에 외래키가 들어간다.
    - 1에 해당하는 team에 member_id가 pk로 존재하면 team을 계속 인서트해야해서 중복이 발생한다.
- 일대다는 객체에선 team이 중심이여도 db입장에서 n쪽에 외래키가 존재하므로 member 테이블을 건드려야 한다.
    - Team.members를 수정하면 team 테이블 대신 member 테이블에 존재하는 외래키 team_id를 업데이트한다.

### 정리
- 객체와 테이블의 차이 때문에 반대편 테이블의 외래키를 관리하는 특이한 구조가 된다.
- @JoinColumn을 꼭 사용해야 한다.
    - 사용하지 않는다면 조인 테이블 방식이 적용된다.
    - 이로 인해서 추가 테이블이 생성되어 운영이 힘들어 진다.
- 실무에서는 되도록 일대다 단방향 매핑이 필요하다면 다대일 양방향 매핑을 사용하는것이 좋다.

### 양방향

- 읽기 전용 필드를 사용하여 양방향 처럼 사용이 가능하나 그냥 다대일 양방향을 사용하는 것이 좋다.

## 일대일
- 일대일 관계는 그 반대도 일대일이다.
    - 외래키를 어디든 넣을 수 있다.
    - 주 테이블이든 대상 테이블이든 둘 중 하나에만 넣으면 된다.
- 외래키에 유니크 제약 조건이 추가되어야 한다.

### 주 테이블 외래키 단방향
![](/assets/주테이블단방향.png)
- 회원은 라커 하나만 가질 수 있다.
- 멤버가 pk로 locker_id를 가지고 유니크 제약조건을 걸거나, 라커가 pk로 member_id로 유니크 제약조건을 걸어도 된다.

#### Member
```java
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    @OneToOne
    // member에 PK를 둔다.
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}
```

#### Locker
```java
@Entity
public class Locker {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
```

### 주 테이블 외래키 양방향

![](/assets/주테이블양방향.png)
- 다대일 양방향 매핑처럼 외래키가 있는 곳이 연관 관계의 주인이 된다.

#### Member
```java
@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;
}
```

#### Locker
```java
@Entity
public class Locker {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // 일대일에도 연관 관계 주인은 똑같은 원리로 적용된다.
    @OneToOne(mappedBy = "locker")
    private Member member;
}
```

- 연관 관계의 주인은 외래키가 있는 곳이 되고 반대편은 `mappedBy`를 적용한다.


### 대상 테이블 외래키 단방향

![](/assets/대상테이블단방향.png)
- 대상 테이블에 외래키가 있는 단방향
    - Member가 연관관계의 주인이 되고 싶은데 테이블에서는 Locker에 fk가 존재하는 상태이다.
- 이러한 형태는 jpa에서 지원하지 않는다.

### 대상 테이블 외래키 양방향

![](/assets/대상테이블양방향.png)
- 양방향은 jpa에서 지원한다.
- Locker.member를 연관 관계의 주인으로 정해 매핑한다.
- 일대일은 내가 내것만 관리하는 방식이다.
    - 내 엔티티에 있는 외래키는 내가 직접 관리하는 방식.

일대일 관계에서 외래키를 어디에 두는 것이 좋을지 생각해보자. 

만약 미래에 회원 한명이 여러 라커를 가질 수 있게 바뀐다면, locker에 외래키를 두는 양방향이 좋을 것이다. locker에 있는 유니크 제약 조건만 제거하면 되기 때문이다.

각각의 프로젝트마다 연관 관계의 주인을 정하는데 딜레마가 존재하겠지만 보통 비즈니스에서는 회원 조회가 많이 발생하기 때문에 member에 fk를 두는것이 더 유리하다. 라커정보를 가져올 때도 조인할 필요없이 쿼리 한방으로 라커 정보를 알아내는 등의 성능 상의 이점이 존재하기 때문이다.

### 정리

#### 주 테이블에 외래키를 두는 방법
- Member가 FK를 가진다.
- 주 객체가 대상 객체의 참조를 가지는 것처럼 주 테이블에 외래키를 두고 대상 테이블을 찾는다.
- 객체 지향 개발자가 선호.
- JPA 매핑이 편리
- 주 테이블만 조호히해도 대상 테이블 데이터를 확인 가능하다.
- 값이 없다면 외래키에 NULL값을 허용한다.

#### 대상 테이블에 외래키를 두는 방법
- Locker가 FK를 가진다.
- 대상 테이블에 외래키가 존재한다.
- 전통적인 DB 개발자가 선호한다.
- 주 테이블과 대상 테이블의 관계를 일대일에서 일대다로 변경할때에도 테이블 구조 유지가 가능하다.
- 프록시 기능의 한계로 항상 즉시 로딩된다.

## 다대다
- @ManyToMany
    - 실무에서 사용하지 않도록 하자.
    - 관계형 db는 정규화된 테이블 2개라 다대다 관계를 표현할 수 없기 때문이다.
- 연결 테이블을 추가하여 일대다, 다대일 관계로 풀어내야 한다.
- 실무에서는 연결 테이블이 단순 연결 기능만 하지 않는다.
    - 추가적인 데이터가 필요해도 테이블에 데이터 추가가 불가능하다.
    - 예상하지 못한 쿼리를 날릴 수 있다.
- 그냥 사용하지 않도록 하자.

## 실전 예제-3

### Entity
![](/assets/예제3엔티티.png)
- order과 delivery는 1:1, product와 category는 N:M이다.

### ERD

![](/assets/erd.png)
- member, order, delivery 관계에서는 fk를 주 테이블인 orders에 넣는다. category와 item은 다대다 관계로 중간 테이블을 둔다.

### 연관 관계 구현

#### Delivery
```java
@Entity
public class Delivery {
    @Id
    @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;

    private DeliveryStatus status;

    @OneToOne(mappedBy = "delivery")
    private Order order;
}
```

#### Category
```java
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    // 자기 자신을 매핑하는 것도 가능하다.
    @ManyToOne
    // 상위 카테고리가 연관 관계의 주인
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    // 각각의 상위 카테고리에 매핑 된 하위 카테고리
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    // 중간 테이블을 만들어준다.
    @JoinTable(
            // 중간 테이블 이름
            name = "CATEGORY_ITEM",
            // 한 쪽이 join 하는 것
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            // 반대쪽이 join 하는 것
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID")
    )
    private List<Item> items = new ArrayList<>();
}
```

#### Item
```java
@Entity
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // Category.items가 연관 관계의 주인이다.
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
```

#### Order
```java
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;
}
```

### @JoinColumn

![](/assets/joincol.png)
- 외래 키 매핑시 사용한다.
- 외래 키가 참조하는 대상 테이블의 칼럼명이 다를 때는 referencedColumnName을 사용해 지정해준다.

### @ManyToOne
![](/assets/manytoone.png)

### @OneToMany
![](/assets/onetomany.png)



# 고급 매핑

## 상속 관계 매핑 정의
- 상속 관계 매핑이란 객체의 상속 구조와 db의 슈퍼 타입, 서브 타입의 관계를 매핑하는 것을 의미한다.

![](/assets/상속관계매핑.png)

- db
    - 논리 모델과 물리 모델이 존재
    - 논리 모델로 음반, 영화, 책을 구상
    - 가격이나 이름 등 공통적인 속성은 물품에 두고 각각에 맞는 데이터는 아래에 둔다.
- 객체
    - 명확하게 상속 관계가 존재한다.
    - 아이템이라는 추상 타입을 만들고 그 아래 상속 관계를 둔다.

## 주요 애너테이션

### @Inheritance

- JOINED
    - 조인 전략
- SINGLE_TABLE
    - 단일 테이블 전략
    - 별도의 설정이 없다면 JPA 기본 전략으로 들어간다.
- TABLE_PER_CLASS
    - 구현 클래스마다 테이블 전략(사용하지 않는것이 좋다.)

추후 요구 사항이 바뀌어도 @Inheritance의 옵션만 바꿔주면 되기 때문에 편리하다.

### @DiscriminatorColumn
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
// DTYPE이 생기면서 album, movie 등의 테이블 이름이 들어간다.
// item 데이터가 생겼을 때 이 데이터가 어디서 온 건지 알 수 있게 된다.
@DiscriminatorColumn
public class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;
}
```
- DTYPE가 추가된다.
- 코드의 유지보수성이 높아지므로 사용하도록 하자.
- 단일 테이블 전략에서는 애노테이션을 사용하지 않더라도 자동으로 생성된다.

### @DiscriminatorValue

```java
@Entity
@DiscriminatorValue("A")
public class Album extends Item {

    private String artist;
}
```
- DTYPE에 들어갈 자식의 값을 정해줄수 있다.

## 상속 관계 매핑

### 조인 전략

![](/assets/조인.png)

- item, album, movie, book 테이블을 각자 만든 다음 조인으로 구성하는 방법
- insert가 두 번 일어난다.
    - 공통 데이터는 item에 들어간다.
    - 각각의 정보는 album, movie, book에 들어간다.
- 조회할 때는 PK로 조인한다.
- item만 보면 그게 movie인지 album인지 모르기 때문에 구분하는 DTYPE 칼럼을 둔다.

#### 장점
- 테이블이 정규화되어있다.
- 외래키 참조 무결성 제약 조건을 활용 가능하다.
    - 주문 테이블에서 외래 키 참조로 아이템을 보고싶다면 외래키인 `item_id`를 활용하면 된다.

#### 단점
- join 사용때문에 성능이 저하된다.
- 데이터 저장시 insert 쿼리를 두번씩 날린다.
- 기본적으로 join 전략이 정석이라고 생각하자.
    - 객체지향과 맞고 설계가 깔끔하기 때문이다.

### 단일 테이블 전략

![](/assets/단일테이블.png)

- 논리 테이블을 하나로 합치는 방법이다.
- 모든 칼럼을 모아서 `DTYPE`로 구분한다.
- 모든 필드가 `item`테이블에 합쳐지고 자식 테이블은 생성x.
- 해당 자식 엔티티와 관련없는 데이터는 db에 null값으로 저장된다.

#### 장점
- 조인이 없기때문에 성능이 빠르다.
- 조회 쿼리가 단순하다.

#### 단점
- 자식 엔티티가 매핑한 칼럼은 모두 null 값을 허용해야 한다.
    - 데이터 무결성 측면에서 애매하다.
- 단일 테이블에 모든것을 저장하므로 테이블이 커진다.
    - 상황에 따라 성능이 더 안좋을 수도 있다.

### 구현 클래스 마다 단일 테이블 전략

![](/assets/구현클래스마다.png)

- `item`테이블을 없 애고 각각의 테이블을 만들어 중복 속성을 허용하는 방법이다.
- 사용하지 말자.

#### 장점
- 서브타입을 명확하게 구분해서 처리할 때 효과적이다.
- not null 제약 조건 사용이 가능하다.

#### 단점
- 사용하지 않도록하자.
- 부모 클래스로 조회할 경우 union으로 모든 테이블에 데이터가 있는지 다 뒤져봐야 한다.
- 자식 테이블을 통합해서 쿼리하기 어렵다.
    - 수정 사항이 발생할때마다 각 테이블을 모두 수정해야 한다.
    - 즉, 유지보수성이 떨어진다.

### 정리
- 객체는 상속이 되기 때문에 어떠한 전략을 사용해도 결과는 똑같다.
- 기본적으로 조인 전략을 사용하고, 테이블이 정말 단순하고 확장 가능성이 없다면 단일 테이블 전략을 사용하자.
    - 그중에서도 비즈니스적으로 중요하고 복잡한 로직은 조인 전략을 사용하자.

## 매핑 정보 상속

![](/assets/매핑정보상속.png)

- 공통으로 사용되는 매핑 정보를 상속하여 사용 가능하다.

생성, 수정 정보를 모든 클래스에 추가해야한다고 가정할 때

```java
// 매핑 정보만 받는 부모 클래스를 명시할 때 사용한다.
@MappedSuperclass
public abstract class BaseEntity {
    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
```

위와 같이 공통된 속성을 클래스로 만들어 다른 클래스가 상속 받으면 된다.
그렇게 하면 테이블 생성시에도 자동으로 컬럼이 생성되는것을 알 수있다.

![](/assets/상속결과.png)

```java
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "INSERT_MEMBER")
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(name = "UPDATE_MEMBER")
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
```
칼럼 이름을 별도로 지정해주는것도 가능하다.

### 특징
- 등록일, 수정일 등 전체 엔티티에서 공통으로 사용하는 정보를 모을 때 사용한다.
- 상속관계를 매핑하는 것이 아니다.
    - 테이블과는 관련이 없다.
    - `@MappedSuperclass`가 달린 클래스는 엔티티가 아니며 테이블과 매핑되지 않는다.
    - 부모클래스는 자식 클래스에게 매핑 정보만 제공한다.
    - 따라서 `em.find()`와 같은 조회나 검색이 불가능ㅎ다ㅏ.
- 추상클래스로 생성하자.

## 실전 예제-4

![](/assets/실전예제4.png)
- 상품과 도서,음반,영화를 상속관계로 생성한다.

![](/assets/실전예제4-1.png)
- db는 단일 테이블 전략을 사용한다.

#### Item
```java
@Entity
// 단일 테이블 전략을 사용한다.
@Inheritance(strategy = SINGLE_TABLE)
// DTYPE으로 데이터를 구분한다.
@DiscriminatorColumn
// 아이템만 단독으로 테이블에 저장할 일이 없다고 가정하고 추상 클래스로 선언한다.
public abstract class Item extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();
}
```

```java
@Entity
public class Album extends Item {

    private String artist;
    private String etc;
}
```

```java
@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
```

# 프록시와 연관 관계

## 프록시

![](/assets/프록시.png)

멤버와 팀이 연관관계가 맺어져있을 때 멤버를 조회하면 팀도 매번 함께 조회해야 하는가?

```java
public class App {
    public void printUserAndTeam(String memberId) {
        ...

        Member member = em.find(Member.class, memberId);
        Team team = member.getTeam();

        System.out.println("회원 이름: " + member.getUsername());
        System.out.println("소속팀: " + team.getName());
    }
}
```

```java
public class App {
    public void printUser(String memberId) {
        ...

        Member member = em.find(Member.class, memberId);
        Team team = member.getTeam();

        System.out.println("회원 이름: " + member.getUsername());
    }
}
```
- 처음 코드처럼 소속 팀이 필요하면 한번에 가져오는 것이 좋다.
- 두번째 경우처럼 회원 정보만이 필요하다면 팀을 가져오는것은 리소스를 낭비하게 된다.
- 이를 해결하기 위해 프록시를 적용해보자.

### 프록시 기초

**em.find()**

데이터베이스를 통해서 실제 Entity 객체를 조회한다.

```java
public class App {
    public class App {
        public void printUserAndTeam(String memberId) {
            ...

            Member member = em.find(Member.class, memberId);
            // 출력 등 사용하는 로직 없음
        }
    }
}
```
- 데이터를 사용하지 않고 `find()`만 사용해도 `select` 쿼리를 실행한다.

**em.getReference()**
- 데이터베이스 조회를 미루는 가짜(프록시) 엔티티 객체를 호출한다.
- db에 쿼리는 안날리지만 객체가 조회된다.
```java
public class App {
    public void printUserAndTeam(String memberId) {
      ...

        Member member = em.getReference(Member.class, memberId);
        // 출력 등 사용하는 로직 없음
    }
}
```
- `select`쿼리를 날리지 않는다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member = em.getReference(Member.class, memberId);
        // 데이터 사용
        System.out.println("회원 이름: " + member.getUsername());
    }
}
```
- `member` 데이터를 실제 호출하는 순간에 `select`쿼리를 호출한다.

![](/assets/레퍼런스.png)
- 프록시를 사용하여 진짜 객체를 주는것이 아닌 가짜 객체를 준다.
- 껍데기만 존재하고 내부는 빈 상태.

### 프록시 특징

![](/assets/프록시특징.png)
- 실제 클래스를 상속받아 만들어진다.
    - 실제 클래스와 겉모습이 같다.
    - 하이버네이트 내부 라이브러리를 사용해 상속한다.
- 사용자입장에서는 진짜인지 프록시 객체인지 구분하지 않는다.

![](/assets/위임.png)
- 프록시 객체는 실제 객체의 참조(target)를 보관한다.
- 프록시 객체에 있는 메서드를 호출하면, 프록시 객체가 실제 객체의 메서드를 호출한다.

### 프록시 객체의 초기화

```java
public class App {
    public void getMemberName() {
        // 실제 객체가 아닌 프록시 객체를 가져온다.
        Member member = em.getReference(Member.class, "id");
        // 처음에 getName()을 호출하면 target에 값이 없는 상태다.
        // 그럼 영속성 컨텍스트에 데이터를 요청해 그 값을 반환한다.
        member.getName();
    }
}
```

![](/assets/프록시초기화.png)

1. 데이터를 요청했는데 Member의 target에 데이터가 없다.
2. JPA가 진짜 Member 객체를 가져오라고 영속성 컨텍스트에 요청한다.
3. 영속성 컨텍스트는 DB를 조회해서 실제 Entity 객체를 생성해 보내준다.
4. target과 진짜 객체인 Entity를 연결한다.
5. target의 진짜 getName()을 통해서 값을 반환한다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member = em.getReference(Member.class, memberId);
        // 실제 레퍼런스 조회
        System.out.println("회원 이름: " + member.getUsername());
    }
}
```
- `userName`을 실제 가져다 쓴느 시점에 영ㅅ혹성 컨텍스트로 `Member`를 요청하여 실제 레퍼런스를 가진다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member = em.getReference(Member.class, memberId);
        // 실제 레퍼런스 조회
        System.out.println("회원 이름: " + member.getUsername());
        // 다시 하면 프록시에서 조회
        System.out.println("회원 이름: " + member.getUsername());
    }
}
```
- 이제 target에 값이 존재하기 때문에 다음에 조회해도 db에 쿼리를 다시 날리지 않는다.

### 주의 사항

- 프록시 객체는 처음 사용할 때 한번만 초기화 된다.
  - 한 번 초기화 되면 그 내용을 그대로 사용한다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member = em.getReference(Member.class, memberId);

        System.out.println("before: " + member.getClass());
        System.out.println("회원 이름: " + member.getUsername());
        System.out.println("after: " + member.getClass());  // before와 같은 값 출력
    }
}
```
- 프록시 객체를 초기화 할 때, 그 즉시 프록시 객체가 실제 엔티티로 바뀌는 것이 아니다.
  - 초기화되면 프록시 객체를 통해 실제 엔티티에 접근이 가능해 지는 것.
  - 그래서 프록시를통해 데이터를 가져온 뒤에도 `getClass()`의 값은 동일하다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        em.persist(member2);

        em.flush();
        em.clear();

        Member m1 = em.find(Member.class, member1.getId());
        Member m2 = em.find(Member.class, member2.getId());

        // find()로 가져왔고 타입을 정확하게 비교하는 것이므로 true를 출력한다.
        System.out.println("m1 == m2: " + (m1.getClass() == m2.getClass()));

        Member m3 = em.getReference(Member.class, member1.getId());
        Member m4 = em.find(Member.class, member2.getId());

        // m3는 getReference()이므로 false를 출력한다.
        System.out.println("m1 == m2: " + (m1.getClass() == m2.getClass()));

        tx.commit();
    }
}
```

```java
public class App {
    public static void main(String[] args) {
        Member m1 = em.find(Member.class, member1.getId());
        Member m2 = em.find(Member.class, member2.getId());
        // true
        System.out.println("m1 == Member: " + (m1.getClass() == m2.getClass()));

        Member m1 = em.find(Member.class, member1.getId());
        Member m2 = em.getReference(Member.class, member2.getId());
        // false
        System.out.println("m1 == Member: " + (m1.getClass() == m2.getClass()));
    }

    public void logic(Member m1, Member m2) {
        // 실제 로직 상에서는 실제 인티티가 넘어올지 프록시가 넘어올지 모르기 때문에
        // 비교할 때는 instance of를 사용해야 한다.
        // false
        System.out.println("m1 == m2: " + (m1 == m2));
        // true
        System.out.println("m1 == m2: " + (m1 instanceof m2));
        System.out.println("m2 == m2: " + (m2 instanceof m2));

    }
}
```
- 프록시 객체는 원본 엔티티를 상속 받든다.
  - 즉, 프록시인 멤버와 아닌 멤버가 타입이 맞지 않을 수 있으므로 주의해야 한다.
  - 타입 비교시 `instance of`를 사용하도록 하자.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        Member m1 = em.find(Member.class, member1.getId());
        // find()로 진짜 객체를 가져오기 때문에 'Member'라고 출력된다.
        System.out.println("m1 = " + m1.getClass());

        // find()한 뒤에 getReference()로 가져오게 되면
        Member m2 = em.getReference(Member.class, member1.getId());
        // 'Proxy'가 아니라 'Member'로 출력된다.
        System.out.println("m2 = " + m2.getClass());

        // 프록시든 아니면 한 영속성 컨텍스트에서 가져오고 PK가 같다면 항상 true가 된다.
        System.out.println("m1 == reference = " + (m1 == reference));

        tx.commit();
    }
}
```
- `find()`후 `getReference()`를 호출하면 실제 엔티티를 반환한다.
  - 이미 멤버를 영속성 컨텍스트에 넣어놨기 때문
  - 성능 최적화의 이점이 사라진다.
- 실제 객체와 레퍼런스로 가져온 객체를 같다고 취급한다.
  - jpa는 한 트랜잭션 안에서 pk가 같다면 같은 객체임을 보장한다.
  - 원본과 레퍼런스를 `==`비교시 항상 `true`가 나온다.
  
```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        Member m1 = em.getReference(Member.class, member1.getId());
        System.out.println("m1 = " + m1.getClass());

        Member reference = em.getReference(Member.class, member1.getId());
        System.out.println("reference = " + reference.getClass());

        // true
        System.out.println("a == a: " + (m1 == reference));

        tx.commit();
    }
}
```

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        // 프록시로 불러온 다음
        Member refMember = em.getReference(Member.class, member1.getId());
        // 출력을 위해 실제 값으로 초기화 한다.
        // 클래스 값은 프록시로 출력된다.
        System.out.println("refMember = " + refMember.getClass());

        Member findMember = em.find(Member.class, member1.getId());
        // 프록시가 초기화 되었으니 당연히 Member 타입이 출력되어야 하는 것 아닌가? 할 수 있지만
        // JPA는 PK가 같으면 무조건 같음을 보장해줘야 하기 때문에 프록시로 나온다.
        System.out.println("findMember = " + findMember.getClass());

        // JPA에서는 무조건 이게 참이 되도록 맞춘다!
        System.out.println("a == a: " + (refMember == findMember));

        tx.commit();
    }
}
```
- 프록시가 초기화 된 상태에서 `find()`를 하면 어떻게 될까?
- JPA는 기본적으로 `refMember == findMember`의 값이 `true`임을 보장해야 한다.
- 따라서 `refMember`, `findMember` 모두 프록시로 출력된다.
- 프록시를 한번 조회한 뒤에는 `find()`를 한 객체에도 프록시로 반환한다.
  - jpa의 룰을 보장
  - 처음에 엔티티로 반환하면 엔티티로, 프록시로 반환하면 계속 프록시로 반환한다.
- `instance of`를 기억하도록 하자.

### 준영속 상태의 프록시
- 영속성 컨텍스트의 도움을 받을 수 없는 준영속 상태이면, 프록시를 초기화할 때 문제가 발생한다.
  - 하이버네이트는 `org.hibernate.LazyInitializationException` 예외를 터뜨린다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        // 프록시 생성
        Member refMember = em.getReference(Member.class, member1.getId());
        // 프록시로 출력한다.
        System.out.println("refMember = " + refMember.getClass());

        // 실제 DB를 조회하면서 쿼리를 날리고 프록시를 초기화 한다.
        refMember.getUsername();
        // 타입은 여전히 프록시로 출력된다.
        System.out.println("refMember = " + refMember.getClass());

        tx.commit();
    }
}
```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        // 프록시 생성
        Member refMember = em.getReference(Member.class, member1.getId());
        // 프록시로 출력한다.
        System.out.println("refMember = " + refMember.getClass());

        // 실제 DB를 조회하면서 쿼리를 날리고 프록시를 초기화 한다.
        refMember.getUsername();
        // 타입은 여전히 프록시로 출력된다.
        System.out.println("refMember = " + refMember.getClass());

        tx.commit();
    }
}
```
- 같은 영속성 컨텍스트 안에서는 같은 프록시를 출력한다.
```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        // 프록시 생성
        Member refMember = em.getReference(Member.class, member1.getId());
        // 프록시로 출력
        System.out.println("refMember = " + refMember.getClass());

        // detach(), clear(), close()로 영속성 컨텍스트를 준영속으로 만든다.
        em.close();

        // 실제 데이터로 초기화 하면서 데이터를 가져와야 하지만
        // 영속성 컨텍스트로 관리하지 않게 되면서 exception이 떨어진다.
        refMember.getUsername();
        System.out.println("refMember = " + refMember.getClass());

        tx.commit();
    }
}
```

### 프록시 유틸리티 메서드

**프록시 인스턴스의 초기화 여부 확인**
```java
public class App {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory();
        ...
        // 앞에서 초기화 했다면 true, 아니라면 false
        System.out.println("isLoaded: " + emf.getPersistenceUnitUtil().isLoaded(refMember));
    }
}
```
- `PersistenceUnitUtil.isLoaded(Object entity)`

**프록시 클래스 확인**
- `entity.getClass().getName()`

**프록시 강제 초기화**

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Member member1 = new Member();
        member1.setUsername("member1");
        em.persist(member1);

        em.flush();
        em.clear();

        Member refMember = em.getReference(Member.class, member1.getId());
        System.out.println("refMember = " + refMember.getClass());

        // 이런 방식으로 강제 호출하는 것 보다는
        refMember.getUsername();
        // 이 방식을 더 권유한다.
        Hibernate.initialize(refMember);

        tx.commit();
    }
}
```

## 즉시 로딩과 지연 로딩

### 지연 로딩
```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Team team;
}
```
- `fetch = FetchType.LAZY`
  - 프록시 객체로 조회한다.
  - 즉, 멤버 클랫흐만 db에서 조회하고 team은 조회하지 않는다.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setTeam(team);
        em.persist(member1);

        em.flush();
        em.clear();

        // 이때는 프록시로 남아있다가
        Member m = em.find(Member.class, member1.getId());
        System.out.println("m = " + m.getTeam().getClass());

        System.out.println("=============");
        // 이 시점이 되어서야 필요한 정보를 얻기 위한 쿼리가 날아간다.
        // 지연 로딩을 사용하면 이렇게 연관된 데이터를 프록시로 가져온다.
        // m.getTeam()은 프록시로 가져오기 때문에 이 시점에는 Entity로 초기화되지 않는다.
        // getTeam().getName()으로 그 안에 있는 데이터를 조회해야할 때 쿼리가 나가면서 초기화 된다.
        m.getTeam().getName();
        System.out.println("=============");

        tx.commit();
    }
}
```
![](/assets/lazy.png)

- 멤버를 조회하면 당장 조회하지 않는 team을 프록시로 박아놓는다.
- `member.getTeam().getName()`으로 실제 사용하는 시점이 되면 DB를 조회해서 초기화 된다.
  - `getTeam()`이 아니라 그 안의 `getName()`으로 데이터를 실제 조회할 때임을 주의하자.

### 즉시 로딩

만약 Member와 Team 둘 다 자주 사용한다면 매번 쿼리가 두 번씩 나가게 되면서 성능상 손해를 보게 된다. 그래서 사용하는 것이 EAGER 로딩이다.

```java
@Entity
public class Member {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Team team;
}
```

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setTeam(team);
        em.persist(member1);

        em.flush();
        em.clear();

        Member m = em.find(Member.class, member1.getId());
        System.out.println("m = " + m.getTeam().getClass());

        System.out.println("=============");
        m.getTeam().getName();
        System.out.println("=============");

        tx.commit();
    }
}
```
- member와 team에 대한 쿼리를 한번에 날린다.
- 따라서 team에 대한 정보를 조회시 `getClass()`를 사용하면 프록시가 아니라 진짜 객체가 나온다.

![](/assets/eager.png)
1. Member를 가지고 올때 EAGER로 걸린 엔티티를 조인하여 쿼리를 한번에 날린다.
   - 웬만한 JPA 쿼리는 이렇게 한방에 날리는걸 선호.
2. 일단 Member를 가지고 온 다음 EAGER로 되어있는 엔티티를 확인하여 한번 더 쿼리를 날린다.
   - Member, Team에 `em.find()`를 각각하여 두번의 쿼리를 날린다.
   - 성능이 좋지않다.

가급적이면 실무에서는 즉시로딩을 사용하지말고 지연로딩을 사용하도록 하자.
- 예상치 못한 sql이 발생 가능하다.
- N+1 문제를 일으킨다.

따라서, 즉시로딩은 사용하지 말고 지연로딩을 사용하자. 
- 기본적으로 모든 연관관계 매핑은 lazy로 설정한다.
- 특히, `@ManyToOne`, `@OneToOne`은 기본이 즉시 로딩이므로 따로 설정해주자.

### fetch join
- 런타임에 동적으로 내가 원하는 정보만 선택해서 가져오는 방법
- join을 이용하여 쿼리를 한번만 날린다.
  - 상황에 따라 이용.

```java
public class App {
    public void printUserAndTeam(String memberId) {
        Team teamA = new Team();
        teamA.setName("teamA");
        em.persist(teamA);

        Team teamB = new Team();
        teamB.setName("teamB");
        em.persist(teamB);

        Member member1 = new Member();
        member1.setUsername("member1");
        member1.setTeam(teamA);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUsername("member2");
        member2.setTeam(teamB);
        em.persist(member2);

        em.flush();
        em.clear();

        // fetch join으로 한 방 쿼리를 날린다.
        List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();

        // 이제 다 값이 채워져서 어떤 값을 조회해도 쿼리가 나가지 않는다.

        tx.commit();
    }
}
```

## 영속성 전이와 고아 객체

### 영속성 전이

- 특정 엔티티를 영속 상태로 만들 때 관련 엔티티도 영속 상태로 만드는것.
- 즉시 로딩, 지연 로딩, 연관관계 세팅과 관련이 없다.

```java
public class JpaMain {

    public static void main(String[] args) {
        Child child1 = new Child();
        Child child2 = new Child();

        Parent parent = new Parent();
        parent.addChild(child1);
        parent.addChild(child2);

        // 부모, 자식 각각 영속화 하면 총 3번 해줘야 해서 번거롭다.
        em.persist(parent);
        em.persist(child1);
        em.persist(child2);

        tx.commit();
    }
}
```
- 각각의 엔티티를 영속화해야 insert 쿼리를 날린다.
- 이러한 반복은 번거롭고 유지보수성이 떨어진다.

```java
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    // cascade 옵션을 주면 영속화가 자식에게도 적용된다.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
}
```

```java
@Entity
public class Child {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;
}
```

- Cascade를 사용하면 parent만 영속화하여도 자식까지 영속화된다.

![](/assets/cascade.png)

- 부모를 영속화할 때 자식도 적용시키는 것이 `cascade`이다.
- 영속성 전이는 연관 관계 매핑과 관련이 없다.
- 자식의 부모가 하나일 때, 단일 엔티티에 완전 종속적이고 라이프 사이클이 같을때만 적용한다.
- 다른 엔티티에서도 관리하는 자식이라면 쓰지 않도록 하자.


### 고아 객체 제거
- 부모 엔티티와 연관관계가 끊어진 자식을 자동으로 삭제한다.

#### orphanRemoval = true
```java
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    // 고아 객체 옵션을 주면 부모가 삭제됐을 때 자식 엔티티를 컬렉션에서 삭제한다.
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
}
```

```java
public class JpaMain {

    public static void main(String[] args) {
        Parent parent = em.find(Parent.class, id);
        // 자식 Entity를 컬렉션에서 제거한다. 즉, 연관 관계를 끊는다.
        // 그럼 그 Entity를 삭제한다.
        parent.getChildren().remove(0);

        tx.commit();
    }
}
```
- 부모 Entity와 연관 관계가 끊어진 자식 Entity를 자동으로 삭제한다.
  - Entity의 참조가 제거되면 그 객체를 다른 곳에서 참조하지 않는 고아 객체로 보고 삭제한다.
  - 따라서 참조하는 곳이 하나일 때 사용해야 한다.
- 연관 관계를 끊었을 때 `delete from child where id = ?` 쿼리가 자동으로 나간다.
- `@OneToOne`, `@OneToMany`만 가능하다.

#### CascadeType.REMOVE
```java
@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    // cascade에 remove 옵션을 준다.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<Child> childList = new ArrayList<>();

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }
}
```

```java
public class JpaMain {

    public static void main(String[] args) {
        Parent parent = em.find(Parent.class, id);
        // 부모를 삭제하면 자식도 같이 삭제한다.
        em.remove(parent);

        tx.commit();
    }
}
```
- 부모를 제거할 때 자식은 고아가 되므로 orphanRemoval = true라면 자식도 제거된다.

#### `CascadeType.ALL`과 `orphanRemoval = true` 동시 사용

- Entity는 스스로 생명 주기를 관리한다.
  - JPA 영속성 컨텍스트(EntityManager)를 통해 라이프 사이클을 관리한다.
    - em.persist()로 영속화하고 em.remove()로 제거한다.
- 두 옵션을 모두 활성화 하면, 부모 Entity를 통해서 자식의 생명 주기를 관리할 수 있다.
  - 부모만 JPA로 영속화하거나 제거하고 자식은 부모가 관리하게 된다.
  - DB로 따지면 자식의 DAO나 repository가 없어도 된다.
- 도메인 주도 설계의 Aggregate Root 개념을 구현할 때 유용하다.
  - repository는 Aggregate Root만 컨택하고 나머지는 repository를 만들지 않는 방법
  - Aggregate Root를 통해서 생명주기를 관리한다.
    - Aggregate Root가 parent이고, child는 Aggreate Root가 관리한다.

## 실전 예제-5

### 글로벌 fetch 전략 설정
- 모든 연관관계를 지연 로딩으로 설정.

```java
@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 지연 로딩으로 수정한다.
    // CascadeType.ALL: 주문을 할 때 배송도 생성하겠다는 의미. 둘의 라이프 사이클을 맞추게 된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    // 주문을 생성할 때 주문 아이템도 같이 생성한다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
```
