# QueryDsl

# 목차

# 기본 문법

## JPQL vs QueryDsl
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
        String qlString = "select m from Member m " +
                "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // 엔티티 매니터를 파라미터로 넘겨줘야 이걸 통해 데이터를 찾는다.
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // complieQuerydsl로 Q 클래스 생성이 확인됐다면 바로 사용할 수 있다.
        QMember m = new QMember("m");   // 구분하는 이름을 지정한다.

        Member findMember = queryFactory.select(m)
                .from(m)
                // JPQL과 달리 파라미터 바인딩을 지정해주지 않아도 알아서 바인딩한다.
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
```
- EntityManager를 넘겨 JPAQueryFactory를 생성한다.
- JPQL
  - 문자 기반으로 작성
  - 실행 시점에 오류를 발견할 수 있다.
  - 파라미터 바인딩을 직접 한다.
- QueryDsl
  - 코드 기반으로 작성
  - 컴파일 시점에 오류를 발견할 수 있다.
  - 파라미터 바인딩을 자동으로 처리한다.

### JPAQueryFactory를 필드로 전환
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        // 다수의 스레드가 접근해도 문제 없게 설계되어 있다.
        queryFactory = new JPAQueryFactory(em);

        ...
    }

    @Test
    public void startQuerydsl2() {
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
```
- JPAQueryFactory를 공통으로 사용하도록 빼낼 수 있다.

### 동시성 문제
- JPAQueryFactory를 생성할 때 넘기는 엔티티매니저에 달려있다.
- 스프링은 여러 스레드에서 동시에 같은 엔티티매니저에 접근해도 트랜잭션마다 별도의 영속성 컨텍스트를 제공하기 때문에 동시성 문제는 고려할 필요가 없다.

## Q-Type 활용

### 별칭 직접 지정

```java
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    public static final QMember member = new QMember("member1");

    ...

}
```
```java
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    
    ...

    @Test
    public void startQuerydsl3() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

}
```
- Q클래스에서 만들어놓은 QMember를 static import해서 사용할 수 있다.

### JPQL 로그 확인
`spring.jpa.properties.hibernate.use_sql_comments: true`
- QueryDsl은 결국 JPQL을 만드는 빌더 역할을 한다.
  - 따라서 JPQL 로그를 보려면 위와 같이 추가해야한다.
```sql
select member1
from Member member1
where member1.username = ?1 
```
- QMember에서 variable 값을 member1으로 자동 생성했기 때문에 쿼리에도 member1으로 나간다.
```java
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    
    ...

    @Test
    public void startQuerydsl3() {
        QMember m1 = new QMember("m1");

        Member findMember = queryFactory
                .select(m1)
                .from(m1)
                .where(m1.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

}
```
- 직접 지정하면 m1이란 이름으로 JPQL을 날린다.
- 같은 테이블을 조인해야 하는 경우 이름이 같으면 안되니까 이렇게 따로 선언하여 사용한다.
  - 이러한 특수한 상황이 아니면 기본 인스턴스를 사용하도록 하자.

## 검색 조건
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void search() {
        Member findMember = queryFactory
                // select와 from이 같으면 합칠 수 있다.
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10))
                ).fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
```
```sql
select member1
from Member member1
where member1.username = ?1
  and member1.age = ?2
```
- and, or를 메서드 체인으로 연결 가능하다.
- select, from을 selectfrom으로 합칠 수 있다.

### 제공하는 검색 조건
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    void example() {
        member.username.eq("member1")

        // username != 'member1'
        member.username.ne("member1")

        // username != 'member1'
        member.username.eq("member1").not()

        // 이름이 is not null
        member.username.isNotNull()

        // age in (10,20)
        member.age.in(10, 20)

        // age not in (10, 20)
        member.age.notIn(10, 20)

        // between 10, 30
        member.age.between(10, 30)

        // age >= 30
        member.age.goe(30)

        // age > 30        
        member.age.gt(30)

        // age <= 30
        member.age.loe(30)

        // age < 30        
        member.age.lt(30)

        // like 검색         
        member.username.like("member%")

        // like ‘%member%’ 검색
        member.username.contains("member")

        // like ‘member%’ 검색
        member.username.startsWith("member")
    }
}
```
- JQPL이 제공하는 몯느 검색 조건을 제공한다.

