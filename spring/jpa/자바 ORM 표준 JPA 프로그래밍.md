# JPA 자바 ORM 표준 프로그래밍

## 목차
- [JPA 시작하기](#jpa-시작하기)
    - [Dialect](#dialect-방언)
    - [애플리케이션 개발](#애플리케이션-개발)
    - [주의할 점](#주의-할-점)
    - [JPQL](#jpql)
## JPA 시작하기

### Dialect; 방언

![alt text](/assets/방언.png)

- JPA는 특정 DB에 종속되지 않는다.
- 각각의 DB는 제공하는 SQL 문법이 조금씩 다른데 이를 방언이라 한다.

### 애플리케이션 개발

#### JPA 구동방식
![alt text](/assets/구동방식.png)

JPA의 `Persistence` 클래스가 `META-INF/persistence.xml` 설정 파일을 읽어서 `EntityManagerFactory`라는 클래스를 생성한다. 여기서 필요할 때마다 `EntityManager`를 만든다.

#### 회원 생성

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

#### 회원 수정
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

### 주의 할 점

- EntityManagerFactory
    - 애플리케이션 실행 시점에 하나만 생성해서 애플리케이션 전체에 걸쳐 공유한다.
- EntityManager
    - 요청이 왔을 때 썼다가 끝나면 버리는 사이클을 반복한다.
    - 그래서 절대 스레드 간에 공유해서는 안된다. 사용하고 바로 버려야 한다.
- JPA의 모든 데이터 변경은 트랜잭션 안에서 실행해야 한다.
    - 트랜잭션을 실제 지정해본 경험이 없어도 DB 자체적으로 트랜잭션을 사용하기 때문에 적용해왔을 것이다.

### JPQL
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