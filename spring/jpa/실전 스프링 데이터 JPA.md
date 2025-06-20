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

# 확장 기능

## 사용자 정의 리포지토리
- 스프링 데이터 JPA의 리포지토리는 인터페이스만 정의하면 스프링이 구현체를 자동으로 생성.
  - 인터페이스를 직접 구현하면 부모의 기능까지 합쳐서 구현해야 하는 기능이 너무 많다.
- 그럼에도 불구하고 직접 구현하고 싶다면
  - EntityManager로 JPA 직접 사용
  - 스프링 JDBC Template 사용
  - 마이바티스 사용
  - 데이터베이스 커넥션 직접 사용
  - QueryDsl 사용

### 예제
```java
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
```
```java
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    // 생성자에 하나만 있으면 알아서 injection 해준다.
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
```
```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
```
- 인터페이스에 대한 실제 구현은 MemberRepositoryImpl에서 한다.
- MemberRepository가 인터페이스를 상속한다.
```java
class MemberRepositoryTest {
    
    ...

    @Test
    void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }
}
```
- 호출될 때는 실제 구현인 MemberRepositoryImpl의 로직을 실행한다.

### 규칙
- 구현 클래스 이용
  - 리포지토리의 인터페이스 이름 + Impl
  - 스프링 데이터 JPA가 이 이름으로 인식하여 스프링 빈으로 자동 등록한다.
- MemberRepositoryCustom등 인터페이스는 자유롭게 지을 수 있다.

### 참고
- 실무에서 QueryDsl이나 SpringJDBCTemplate을 사용할 때 사용한다.
- 항상 사용자 정의 리포지토리가 필요한것이 아니다.
  - 핵심 비즈니스를 처리하는 로직과 화면에 맞춘 복잡한 쿼리는 클래스를 분해하는것이 좋다.
  - 화면에 맞춘 쿼리는 이해만하는것도 힘들기때문에 로직이 한군데 몰리면 개발자가 혼란을 겪는다.
  - 커스텀 리포지토리를 배웠다고 해서 여기에 다 몰아넣는 실수는 하지 않도록 한다.

```java
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager entityManager;

    List<Member> findAllMembers() {
        List result = entityManager.createQuery("")
                .getResultList();

        return result;
    }
}
```
- 인터페이스가 아닌 클래스로 만들어 빈으로 등록하고 직접 사용 가능하다.
  - 이때는 스프링 데이터 JPA와는 아무 관계 없이 별도로 동작한다.
  - 핵심은 비즈니스 로직과 그렇지 않는 것을 분리해야 한다는 것이다.

### 최신 구현 방식
```java
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
```
- 스프링 데이터 2.x 이상에서 사용자 정의 구현 클래스 이름 + Impl 대신
- MemberRepositoryCustomImpl등 사용자 정의 인터페이스 이름 + Impl도 지원한다.
- 사용자 정의 인터페이스와 구현 클래스 이름이 비슷하므로 기존 방식보다 직관적이다.
- 여러 인터페이스를 분리해서 구현하는 것도 가능하므로 이 방식을 사용하도록 하자.

## Auditing
- 엔티티를 생성, 변경할 때 변경한 사람이나 시간을 추적할 때 사용한다.
- 웬만하면 모든 엔티티에 적용해서 운영에 편의를 얻자.

### 순수 JPA 사용
```java
// 상속처럼 프로퍼티를 테이블에서 내려서 쓸 수 있는 애너테이션
// 진짜 상속과는 다르다.
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    // 실수로라도 DB 값이 변경되지 않게 막는다.
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    // 영속화 하기 전에 발생시키는 이벤트
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        // 등록과 수정을 처음부터 똑같이 맞춰둔다.
        // 값에 null이 있으면 쿼리가 지저분해질 수 있고 created와 값이 같으면 최초 값이라는 것을 알 수 있어 편하다.
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
```
```java
@Entity
public class Member extends JpaBaseEntity {

}
```
```sql
create table member
(
    member_id    bigint  not null,
    created_date timestamp,
    updated_date timestamp,
    age          integer not null,
    username     varchar(255),
    team_id      bigint,
    primary key (member_id)
)
```
- JPA 주요 이벤트 애너테이션
  - @PrePersist
  - @PostPersist
  - @PreUpdate
  - @PostUpdate