### AND 조건을 파라미터로 처리
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void searchAndParam() {
        List<Member> result1 = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),
                        member.age.eq(10))
                .fetch();

        assertThat(result1.size()).isEqualTo(1);
    }
}
```
- where()의 파라미터로 검색 조건을 추가하면 and가 적용된다.
- null값을 무시하기 때문에 메서드 추출을 활용하여 동적 쿼리를 깔끔하게 짤 수있다.

## 결과 조회
- fetch()
  - 리스트 조회
  - 데이터가 없으면 빈 리스트 반환
- fetchOne()
  - 단건 조회
  - 결과가 없으면 null
  - 결과가 둘 이상이면 com.querydsl.core.NonUniqueResultException
- fetchFirst()
  - limit(1).fetchOne()
- fetchResults()
  - 페이징 정보 포함
  - total count 쿼리 추가 실행
- fetchCount()
  - count 쿼리로 변경하여 count 수 조회
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void result() {
        // List
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        // 단건
        Member findMember1 = queryFactory
                .selectFrom(member)
                .fetchOne();

        // 맨 처음 한 건만 조회
        Member findMember2 = queryFactory
                .selectFrom(member)
                .fetchFirst();

        // 페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        // count 쿼리로 변경
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
    }
}
```
- 스프링부트 2.6 이상에서는 fetchResults(), fetchCount()가 deprecated 되었다.

## 정렬
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    /**
     *회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last) 
     * */
    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                // 정렬
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }
}
```
- desc(), asc()
  - 일반 정렬
- nullLast(), nullsFirst()
  - null에 순서 부여

## 페이징

### 조회 건수 제한
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                // 0부터 시작(zero index)
                .offset(1)
                // 최대 2건 조회
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }
}
```

### 전체 조회 count가 필요하다면
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void paging2() {
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetchResults();

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }
}
```
- count 쿼리가 실행되므로 성능에 주의

### 실무에서 페이징 쿼리를 작성할 때 주의점
- 데이터 조회 쿼리는 조인이 필요하지만 count 쿼리는 조인이 필요없는 경우가 있다.
- 자동화된 count 쿼리는 원본 쿼리처럼 모두 조인을 해버리기때문에 성능 문제가 존재한다.
- count 쿼리에 조인이 없도록 성능 최저고하가 필요하다면 count 전용 쿼리를 별도로 작성해야 한다.

## 집합 함수
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }
}
```
- JPQL이 제공하는 모든 집함 함수를 제공한다.

### GroupBy
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }
}
```
```sql
select team.name, avg(member1.age)
from Member member1
         inner join member1.team as team
group by team.name
```
- 각 팀의 이름으로 묶는다.

### Having
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                ...
                .groupBy(item.price)
                .having(item.price.gt(1000));
    }
}
```
- groupBy한 결과 중에서 having 조건에 해당하는 것만 뽑는다.

## 조인

### 기본 조인
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    /**
     * 팀A에 소속된 모든 회원
     */
    @Test
    public void join() throws Exception {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(member)
                // 멤버와 팀을 조인한다.
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result).extracting("username")
    }
}
```
`join(조인 대상, 별칭으로 사용할 Q타입)`
```sql
select member1
from Member member1
         inner join
     member1.team as team
where team.name = ?1
```
- join(), innerJoin()
  - inner join
- leftJoin()
  - left outer join
- rightJoin()
  - right outer join
- JPQL의 on과 성능 최적화를 위한 fetch join도 제공한다.

### 세타 조인
- 연관 관계가 없는 필드로 조인한다.
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    /**
     * 세타 조인(연관 관계가 없는 필드로 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                // 일반 조인은 멤버와 연관이 있는 팀을 지정했지만
                // 세타 조인은 그냥 필요한 엔티티를 나열한다.
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }
}
```
- from에 여러 엔티티를 선택하여 세타 조인한다.
- outer join은 불가능하다.
  - on을 사용하면 outer join이 가능해진다.

