# 실전 스프링 데이터 JPA

# 목차

# 예제 도메인 모델

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

# 공통 인터페이스 기능

## 순수 JPA 기반 리포지토리

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

## 공통 인터페이스 설정
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

## 공통 인터페이스 분석

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

## 주요 메서드
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

# 쿼리 메서드 기능

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
```

- 메서드 이름을 분석하여 JPQL을 생성하고 실행.
- 엔티티 필드명이 변경되면 메서드 이름도 함께 변경되어야 한다.
  - 애플리케이션 로딩시점에 오류를 발생시킬 수 있음.
- 조회
  - find...By
  - read...By
  - query...By
  - get...By
  - By를 넣지 않으면 조건 없이 전체 조회 실행.
- count
- exists
- 삭제
- distinct
- limit
  - findFirst
  - findFirst3
  - findTop
  - findTop3

## JPA Named Query

**스프링 데이터 JPA**
```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    //    @Query(name = "Member.findByUsername") 생략 가능
    List<Member> findByUsername(@Param("username") String username);
}
```

**순수 JPA**
```java
public interface MemberRepository extends JpaRepository<Member, Long> {
    //    @Query(name = "Member.findByUsername") 생략 가능
    List<Member> findByUsername(@Param("username") String username);
}
```

**NamedQuery 정의**
```java
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username")
public class Member {
    ...
}
```

- 쿼리의 이름을 정해놓고 불러오는 기능
- `@Query`를 생략하고 메서드만으로 호출이 가능하다.
  - `엔티티 클래스.메서드 이름`으로 NamedQuery가 선언된 게 있는지 찾아준다.
  - ex. Member.findByUsername
  - 없다면 메서드 이름으로 쿼리를 자동 생성한다.
- 애플리케이션 로딩 시점에 미리 sql을 만들어 보고 문법 오류를 체크 가능.
- NamedQuery는 실무에서 잘 사용하지 않는다. -> `@Query`를 이용해 직접 정의.

## @Query

### 엔티티 조회
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
```
- 복잡한 쿼리나 JPQL을 `@Query`에 넣어 해결 가능하다.
  - 메서드 이름에 따라 쿼리를 자동 생성하면 이름이 너무 길어진다.
- 실행할 메서드의 정적 쿼리를 직접 작성하므로 이름 없는 Named Query라고 할 수 있다.
- 실행 시점에 문법 오류 발견이 가능하다.
  
### 값 조회
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m.username from Member m")
    List<String> findUsernameList();
}
```

### DTO 조회
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    // new 명령어 필요
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();
}

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }
}
```
- JPA의 new 명령어를 사용한다.
- 생성자가 일치하는 DTO가 필요하다.

## 파라미터 바인딩
- 위치 기반
- 이름 기반
```java
select m
from Member m
where m.username = :name
```
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.username = :name")
    Member findMembers(@Param("name") String username);

    // 컬렉션 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
```
- in절을 사용한 Collection 타입을 지원한다.

## 반환 타입
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 컬렉션
    List<Member> findByUsername(String name);

    // 단건
    Member findByUsername(String name);

    // 단건 Optional
    Optional<Member> findByUsername(String name);
}
```

### 단건
**결과가 없을 때**

null 반환
- 단건을 조회하면 내부에서 JPQL의 `Query.getSingleResult()`를 호출한다.
  - 결과가 없으면 NoResultException를 던진다.
  - 스프링 데이터 JPA가 예외를 무시하고 null을 반환한다.
- 없을 수 있으면 Optional을 쓴다.

** 결과가 두 건 이상 일 때**
- jakrta.persistence.NonUniqueResultException

### 컬렉션
**결과가 없을 때**
- 빈 컬렉션 반환

## 페이징과 정렬
- 검색 조건
  - age = 10
- 정렬 조건
  - 이름 순 내림차순
- 페이징 조건
  - 첫 번째 페이지
  - 페이지 당 보여줄 데이터는 3건

### 순수 JPA
```java
@Repository
public class MemberJpaRepository {

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age", age)
                // 어디서 부터 가져올 것인지
                .setFirstResult(offset)
                // 몇 개를 가져올 것인지
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        // 단순 count니까 sort 조건은 빠진다.
        return em.createQuery("select count(m) from Member  m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void paging() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }
}
```
- DB가 달라져도 JPA가 DB에 맞는 쿼리를 날린다.

### 스프링 데이터 JPA

#### 파라미터
- org.springframework.data.domain.Sort
  - 정렬
- org.springframework.data.domain.Pageable
  - 페이징
  - 내부에 sort가 포함되어 있다.

#### 반환 타입
```JAVA
interface MemberRepository extends JpaRepository<Member, Long> {
    // count 쿼리 사용
    Page<Member> findByUsername(String name, Pageable pageable);

    // count 쿼리 사용 안함
    Slice<Member> findByUsername(String name, Pageable pageable);

    // count 쿼리 사용 안함
    List<Member> findByUsername(String name, Pageable pageable);

    // count 쿼리 사용 안함
    List<Member> findByUsername(String name, Sort sort);
}
```
- org.springframework.data.domain.Page
  - 페이징과 total count 쿼리가 같이 나간다.