```java
class MemberTest {
    @Test
    public void JpaEventBaseEntity() throws Exception {
        Member member = new Member("member1");
        // @PrePersist 발생
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        // @PreUpdate 발생
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

        System.out.println("findMember.createdDate = " + findMember.getCreatedDate());
        System.out.println("findMember.updatedDate = " + findMember.getUpdatedDate());
    }
}
```
- JPABaseEntity만 만들어놓으면 여러 엔티티에 공용으로 사용 가능하여 편리하다.

### 스프링 데이터 JPA 사용
- 설정
  - 스프링 부트 설정 클래스
    - @EnableJpaAuditing
  - 엔티티
    - @EntityListeners(AuditingEntityListener.class)
- 사용 애너테이션
  - @CreatedDate
  - @LastModifiedDate
  - @CreatedBy
  - @LastModifiedBy

### 등록일, 수정일
```JAVA
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
```
```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

}
```
```java
public class Member extends BaseEntity {

}
```

### 등록자, 수정자
```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataJpaApplication.class, args);
    }

    // 등록자, 수정자를 처리해주는 AuditorAware 스프링 빈을 등록한다.
    @Bean
    public AuditorAware<String> auditorProvider() {
        // 실제로는 스프링 시큐리티 정보나 HTTP 세션에서 가져온다.
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
```
- 등록이나 수정할 때마다  auditorProvider 빈을 호출한 뒤 결과물을 채운다.

### 전체 적용
```xml
<?xml version=“1.0” encoding="UTF-8”?>
<entity-mappings xmlns=“http://xmlns.jcp.org/xml/ns/persistence/orm”
        xmlns:xsi=“http://www.w3.org/2001/XMLSchema-instance”
        xsi:schemaLocation=“http://xmlns.jcp.org/xml/ns/persistence/
        orm http://xmlns.jcp.org/xml/ns/persistence/orm_2_2.xsd”
        version=“2.2">
<persistence-unit-metadata>
<persistence-unit-defaults>
    <entity-listeners>
        <entity-listener
                class="org.springframework.data.jpa.domain.support.AuditingEntityListener”/>
                </entity-listeners>
            </persistence-unit-defaults>
        </persistence-unit-metadata>
    </entity-mappings>
```
- @EntityListeners(AuditingEntityListener.class)를 생략하고 엔티티 전체에 적용한다.

### 참고
```java
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}

public class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;
}
```
- 실무에서 시간은 필요해도 등록자, 수정자는 필요없을 수 있다.
- 따라서 Base 타입을 분리하고 원하는 타입을 선택하여 상속하면 된다.

## Web 확장
### 도메인 클래스 컨버터
```java
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터 사용 후
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        // 바로 찾아준다.
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}
```
- 도메인 클래스 컨버터가 중간에 동작하여 id로 회원 엔티티 객체를 반환한다.
  - 리파지토리를 사용해 엔티티를 찾는것.
- 이 방식을 사용하면 엔티티는 단순 조회용으로만 사용해야 한다.
  - 트랜잭션이 없는 범위에서 엔티티를 조회하므로 엔티티를 변경해도 DB에 반영되지 않기 때문.