## on절

### 조인 대상 필터링
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }
}
```
```sql
SELECT m.*, t.*
FROM Member m
         LEFT JOIN Team t ON m.TEAM_ID = t.id and
                             t.name = 'teamA'
```
- 회원과 팀을 조인하면서 팀 이름이 teamA인 것만 조인한다.
- 회원은 모두 조회한다.
- inner join을 사용하면 where에서 필터링하는 것과 동일하다.
  - inner join이라면 익숙한 where절로 해결한다.
  - outer join이 필요한 경우에만 on 절 사용한다.
```
tuple = [Member(id=3, username=member1, age=10), Team(id=1, name=teamA)]
tuple = [Member(id=4, username=member2, age=20), Team(id=1, name=teamA)]
tuple = [Member(id=5, username=member3, age=30), null]
tuple = [Member(id=6, username=member4, age=40), null]
```
- left Join이므로 teamB는 null인 상태로 조회한다.

### 연관 관계 없는 엔티티 외부 조인
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                // 일반 조인과 다르게 엔티티 하나만 들어간다.
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("t=" + tuple);
        }
    }
}
```
```sql
SELECT m.*, t.*
FROM Member m
         LEFT JOIN Team t ON m.username = t.name
```
- 회원의 이름과 팀의 이름이 같은 데이터를 outer join 한다.
- 하이버네이트 5.1부터 관계없는 필드로도 on으로 외부 조인이 가능하다.
- 일반 조인과 다르게 엔티티 하나만 넘긴다.
  - 일반 조인
    - leftJoin(member.team, team)
    - id가 매칭되는것을 가져온다.
  - on 조인
    - from(member).leftJoin(team).on()
    - on 조건으로만 필터링한다.
      - SQL을 보면 id없이 이름으로만 매칭한다.
```sql
t=[Member(id=3, username=member1, age=10), null]
t=[Member(id=4, username=member2, age=20), null]
t=[Member(id=5, username=member3, age=30), null]
t=[Member(id=6, username=member4, age=40), null]
t=[Member(id=7, username=teamA, age=0), Team(id=1, name=teamA)]
t=[Member(id=8, username=teamB, age=0), Team(id=2, name=teamB)]
```
- 이름이 매칭되는 것만 team 데이터를 조인하여 가져온다.

## fetch join
- fetch join은 SQL이 제공하는건 아니고 SQL 조인을 활용해 한 방에 조회하는 기능이다.
- 성능 최적화에서 주로 사용한다.

### before
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        // team이 지연 로딩이기 때문에 데이터가 비어있다.
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // 해당 데이터가 이미 로딩된 엔티티인지 확인한다.
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }
}
```

### after
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                // fetch join 적용
                .fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        // 연관 관계가 같이 로딩된다.
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("페치 조인 적용").isTrue();
    }
}
```
```sql
select member0_.member_id as member_i1_1_0_,
       team1_.team_id     as team_id1_2_1_,
       member0_.age       as age2_1_0_,
       member0_.team_id   as team_id4_1_0_,
       member0_.username  as username3_1_0_,
       team1_.name        as name2_2_1_
from member member0_
         inner join
     team team1_ on member0_.team_id = team1_.team_id
where member0_.username = ?
```
- join(), leftJoin() 등 조인 뒤에 fetchJoin()을 추가한다.

## 서브 쿼리
- com.querydsl.jpa.JPAExpressions를 사용한다.

### eq
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void subQuery() throws Exception {
        // 밖에 있는 member와 서브 쿼리의 member의 alias가 겹치면 안되므로 직접 만든다.
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)))
                .fetch();

        assertThat(result).extracting("age").containsExactly(40);
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.age = (
    select max(member1_.age)
    from member member1_
)
```
- 나이가 가장 많은 회원 조회

### goe
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.age >= (
    select avg(cast(member1_.age as double))
    from member member1_
)
```
- 나이가 평균 이상인 회원 조회

