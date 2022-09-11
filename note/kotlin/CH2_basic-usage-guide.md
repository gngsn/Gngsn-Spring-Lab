# Chapter 2.

<br/>

### TLDR;

**요약**
✔️ 함수 정의: fun 키워드. 
✔️ 읽기 전용 변수: val. 변경 가능한 변수: var
✔️ 문자열 템플릿: 문자열을 연결하지 않아도 되므로 코드 간결. 변수 이름 앞에 $를 붙이거나, 식을 $｛*expression*｝처럼 ${}로 둘러싸면 변수나 식의 값을 문자열 안에 넣을 수 있음
✔️ 값 객체 클래스(Value Object)를 아주 간결하게 표현할 수 있고, 프로퍼티 정의하면 접근자 메서드(accessor method)를 자동 생성
✔️ if는 식(expression)이며, 값을 만들어낸다.
✔️ 코틀린 when은 자바의 switch와 비슷하지만 더 강력하다.
✔️ 컴파일러가 스마트 캐스트를 활용해 자동으로 타입을 바꿔준다. 변수 타입을 검사 후 변수를 캐스팅하지 않아도 검사한 타입의 변수처럼 사용할 수 있다. 
✔️ for, while, do-while 루프는 자바가 제공하는 같은 키워드의 기능과 비슷하지만 더 편리. 특히 맵을 이터레이션하거나 이터레이션하면서 컬렉션의 원소와 인덱스를 함께 사용해야 하는 경우.
✔️ 1..5와 같은 식은 범위를 만들어낸다. 범위와 수열은 코틀린에서 같은 문법을 사용하며, for 루프에 대해 같은 추상화를 제공한다. 
✔️ 어떤 값이 범위 안에 들어 있거나 들어있지 않은지 검사하기 위해서 in이나 !in을 사용한다.
✔️ 코틀린 예외 처리는 자바와 비슷하다. 다만 코틀린에서는 함수가 던질 수 있는 예외를(throws) 선언하지 않아도 된다.


<br/>

## 2.1. 함수와 변수

### fun

: 함수 선언

- 파라미터 뒤에 파라미터의 타입 명시
- 최상위 수준 정의 가능 （자바와 다르게 클래스 안에 넣어야 할 필요가 없음)

``` kotlin
fun max (a: Int, b: Int) : Int {
    return if (a > b) a else b
}
```

<br/>

### if

코틀린 if는 값을 만들어내지 못하는 문장statement이 아니고 결과를 만드는 식 expression
삼항연산자와 비슷 (e.g. `(a > b) ? a : b`)

``` kotlin
fun max (a: Ir1tz b: Int) : Int = if (a > b) a else b
```

> expression (식): 값을 만들어 내며 다른 식의 하위 요소로 계산에 참여할 수 있음
> statement (문): 자신을 둘러싸고 있는 가장 안쪽 블록의 최상위 요소로 존재하며 아무런 값을 만들어내지 않음

<br/>

### println

- Java의 System.out.printin 대신 사용
- (코틀린 표준 라이브러리는 여러 가지 표준 자바 라이브러리 함수를 간결하게 사용할 수 있게 감싼 래퍼 wrapper를 제공하며, println도 그 중 하나)

<br/>

### 변수

val: value. 변경 불가능한 immutable 참조를 저장하는 변수. val로 선언된 변수는 일단 초기화하고 나면 재할당이 불가능하다. 자바의 final 변수에 해당.
var: variable. 변경 가능한 mutable 참조. 변수의 값은 바뀔 수 있다. 자바의 일반 변수에 해당

> 💡 기본적으로는 모든 변수를 val 키워드를 사용해 불변 변수로 선언하고, 나중에 꼭 필요 할 때에만 var로 변경하라.
> 변경 불가능한 참조와 변경 불가능한 객체를 부수 효과가 없는 함수와 조합해 사용하면 코드가 함수형 코드에 가까워진다

``` kotlin
val gngsn = "변수 지정"
val answer = 42
// or
val answer:Int = 42
```

타입을 지정하지 않으면 컴파일러가 초기화 식을 분석해서 초기화 식의 타입을 변수 타입으로 지정한다.
여기서 초기화 식은 42로 Int 타입이다. 따라서 변수도 Int 타입이 된다.

