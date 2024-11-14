# 목차
- [JDBC 등장 배경](#jdbc-등장-배경)
- [JDBC란](#jdbc란)
- [JDBC와 최신기술](#jdbc와-최신-기술)
 

# JDBC 등장 배경

애플리케이션을 개발할 때 중요한 데이터는 대부분 DB에 저장된다.

클라이언트가 DB를 호출하려면 아래와 같은 과정을 거친다.

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbwzRx8%2FbtrzvyuC4Sl%2FyqKRQHdprsqpJFdH7xobHK%2Fimg.png)

여기서 Application Server에서 DB까지의 과정을 좀 더 살펴보자.

커넥션 연결: 주로 TCP/IP 사용
SQL 전달: 커넥션을 통해 DB에 SQL 전달
결과 응답: DB가 SQL 수행 후 결과 응답. application server는 응답 결과 활용
 

 과거에는 DB마다 커넥션을 연결하는 방법, SQL 전달하는 방법, 결과를 응답 받는 방법이 모두 달랐다. 게다가 MySQL, Oracle DB, h2 등등등 DB의 종류는 정말 정말 많다. DB 종류에 따라 코드도 변경해야 하고 개발자가 연결 방법을 매번 배워야 했다. 이때 등장한 것이 JDBC 표준 인터페이스이다.

 # JDBC란

JDBC는 크게 3가지 기능을 인터페이스로 정의해서 제공한다.

java.sql.Connection: 연결
java.sql.Statement: SQL을 담은 내용
java.sql.ResultSet: SQL 요청 응답
 

 이제 개발자는 위 표준 인터페이스만 사용해서 개발하면 된다. 알다시피 코드는 인터페이스만 존재한다고 동작하지 않고 '구현체'를 필요로 한다. 요 JDBC 인터페이스를 각각의 DB 회사에서 구현해 라이브러리로 제공하는데 이것이 JDBC 드라이버다. MySQL JDBC 드라이버는 MySQL DB에 접근할 수 있게 하고 Oracle JDBC 드라이버는 Oracle DB에 접근할 수 있게 해준다.

# 표준화의 한계

JDBC의 등장으로 많은 것이 편리해졌다. 하지만 일반적인 부분만 공통화했기 때문에 DB마다 다른 특성들을 모두 표준화하기에는 한계가 있다.
 
# JDBC와 최신 기술

이제 DB 변경 시 JDBC 코드는 변경하지 않아도 되지만 SQL은 모두 수정해야한다.
이를 좀 더 편리하게 하기 위해 SQL Mapper와 ORM이라는 기술이 등장하게 되었다.

JDBC

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FuBZqh%2FbtrzuQbNHYl%2F6bmsT6rMKCNbS7cRJtqO21%2Fimg.png)

SQL Mapper

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FC3oFm%2FbtrzuVYdAig%2FjghdiqbHPuKKTpwpSaPJOk%2Fimg.png)

ORM 기술

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FdLxD2S%2Fbtrzvxig8b7%2FVZHKq7gARRpPj5W9rDL1O1%2Fimg.png)

ORM이란 객체를 관계형 DB와 매핑하는 기술을 의미한다.


