
# SQLException이란?

- 데이터베이스 접근 또는 다른 에러에 대한 정보를 제공하는 예외
- Checked Exception


## SQLException, 사라지다?
 유니크한 값인 ID가 중복되어서 SQLException이 발생하는 경우 어떻게 복구해야 할까? 유저가 입력했던 ID에 난수를 더해 insert 시키는 방법이라던가 여러 방법을 사용할 수 있을 것이다. 하지만 가장 일반적인 방법은 RuntimeException을 발생시키고 입력을 다시 요청하는 것이다.

 중요한 포인트는 Exception을 발생시킬 때 어떤 예외가 발생했는지 정보를 전달해줘야 한다는 것이다. Checked Exception을 만나면 더 구체적인 Unchecked Exception을 발생시켜 정확한 정보를 전달하고 로직의 흐름을 끊는 것이 좋다. Spring이나 JPA 등에서 SQLException을 처리하지 않는 이유도 적절한 RuntimeException으로 던져주고 있기 때문이다.

 
Spring의 JdbcTemplate 안에서는 모든 SQLException을 DataAccessException으로 던진다.

호출하는 클래스에서는 필요할 때 DataAccessException을 잡아서 처리하면 된다.

 
```java
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    // ...
    
    @Override
    @Nullable
    public <T> T execute(ConnectionCallback<T> action) throws DataAccessException {
        // ...
        try {
            // ...
        }
        catch (SQLException ex) {
            // ...
            throw translateException("ConnectionCallback", sql, ex);
        }
        finally {
            // ...
        }
    }
    
    protected DataAccessException translateException(String task, @Nullable String sql, SQLException ex) {
        DataAccessException dae = getExceptionTranslator().translate(task, sql, ex);
        return (dae != null ? dae : new UncategorizedSQLException(task, sql, ex));
    }
}
```

## Checked Exception 처리하기


예외를 처리하는 방법에 대해서는 크게 3가지로 나뉜다.

예외 복구: 다른 작업 흐름으로 유도
예외처리 회피: throw를 통해 호출한 쪽에서 예외 처리하도록
예외 전환: 명확한 의미의 예외로 전환하여 throw
 

나는 JdbcTemplate처럼 예외 전환을 해보려고 한다.

PieceDao에는 아래와 같은 메서드가 존재한다.

```java
public void delete(String position) {
    String sql = "delete from piece where coordinate = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, position);
        preparedStatement.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
``` 

SQLException이 발생하면 콘솔에 에러 상황을 프린트만 해두고 별도로 처리하지 않는다.

해당 메서드를 호출하는 곳에서 쿼리문이 잘 실행됐는지 안됐는지 잘 모른다.

 
```java
public class DataAccessException extends RuntimeException {

    private final static String MESSAGE = "쿼리가 정상적으로 실행되지 않았습니다.";

    public DataAccessException() {
        super(MESSAGE);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
public void delete(String position) {
    String sql = "delete from piece where coordinate = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, position);
        preparedStatement.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
        throw new DataAccessException();
    }
}
```

RuntimeException을 상속받은 Custom Exception을 생성했다.

SQLException 발생하는 경우 해당 Exception으로 throw를 해주면 이제 delete()를 호출하는 곳에서도 SQL이 잘 실행됐는지에 대한 여부를 확인할 수 있다.

잘 처리가 되지 않는 경우 특정 메시지를 출력한다던가 별도의 처리 로직을 추가할 수도 있을 것이다.

 

그냥 try-catch로 안감싸면 되는거 아니냐? 하는 의견이 있을 수 있다.

 
```java
public void delete(String position) throws SQLException {
    String sql = "delete from piece where coordinate = ?";

    PreparedStatement preparedStatement = connection.prepareStatement(sql);
    preparedStatement.setString(1, position);
    preparedStatement.executeUpdate();
}
```

delete를 호출한 적 있는 메서드와 그 메서드를 호출하는 메서드들 전부 throws SQLException을 처리해야 한다.

throws를 해주지 않는다면 실행도 전에 컴파일 단계에서 빨간줄이 그어질 것이다.

 

## SQLException이 Checked인 이유

 Checked Exception은 컴파일러가 check를 하기 때문에 붙여진 이름이다. 예측 가능하지만, 예방할 수 없는 경우에 사용한다. 
 
 SQLException같은 경우에는 DB 서버가 꺼져있어서 등의 사유로 오류 발생 가능하다. 
 
 하지만 이걸 Java 코드 쪽에서 예방하기는 어렵다. 이에 대한 처리가 반드시 필요하다!를 컴파일 단계에서 알려주기 위함이라고 생각한다.