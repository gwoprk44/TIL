# 목차

- [목차](#목차)
- [1장 오브젝트와 의존관계](#1장오브젝트와의존관계)
    - [관심사의 분리](#관심사의분리)
    - [중복 코드의 분리](#중복코드의분리)
    - [상속을 통한 확장](#상속을통한확장)
        - [문제점](##문제점)
    - [DAO의 확장](#DAO의확장)
        - [계방 폐쇄의 원칙](##계방폐쇄의원칙)
        - [전략 패턴](##전략패턴)
        - [IoC](##IoC란=InversionofControl=제어의역전)
# 1장 오브젝트와 의존관계

# 관심사의 분리
요구사항은 끊임없이 변경되고 발전한다. 모든 관심사들이 한 곳에 응집되어 있다면 해당 오브젝트는 끊임없이 변화해야 한다. 추가로 해당 오브젝트를 의존하고 있는 다른 오브젝트에까지 영향이 갈 수 있다. 이 변화의 폭을 최소화하기 위해서 관심사를 최대한 분리해야 한다.

# 중복 코드의 분리
```JAVA
public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3307/spring?characterEncoding=UTF-8", "root",
                "12345678");
        PreparedStatement ps = c
                .prepareStatement("select * from users where id = ?");

        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }
```
add() 메소드와 get()메소드에 DB커넥션을 가져오는 코드가 중복되어 있듯이 이러한 코드가 많아 지면 오브젝트가 변화할때 다른 오브젝트에도 영향을 미칠수 있으므로 이러한 중복코드의 메소드를 추출하여 독립적인 메소드를 만들어 줄 필요가 있다. 이러한 수정을 통하여 다른 메소드에 대한 영향력을 감소시키고 코드의 수정도 간단해진다.

# 상속을 통한 확장

기존 UserDao 코드를 한 단계 더 분리한다. 우리가 만든 UserDao에서 메소드의 구현 코드를 제거하고 getConnection()을 추상 메소드로 만들어놓는다.

이제 이 추상 클래스인 UserDao를 N 사와 K 사에 판매한다. UserDao를 구입한 포탈사들은 UserDao 클래스를 상속해서 각각 NUserDao와 DUserDao라는 서브클래스를 만든다.

기존에는 같은 클래스에 다른 메소드로 분리됐던 DB 커넥션 연결이라는 관심을 이번에는 상속을 통해 서브클래스로 분리해버렸다.
→ 클래스 계층구조를 통해 두 개의 관심이 독립적으로 분리되면서 변경 작업은 한층 용이해짐

이렇게 슈퍼클래스에 기본적인 로직의 흐름(커넥션 가져오기, SQL 생성, 실행, 반환)을 만들고, 그 기능의 일부를 추상 메소드나 오버라이딩이 가능한 protected 메소드 등으로 만든 뒤 서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법을 디자인 패턴에서 템플릿 메소드 패턴이라고 한다.

UserDao의 getConnection() 메소드는 Connection 타입 오브젝트를 생성한다는 기능을 정의해놓은 추상 메소드다. UserDao의 서브클래스의 getConnection() 메소드는 어떤 Connection 클래스의 오브젝트를 어떻게 생성할 것인지를 결정하는 방법이라고도 볼 수 있다. 이렇게 서브클래스에서 구체적인 오브젝트 생성 방법을 결정하게 하는 것을 팩토리 메소드 패턴이라고 부르기도 한다.

UserDao는 어떤 기능을 사용한다는 데에만 관심이 있고, NUserDao와 KUserDao에서는 어떤 식으로 Connection 기능을 제공하는지에 관심을 두고 있는 것이다. 또 어떤 방법으로 Connection 오브젝트를 만들어내는지도 NUserDao와 DUserDao의 관심사항이다.

UserDao는 Connection 오브젝트가 만들어지는 방법과 내부 동작 방식에는 상관없이 자신이 필요한 기능을 Connection 인터페이스를 통해 사용하기만 할 뿐이다.
```JAVA
public abstract class Super{
	// 기본 알고리즘 골격을 담은 메소드를 템플릿 메소드라 부른다.
	// 템플릿 메소드는 서브클래스에서 오버라이드하거나 구현할 메소드를 사용한다.
	public void templateMethod(){
		// 기본 알고리즘 코드
		hookMethod();
		abstractMethod();
	}
	
	// 선택적으로 오버라이드 가능한 훅 메소드
	protected void hookMethod(){}		
	public abstract void abstractMethod();
}

public class Sub1 extends Super{
	protected void hookMethod(){
	}
	public void abstractMethod(){
	}

}
```

## 문제점

1. 상속을 통해 관심이 다른 기능을 분리하고, 필요에 따라 다양한 변신이 가능하도록 확장성도 줬지만 여전히 상속관계는 두 가지 다른 관심사에 대해 허용한다. 서브클래스는 슈퍼클래스의 기능을 직접 사용할 수 있다. 그래서 슈퍼클래스 내부의 변경이 있을 때 모든 서브클래스를 함께 수정하거나 다시 개발해야 할 수도 있다. 반대로 그런 변화에 따른 불편을 주지 않기 위해 슈퍼클래스가 더 이상 변화하지 않도록 제약을 가해야 할지도 모른다.

2. 확장된 기능인 DB 커넥션을 생성하는 코드를 다른 DAO 클래스에 적용할 수 없다는 것도 큰 단점이다. 만약 UserDao 외의 DAO 클래스들이 계속 만들어진다면 그때는 상속을 통해서 만들어진 getConnection()의 구현 코드가 매 DAO 클래스마다 중복돼서 나타나는 심각한 문제가 발생할 것이다.

# DAO의확장

## 개방 폐쇄의 원칙

클래스나 모듈은 확장에 대해서는 열려 있고 변경에 대해서는 닫혀 있어야한다는 의미이다. 기능을 변경하거나 확장할 수 있으면서도 그 기능을 사용하는 코드에서는 수정되지 않는다. 예를 들면 UserDao에서는 ConnectMaker라는 인터페이스를 이용해 DB 연결과 관련된 로직을 수행하고 있다. ConnectMaker를 통해 DB 연결 방법은 언제든지 바뀔 수 있다. 하지만 ConnectMaker의 변경으로 인해 UserDao의 로직은 변경될 필요가 없다. 이 경우 개방 폐쇄의 원칙을 따른다고 표현할 수 있다.

## 전략 패턴

인터페이스를 통해 로직을 외부에 분리시키고 이를 구현한 클래스를 필요에 따라 바꿔 사용할 수 있게 하는 디자인 패턴. ConnectMaker라는 인터페이스를 DB 종류에 따라 MySqlConnectMaker, H2SConnectMaker, MongoDbConnectMaker 등으로 구현체를 생성하고 이를 사용하는 UserDao에서는 필요한 구현체를 사용하면 된다.

## IoC란 = Inversion of Control = 제어의 역전

자신이 사용할 오브젝트를 선택하는 책임을 다른 대상에게 위임하는 것이다.

ex) 프레임워크: 라이브러리는 애플리케이션 코드가 능동적으로 필요할 때에 라이브러리의 기능을 사용한다. 반면 프레임워크는 애플리케이션 코드가 프레임워크에 의해 사용된다. 프레임워크가 짜놓은 틀에서 수동적으로 동작하는 것이다.