부동소수점floating point 상수를 사용한다면 변수 타입은 Double이 된다.

``` kotlin
val yearsToCompute = 7.5e6  <----- 7.5 X 10 6 = 7500000.0
```

<br/>

### 문자열 템플릿 String Template

```kotlin
fun main(args: Array<String>) {
    val name = if (args.size > 0)
        printin("Hello, $name!") args [0] else "Kotlin"
}
```

자바의 `"Hello, " + name + "!"` 와 코틀린의 `"Hello, $name!"` 과 동일
중괄호로 둘러싼 식 안에서 문자열 템플릿을 사용해도 된다. 
예를 들어 "${if (s.length>2) short" else "normal string ${s}"}"와 같은 문자열도 사용할 수 있다. - 옮긴이

|                 | Java                   | Kotlin          |
|-----------------|------------------------|-----------------|
|    Variable     | final                  | val             |
|    Variable     | final                  | var             |
| String Template | "Hello, " + name + "!" | "Hello, $name!" |


<br/>

### 클래스와 프로퍼티

```java
/* 자바 */

public class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

코틀린으로 변환하면 아래와 같다.

```kotlin
/* 자바 */

class Person(val name: String)
```
코드가 없이 데이터만 저장하는 클래스를 **값 객체**value object라 부르며, 
다양한 언어가 값 객체를 간결하게 기술할 수 있는 구문을 제공

<br/>

### 프로퍼티

클래스의 목적은 데이터를 **캡슐화**encapsulate

자바에서는 데이터를 필드field에 저장하며, 멤버 필드의 가시성은 보통 비공개Private다. 클래스는 자신을 사용하 는 클라이언트가 그 데이터에 접근하는 통로로 쓸 수 있는 접근자 메소드accessor method를 제공한다. 보통은 필드를 읽기 위한 게터getter를 제공하고 필드를 변경하게 허용해야 할 경우 세터setter를 추가 제공할 수 있다. 이런 예를 person 클래스에서도 볼 수 있다. 세터는 자신이 받은 값을 검증하거나 필드 변경을 다른 곳에 통지하는 등의 로직을 더 가질 수 있다.

자바에서는 필드와 접근자를 한데 묶어 프로퍼티property라고 부르며, 프로퍼티라는 개념을 활용하는 프레임워크가 많다. 코틀린은 프로퍼티를 언어 기본 기능으로 제공하며, 코틀린 프로퍼티는 자바의 필드와 접근자 메소드를 완전히 대신한다. 클래스에서 프로퍼티를 선언할 때는 앞에서 살펴본 변수를 선언하는 방법과 마찬가지로 val이나 var를 사용한다. val로 선언한 프로퍼티는 읽기 전용이며, var로 선언한 프로퍼티는 변경 가능하


```kotlin
class Person( 
    val name: String,   // 읽기 전용 프로퍼티로, 코틀린은 （비공개） 필드와 필드를 읽는 단순한 （공개） 게터를 만들어낸다.
    var isMarried: Boolean  // 쓸 수 있는 프로퍼티로, 코틀린은 （비공개） 필드, （공개） 게터, （공개） 세터를 만들어낸다.
}
```

읽기 전용 프로퍼티의 경우 게터만 선언하며 변경할 수 있는 프로퍼티의 경우 게터 와 세터를 모두 선언

```kotlin

class Person( val name: String, var isMarried: Boolean )

Rectangle(val height: Int, val width:Int) {
    val isSquare: Boolean
        get() = height == width
}