### in
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.age in (
    select member1_.age
    from member member1_
    where member1_.age > ?
)
```

### select절에 subquery
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void selectSubQuery() {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> fetch = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ).from(member)
                .fetch();

        for (Tuple tuple : fetch) {
            System.out.println("username = " + tuple.get(member.username));
            System.out.println("age = " +
                    tuple.get(JPAExpressions.select(memberSub.age.avg())
                            .from(memberSub)));
        }
    }
}
```
```sql
select member0_.username      as col_0_0_,
       (select avg(cast(member1_.age as double))
        from member member1_) as col_1_0_
from member member0_
```

### static import 활용
```java
import static com.querydsl.jpa.JPAExpressions.select;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void selectSubQuery() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                )).fetch();

        ...
    }
}
```

### from절의 서브쿼리 한계
- JPA 서브쿼리는 from절에 적용할 수 없다.
- QueryDsl도 지원하지 않는다.
- 원래 select는 사용 불가능하지만 하이버네이트 구현체를 이용해 가능하다.

### 해결 방안
- 서브 쿼리는 join으로 변경한다.
  - 불가능할때도 존재
- 애플리케이션에서 쿼리를 2번 분리해 실행한다.
- nativeSql을 사용한다.

## Case 문
- select, where, order by에서 사용 가능하다.

### 단순한 조건
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void caseStatement() {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
    }
}
```
```sql
select case
           when member0_.age = ? then ?
           when member0_.age = ? then ?
           else '기타'
           end as col_0_0_
from member member0_
```

### 복잡한 조건
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void caseStatement() {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
    }
}
```
```sql
select case
           when member0_.age between ? and ? then ?
           when member0_.age between ? and ? then ?
           else '기타'
           end as col_0_0_
from member member0_
```
- 복잡한 조건에는 CaseBuilder를 사용한다.

### orderBy + case
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void orderby_case() {
        // 복잡한 조건은 rankPath처럼 변수로 선언해서 활용한다.
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(rankPath);

            System.out.println("username = " + username + " age = " + age + " rank = " + rank);
        }
    }
}
```
```sql
select member0_.username as col_0_0_,
       member0_.age      as col_1_0_,
       case
           when member0_.age between ? and ? then ?
           when member0_.age between ? and ? then ?
           else 3
           end           as col_2_0_
from member member0_
order by case
             when member0_.age between ? and ? then ?
             when member0_.age between ? and ? then ?
             else 3
             end desc
```
```
username = member4 age = 40 rank = 3
username = member1 age = 10 rank = 2
username = member2 age = 20 rank = 2
username = member3 age = 30 rank = 1
```
- 복잡한 조건은 변수로 선언하여 select, orderBy에서 활용한다.
- 웬만해서는 DB에서 최소한의 조건만 걸고 case로 들어갈 수 있는 조건들은 애플리케이션에서 하도록 한다.

## 상수, 문자 더하기

### 상수
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    
    @Test
    public void constant() {
        Tuple result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetchFirst();
    }
}
```
`result = [member1, A]`
- Expressions.constant()를 사용한다.

### 문자 더하기
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    
    @Test
    public void concat() {
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();
    }
}
```
`member1_10`
- ENUM과 문자가 아닌 타입은 stringValue()로 변환한다.

# 중급 문법

## 프로젝션과 결과 반환
- 프로젝션
  - select할 대상을 지정하는 것

### 프로젝션 대상이 하나일 때
```java
class Projection {
    void example() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();
    }
}
```

### 프로젝션 대상이 둘일 때

#### Tuple
```java
class Projection {
    void example() {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            // 프로젝션 한 데이터를 각각 꺼내서 사용하면 된다.
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username=" + username);
            System.out.println("age=" + age);
        }
    }
}
```
- 튜플
  - 결과가 여러 개일 때 담을 수 있도록 만든 QueryDsl 객체
  - 리포지토리 계층에서만 사용하고 다른 레이어가 종속되지 않도록 한다.
- 대상이 둘 이상이면 튜플이나 DTO로 조회해야 한다.

#### 순수 JPA에서 DTO 조회
```java
@Data
public class MemberDto {
    private String username;
    private int age;

