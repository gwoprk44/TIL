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