### 페이징과 정렬
```java
@RestController
@RequiredArgsConstructor
public class MemberController {

  ...

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }
}
```
```sql
{
  "content": [
    {
      "createdDate": "2022-05-22T11:58:18.047317",
      "lastModifiedDate": "2022-05-22T11:58:18.047317",
      "createdBy": "dc7d4eee-f366-4bcf-ab64-47b87f084771",
      "lastModifiedBy": "dc7d4eee-f366-4bcf-ab64-47b87f084771",
      "id": 1,
      "username": "user0",
      "age": 0,
      "team": null
    },
    {
      "createdDate": "2022-05-22T11:58:18.078782",
      "lastModifiedDate": "2022-05-22T11:58:18.078782",
      "createdBy": "3aea9092-a323-452a-ad4c-a0d405842ba5",
      "lastModifiedBy": "3aea9092-a323-452a-ad4c-a0d405842ba5",
      "id": 2,
      "username": "user1",
      "age": 1,
      "team": null
    },
    ...

    {
      "createdDate": "2022-05-22T11:58:18.121901",
      "lastModifiedDate": "2022-05-22T11:58:18.121901",
      "createdBy": "51da627f-8564-48a1-bfdb-33b96ca6fd25",
      "lastModifiedBy": "51da627f-8564-48a1-bfdb-33b96ca6fd25",
      "id": 20,
      "username": "user19",
      "age": 19,
      "team": null
    }
  ],
  "pageable": {
    "sort": {
      "sorted": false,
      "unsorted": true,
      "empty": true
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "numberOfElements": 20,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false,
    "unsorted": true,
    "empty": true
  },
  "empty": false
}
```
- 어떤 쿼리든 Pageable을 파라미터로 넘기면 페이징이 가능해진다.
`/members?page=0&size=3&sort=id,desc&sort=username,desc`
- 요청 파라미터에 page, size, sort을 넘기면 자동으로 처리한다.
  - 컨트롤러를 바인딩할 때 Pageable이 있는지 확인한다.
  - Pageable 인터페이스가 org.springframework.data.domain.PageRequest 객체를 생성한다.
- page
  - 현재 페이지
  - 0부터 시작
- size
  - 한 페이지와 노출할 데이터 건 수
- sort
  - 정렬 조건
  - asc 생략 가능

### 기본 값 변경
`
# 기본 페이지 사이즈
spring.data.web.pageable.default-page-size=20
  # 최대 페이지 사이즈
spring.data.web.pageable.max-page-size=2000
`

### 개별 설정
```java
@RestController
@RequiredArgsConstructor
public class MemberController {
    
   ...

    @RequestMapping(value = "/members_page", method = RequestMethod.GET)
    public String list(
            @PageableDefault(
                    size = 12,
                    sort = "username",
                    direction = Sort.Direction.DESC)
            Pageable pageable) {
    ...

    }
}
```
- @PageableDefault 설정이 우선하여 적용된다.

### 접두사
```java
@RestController
@RequiredArgsConstructor
public class MemberController {
    
   ...

    @RequestMapping(value = "/members_page", method = RequestMethod.GET)
    public String list(
            @Qualifier("member") Pageable memberPageable,
            @Qualifier("order") Pageable orderPageable) {
    ...

    }
}
```
`/members?member_page=0&order_page=1`
- 페이징 정보가 둘 이상이면 접두사로 구분한다.
- @Qualifier
  - value에 접두사 이름을 추가하면 요청에서 접두사_xxx를 구분한다.

### Page 내용을 DTO로 변환
- 엔티티를 API로 바로 노출하면 문제가 발생한다.
- 꼭 DTO로 변환해서 반환한다.
- Page는 map()을 지원해서 내부 데이터를 변환할 수 있다.
```java
@Data
public class MemberDto {
    private Long id;
    private String username;

    public MemberDto(Member m) {
        this.id = m.getId();
        this.username = m.getUsername();
    }
}
```
```java
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public Page<MemberDto> listPageMap1(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;
    }

    // Page.map() 최적화
    @GetMapping("/members")
    public Page<MemberDto> listPageMap2(Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }
}
```

### Page를 1부터 시작하기
- 스프링 데이터는 Page를 0부터 시작한다.
- 1부터 시작하려면?

#### 직접 클래스 생성
- Pageable, Page를 파리미터와 응답 값으로 사용하지 않고 직접 클래스를 만들어서 처리한다.
- Pageable 구현체인 PageRequest를 직접 생성해서 리포지토리에 넘긴다.
- 물론 응답값도 Page 대신 직접 만들어 제공해야 한다.

#### one-indexed-parameters 설정
`spring.data.web.pageable.one-indexed-parameters=true`
`http://localhost:8080/members?page=1`
```json
{
  "content": [
    ...
  ],
  "pageable": {
    "offset": 0,
    "pageSize": 10,
    // 0 인덱스
    "pageNumber": 0
  },
  // 0 인덱스
  "number": 0,
  "empty": false
}
```
- 이 방법은 web에서 page 파라미터를 -1로 처리할 뿐이다.
- 따라서 응답값인 Page 에 모두 0 페이지 인덱스를 사용하는 한계가 있다.