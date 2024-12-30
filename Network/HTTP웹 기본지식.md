# HTTP웹 기본지식

# 목차
- [HTTP웹 기본지식](#http웹-기본지식)
- [목차](#목차)
- [인터넷 네트워크](#인터넷-네트워크)
    - [인터넷통신](#인터넷통신)
    - [IP(인터넷 프로토콜)](#ip인터넷-프로토콜)
    - [TCP와 UDP](#tcp와-udp)
    - [PORT](#dns)
    - [DNS](#port)
- [URI와 웹 브라우저 요청 흐름](#uri와-웹-브라우저-요청-흐름)
    - [URI](#uri)
    - [웹 브라우저 요청 흐름](#웹-브라우저-요청-흐름)
- [HTTP]
    - [모든것이 HTTP](#모든것이-http)
    - [클라이언트 서버 구조](#클라이언트-서버-구조)
    - [Stateful, Stateless](#stateful-stateless)
    - [비 연결성](#비-연결성)
    - [HTTP 메시지](#http-메시지)

# 인터넷 네트워크

## 인터넷통신

Http를 위해 네트워크 기본 지식 필요
클라이언트와 서버가 바로 옆에 붙어있으면 상관없는데, 중간에 인터넷망이 있어 데이터를 안전하게 전송하기 위한 방법을 생각해야 한다.

## IP(인터넷 프로토콜)

클라이언트와 서버에 IP 주소를 부여

지정한 IP 주소(IP Address)에 데이터 전달
패킷(Packet)이라는 통신 단위로 데이터 전달
IP패킷 정보: 출발지 IP, 목적지IP, 전송 데이터, 기타...
클라이언트와 서버 사이에 있는 인터넷 망은 수많은 노드로 구성되어 있다.
그중에서 목적지IP로 갈 수 있는 노드들을 통해 데이터가 전송된다.

클라이언트와 서버의 패킷 전달 경로는 서로 다를 수 있다.

> IP 프로토콜의 한계

비연결성 : 패킷을 받을 대상이 없거나 서비스 불능 상태여도 패킷 전송
클라이언트는 대상 서버가 패킷을 받을 수 있는 상태인지 모른다. 그리고 서버가 서비스 불능상태여도 일단 패킷을 전송하고 사라진다.

비신뢰성 : 중간에 패킷이 사라지면? 패킷이 순서대로 안오면?
중간에 노드가 꺼지거나 문제가 생기면 패킷이 소실된다.

프로그램 구분 : 같은 IP를 사용하는 서버에서 통신하는 애플리케이션이 둘 이상이면?
패킷의 용량이 크면 여러패킷으로 끊어서 보내는데 (문자의 경우 1500바이트 정도), 최단노드를 찾아서 보내므로 순서가 바껴서 도착할수도 있다.
예) 클라이언트 1: Hello, 2: world! -> 서버 1: world!, 2: Hello

이 문제들을 TCP가 해결해 준다.

## TCP와 UDP

인터넷 프로토콜 스택의 4계층
애플리케이션 계층 - HTTP, FTP
전송 계층 - TCP, UDP
인터넷 계층 - IP
네트워크 인터페이스 계층

예) 채팅프로그램 사용시
1. 프로그램이 Hello world! 메시지 생성
2. SOCKET 라이브러리를 통해 전달
3. TCP 정보 생성, 메시지 데이터 포함
4. IP 패킷 생성, TCP 데이터 포함
5. LAN카드로 서버에 보냄(Ethernet frame)

TCP/IP 패킷 정보: IP 패킷 정보 안에 TCP 패킷 정보가 있다.
TCP 세그먼트: 출발지 port, 목적지 port, 전송 제어, 순서, 검증 정보

> TCP 특징

전송 제어 프로토콜 (Transmission Control Protocol)

연결지향 - TCP 3way handshake(가상 연결): 진짜 연결이 된게 아니고 개념적으로만 연결됨

데이터 전달 보증: 클라이언트가 서버로 데이터 전송을 하면 서버는 클라이언트로 데이터 잘 받았다고 보낸다.

순서 보장: 순서 안맞으면 다시보내라고 요청보냄
신뢰할 수 있는 프로토콜

현재는 대부분 TCP 사용

TCP 3way handshake
1. 클라이언트에서 서버로 SYN 보냄
2. 서버에서 클라이언트로 SYN+ACK 보냄
3. 클라이언트에서 서버로 ACK 보냄
4. 클라이언트에서 서버로 데이터 전송

1,2,3: connect 과정

SYN: 접속 요청, ACK: 요청 수락

> UDP 특징

사용자 데이터그램 프로토콜 (User Datagram Protocol)

하얀 도화지에 비유(기능이 거의 없음)

연결지향 - TCP 3 way handshake X
데이터 전달 보증 X
순서 보장 X

데이터 전달 및 순서가 보장되지 않지만, 단순하고 빠름


IP와 거의 같다. PORT, 체크섬 정도만 추가
애플리케이션에서 추가 작업 필요

port: 하나의 IP에서 여러 애플리케이션을 사용할때(게임, 음악 등등) 어떤게 게임용이고 어떤게 음악용 패킷인지 구분하는 역할

체크섬: 이 메세지에 대해 맞는지 검증해주는 데이터 정도 추가되어있음

TCP는 좋지만 전송속도를 빠르게할 수 없고 데이터 양도 많아진다. 그리고 TCP는 손을 못대기 때문에 UDP위에 내가원하는것을 만들 수 있다. http3에서 udp protocol 사용.

## PORT


클라이언트에서 한번에 둘 이상의 서버와 연결 할때 구분하기 위해 사용한다.
TCP/IP 패킷 정보에는 출발지 port와 목적지 port가 포함되어있다.

예) 클라이언트 100.100.100.1 게임(8090), 화상통화(21000), 웹브라우저 요청(10010)
서버 200.200.200.2 게임(11220), 화상통화 (32202), 서버 200.200.200.3 웹 브라우저 요청(80)

100.100.100.1 게임(8090)- 200.200.200.2 게임(11220)
100.100.100.1 화상통화(21000)- 200.200.200.2 화상통화(32202)
100.100.100.1 웹브라우저 요청(10010)- 200.200.200.3 웹브라우저 요청(80)

0~65535 할당 가능

0~1023 잘 알려진 포트, 사용하지 않는 것이 좋음

FTP - 20,21

TELNET - 23

HTTP - 80

HTTPS - 443

## DNS

IP는 기억하기 어렵다. IP: 100.100.100.1

IP는 변경될 수 있다. -> IP: 100.100.100.2

DNS
도메인 네임 시스템 (Domain Name System)

전화번호부
도메인 명을 IP주소로 변환

# URI와 웹 브라우저 요청 흐름

## URI

resource를 식별하는 통합된 방법을 의미한다.

URL는 locator, name 또는 둘 다 추가로 분류가 가능하다.
URI 안에 URL, URN이 존재.

URI 단어 뜻

- Uniform : 리소스 식별하는 통일된 방식
- Resource: 자원, URI로 식별할 수 있는 모든 것(제한없음)
- Identifier: 다른 항목과 구분하는데 필요한 정보

URL, URN 단어 뜻

- URL - Locator : 리소스가 있는 위치를 지정
- URN - Name : 리소스에 이름을 부여
- 위치는 변할 수 있지만, 이름은 변하지 않는다.
- urn:isbn:8960777331 (어떤 책의 isbn URN)
- URN 이름만으로 실제 리소스를 찾을 수 있는 방법이 보편화 되지 않음

URL 분석

`https://www.google.com/search?q=hello&hl=ko`

전체문법

scheme://[userinfo@]host[:port][/path][?query][#fragment]

`https://www.google.com:443/search?q=hello&hl=ko`

- 프로토콜(https)
- 호스트명(www.google.com)
- 포트 번호 (443)
- 패스(/search)
- 쿼리 파라미터(q=hello&hl=ko)

scheme

**scheme**://[userinfo@]host[:port][/path][?query][#fragment]

`https://www.google.com:443/search?q=hello&hl=ko`

- 주로 프로토콜 사용
- 프로토콜: 어떤 방식으로 자원에 접근할 것인가 하는 약속 규칙
    예) http, https, ftp 등등
- http는 80포트, https는 443 포트를 주로 사용, 포트는 생략 가능
- https는 http에 보안 추가 (HTTP Secure)

userinfo

scheme://[**userinfo@**]host[:port][/path][?query][#fragment]

htps://www.google.com:443/search?q=hello&hl=ko

- URL에 사용자정보를 포함해서 인증
- 거의 사용하지 않음

host

scheme://[userinfo@]**host**[:port][/path][?query][#fragment]
htps://**www.google.com**:443/search?q=hello&hl=ko

- 호스트명
- 도메인명 또는 IP주소를 직접 사용가능

port

scheme://[userinfo@]host[**:port**][/path][?query][#fragment]
htps://www.google.com:**443**/search?q=hello&hl=ko

- 포트(PORT)
- 접속 포트
- 일반적으로 생략, 생략시 http는 80, https는 443

path

scheme://[userinfo@]host[:port][**/path**][?query][#fragment]

htps://www.google.com:443/**search**?q=hello&hl=ko

- 리소스 경로(path), 계층적 구조
예) /home/file1.jpg, /members, /members/100, /items/iphone12

query

scheme://[userinfo@]host[:port][/path][**?query**][#fragment]

htps://www.google.com:443/search**?q=hello&hl=ko**

- key=value 형태
- ?로 시작, &로 추가 가능 ?keyA=valueA&keyB=valueB
- query parameter, query string 등으로 불림, 웹서버에 제공하는 파라미터, 문자 형태

fragment

scheme://[userinfo@]host[:port][/path][?query][**#fragment**]

`https://docs.spring.io/spring-boot/docs/current/reference/html/
getting-started.html#getting-started-introducing-spring-boot`

- fragment
- html 내부 북마크 등에 사용
- 서버에 전송하는 정보 아님

## 웹 브라우저 요청 흐름

`https://www.google.com:443/search?q=hello&hl=ko`

DNS조회, HTTPS PORT 생략, 443

웹브라우저에서 http 요청 메시지 생성한다

```HTTP 요청 메시지
GET /search?q=hello&hl=ko HTTP/1.1
Host: www.google.com
```

1. 웹브라우저가 http 메시지 생성
2. SOCKET 라이브러리를 통해 전달
- A:TCP/IP 연결(IP,PORT)
- B:데이터 전달
3. TCP/IP 패킷 생성, http 메시지 데이터 포함
4. LAN카드로 인터넷을 통해 http 메시지를 서버에 전송

구글 서버에서는 TCP/IP 패킷은 까서 버리고 http 메시지만 해석을 한다.

```
HTTP 응답 메시지

HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Length: 3423

<html>
	<body>...</body>
</html>
```

클라이언트의 웹 브라우저가 http 응답 메시지를 렌더링 해서 결과를 볼 수 있다.

# HTTP

## 모든것이 HTTP

HTTP (Hyper Text Transfer Protocol)

html을 전송하는 프로토콜로 처음 시작됐는데 지금은 모든 것을 전송할 수 있다.

- HTML, TEXT
- IMAGE, 음성, 영상, 파일
- JSON, XML (API)
- 거의 모든 형태의 데이터 전송 가능
- 서버간에 데이터를 주고 받을 때도 대부분 HTTP 사용

HTTP 역사

- HTTP/0.9 1991년: GET 메서드만 지원, HTTP 헤더X
- HTTP/1.0 1996년: 메서드, 헤더 추가
- HTTP/1.1 1997년: 가장 많이 사용, 우리에게 가장 중요한 버전
- RFC2068 (1997) -> RFC2616 (1999) -> RFC7230~7235 (2014)
- HTTP/2 2015년: 성능 개선
- HTTP/3 진행중: TCP 대신에 UDP 사용, 성능 개선

기반 프로토콜

- TCP: HTTP/1.1, HTTP/2
- UDP: HTTP/3
- 현재 HTTP/1.1 주로 사용
- HTTP/2, HTTP/3도 점점 증가

HTTP 특징

- 클라이언트 서버 구조로 동작한다
- 무상태 프로토콜(Stateless) 지향, 비연결성
- HTTP 메시지를 통해서 통신한다
- 단순하고 확장 가능하다

## 클라이언트 서버 구조

- Request Response 구조이다
- 클라이언트는 서버에 요청을 보내고, 응답을 대기한다
- 서버가 요청에 대한 결과를 만들어서 응답한다
- 응답 결과를 열어서 클라이언트가 동작한다

옛날에는 클라이언트와 서버가 통합되어 있었는데, 지금은 분리해서 비즈니스로직과 데이터 등은 서버에 밀어넣고 클라이언트는 ui,사용성 등에 집중한다. 그러면 클라이언트와 서버가 각각 독립적으로 진화할 수 있다.

## Stateful, Stateless

무상태 프로토콜(stateless)

- 서버가 클라이언트의 상태를 보존X
- 장점: 서버 확장성 높음 (스케일 아웃)
- 단점: 클라이언트가 추가 데이터 전송

상태 유지 - Stateful

고객: 이 노트북 얼마인가요?

점원: 100만원 입니다. (노트북 상태 유지)

고객: 2개 구매하겠습니다.

점원: 200만원 입니다. 신용카드, 현금 중에 어떤 걸로 구매 하시겠어요? (노트북, 
2개 상태 유지)

고객: 신용카드로 구매하겠습니다.

점원: 200만원 결제 완료되었습니다. (노트북, 2개, 신용카드 상태 유지)

무상태 - Stateless

고객: 이 노트북 얼마인가요?

점원: 100만원 입니다.

고객: 노트북 2개 구매하겠습니다.

점원: 노트북 2개는 200만원 입니다. 신용카드, 현금중에 어떤 걸로 구매 하시겠어요?

고객: 노트북 2개를 신용카드로 구매하겠습니다.

점원: 200만원 결제 완료되었습니다.

Stateful, Stateless 차이

정리

- 상태 유지: 중간에 다른 점원으로 바뀌면 안된다. -> 중간에 서버 장애나면 클라이언트 일을 처음부터 다시 해야한다.
(중간에 다른 점원으로 바뀔 때 상태 정보를 다른 점원에게 미리 알려줘야 한다.)
- 무상태: 중간에 다른 점원으로 바뀌어도 된다.
    - 갑자기 고객이 증가해도 점원을 대거 투입할 수 있다.
    - 갑자기 클라이언트 요청이 증가해도 서버를 대거 투입할 수 있다. (스케일아웃)
무상태는 응답 서버를 쉽게 바꿀 수 있다. -> 무한한 서버 증설 가능

Stateless

실무한계

- 모든 것을 무상태로 설계할 수 있는 경우도 있고 없는 경우도 있다
- 무상태 예) 로그인이 필요 없는 단순한 서비스 소개 화면
- 상태 유지 예) 로그인
- 로그인한 사용자의 경우 로그인 했다는 상태를 서버에 유지
- 일반적으로 브라우저 쿠키와 서버 세션 등을 사용해서 상태 유지
- 상태 유지는 최소한만 사용

## 비 연결성

클라이언트와 서버는 요청을 주고 받을때만 연결을 유지해서 최소한의 자원을 사용한다.

- HTTP는 기본이 연결을 유지하지 않는 모델
- 일반적으로 초 단위 이하의 빠른 속도로 응답
- 1시간 동안 수천명이 서비스를 사용해도 실제 서버에서 동시에 처리하는 요청은 수십개 이하로 매우 작음

    예) 웹 브라우저에서 계속 연속해서 검색 버튼을 누르지는 않는다.
- 서버 자원을 매우 효율적으로 사용할 수 있음

한계와 극복

- TCP/IP 연결을 새로 맺어야 함 - 3 way handshake 시간 추가
- 웹 브라우저로 사이트를 요청하면 HTML뿐만 아니라 자바스크립트, css, 추가 이미지 등 수 많은 자원이 함께 다운로드
- 지금은 HTTP 지속 연결 (Persistent Connections)로 문제 해결
- HTTP/2, HTTP/3에서 더 많은 최적화

![connection](C:\Users\gwoprk\Desktop\박건우\TIL\assets\connection.png)

Stateless를 기억하자

서버개발자들이 어려워하는 업무

- 정말 같은 시간에 딱 맞추어 발생하는 대용량 트래픽
- 예) 선착순 이벤트, 명절 KTX 예약, 학과 수업 등록
- 예) 저녁 6:00 선착순 1000명 치킨 할인 이벤트 -> 수만명 동시 요청

## HTTP 메시지

![http메시지](C:\Users\gwoprk\Desktop\박건우\TIL\assets\http메시지.png)

시작라인

요청 메시지

- tart-line = request-line / status-line

- request-line = method SP(공백) request-target(PATH=요청하는대상) SP HTTP-version CRLF (엔터)

- HTTP 메서드 (GET:조회)

    종류: GET, POST, PUT, DELETE...

    서버가 수행해야 할 동작 지정

    GET: 리소스 조회
    
    POST: 요청 내역 처리

- 요청 대상 (/search?q=hello&hl=ko)

    absolute-path[?query] 절대경로[?쿼리]

    절대경로 = "/"로 시작하는 경로
    
    참고: *, http://...?x=y 와 같이 다른 유형의 경로지정 방법도 있다.

- HTTP version

응답 메시지

- start-line = request-line / status-line

- status-line = HTTP-version SP status-code SP reason-phrase CRLF

- HTTP 버전
    - HTTP 상태 코드: 요청 성공, 실패를 나타냄
    
    200: 성공
    
    400: 클라이언트 요청 오류
    
    500: 서버 내부 오류

- 이유 문구: 사람이 이해할 수 이쓴 짧은 상태 코드 설명 글

HTTP 헤더

header-field = field-name ":" OWS field-value OWS (OWS:띄어쓰기 허용)

field-name은 대소문자 구분 없음

HTTP 요청 메시지

GET /search?q=hello&hl=ko HTTP/1.1

Host: www.google.com

HTTP 응답 메시지

HTTP/1.1 200 OK

Content-Type: text/html;charset=UTF-8

Content-Length: 3423

...

용도

- HTTP 전송에 필요한 모든 부가정보
- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트(브라우저) 정보, 서버 애플리케이션 정보, 캐시 관리 정보...
- 표준 헤더가 너무 많음
- 필요시 임의의 헤더 추가 가능
- 예) heeloworld: hihi

HTTP 메시지 바디

용도

- 실제 전송할 데이터
- HTML문서, 이미지, 영상, JSON 등등 byte로 표현할 수 있는 모든 데이터 전송 가능



