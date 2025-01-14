# 스프링 MVC - 1

# 목차
- [스프링 MVC - 1](#스프링-mvc---1)
- [목차](#목차)
- [웹 애플리케이션 이해](#웹-애플리케이션-이해)
    - [웹 서버, 웹 애플리케이션 서버](#웹-서버-웹-애플리케이션-서버)
    - [서블릿](#서블릿)
    - [동시요청 - 멀티 쓰레드](#동시요청---멀티-쓰레드)
    - [html, http api, csr, ssr](#html-http-api-csr-ssr)
- [서블릿 프로젝트](#서블릿-프로젝트)
	- [프로젝트 생성](#프로젝트-생성)
	- [hello servlet 생성](#hello-servlet-생성)
	- [html 파일 생성](#html-파일-생성)
	- [HttpServletRequest](#httpservletrequest)
	- [HttpServletResopnse](#httpservletresponse)
- [회원 관리 웹 애플리케이션](#회원-관리-웹-애플리케이션)
	- [Servlet 이용](#servlet-이용)
	- [JSP 이용](#jsp-이용)



# 웹 애플리케이션 이해

## 웹 서버, 웹 애플리케이션 서버

### 웹서버
- http 기반 동작
- 정적 리소스 제공, 기타부가기능
- 정적 html, css, js, 이미지, 영상
- 예) nginx, apache

### 웹 애플리케이션 서버(was)
- http 기반 동작
- 웹 서버 기능 포함 + 정적 리소스 제공 
- 프로그램 코드를 실행해서 애플리케이션 로직 수행
- 동적 html, http api(json)
- 서블릿, jsp, 스프링 mvc
	- 예) 톰캣, jetty, undertow
	
### 웹서버, was 차이
- 웹서버는 정적, was는 애플리케이션 로직
- 용어 경계도 모호
	- 웹 서버도 프로그램 실행 기능 포함
	- 웹 애플리케이션 서버도 웹 서버 기능 제공
- 자바는 서블릿 컨테이너 기능을 제공하면 was	
	- 서블릿 없이 자바코드를 실행하는 서버 프레임워크도 존재한다.
- was는 애플리케이션 코드를 실행하는데 더 특화 되어있다.

### 웹 시스템 구성 - was, db
- was, db만으로 시스템 구성 가능
- was는 정적 리소스, 애플리케이션 로직 모두 제공 가능하다.
- was가 너무 많은 역할, 서버 과부하 우려
- 가장 비싼 애플리케이션 로직이 정적 리소스때문에 수행 방해 가능성 존재
- was 장애시 오류 화면도 노출 불가능

### 웹 시스템 구성 - web, was, db
- 정적 리소스는 웹 서버가 처리
- 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 was에 요청 ㅜ이임
- was는 중요한 애플리케이션 로직 처리 전담
- 효율적인 리소스 관리
	- 정적 리소스 많으면 web 서버 증설
	- 애플리케이션 리소스 많으면 was 서버 증설
- 정적 리소스만 제공하는 웹 서버는 잘 죽지 않음
- 애플리케이션 로직이 동작하는 was 서버는 잘 죽음
- was, db 장애시 web 서버가 오류 화면 제공 가능

## 서블릿

### 특징
- url패턴의 url이 호출되면 서블릿 코드 실행
- http 요청 정보를 편리하게 사용하는 httpservletrequest
- http 응답 정보를 편리하게 제공할 수 있는 httpservletresponse
- 개발자는 http 스펙을 매우 편리하게 사용 가능하다.

### http 요청, 응답 흐름
- http 요청시
	- was는 requset, response 객체를 새로 만들어서 서블릿 객체 호출
	- 개발자는 requset 객체에서 http 요청 정보를 편리하게 꺼내서 사용
	- 개발자는 response 객체에 htpp 응답 정보를 편리하게 입력
	- was는 response 객체에 담겨있는 내용으로 http 응답 정보 생성

### 서블릿 컨테이너
- 톰캣처럼 서블릿을 지원하는 was를 서블릿 컨테이너라고 한다.
- 서블릿 컨테이너는 서블릿 객체 생성, 초기화, 호출, 종료하는 생명주기 관리
- 서블릿 객체는 **싱글톤으로 관리**
	- 고객의 요청이 올때마다 계속 객체 생성하는 것은 비효 율
	- 최초 로팅 시점에 서블릿 객체를 미리 만들고 재활용
	- 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
	- **공유 변수** 사용에 주의한다.
	- 서블릿 컨테이너 종료시 함께 종료
- jsp도 서블릿으로 변환되어서 사용
- 동시 요청을 위한 멀티 쓰레드 처리 지원

## 동시요청 - 멀티 쓰레드

### 쓰레드

- 애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 쓰레드
- 자바 메인 메서드를 처음 실행하면 main이라는 이름의 쓰레드 실행
- 쓰레드가 없다면 자바 애플리케이션 실행x
- 쓰레드는 한번에 하나의 코드 라인만 수행
- 동시 처리가 필요하면 쓰레드를 추가로 생성해야한다.

### 단일 요청 - 쓰레드 하나 사용

### 다중 요청 - 쓰레드 하나 사용

### 요청 마다 쓰레드 생성

- 장점
	- 동시 요청 처리
	- 리소스 허용할때 까지 처리 가능성
	- 하나의 쓰레드가 지연 되어도, 나머지 쓰레드는 정상 동작.
- 단점
	- 쓰레드 생성 비용 매우 비쌈
		- 고객의 요청마다 쓰레드 생성시 응답 속도 느려짐
	- 쓰레드는 컨텍스트 스위칭 비용 발생
	- 쓰레드 생성에 제한x
		- 고객 요청이 너무 많이 오면, cpu, 메모리 임계점을 넘어 서버 다운 가능.
		
### 쓰레드 풀

- 특징
	- 필요한 쓰레드를 쓰레드 풀에 보관하고 관리
	- 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리. 톰캣은 최대 200개 기본 설정 (변경가능)
- 사용
	- 쓰레드가 필요하면, 이미 생성되있는 쓰레드를 쓰레드 풀에서 꺼내서 사용.
	- 사용 종료시 쓰레드 풀에 해당 쓰레드 반납.
	- 최대 쓰레드가 모두 사용중이라 쓰레드 풀에 쓰레드가 없다면?
		- 기다리는 요청은 거절하거나 특정 숫자만큼 대기하도록 설정 가능
- 장점
	- 쓰레드가 미리 생성되어있으므로, 쓰레드를 생성하고 종료하는 비용 절약, 응답시간 빠름
	- 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리.
	
### was의 멀티 쓰레드 지원

- 멀티 쓰레드에 대한 부분은 was가 처리
- 개발자가 멀티 쓰레드 관련 코드 신경x
- 개발자는 싱글 쓰레드 프로그래밍을 하듯이 편리하게 코드 개발
- 멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈) 주의 해서 사용.

## html, http api, csr, ssr

### 정적 리소스, html

### html api
- html이 아니라 데이터를 전달
- 주로 json 사용
- 다양한 시스템에서 호출
- 데이터만 주고 받음, ui화면이 필요하면, 클라이언트가 별도 처리
- 앱, 웹 클라이언트, 서버 to 서버
- 다양한 시스템 연동
	- 주로 json 형태
	- ui 클라이언트 접점
		- 앱 클라이언트 (아이폰, 안드로이드 pc앱)
		- 웹 브라우저에서 자바스크립트를 통한 http api 호출
		- react, vue.js같은 웹 클라이언트
	- 서버 to 서버
		- 주문 서버 -> 결제 서버
		- 기업간 데이터 통신

### 서버 사이드 렌더링, 클라이언트 사이드 렌더링

- ssr - 서버사이드 렌더링	
	- html 최종 결과를 서버에서 만들어 웹 브라우저에 전달
	- 주로 정적인 화면에 사용
	- 관련  기술: jsp, 타임리프 -> 백엔드
- csr - 클라이언트 사이드 렌더링
	- html 결과를 자바스크립트를 사용해 웹브라우저에서 동적으로 생성하여 적용
	- 주로 동적인 화면에 사용, 웹 환경을 마치 앱 처럼 필요한 부분부분 변경 가능 
	- 예) 구글 지도, gamil, 구글 캘린더
	- 관련 기술: react,vue.js -> 프론트엔드
	
# 서블릿 프로젝트

## 프로젝트 생성

서블릿을 이용하기 위해서는 Spring이 필요없다.

하지만 내장 톰캣 덕분에 설정하기 편하니까 Spring Initializr를 이용해 프로젝트를 생성한다.

패키징 형태만 war로 선택하고 나머지 설정은 달라도 상관없다.

## hello servlet 생성

먼저, application 파일에 어노테이션을 하나 추가해준다.

```
@ServletComponentScan //servlet 자동 등록
@SpringBootApplication
public class ServletApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}
}
```

**@ServletComponentScan**은 스프링이 자동적으로 하위 패키지에서 **서블릿을 모두 찾아서 자동으로 등록**해준다.

basic 패키지를 생성하고 HelloServlet.java을 만들어보자.

```
@WebServlet(name="helloservlet", urlPatterns="/hello")
public class HelloServlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
    }
}
```

/hello에 접근하면 실행되는 페이지다.

위 코드를 입력한 뒤, localhost:8080/hello에 접속하면 콘솔창에 문자열이 출력된 것이 보인다.

화면에는 아무것도 없는게 정상이다. (콘솔에 찍어주는 작업만 입력했으므로)

service 메소드를 보면 HttpServletRequest와 HttpServletResponse 두 파라미터가 눈에 띈다.

코드를 다음과 같이 수정해서 확인해보자.

```
@WebServlet(name="helloservlet", urlPatterns="/hello")
public class HelloServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String username = req.getParameter("username");
      		System.out.println(username);
        
 		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		res.getWriter().write("hello "+username);
	}
}
```

도메인 옆에 ?키=값 형식으로 데이터를 나열하면, 파라미터를 보낼 수 있다.

예를 들어 localhost:8080/hello?username=yeonlog

-> username에 yeonlog라는 값을 넣어 파라미터로 보냄

-> Helloservlet의 **HttpServletRequest**에 저장

-> .getParameter()를 이용해 값을 꺼낼 수 있음

 

**HttpServletResponse**는 Client로 보낼 값에 대해 설정할 수 있다.

위 예제코드 같은 경우에는 ContentType과 Encoding 타입에 대해 설정한 뒤, hello + username을 보냈다.

도메인에 접속하면 아래와 같이 결과를 확인할 수 있다.

## html 파일 생성

이제 src/main에 webapps 폴더를 만든 뒤, index.html과 basic.html 파일을 생성하겠다.

(우리의 주 목적은 백엔드 개발이니 html에 관한 설명은 생략한다.)

basic.html 외에 다른 /servlet/members/new-form 같은 링크들은 이후 차근차근 추가해나갈 것이다.

**index.html**

```
<!DOCTYPE html>
<html>
<head>
 <meta charset="UTF-8">
 <title>Title</title>
</head>
<body>
	<ul>
		 <li><a href="basic.html">서블릿 basic</a></li>
		 <li>서블릿
			 <ul>
				 <li><a href="/servlet/members/new-form">회원가입</a></li>
				 <li><a href="/servlet/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>JSP
			 <ul>
				 <li><a href="/jsp/members/new-form.jsp">회원가입</a></li>
				 <li><a href="/jsp/members.jsp">회원목록</a></li>
			 </ul>
		 </li>
		 <li>서블릿 MVC
			 <ul>
				 <li><a href="/servlet-mvc/members/new-form">회원가입</a></li>
				 <li><a href="/servlet-mvc/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v1
			 <ul>
				 <li><a href="/front-controller/v1/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v1/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v2
			 <ul>
				 <li><a href="/front-controller/v2/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v2/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v3
			 <ul>
				 <li><a href="/front-controller/v3/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v3/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v4
			 <ul>
				 <li><a href="/front-controller/v4/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v4/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v5 - v3
			 <ul>
				 <li><a href="/front-controller/v5/v3/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v5/v3/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>FrontController - v5 - v4
			 <ul>
				 <li><a href="/front-controller/v5/v4/members/new-form">회원가입</a></li>
				 <li><a href="/front-controller/v5/v4/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>SpringMVC - v1
			 <ul>
				 <li><a href="/springmvc/v1/members/new-form">회원가입</a></li>
				 <li><a href="/springmvc/v1/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>SpringMVC - v2
			 <ul>
				 <li><a href="/springmvc/v2/members/new-form">회원가입</a></li>
				 <li><a href="/springmvc/v2/members">회원목록</a></li>
			 </ul>
		 </li>
		 <li>SpringMVC - v3
			 <ul>
				 <li><a href="/springmvc/v3/members/new-form">회원가입</a></li>
				 <li><a href="/springmvc/v3/members">회원목록</a></li>
			 </ul>
		 </li>
	</ul>
</body>
</html>
```

**basic.html**
<!DOCTYPE html>
<html>
  <head>
     <meta charset="UTF-8">
     <title>Title</title>
  </head>
  <body>
    <ul>
       <li>hello 서블릿
         <ul>
            <li><a href="/hello?username=servlet">hello 서블릿 호출</a></li>
         </ul>
       </li>
       <li>HttpServletRequest
         <ul>
           <li><a href="/request-header">기본 사용법, Header 조회</a></li>
           <li>HTTP 요청 메시지 바디 조회
             <ul>
               <li><a href="/request-param?username=hello&age=20">GET -쿼리 파라미터</a></li>
               <li><a href="/basic/hello-form.html">POST - HTML Form</a></li>
               <li>HTTP API - MessageBody -> Postman 테스트</li>
             </ul>
           </li>
         </ul>
       </li>
       <li>HttpServletResponse
         <ul>
           <li><a href="/response-header">기본 사용법, Header 조회</a></li>
           <li>HTTP 요청 메시지 바디 조회
             <ul>
               <li><a href="/response-html">HTML 응답</a></li>
               <li><a href="/response-json">HTTP API JSON 응답</a></li>
             </ul>
           </li>
         </ul>
       </li>
    </ul>
  </body>
</html>
```

## HttpServletRequest 

HTTP 요청 메시지를 편리하게 사용할 수 있도록 하는 객체

### HTTP 요청 메시지

- START LINE: HTTP 메소드, URL 쿼리 스트링, 스키마, 프로토콜
- HEADER: 헤더 조회
- BODY: form 파라미터 형식 조회, message body 데이터 직접 조회

### HttpServletRequset 객체의 여러 기능
- 임시 저장소 기능: 해당 http 요청 시작부터 끝날 때 가지 유지
- 세션 관리 기능

## HTTP 요청 데이터 - GET, POST, Text, JSON

### GET 쿼리 파라미터

**GET URL 쿼리 파라미터**의 기본구조는 다음과 같다.

```java
@WebServlet(name="requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		...
        	res.getWriter().write("ok");
	}
}
```
도메인주소/request-param을 통해 접근이 가능하며,

도메인주소/request-param?키=값을 통해 파라미터를 전달할 수 있다.

여러개의 파라미터를 전달할 경우 & 기호를 이용해 나열하면 된다.

(ex: http://localhost:8080/request-param?username=kim&age=20)

1. 파라미터 전체 조회하기
```java
System.out.println("[전체 파라미터 조회] - start ");
req.getParameterNames().asIterator()
	.forEachRemaining(paramName -> System.out.println(paramName + "=" + req.getParameter(paramName)));
System.out.println("[전체 파라미터 조회] - end ");
```

- getParameterNames(): 파라미터 이름 목록을 가져오기
- asIterator(): 이터레이터로 변환
- forEachRemaining(): 각 요소에 대해 지정된 작업 수행
- getParameter(): 파라미터 키 이름을 이용해 키 값 가져오기

2. 단일 파라미터 조회하기
```java
System.out.println("[단일 파라미터 조회] - start ");
String username = req.getParameter("username");
String age = req.getParameter("age");
System.out.println(username + age);
System.out.println("[단일 파라미터 조회] - end ");
```

만약 복수 파라미터를 입력했는데 .getParameter()를 사용한다면, 첫 번째로 입력한 파라미터의 값을 가져온다.

( ex: http://localhost:8080/request-param?username=a&username=b&username=c 라면,

req.getParameter("username")을 했을 때, a 값이 가져와진다. )

3. 복수 파라미터 조회하기
```java
System.out.println("[복수 파라미터 조회] - start ");
String[] usernames = req.getParameterValues("username");
for(String name:usernames)  {
	System.out.println(name);
}
System.out.println("[복수 파라미터 조회] - end ");
```

- getParameterValues(): 복수 파라미터에 대한 모든 값 가져오기

### POST HTML Form

먼저 Form 태그를 이용하는 html 화면을 하나 생성한다.

```html
<!DOCTYPE html>
<html>
<head>
	 <meta charset="UTF-8">
	 <title>Title</title>
</head>
<body>
	<form action="/request-param" method="post">
		 username: <input type="text" name="username" />
		 age: <input type="text" name="age" />
		 <button type="submit">전송</button>
	</form>
</body>
</html>
```

 

html 코드를 보면 <form>태그의 action이 /request-param으로 되어있는데,

이는 앞에서 Get 쿼리 파라미터 테스트용으로 만든 파일과 이어진다.

전송 버튼을 누르면 Get URL 쿼리 파라미터 테스트 했을 때처럼 콘솔 창에 조회된 파라미터들을 확인할 수 있다.

 

위에서 테스트 했던 것은 GET 방식이고, 이번엔 POST로 보낸 것인데 왜 작동하는걸까?

- 각각의 전송 결과에서 Content-Type을 살펴보자 (Content-Type: HTTP 메시지 바디의 데이터 형식 지정)

- GET의 Content-Type은 존재하지 않는다.

- POST의 Content-Type이 application/x-www-form-urlencoded인 것을 확인할 수 있다.

-> GET은 메시지 바디를 사용하지 않기 때문에 Content-Type이 없어도 무방하지만, POST는 메시지 바디를 사용하기에 Form 태그가 Content-Type을 지정해준다.

=>  클라이언트에서는 보내는 형식이 다르다고 생각하지만, 서버 입장에서는 둘의 형식은 동일하므로 메소드를 그대로 사용해도 무방하다.

### API 메시지 바디

GET과 POST 같은 HTTP 요청은 메시지 바디에 원하는 데이터를 담아서 전송할 수 있다.

보통은 JSON 형식을 많이 사용하지만 옛날에는 XML을 이용하기도 했다.

#### Text

가장 기본적인 Text 형식이다.

```java
@WebServlet(name="requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		ServletInputStream inputStream = req.getInputStream();
		String msgBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
		
		System.out.println("msg Body = " + msgBody);
		
		res.getWriter().write("ok");
	}
}
```

POST 방식을 사용해야하기 때문에 Postman을 이용해 테스트했다.

아래와 같이 내가 보낸 텍스트를 그대로 받아낸 것을 확인할 수 있다.

![alt text](image.png)

#### Json

위의 Text와 같은 방식으로 RequestBodyJsonServlet.java를 생성하고 Postman으로 테스트 해보았다.

```java
@WebServlet(name="requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		ServletInputStream inputStream = req.getInputStream();
		String msgBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
		
		System.out.println("msg Body = " + msgBody);
		
		res.getWriter().write("ok");
	}
}
```

테스트 방식이 Text에서 JSON으로 바뀐 것 외에는 다른게 없어보인다.

하지만 JSON 형식은 매핑이 가능한데, 객체 하나를 생성해보자.

 

HelloData.java

```java
@Getter @Setter
public class HelloData {
	private String username;
	private int age;
}
```
(참고로 @Getter, @Setter는 롬복 라이브러리를 이용한 것이다.)

 

RequestBodyJsonServlet.java를 수정해 mapper를 이용하자.

mapper는 Springboot에서 기본적으로 제공하는 Jackson을 사용했다.

```java
@WebServlet(name="requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet{

	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		ServletInputStream inputStream = req.getInputStream();
		
		String msgBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
		System.out.println("msg Body = " + msgBody);
		
		HelloData helloData = mapper.readValue(msgBody, HelloData.class);
		System.out.println(helloData.getUsername());
		System.out.println(helloData.getAge());
		
		res.getWriter().write("ok");
	}
}
``` 

마찬가지로 Postman으로 테스트하면 콘솔 창을 통해 객체에 잘 매핑된 것을 확인할 수 있다.

## HttpServletResponse

HttpServletResponse의 역할
- HTTP 응답, 헤더, 바디 생성
- Content-type, Cookie, Redirect 등의 기능 제공

### 응답 헤더

새로 response 폴더를 만들고 ResponseHeaderServlet.java를 생성했다.

```java
@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet{

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// status-line
		res.setStatus(HttpServletResponse.SC_OK);
		
		// response header
		res.setContentType("text/plain");
		res.setCharacterEncoding("utf-8");
		//res.setHeader("Content-Type", "text/plain;charset=utf-8");
		res.setHeader("Cache-Contorl", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "no-cache");
		res.setHeader("temp-header", "hello world");

		PrintWriter writer = res.getWriter();
		writer.println("ok");
	}
}
```
HttpServletResponse는 기본적으로 `.setHeader()`를 통해 헤더를 세팅할 수 있다.

이 외에도 `setContentType, setCharacterEncoding` 등을 사용할 수 있다.

이 외에도 쿠키나 리다이렉트를 설정할 수 있는데, 예시는 아래와 같다.

#### 쿠키 설정하기

```java
private void cookie (HttpServletResponse res) {
	Cookie cookie = new Cookie("myCookie","good");
	cookie.setMaxAge(600);
	res.addCookie(cookie);
}
```

#### 리다이렉트 설정하기

```java
private void redirect (HttpServletResponse res) {
	res.setStatus(HttpServletResponse.SC_FOUND);
	res.setHeader("Location", "/basic/hello-form.html");
//	res.sendRedirect("/basic/hello-form.thml");
}
```

### 응답 데이터

#### Html 형식으로 응답하기

ResponseHtmlServlet.java를 생성한다.

```java
@WebServlet(name="responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		// Content-Type: text/html;charset=utf-8
		res.setContentType("text/html");
		res.setCharacterEncoding("utf-8");
		
		PrintWriter writer = res.getWriter();
		writer.println("<html>");
		writer.println("<body>");
		writer.println(" <div>안녕?</div>");
		writer.println("</body>");
		writer.println("</html>");
	}
}
```
한글이 깨지는 것을 대비해 ContentType과 Encoding을 설정.

#### Json 형식으로 응답하기

ResponseJsonServlet.java 생성

```java
@WebServlet(name="responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet{
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/json");
		res.setCharacterEncoding("utf-8");
		
		// HelloData 객체
		HelloData helloData = new HelloData();
		helloData.setUsername("Woo");
		helloData.setAge(20);
		
		// json 형태로 바꾸기
		String result = mapper.writeValueAsString(helloData);
		res.getWriter().write(result);
	}
}
```
HelloData에 값을 넣고, json 형태로 바꾸고 응답을 보내줬다.

추가적으로, application/json;charset=utf-8은 의미 없는 코드다.

json은 기본적으로 utf-8을 사용하기 때문에..

하지만 getWriter()를 사용하다보면 자동적으로 뒤에 추가되므로, 이 경우에는 getOutputStream()으로 출력하면 된다.

# 회원 관리 웹 애플리케이션

회원 관리 웹 애플리케이션을 Servlet, JSP, MVC 패턴 차례대로 개발하며 비교할 것이다.

회원 관리 웹 애플리케이션의 요구사항은 간단하게 구성하였다.

- 회원 저장
- 회원 목록 조회

### 회원 모델 생성

```java
@Getter @Setter
public class Member {
	private Long id;
	private String username;
	private int age;
	
	public Member() {
	}
	
	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}
}
```
- 아이디, 이름, 나이를 저장하는 Member 객체
- @Getter @Setter 롬복 라이브러리를 이용하였다.

### 회원 저장소 생성

```java
public class MemberRepository {
	private static Map<Long, Member> store = new ConcurrentHashMap<> ();
	private static long sequence = 0L;
	
	// 싱글톤으로 생성
	private static final MemberRepository instance = new MemberRepository();
	
	public static MemberRepository getInstance() {
		return instance;
	}
	
	private MemberRepository() {
	}
	
	public Member save(Member member) {
		member.setId(++sequence);
		store.put(member.getId(), member);
		return member;
	}
	
	public Member findById(Long id) {
		return store.get(id);
	}
	
	public List<Member> findAll() {
		return new ArrayList<>(store.values()); //store룰 보호하기 위해 arraylist를 새로 생성
	}
	
	public void clearStore() {
		store.clear();
	}
}
```
- 멤버 저장, 아이디로 찾기, 전체 조회 기능 생성
- clearStore은 테스트에서 사용하기 위함
- private static final 키워드를 이용해 MemberRepository를 싱글톤으로 생성

### 회원 저장소 테스트 작성

```java
public class MemberRepositoryTest {
	MemberRepository memberRepository = MemberRepository.getInstance();
	
	@AfterEach 
	void afterEach() { // 테스트 종료 할때마다 store 날려주기
		memberRepository.clearStore();
	}
	
	@Test
	void save() {
		// given
		Member member = new Member("hello",20);
		
		// when
		Member savedMember = memberRepository.save(member);
		
		// then
		Member findMember = memberRepository.findById(savedMember.getId());
		Assertions.assertThat(findMember).isEqualTo(savedMember);
	}
	
	@Test
	void findAll() {
		// given
		Member member1 = new Member("mem1",20);
		Member member2 = new Member("mem2",25);
		
		memberRepository.save(member1);
		memberRepository.save(member2);
		
		// when
		List<Member> result = memberRepository.findAll();
		
		// then
		Assertions.assertThat(result.size()).isEqualTo(2);
		Assertions.assertThat(result).contains(member1, member2);
	}
}
```
- 저장 및 전체 조회 기능 테스트
- @AfterEach를 이용해 테스트 종료 시마다 store clear

## Servlet 이용

이제 본격적으로 Servlet을 이용하여 회원 관리 웹 애플리케이션을 생성한다.
각 서블릿은 web/servlet 패키지를 생성하여 저장하였다.


#### MemberFormServlet
```java
@WebServlet(name="memberFormServlet", urlPatterns = "/servlet/members/new-form")
public class MemberFormServlet extends HttpServlet {
	private MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		res.setCharacterEncoding("utf-8");
		
		PrintWriter w = res.getWriter();
		w.write("<!DOCTYPE html>\n" +
				 "<html>\n" +
				 "<head>\n" +
				 " <meta charset=\"UTF-8\">\n" +
				 " <title>Title</title>\n" +
				 "</head>\n" +
				 "<body>\n" +
				 "<form action=\"/servlet/members/save\" method=\"post\">\n" +
				 " username: <input type=\"text\" name=\"username\" />\n" +
				 " age: <input type=\"text\" name=\"age\" />\n" +
				 " <button type=\"submit\">전송</button>\n" +
				 "</form>\n" +
				 "</body>\n" +
				 "</html>\n");
	}
}
```

- Content-Tyep, encoding 설정.
- PrintWriter를 이용하여 html 코드를 직접 입력.

#### MemberSaveServlet.java

```java
@WebServlet(name="memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {
	private MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("MemberSaveServlet.service");
		String username = req.getParameter("username");
		int age = Integer.parseInt(req.getParameter("age"));
		
		Member member = new Member(username, age);
		memberRepository.save(member);
		
		res.setContentType("text/html");
		res.setCharacterEncoding("utf-8");
		PrintWriter w = res.getWriter();
		w.write("<html>\n" +
				 "<head>\n" +
				 " <meta charset=\"UTF-8\">\n" +
				 "</head>\n" +
				 "<body>\n" +
				 "성공\n" +
				 "<ul>\n" +
				 " <li>id="+member.getId()+"</li>\n" +
				 " <li>username="+member.getUsername()+"</li>\n" +
				 " <li>age="+member.getAge()+"</li>\n" +
				 "</ul>\n" +
				 "<a href=\"/index.html\">메인</a>\n" +
				 "</body>\n" +
				 "</html>");
	}
}
```

- MemberFormServlet에서 호출되는 /servlet/members/save 부분
- 성공 시 member에서 정보를 가져와 화면에 보여줌

#### MemberListServlet.java

```java
@WebServlet(name="memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {
	private MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		List<Member> members = memberRepository.findAll();
		
		res.setContentType("text/html");
		res.setCharacterEncoding("utf-8");
		
		PrintWriter w = res.getWriter();
		w.write("<html>");
		 w.write("<head>");
		 w.write(" <meta charset=\"UTF-8\">");
		 w.write(" <title>Title</title>");
		 w.write("</head>");
		 w.write("<body>");
		 w.write("<a href=\"/index.html\">메인</a>");
		 w.write("<table>");
		 w.write(" <thead>");
		 w.write(" <th>id</th>");
		 w.write(" <th>username</th>");
		 w.write(" <th>age</th>");
		 w.write(" </thead>");
		 w.write(" <tbody>");

		 for (Member member : members) {
		 w.write(" <tr>");
		 w.write(" <td>" + member.getId() + "</td>");
		 w.write(" <td>" + member.getUsername() + "</td>");
		 w.write(" <td>" + member.getAge() + "</td>");
		 w.write(" </tr>");
		 }
		 
		 w.write(" </tbody>");
		 w.write("</table>");
		 w.write("</body>");
		 w.write("</html>");
	}
}
```

- for문에서 보듯 동적으로 데이터 추가 가능


예제를 통해 보듯이 **Servlet은 너무 불편**하다.

Printwriter를 통해 html을 다 작성해야해서 비효율적이고 Java 코딩이라기보다 html의 코딩이라는 느낌이 강하다.

이를 좀더 편리하게 개선하기 위해 나온 것이 **JSP**이다. 

## JSP 이용

### new-form
```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
 	<title>Title</title>
</head>
<body>
	<form action="/jsp/members/save.jsp" method="post">
		 username: <input type="text" name="username" />
		 age: <input type="text" name="age" />
		 <button type="submit">전송</button>
	</form>
</body>
</html>
```
- name, age를 입력해 button을 누르면, form 태그를 이용해 /jsp/members/save.jsp로 데이터 전송

### save.jsp
```java
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	 MemberRepository memberRepository = MemberRepository.getInstance();
	 System.out.println("save.jsp");
	 
	 String username 	= request.getParameter("username");
	 int 	age 		= Integer.parseInt(request.getParameter("age"));
	 
	 Member member = new Member(username, age);
	 System.out.println("member = " + member);
	 
	 memberRepository.save(member);
%>
<html>
<head>
 	<meta charset="UTF-8">
</head>
<body>
	성공
	<ul>
		 <li>id=<%=member.getId()%></li>
		 <li>username=<%=member.getUsername()%></li>
		 <li>age=<%=member.getAge()%></li>
	</ul>
	<a href="/index.html">메인</a>
</body>
</html>
```
- <%@ page import="" %>를 이용해 import
- <% %> java 코드 입력
- <%= %> java 코드 출력

### members.jsp
```java
<%@ page import="java.util.List" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	MemberRepository memberRepository = MemberRepository.getInstance();
	List<Member> members = memberRepository.findAll();
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
</head>
<body>
	<a href="/index.html">메인</a>
	<table>
		 <thead>
			 <th>id</th>
			 <th>username</th>
			 <th>age</th>
		 </thead>
		 <tbody>
		<%
			for(Member member: members) {
				out.write(" <tr>");
				out.write(" 	<td>" + member.getId() + "</td>");
				out.write(" 	<td>" + member.getUsername() + "</td>");
				out.write(" 	<td>" + member.getAge() + "</td>");
				out.write(" </tr>");
			}
		%>
		 </tbody>
	</table>
</body>
```

JSP는 Servlet에 비해 많이 편리해졌다.

동적으로 실행하는 부분을 제외하고는 Java와 Html이 분리되어있다.

하지만 여전히 Java코드와 html코드를 섞여있다는 느낌이 강하다.

 

JSP 역할이 너무 크고 현재의 예시는 굉장히 간단한 편이지만.

실무에서는 이렇게 간단한 수준에서 그칠 수 없어 코드가 몇천 몇만줄이 되기도 해서 유지보수가 굉장히 힘들다.

이를 개선하기 위한 것이 MVC패턴으로 Java코드와 html 코드를 완전히 분리하기 시작한다.

## MVC 패턴 이용

예제에 들어가기 앞서 MVC 패턴에 대해 간단히 설명하겠다.

![alt text](image.png)


- 프로젝트 구성 요소를 Model, View, Controller 3가지로 나눈 패턴
	- Model: View에 출력할 데이터 담아두기
	- View: Model에 담긴 데이터를 사용해 화면 그려내기
	- Controller: HTTP 요청을 받아 파라미터 검증 및 비즈니스 로직 실행. View에 전달할 결과 데이터를 Model에 담기

> WEB-INF 폴더

WEB-INF는 외부에서 직접 호출이 불가능하게 하기 위한 일종의 약속이다.

예를 들어, localhost:8080/파일.jsp 는 직접 링크를 입력해서 접근할 수 있다.

하지만 파일.jsp를 WEB-INF 안에 넣는다면?

localhost:8080/WEB-INF/파일.jsp 링크를 입력해도 접근이 불가능하다.

Controller를 이용해 WEB-INF/파일.jsp에는 접근이 가능하지만, 직접 도메인을 입력하면 접근이 불가능하다.

### MvcMemberFormServlet.java

```java
@WebServlet(name = "mvcMemberFormSerlvet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String viewPath = "/WEB-INF/views/new-form.jsp";
		RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
		dispatcher.forward(req, res);
	}
}
```
- /servlet-mvc/members/new-form을 통해 접근 가능
- /WEB-INF/views/new-form.jsp으로 접근
- req.getRequestDispatcher(): 다른 servlet 또는 JSP로 이동 가능
- dispatcher.redirect(): 클라이언트에 응답이 갔다가 redirect 경로로 다시 요청
- dispatcher.forward(): 서버 내부에서 호출하고 끝. 클라이언트가 인지할 수 X

### new-form.jsp
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
 	<title>Title</title>
</head>
<body>
<!-- 	<form action="/servlet-mvc/members/save" method="post"> -->
	<form action="save" method="post">
		username: <input type="text" name="username" />
		age: <input type="text" name="age" />
	 	<button type="submit">전송</button>
	</form>
</body>
</html>
```

