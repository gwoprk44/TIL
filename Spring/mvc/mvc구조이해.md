# MVC 구조 이해
# 목차
- [MVC구조 이해](#mvc-구조-이해)
- [목차](#목차)
- [DispatcherServlet 구조](#dispatcherservlet-구조)
    - [요청의 흐름](#요청의-흐름)
- [핸들러 매핑, 핸들러 어댑터](#핸들러-매핑-핸들러-어댑터)
    - [HandlerMapping; 핸들러 매핑](#handlermapping-핸들러-매핑)
    - [HandlerAdapter; 핸들러 어댑터](#handleradapter-핸들러-어댑터)
    - [HttpRequestServlet](#httprequestservlet)
- [뷰 리졸버](#뷰-리졸버)
- [@Controller, @RequestMapping](#controller-requestmapping)

![alt text](/assets/mvc구조.png)

스프링 MVC의 구조와 실행 순서는 위 그림과 같다.

스프링 mvc 역시 프론트 컨트롤러 패턴으로 구현되어있고 스프링 mvc의 프론트 컨트롤러가 바로 디스패쳐 서블릿이다. 이 디스패쳐 서블릿이 스프링 mvc의 핵심이라고 할 수 있다.

1. 핸들러 조회: Handler 매핑을 통해 요청 url에 매핑된 핸들러(컨트롤러) 조회
2. 핸들러 어댑터 조회: 핸들러를 처리할 수 있는 핸들러 어댑터 조회
3. 핸들러 어댑터 실행
4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러 실행
5. ModelAndView 반환: 핸들러 어댑터가 핸들러의 반환 정보를 ModelAndView 형태로 변환해서 반환
6. viewResolver 호출: jsp의 경우 InternalResourceviewResolver가 자동 등록 및 사용됨
7. View 반환: viewResolver가 view의 논리 이름 -> 물리 이름으로 변환 후 렌더링 역할을 하는 View 객체 반환
8. View 렌더링: View를 이용해 렌더링

# DispatcherServlet 구조 
- 요청을 처리하고 적절한 응답을 렌더링 하기위한 특별한 스프링 빈.
- DispatcherServlet -> FrameworkServlet -> HttpServletBean -> HttpServlet 상속받고 있다.
- Spring Boot에서는 DispatcherServlet를 자동으로 urlPattern"/"으로 등록한다.
- urlPattern"/"으로 등록하여도 **더 자세한 경로일수록 우선순위가 높으므로** 상관이 없다.

## 요청의 흐름

1. 서블릿 호출 시 HttpServlet이 제공하는 service() 호출

2. FrameworkServlet에서 오버라이드한 service() 호출
```java
  @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ...
	doDispatch(request, response);
        ...
    }
``` 

3. 2가 실행되며 DispatcherServlet.doDispatch() 호출

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse  response) throws Exception {

  HttpServletRequest processedRequest = request;
  HandlerExecutionChain mappedHandler = null;
  ModelAndView mv = null;
  
  // 1. 핸들러 조회
  mappedHandler = getHandler(processedRequest);
  if (mappedHandler == null) {
    noHandlerFound(processedRequest, response);
    return;
  }
  
  // 2. 핸들러 어댑터 조회 - 핸들러를 처리할 수 있는 어댑터
  HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
  
  // 3. 핸들러 어댑터 실행 -> 4. 핸들러 어댑터를 통해 핸들러 실행 -> 5. ModelAndView 반환
  mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
  processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
}

private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {
  // 뷰 렌더링 호출
  render(mv, request, response);
}

protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
  View view;
  String viewName = mv.getViewName();
  
  // 6. 뷰 리졸버를 통해서 뷰 찾기, 7. View 반환
  view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
  
  // 8. 뷰 렌더링
  view.render(mv.getModelInternal(), request, response);
}
```

# 핸들러 매핑, 핸들러 어댑터

컨트롤러 인터페이스를 구현한 컨트롤러가 실행되려면 HandlerMapping, HandlerAdapter 두 가지가 필요하다.

## HandlerMapping; 핸들러 매핑

- 핸들러 매핑에서 특정 컨트롤러를 찾을 수 있어야한다.
- 예를 들어 Spring Bean의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요하다.

```java
0 = RequestMappingHandlerMapping // 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
1 = BeanNameUrlHandlerMapping    // 스프링 빈의 이름으로 핸들러를 찾기
```

## HandlerAdapter; 핸들러 어댑터

- 핸들러 매핑을 통해 찾은 핸들러를 실행하는 핸들러 어댑터가 필요.
- 예를 들어 컨트롤러 인터페이스를 찾고 실행할 수 있는 핸들러 어댑터를 찾고 실행하여야한다.

```java
0 = RequestMappingHandlerAdapter   // 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
1 = HttpRequestHandlerAdapter      // HttpRequestHandler 처리
2 = SimpleControllerHandlerAdapter // Controller 인터페이스 처리
```

```java
@Component("/springmvc/old-controller")
public class OldController implements Controller{
	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("OldController.handleRequest");
		return null;
	}
}
```
- `@Component`: 해당 컨트롤러를 /springmvc/old-controller 이름의 스프링 빈으로 등록한다.
- 빈의 이름으로 url을 매핑.

### 실행 순서
1. /springmvc/old-controller 경로로 접근
2. Handler Mapping으로 Handler 조회
    - HandlerMapping을 순서대로 실행해 핸들러 찾기
    - 스프링 bean 이름 그대로 찾아주는 핸들러인 BeanNameUrlHandlerMapping 실행 -> Handler인 OldController 반환
3. Handler Adapter 조회
    - HandlerAdapter의 supports() 순서대로 호출
    - SimpleControllerHandlerAdapter가 Controller 인터페이스 지원하므로 선택됨
4. Handler Adapter 실행
    - DispatcherServlet이 조회한 SimpleControllerHandlerAdapter를 실행하며 Handler 정보도 함께 넘김
    - SimpleControllerHandlerAdapter는 Handler인 OldController를 내부에서 실행 - > 결과 반환

# HttpRequestServlet
- Servlet와 가장 유사한 형태의 핸들러
```java
@Component("/springmvc/request-handler")
public class MyHttpRequestHandler implements HttpRequestHandler{
	@Override
	public void handleRequest (HttpServletRequest req, HttpServletResponse res) throws ServletException {
		System.out.println("MyHttpRequestHandler.handleRequest");
	}
}
```
### 실행 순서
1. /springmvc/request-handler 경로로 접근
2. Handler Mapping으로 Handler 조회
    - HandlerMapping을 순서대로 실행해 핸들러 찾기
    - 스프링 bean 이름 그대로 찾아주는 핸들러인 BeanNameUrlHandlerMapping 실행 -> Handler인 MyHttpRequestHandler 반환
3. Handler Adapter 조회
    - HandlerAdapter의 supports() 순서대로 호출
    - HttpRequestHandlerAdapter가 HttpRequestHandler 인터페이스 지원하므로 선택됨
4. Handler Adapter 실행
    - DispatcherServlet이 조회한 HttpRequestHandlerAdapter를 실행하며 Handler 정보도 함께 넘김
    - HttpRequestHandlerAdapter는 Handler인 MyHttpRequestHandler를 내부에서 실행 - > 결과 반환

# 뷰 리졸버

### OldController
```java
@Component("/springmvc/old-controller")
public class OldController implements Controller{
	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("OldController.handleRequest");
		return new ModelAndView("new-form");
	}
}
```
- jsp 파일의 논리 이름을 반환하도록 수정.

```
application.properties
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```
- Spring boot는 InternalResourceViewResolver라는 View resolver를 자동으로 등록한다.
- 위 설정 정보를 사용한다.

### 동작 과정

1. 핸들러 어댑터 호출               // "new-form"이라는 논리 뷰 이름 획득
2. ViewResolver 호출                // InternalResourceViewResolver 호출
3. InternalResourceViewResolver // return InternalResourceView
4. InternalResourceView            // JSP처럼 forward()를 호출해 처리할 수 있는 경우에 사용
5. view.render()                       // view.render() 호출 -> InternalResourceView는 forward() 사용해 JSP 실행

- InternalResourceViewResolver는 JSTL 라이브러리가 있으면 JstlView를 반환한다. (기능 추가됨)
- forward()를 이용하는건 JSP뿐. 다른 뷰들은 바로 렌더링 가능하다.

# @Controller, @RequestMapping

컨트롤러를 작성할 때 가장 중요한 어노테이션이 바로 @Controller, @RequestMapping 이다.

이 두 어노테이션에 대해 설명하고 코드를 통해 사용법을 알아 볼 것이다.

## @Controller
- 스프링이 자동으로 스프링 빈으로 등록한다.
- 스프링 mvc에서 애노테이션 기반 컨트롤러로 인식한다.

## @RequestMapping
- 요청 정보를 매핑한다.
- 해당 url이 호출되면 이 메서드가 호출된다.
- 애노테이션을 기반으로 동작하기 때문에 메서드 이름은 상관이없다.

 핸들러 매핑과 핸들러 어댑터 중에서 `RequestMappingHandlerMapping ,RequestMappingHandlerAdapter`가 가장 우선순위가 높다. 실무의 대부분에서는 이를 사용한다.

### SpringMemberFormControllerV1
```java
@Controller	
public class SpringMemberFormControllerV1 {
    @RequestMapping("/springmvc/v1/members/new-form")	
    private ModelAndView process() {
        return new ModelAndView("new-form");
    }
}
```
- ModelAndView: 모델, 뷰 정보 담아서 반환

### SpringMemberSaveControllerV1
```java
@Controller
public class SpringMemberSaveControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
	
    @RequestMapping("/springmvc/v1/members/save")
    private ModelAndView process(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");
        int age = Integer.parseInt(req.getParameter("age"));
		
        Member member = new Member(username, age);
        memberRepository.save(member);
		
        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }
}
```
- addObject(): 스프링이 제공하는 ModelAndView를 이용해 데이터 추가시 해당 메소드를 사용한다.
- mv는 이후 뷰를 렌더링 할 때 사용한다.

### SpringMemberListControllerV1
```java
@Controller
public class SpringMemberListControllerV1 {
    MemberRepository memberRepository = MemberRepository.getInstance();
	
    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {
        List<Member> members = memberRepository.findAll();
		
        ModelAndView mv = new ModelAndView("members");
        mv.getModel().put("members", members);
		
        return mv;
    }
}
``` 

코드를 짧게 줄였지만 여전히 불편한점이 남아있다. 그렇기 때문에 컨트롤러를 하나의 클래스로 통합하고

ModelAndView를 개선하여 실무에서 사용하는 코드와 비슷하게 리팩토링 할 것이다.

 

### SpringMemberControllerV3
```java
@Controller	
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();
	
    @RequestMapping("/new-form")
    private String newForm() {
        return "new-form";
    }
	
    @RequestMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members",members);
        return "members";
    }
	
    @RequestMapping("/save")
    private String save(@RequestParam("username") String username, @RequestParam("age") int age, Model model) {
        Member member = new Member(username, age);
        memberRepository.save(member);
		
        model.addAttribute("member",member);
        return "save-result";
    }
}
```
- String 형태로 변환하여 더 이상 모델앤뷰를 매번 생성하지 않아도 된다.
- @RequestParam: 파라미터를 직접 받아올 수 있다.

위 코드는 GET, POST 방식 구분 없이 모두 적용되고 있기 때문에 실제 사용하는 코드처럼 만들기 위해서는

`@RequestMapping(value = "/save", method = Request.POST(GET))`과 같이 매핑부분을 수정하여 GET, POST방식을 적용할 수 있다.

 
여기서 더 간단하게 사용하기 위해서

`@PostMapping("/save"), @GetMapping("/~")`
 

와 같이 코드를 수정할 수 있다.


