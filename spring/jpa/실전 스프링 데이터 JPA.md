# 실전 스프링 데이터 JPA

## 목차

## 예제 도메인 모델

![](/assets/예제도메인모델.png)

```java
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 대상을 적는다. team을 적으면 무한 루프가 되므로 주의한다.
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")  // FK 이름
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    // 연관 관계 메서드로 반대쪽 멤버도 바꿔줘야 한다.
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
```

```java
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
```
- @NoArgsConstructor(access = AccessLevel.PROTECTED)
  - JPA는 파라미터가 없는 기본 생성자가 필요하다.
  - protected까지만 허용된다.
  - 롬복 사용.
- ToString
  - 순환 참조를 조심하자.
- 연관 관계 편의 메서드로 양쪽 값을 세팅한다.
```
member = Member(id=5, username=member3, age=30)

select team0_.team_id as team_id1_1_0_, team0_.name as name2_1_0_ from team team0_ where team0_team_id=?
select team0_.team_id as team_id1_1_0_, team0_.name as name2_1_0_ from team team0_ where team0_.team_id=2;

member.team = Team(id=2, name=teamB)
```
- @ManyToOne
  - 기본이 EAGER이기 때문에 LAZY로 교체한다. 성능 튜닝의 기본.
- member.team을 가져올 때 team에 대한 쿼리를 다시 날린다.

## 공통 인터페이스 기능

### 순수 JPA 기반 리포지토리

```java
@Repository
public class MemberJpaRepository {

    // 스프링 컨테이너가 JPA 영속성 컨텍스트인 엔티티 매니저를 가져온다.
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
```

```java
@Repository
public class TeamJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }
}
```

### 공통 인터페이스 설정
![](/assets/공통인터페이스.png)
`public interface MemberRepository extends JpaRepository<Member, Long> {
}`
`memberRepository.getClass(): class com.sun.proxy.$ProxyXXX`

- MemberRepository
  - `org.springframework.data.repository.Repository`를 구현한 클래를 자동으로 스캔한다.
  - 스프링 데이터 JPA 관련 인터페이스를 발견하면 프록시에 구현체를 넣는다.
- @Repository의 역할
  - 컴포넌트 스캔
  - JPA 예외를 스프링 예외로 자동 변환한다.

### 공통 인터페이스 분석

![](/assets/공통인터페이스분석.png)
```java 
public interface JpaRepository<T, ID>
        extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {
  ...
}
```
- JpaRepository
  - 공통 CRUD 제공
- 제네릭
  - T
    - 엔티티 타입
  - ID
    - 식별자 타입
  - S
    - 엔티티와 그 자식 타입

### 주요 메서드
- save(S)
  - 새로운 엔티티는 저장하고 이미 존재하는 엔티티는 병합한다.
- delete(T)
  - 엔티티 하나를 삭제한다.
  - 내부에서 EntityManager.remove()를 호출한다.
- findById(ID)
  - 엔티티하나를 조회한다.
  - 내부에서 EntityManager.find()를 호출한다.
- getOne(ID)
  - 엔티티를 프록시로 조회한다.
    - 실제 값을 꺼낼때 DB에 쿼리를 날려 가져온다.
  - 내부에서 EntityManager.getReference()를 호출한다.
- findAll()
  - 모든 엔티티를 조회한다.
  - 정렬이나 페이징 조건을 파라미터로 제공 가능하다.