### MvcMemberSaveServlet.java
```java
@WebServlet(name = "mvcMemberSaveSerlvet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet{
	
	MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String username = req.getParameter("username");
		int age = Integer.parseInt(req.getParameter("age"));
		
		Member member = new Member(username, age);
		memberRepository.save(member);
		
		// Model에 데이터 보관
		req.setAttribute("member", member);
		
		String viewPath = "/WEB-INF/views/save-result.jsp";	// WEB-INF 이하의 자원들은 외부의 직접 호출 X. 컨트롤러 통해서 호출 O. 
		RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath); // 다른 servlet이나 JSP로 이동 가능
		dispatcher.forward(req, res);	// redirect: 클라이언트가 다시 요청. forward: 서버에서 호출하고 끝. 클라이언트가 인지 x.
	}
}
```

### save-result.jsp
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	<meta charset="UTF-8">
</head>
<body>
	성공
	<ul>
		 <li>id=${member.id}</li> <%-- <%=((Member)request.getAttribute("member")).getId()%> --%>
		 <li>username=${member.username}</li>
		 <li>age=${member.age}</li>
	</ul>
	<a href="/index.html">메인</a>
</body>
</html>
```

### MvcMemberListServlet.java
```java
@WebServlet(name = "mvcMemberListSerlvet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet{
	
	MemberRepository memberRepository = MemberRepository.getInstance();
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		List<Member> members = memberRepository.findAll();
		req.setAttribute("members", members);
		
		String viewPath = "/WEB-INF/views/members.jsp";	
		RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath); 
		dispatcher.forward(req, res);	
	}
}
```

### members.jsp
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
 <meta charset="UTF-8">
 <title>Title</title>
</head>
<body>
	<a href="/index.html">메인</a>
	<table>
		 <thead>
			 <th>id</th>
			 <th>username</th>
			 <th>age</th>
		 </thead>
		 <tbody>
			 <c:forEach var="item" items="${members}"> <!-- 반복문 -->
				 <tr>
					 <td>${item.id}</td>
					 <td>${item.username}</td>
					 <td>${item.age}</td>
				 </tr>
			 </c:forEach>
		 </tbody>
	</table>
</body>
</html>
```

Servlet 또는 JSP만 사용했을 때보다는 **코드가 깔끔하고 직관적**이게 됐다.

다만, 위 작업을 따라하다 보면 비슷한 **행위가 반복**된다는 것이 느껴질 것이다.

특히 Controller마다 RequestDispatcher를 호출하고 보내주는 작업이 반복된다.

이후에 비슷한 기능을 하는 Controller를 **공통으로 처리하기 어렵다.**

여기서 등장한 것이 **프론트 컨트롤러 패턴**이다.

스프링 MVC의 핵심도 이 프론트 컨트롤러 패턴에 있는데,

Controller 호출 전에 공통 기능을 처리할 수 있게 한다.