- org.springframework.data.domain.Slice
  - total count 없이 해당 페이지만 가져온다.
  - 내부적으로 limit + 1만큼 조회한다.
- List
  - total count 쿼리 없이 결과만 반환한다.

### 예제

#### Page
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    Page<Member> findByAge(int age, Pageable pageable);

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // 페이징은 0부터 시작한다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(DESC, "username"));
        int age = 10;

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 조회된 데이터
        List<Member> content = page.getContent();
        // 조회된 데이터 수
        assertThat(content.size()).isEqualTo(3);
        // 전체 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5);
        // 페이지 번호
        assertThat(page.getNumber()).isEqualTo(0);
        // 전체 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);
        // 첫번째 항목인가?
        assertThat(page.isFirst()).isTrue();
        // 다음 페이지가 있는가?
        assertThat(page.hasNext()).isTrue();
    }
}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
where member0_.age = 10
order by member0_.username desc limit 3;

select count(member0_.member_id) as col_0_0_
from member member0_
where member0_.age = 10;
```
- count를 구하는 별도의 메서드 없이 자동으로 쿼리를 날린다.
- PageRequest
  - Pageable 인터페이스를 구현한 객체

#### Slice
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    Slice<Member> findByAge(int age, Pageable pageable);

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    void slice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(DESC, "username"));
        int age = 10;

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        // 조회된 데이터
        List<Member> content = page.getContent();
        // 조회된 데이터 수
        assertThat(content.size()).isEqualTo(3);
        // 페이지 번호
        assertThat(page.getNumber()).isEqualTo(0);
        // 첫번째 항목인가?
        assertThat(page.isFirst()).isTrue();
        // 다음 페이지가 있는가?
        assertThat(page.hasNext()).isTrue();
    }
}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
where member0_.age = 10
order by member0_.username desc limit 4;
```
- count를 가져오지 않는다.
- 컨텐츠만 limit+1만큼 가져온다.
- 더보기 방식으로 개발할 때 사용한다.

#### List
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByAge(int age, Pageable pageable);

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    void list() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(DESC, "username"));
        int age = 10;

        List<Member> page = memberRepository.findByAge(age, pageRequest);
    }
}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
where member0_.age = 10
order by member0_.username desc limit 3;
```

### Count 최적화
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m left join m.team t")
    Page<Member> findByAge(int age, Pageable pageable);

}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id
order by member0_.username desc limit 3;

select count(member0_.member_id) as col_0_0_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id;
```
- count 쿼리는 매번 총 개수를 세야해서 부하가 걸린다.
- join으로 가져오는 데이터라면 count 쿼리에 불필요한 join을 날린다.
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id
order by member0_.username desc limit 3;

select count(member0_.username) as col_0_0_
from member member0_;
```
- countQuery를 사용하면 count 쿼리가 심플하게 나간다.

### 페이지를 유지하면서 엔티티를 DTO로 변환
```JAVA
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    @Test
    void paging() {
        
        ...

        Page<Member> page = memberRepository.findByAge(10, pageRequest);
        Page<MemberDto> dtoPage = page.map(m -> new MemberDto(m.getId(), m.getUsername()));

      ...
    }
}
```
- map
  - 엔티티 그대로 컨트롤러에서 넘기면 안되므로 dto로 변환한다.

## 벌크성 수정 쿼리

### 순수 JPA
```java
@Repository
public class MemberJpaRepository {

    public int bulkAgePlus(int age) {
        int resultCount = em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
        return resultCount;
    }
}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Test
    public void bulkUpdate() throws Exception {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        int resultCount = memberJpaRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);
    }
}
```
```sql
update member
set age=age + 1
where age >= 20;
```

### 스프링 데이터 JPA
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    public void bulkUpdate() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);
    }
}
```
```sql
update member
set age=age + 1
where age >= 20;
```
- `@Modifying`
  - executeUpdate()를 실행한다.
  - 붙이지않으면 getSingleResult(), getResultList()를 호출하면서 에러가 발생한다.