fun test_properties() {
    val person = Person("gngsn", false)
    println(person.name) // Getter! gngsn
    println(person.isMarried) // Getter! fasle

    person.isMarried = false;

    val rect = Rectangle(200, 200)
    println(rect.isSquare)
    // rect.isSquare = true  !ERROR
}
```

<br/>

### 디렉터리와 패키지

자바의 경우 모든 클래스를 패키지 단위로 관리

코틀린에도 자바와 비슷한 개념의 패키지가 있다. 모든 코틀린 파일의 맨 앞에 package 문을 넣을 수 있다. 그러면 그 파일 안에 있는 모든 선언(클래스, 함수. 프로퍼티 등)이 해당 패키지에 들어간다. 같은 패키지에 속해 있다면 다른 파일에서 정의한 선언일지라도 직접 사용할 수 있다. 반면 다른 패키지에 정의한 선언을 사용하려면 임포트를 통해 선언을 import 키워드로 불러와야한다.


### enum과 when

when은 자바의 switch를 대치하되 훨씬 더 강력하며, 앞으로 더 자주 사용할 프로그래밍 요소라고 생각할 수 있다. when에 대해 설명하는 과정에서 코틀린에서 enum을 선언하는 방법과 스마트 캐스트 smart cast에

enum 클래스 정의

```
enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}
```

### while과 for 루프

> for ＜아이템＞ in ＜원소들＞

코틀린에는 자바의 for 루프（어떤 변수를 초기화하고 그 변수를 루프를 한 번 실행할 때마다 갱신하고 루프 조건이 거짓이 될 때 반복을 마치는 형태의 루프）에 해당하는 요소가 없다. 


**범위 생성**

루프의 가장 흔한 용례인 초깃값, 증가 값, 최종 값을 사용한 루프를 대신하기 위해 코틀린에서는 범위range를 사용한다.

범위는 기본적으로 두 값으로 이뤄진 구간. 보통은 그 두 값은 정수 등의 숫자 타입의 값이며, `..` 연산자로 시작 값과 끝 값을 연결해서 범위를 만든다.

> val oneToTen = 1..10

코틀린의 범위는 폐구간（닫힌 구간） 또는 양끝을 포함하는 구간이다. 예에서는 10이 항상 범위에 포함

수열progression: 어떤 범위에 속한 값을 일정한 순서로 이터레이션하는 경우



범위는 문자에만 국한되지 않는다. 

비교가 가능한 클래스라면(`java.lang.Comparable` 인터페이스를 구현한 클래스라면) 그 클래스의 인스턴스 객체를 사용해 범위를 만들 수 있다.

Comparable을 사용하는 범위의 경우 그 범위 내의 모든 객체를 항상 이터레이션하지는 못한다. 
예를 들어 Java’와 ‘Kotlin’ 사이의 모든 문자열을 이터레이션할 수 없다. 
하지만 in 연산자를 사용하면 값이 범위 안에 속하는지 항상 결정할 수 있다.



## Exception

코틀린의 예외exception 처리는 자바나 다른 언어의 예외 처리와 비슷
함수는 정상적으로 종료할 수 있지만 오류가 발생하면 예외를 던질throw 수 있음

```kotlin
if (percentage !in 0..100) {
    throw IllegalArgumentException( // new 키워드를 사용하지 않음
        "A percentage value must be between 0 and 100: $percentage")
}
```


### try, catch, finally

자바에서는 체크 예외를 명시적으로 처리해야 한다. 
어떤 함수가 던질 가능성이 있는 예외나 그 함수가 호출한 다른 함수에서 발생할 수 있는 예외를 모두 catch로 처리해야 하며, 처리하지 않은 예외는 throws 절에 명시해야 한다.

다른 최신 JVM 언어와 마찬가지로 코틀린도 체크 예외와 언체크 예외unchecked exception를 구별하지 않는다. 
코틀린에서는 함수가 던지는 예외를 지정하지 않고 발생한 예외를 잡아내도 되고 잡아내지 않아도 된다. 
실제 자바 프로그래머들이 체크 예외를 사용하는 방식을 고려해 이렇게 코틀린 예외를 설계했다. 자바는 체크 예외 처리를 강제한다. 
하지만 프로그래머들이 의미 없이 예외를 다시 던지거나, 예외를 잡되 처리하지는 않고 그냥 무시하는 코드를 작성하는 경우가 흔하다. 
그로 인해 예외 처리 규칙이 실제로는 오류 발생을 방지하지 못하는 경우가 자주 있다.

```kotlin
fun readNumber(reader:BufferedReader): Int? {
        try {
            val line = reader.readLine()
            return Integer.parseInt(line)
        }
        catch (e: NumberFormatException) {
            return null
        }
        finally {
            reader.close()
        }
    }
```