    public MemberDto() {
    }

    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
```
- 엔티티로 가져오면 필요하지 않은 데이터도 다 가져와야하므로 필요한 데이터만 가져올수 있도록 DTO를 만든다.
- 기본 생성자가 필수적이다.

```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoByJPQL() {
        List<MemberDto> result = em.createQuery(
                        "select new study.querydsl.dto.MemberDto(m.username, m.age) " +
                                "from Member m", MemberDto.class)
                .getResultList();
    }
}
```
- select에 DTO 타입을 new 명령어로 명시해준다.
  - `select m from Member m` 이라고 하면 Member 엔티티를 조회하기 때문에 타입이 맞지 않는다.
  - DTO의 패키지 이름을 다 적어야해서 지저분하다.
  - 생성자 방식만 지원한다.

#### QueryDsl 빈 생성
1. 프로퍼티 접근
```java
   @SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                // bean이 setter로 주입해준다.
                .select(Projections.bean(
                        MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
}
```
2. 필드 직접 접근
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoByField() {
        List<MemberDto> result = queryFactory
                // 필드에 바로 넣는다.
                .select(Projections.fields(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
}
```
3. 생성자 사용
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                // 생성자를 사용한다.
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
}
```

#### 별칭이 다를 때
```java
@Data
public class UserDto {
    private String name;
    private int age;
}
```
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoByConstructor() {
        QMember memberSub = new QMember("memberSub");

        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
    }
}
```
- username인데 DTO에는 name으로 되어있어 일치하지않을 겨우 사용한다.
`ExpressionUtils.as(source, alias)`
- 필드나 서브 쿼리에 별칭을 적용한다.
`username.as("memberName")`
- 필드에 별칭을 적용한다.

### @QueryProjection
```java
@Data
public class MemberDto {
    private String username;
    private int age;

    public MemberDto() {
    }

    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
```
```java
/**
 * study.querydsl.dto.QMemberDto is a Querydsl Projection type for MemberDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberDto extends ConstructorExpression<MemberDto> {

    private static final long serialVersionUID = 1356709634L;

    public QMemberDto(com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<Integer> age) {
        super(MemberDto.class, new Class<?>[]{String.class, int.class}, username, age);
    }

}
```
- 생성자에 @QueryProjection을 붙인 뒤 compileQuerydsl 한다.
  - QMemberDto 클래스가 생성된다.

```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                // DTO 클래스를 new로 바로 넣으면 된다.
                // 생성자로 만들기 때문에 타입과 파라미터를 다 맞춰줘서 안정적으로 만들 수 있다.
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
    }
}
```
- 컴파일러로 타입을 체크할 수 있어 안전하다.
  - 컴파일 시점에 오류체크가 가능하다.
- DTO에 QueryDsl 애너테이션을 유지하고 Q파일까지 만들어야 하는 단점이 존재한다.
  - queryDsl에 의존적

## 동적 쿼리

### BooleanBuilder
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCondition, Integer ageCondition) {
        BooleanBuilder builder = new BooleanBuilder();

        // username에 값이 있으면 그 값으로 and 조건을 넣는다.
        if (usernameCondition != null) {
            builder.and(member.username.eq(usernameCondition));
        }

        // age에 값이 있으면 그 값으로 and 조건을 넣는다.
        if (ageCondition != null) {
            builder.and(member.age.eq(ageCondition));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.username = ?
  and member0_.age = ?
```
- username과 age를 조건으로 쿼리.
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = null;

        List<Member> result = searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    ...
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.username = ?
```
- null일 경우 조건으로 넣지 않는다.
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    
    ...

    private List<Member> searchMember1(String usernameCondition, Integer ageCondition) {
        // 필수 값을 미리 넣어둘 수도 있다.
        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCondition));

        ...
    }
}
```
- 빌더에 필수 값을 넣어 초기화도 가능하다.

### where 다중 파라미터
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCond), ageEq(ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.username = ?
  and member0_.age = ?
```
- where 조건에서 null 값은 무시된다.
- 메서드를 다른 쿼리에서도 재사용 가능하다.
- 쿼리 자체의 가독성이 높아진다.