### 주의 사항
- JPA는 영속성 컨텍스트에서 엔티티를 관리한다.
- 벌크형 연산은 이것을 무시하고 진행하기때문에 문제가 발생 할 수있다.
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    public void bulkUpdate() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // 나이를 한 살 더한다.
        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);

        // JPA 영속성 컨텍스트에서는 여전히 40살이다.
        Member member5 = memberRepository.findMembers("member5");
        // fail
        assertThat(member5.getAge()).isEqualTo(41);
    }
}
```
- 아직 DB에 반영되지 않은 상태에서 쿼리를 날려서 데이터가 안맞는다.
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    // 같은 트랜잭션이면 둘 다 같은 엔티티 매니저를 쓴다.
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void bulkUpdate() throws Exception {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);

        // DB에 반영되지 않은 데이터를 반영한다.
        em.flush();
        // 영속성 컨텍스트의 데이터를 날려버린다.
        em.clear();

        Member member5 = memberRepository.findMembers("member5");
        assertThat(member5.getAge()).isEqualTo(41);
    }
}
```
- 벌크 연산 직후에는 꼭 영속성 컨텍스트를 날려준다.
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
```
- `@Modifying`에 옵션을 주면 `clear()`를 자동으로 날린다.

### 참고
- JPA는 update 등의 쿼리가 있으면 영속성 컨텍스트에 있는 데이터를 먼저 flush 하고 JPQL을 실행한다.
  - memberRepository.bulkAgePlus(20)도 결국 JPQL이므로 똑같이 동작한다.
  - 따라서 memberRepository.save가 반영된 뒤 memberRepository.bulkAgePlus(20)를 실행한다.
- JPA 대신 JDBC Template을 직접 사용하는 경우는 해당되지 않는다.

## @EntityGraph

### N+1 문제
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            member.getTeam().getName();
        }
    }
}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_;

select team0_.team_id as team_id1_1_0_, team0_.name as name2_1_0_
from team team0_
where team0_.team_id = 1;

select team0_.team_id as team_id1_1_0_, team0_.name as name2_1_0_
from team team0_
where team0_.team_id = 2;
```
- fetch 방식이 Lazy면 결과 개수에 따라 쿼리가 더 나간다.
- 네트워크를 그만큼 타는 것이기 때문에 성능 저하가 발생한다.

### fetch Join
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    // member를 조회할 때 연관된 team도 함께 가져온다.
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            member.getTeam().getName();
        }
    }
}
```
```sql
select member0_.member_id as member_i1_0_0_,
       team1_.team_id     as team_id1_1_1_,
       member0_.age       as age2_0_0_,
       member0_.team_id   as team_id4_0_0_,
       member0_.username  as username3_0_0_,
       team1_.name        as name2_1_1_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id;
```
- fetch join을 명시해서 조회하면 프록시 없이 한방에 값을 다 채워서 가져온다.
- 스프링 데이터 JPA는 자동으로 제공되는 메서드도 존재하여 fetch join을 적용하기가 번거롭다.

### @EntityGraph

```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메서드 이름으로 쿼리 자동 생성할 때도 가능
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

}
```
```sql
select member0_.member_id as member_i1_0_0_,
       team1_.team_id     as team_id1_1_1_,
       member0_.age       as age2_0_0_,
       member0_.team_id   as team_id4_0_0_,
       member0_.username  as username3_0_0_,
       team1_.name        as name2_1_1_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id;
```
- fetch join을 편하게 적용 가능하다.
- left outer join을 사용한다.
- 기본으로 제공되는 메서드에도 적용 가능하다.

### @NamedEntityGraph

```java
@Entity
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {
    
    ...
}
```
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph("Member.all")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

}
```
```sql
select member0_.member_id as member_i1_0_0_,
       team1_.team_id     as team_id1_1_1_,
       member0_.age       as age2_0_0_,
       member0_.team_id   as team_id4_0_0_,
       member0_.username  as username3_0_0_,
       team1_.name        as name2_1_1_
from member member0_
         left outer join team team1_ on member0_.team_id = team1_.team_id;
```
- NamedQuery를 활용햇허 fetch join도 가능하다.

### 활용
- 보통 직접 @Query에 JPQL로 fetch join을 넣어 사용한다.
- 너무 간단해서 쓰기 귀찮을때 사용하도록 하자.

## JPA Hint & Lock

### JPA Hint
- sql 힌트가 아니라 JPQL 구현체에게 제공하는 힌트
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    public void queryHint() throws Exception {
        memberRepository.save(new Member("member1", 10));

        // DB에 동기화
        em.flush();
        // 영속성 컨텍스트 초기화
        em.clear();

        // 영속성 컨텍스트를 초기화 했으니 무조건 DB에 쿼리를 날린다.
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");

        // 더티 체킹이 반영되지 않는다.
        em.flush();
    }
}
```
```sql
insert into member (age, team_id, username, member_id)
values (10, NULL, 'member1', 1);

select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
where member0_.username = 'member1';
```
- 더티 체킹은 원본과 수정본을 둘 다 메모리에 들고 있어야 하고 체크하는 과정도 필요하기 때문에 비용이 든다.
  - 단지 조회만 하고 끝내고 싶어도 find()를 하는 순간 스냅샷을 떠놓게 된다.
- readOnly 힌트를 넘기면 select만 나가고 update는 나가지 않는다.
- 성능은 복잡한 쿼리 때문이지 readOnly를 적용하지 않아서는 거의 없다.
  - 성능 테스트를 해보고 정말 이점이 있는 곳에서 사용한다.

### Lock
```java
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findByUsername(String name);

}
```
```java
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Test
    public void lock() {
        memberRepository.save(new Member("member1", 10));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findByUsername("member1");
    }
}
```
```sql
select member0_.member_id as member_i1_0_,
       member0_.age       as age2_0_,
       member0_.team_id   as team_id4_0_,
       member0_.username  as username3_0_
from member member0_
where member0_.username = 'member1' for update;
```
- 락을 적용할 수 있다.
- for update
  - 데이터를 수정하려고 select 하는 중이니 다른 사람은 데이터에 손대지 말라는 뜻
- 실시간 트래픽이 많은 곳에 락을 걸면 위험하다.