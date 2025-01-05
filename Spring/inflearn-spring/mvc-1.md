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