#### 조합
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    ...

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }
}
```
```sql
select member0_.member_id as member_i1_1_,
       member0_.age       as age2_1_,
       member0_.team_id   as team_id4_1_,
       member0_.username  as username3_1_
from member member0_
where member0_.username = ?
  and member0_.age = ?
```
- username과 age에 대한 두 조건을 하나로 합쳐서 사용 가능하다.
- null 처리는 따로 해주어야 한다.

## 수정, 삭제 벌크 연산

### 대량 데이터 수정
- 개별로 쿼리를 날리는것보다 한버에 쿼리를 날리는게 성능상 유리하다.
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
    }
}
```
```sql
update member
set username=?
where age < ?

update member
set username=NULL
where age < ?
```
```
m = Member(id=3, username=비회원, age=10)
m = Member(id=4, username=비회원, age=20)
m = Member(id=5, username=member3, age=30)
m = Member(id=6, username=member4, age=40)
```
- JPA는 영속성 컨텍스트에 엔티티를 올려놓는다.
- 벌크 연산을 하게 되면 모든 데이터가 영속성 컨텍스트에 올라간다.
- 벌크 연산은 영속성 컨텍스트를 무시하고 바로 쿼리를 날린다.
- 따라서 DB 상태와 영속성 컨텍스트의 상태가 달라진다.

```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member m : result) {
            System.out.println("m = " + m);
        }
    }
}
```
```sql
m = Member(id=3, username=member1, age=10)
m = Member(id=4, username=member2, age=20)
m = Member(id=5, username=member3, age=30)
m = Member(id=6, username=member4, age=40)
```
- 만약 벌크연산 후 조회 로직을 넣는다면?
- 이미 영속성 컨텍스트에 해당 id로 값이 존재하기 때문에 영속성 컨텍스트의 데이터가 우선적으로 보여진다.
- 즉, DB에 비회원이라고 업데이트된 것과는 다른 값이 출력된다.
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        for (Member m : result) {
            System.out.println("m = " + member1);
        }
    }
}
```
```sql
m = Member(id=3, username=비회원, age=10)
m = Member(id=4, username=비회원, age=20)
m = Member(id=5, username=member3, age=30)
m = Member(id=6, username=member4, age=40)
```
- flush로 영속성 컨텍스트와 DB를 맞춘 뒤, clear로 초기화 한다.

## 대량 데이터 1씩 더하기
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }
}
```
```sql
update member set age=age+?
update member set age=age+NULL
```

## 대량 데이터 삭제
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Test
    void bulkUpdate() {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }
}
```
```sql
delete from member where age>?
delete from member where age>NULL
```

## SQL Function 호출
- JPA등 Dialect에 등록된 내용만 호출 가능하다.

### 컬럼명 변경
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Test
    void sqlFunction() {
        String result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"
                )).from(member)
                .fetchFirst();
    }
}
```
```sql
select replace(member0_.username, ?, ?) as col_0_0_
from member member0_ limit ?
select replace(member0_.username, 1, NULL) as col_0_0_
from member member0_ limit ?;
```
- member라는 단어를 모두 m으로 변경하여 조회한다.

### 소문자 변경
```java
@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @Test
    void sqlFunction() {
        String result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(
                        Expressions.stringTemplate("function('lower', {0})",
                                member.username)))
                .from(member)
                .fetchFirst();
    }
}
```
```sql
select member0_.username as col_0_0_ from member member0_ where member0_.username=lower(member0_.username) limit ?
select member0_.username as col_0_0_ from member member0_ where member0_.username=lower(member0_.username) limit 1;
```
- 소문자 변환 등 자주 사용하는 일반적인 기능은 ANSI 표준이라서 기본적으로 내장되어 있다.
`.where(member.username.eq(member.username.lower()))`
- 위의 방식처럼 `lower()`로 대체 가능하다.