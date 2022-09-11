# Chapter 3.

## Create Collection

```kotlin
val set = hashSetOf(1, 7, 53)
val list = arrayListOf<Number>(1, 7, 53)
val map = hashMapOf<Number, String>(1 to "one", 7 to "seven", 53 to "fifty-three")

println(set.javaClass)  // class java.util.HashSet
println(list.javaClass) // class java.util.ArrayList
println(map.javaClass)  // class java.util.HashMap
```

* `.javaClass`는 자바 getClassO에 해당하는 코틀린 코드

`arrayListOf` 의 정의를 대표적으로 보면 Java ArrayList를 생성하는 wrapper

```kotlin
public fun <T> arrayListOf(vararg elements: T): ArrayList<T> =
    if (elements.size == 0) ArrayList() else ArrayList(ArrayAsCollection(elements, isVarargs = true))
```

**FYI. `to` 는 일반 함수**

> Tuples.kt
> `public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)`

<br/>

## Call Function

이름 붙인 인자

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    sep: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(sep)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```
제네릭 함수의 문법은 자바와 비슷

문제1. 호출 가독성

`joinToString(collection, " ", " ", "."`

함수의 시그니처를 살펴보지 않고는 구별하기 어려움

함수 시그니처를 외우거나 IDE가 함수 시그니처를 표시해서 도움을 줄 수는 있겠지만, 함수 호출 코드 자체는 여전히 모호

```java
/* 자바 */
joinToString（collection, 
    /* separator */ " ”, 
    /* prefix */ " ", 
    /* postfix */ " . ”）;
```

```kotlin
joinToString(list, sep = " ", prefix = " ", postfix = ".")
```

호출 시 인자 중 어느 하나라도 이름을 명시하고 나면 혼동을 막기 위해 그 뒤에 오는 모든 인자는 이름을 꼭 명시해야 한다

자바로 작성한 코드를 호출할 때는 이름 붙인 인자를 사용할 수 없고, 반대로 JDK가 제공하는 함수를 호출할 때도 이름 붙인 인자를 쓸 수 없음. 
클래스 파일（.class 파일）에 함수 파라미터 정보를 넣는 것은 자바 8이후 추가된 선택적 특징인데, 코틀린은 JDK 6와 호환되기 때문.
코틀린 컴파일러는 함수 시그니처의 파라미터 이름을 인식할 수 없고, 호출 시 사용한 인자 이름과 함수 정의의 파라미터 이름을 비교할 수 없다.

```kotlin
list.ensureCapacity(minCapacity= 2) <-- java method: Named arguments are not allowed for non-Kotlin functions
```


### 디폴트 파라미터 값

자바에서는 일부 클래스에서 오버로딩0*10adm9한 메소드가 너무 많아진다는 문제
코틀린에서는 함수 선언에서 파라미터의 디폴트 값을 지정할 수 있으므로 이런 오버 로드 중 상당수를 피할 수 있다


```kotlin
fun <T> joinToString_usingDefault(
    collection: Collection<T>,
    sep: String = " ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(sep)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

대부분의 경우 아무 접두사나 접미사 없이 콤마로 원소를 구분한다. 따라서 그런 값을 디폴트로 지정
이제 함수를 호출할 때 모든 인자를 쓸 수도 있고, 일부를 생략할 수도 있음
```kotlin
// default parameter로 아래와 같이 시그니처를 생략해서 사용할 수 있고, 모두 동일한 결과를 나타냄
joinToString_usingDefault(list)
joinToString_usingDefault(list, " ")
joinToString_usingDefault(list, " ", "")
joinToString_usingDefault(list, " ", "", "")
```

이름을 붙이면 순서를 바꿔도 문제 없음
```kotlin
joinToString(list, "# 1, 2, 3;", postfix = "; ", prefix = "#") // # l, 2, 3;
```


디폴트 값과 자바 
자바에는 디폴트 파라미터 값이라는 개념이 없어서 코틀린 함수를 자바에서 호출하는 경우에는 그 코틀린 함수가 디폴트 파라미터 값을 제공하더라도 모든 인자를 명시해야 한다. 자바에서 코 틀린 함수를 자주 호출해야 한다면 자바 쪽에서 좀 더 편하게 코틀린 함수를 호출하고 싶을 것이 다. 그럴 때 @JvmOverloads 애노테이션을 함수에 추가할 수 있다. @JvmOverloads를 함수에 추가하면 코틀린 컴파일러가 자동으로 맨 마지막 파라미터로부터 파라미터를 하나씩 생략한 오버로딩한 자바 메소드를 추가해준다.

예를 들어 joinToString에 @JvmOverloads를 붙이면 다음과 같은 오버로딩한 함수가 만들어 진다.

```java
String joinToString(Collection<T> collection);
String joinToString(Collection<T> collection, String separator);
String joinToString(Collection<T> collection, String separator, String prefix);
String joinToString(Collection<T> collection, String separator, String prefix, String postfix) ;
```


### 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티


자바에서는 모든 코드를 클래스의 메소드로 작성해야 하지만 어느 한 클래스에 포함시키기 어려운 코드가 많이 생긴다. 
그 결과 다양한 정적 메소드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메소드는 없는 클래스가 생겨난다. 



이 함수가 어떻게 실행될 수 있는 걸까? JVM이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 컴파일러는 이 파일을 컴파일할 때 새로운 클래스를 정의해준다. 코틀린 만 사용하는 경우에는 그냥 그런 클래스가 생긴다는 사실만 기억하면 된다. 하지만 이 함수를 자바 등의 다른 JVM 언어에서 호출하고 싶다면 코드가 어떻게 컴파일되는지 알아야 joinToString과 같은 최상위 함수를 사용할 수 있다. 어떻게 코틀린이 join.kt 를 컴파일하는지 보여주기 위해 join.kt를 컴파일한 결과와 같은 클래스를 자바 코드로 써보면 다음과 같다.

```kotlin
// join.kt
package strings

fun <T> joinToString(
    collection: Collection<T>,
    sep: String,
    prefix: String,
    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(sep)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

```java
/* 자바 */
package strings;

public class JoinKt { // join.kt 파일에 해당하는 클래스
    public static String joinToString(...) {
        // ...
    }
}
```
코틀린 컴파일러가 생성하는 클래스의 이름은 최상위 함수가 들어있던 코틀린 소스 파일의 이름과 대응
따라서

자바에서 joinToString을 호출하기는 쉽다.

```java
import strings.JoinKt;

JoinKt.joinToString(list, ",", "", "");
```
파일에 대응하는 클래스의 이름 변경하기 코틀린 최상위 함수가 포함되는 클래스의 이름을 바꾸고 싶다면 파일에 @JvmName 애노테이션을 추가하라. @JvmName 애노테이션은 파일의 맨 앞, 패키지 이름 선언 이전에 위치해야 한다.

```kotlin
@file:JvmName("StringFunctions")      // 클래스 이름을 지정하는 애노테이션
package strings                         // @file：JvmName 에노테이션 뒤에 패키지 문이 와야 한다.

fun joinToString(...): String {...}
```

이제 다음과 같이 joinToString 함수를 호출할 수 있다.

```java
/* 자바 */

import strings.StringFunctions; 
StringFunctions.joinToString(list, ", ", "", "");
```

@JvmName 애노테이션의 문법에 대해서는 10장에서 설명한다.


### 최상위 프로퍼티

함수와 마찬가지로 프로퍼티도 파일의 최상위 수준에 놓을 수 있다. 어떤 데이터를 클래 스 밖에 위치시켜야 하는 경우는 흔하지는 않지만, 그래도 가끔 유용할 때가 있다.
const 변경자를 주가하면 프로 퍼티를 public static final 필드로 컴파일하게 만들 수 있다 (단, 원시 타입과 String 타입의 프로퍼티만 const로 지정할 수 있다).


이런 프로퍼티의 값은 정적 필드에 저장된다. 최상위 프로퍼티를 활용해 코드에 상수를 추가할 수 있다.

`val UNIX_LINE_SEPARATOR = "\n"`

기본적으로 최상위 프로퍼티도 다른 모든 프로퍼티처럼 접근자 메소드를 통해 자바 코드 에 노출(val의 경우 게터, var의 경우 게터와 세터가 생긴다). 겉으론 상수처럼 보이는데, 실제로는 게터를 사용해야 한다면 자연스럽지 못하다. 더 자연스럽게 사용하려면 이 상수를 `public static final` 필드로 컴파일해야 한다. `const` 변경자를 주가하면 프로 퍼티를 `public static final` 필드로 컴파일하게 만들 수 있다 (단, 원시 타입과 String 타입의 프로퍼티만 const로 지정할 수 있다).

`const val UNIX_LINE_SEPARATOR = "\n"`


### 확장 함수와 확장 프로퍼티

**기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중 하나**

완전히 코틀린으로만 이뤄진 프로젝트조차도 JDK나 안드로이드 프레임워크 또는 다른 서드파티 프레임워크 등의 자바 라이브러리를 기반으로 만들어진다. 
또 코틀린을 기존 자바 프로젝트에 통합하는 경우에는 코틀린으로 직접 변환할 수 없거나 미처 변환하지 않은 기존 자바 코드를 처리할 수 있어야 한다.


확장 함수
: 어떤 클래스의 멤버 메소드인 것처 럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수
기존 자바 API를 재작성하지 않고도 코틀린이 제공하는 여러 편리한 기능을 사용할 수 있다


확장 함수를 만들려면 추가하려는 함수 이름 앞에 그 함수가 확장할 클래스의 이름을 덧붙이기만 하면 된다. 
클래스 이름을 수신 객체 타입receiver type이라 부르며, 확장 함수가 호출되는 대상이 되는 값(객체)을 수신 객체receiver object라고 부른다


```kotlin
package strings

fun String.lastChar(): Char = this.get(this.length - 1)
// String : receiver type
// this : receiver object

println ("Kotlin".lastChar())
// "Kotlin" : receiver object
```

자바나 그루비와 같은 다른 JVM 언어로 작성된 클래스도 확장할 수 있다. 
자바 클래스로 컴파일한 클래스 파일이 있는 한 그 클래스에 원하는 대로 확장을 추가할 수 있다.

일반 메소드의 본문에서 this를 사용할 때와 마찬가지로 확장 함수 본문에도 this 를 쓸 수 있다. 
그리고 일반 메소드와 마찬가지로 확장 함수 본문에서도 this를 생략할 수 있다.

수신 객체의 메소드나 프로퍼티를 바로 사용할 수 있다. 
하지만 확장 함수가 캡슐화를 깨지는 않기 때문에 확장 함수 안에서는 클래스 내부에서만 사용할 수 있는 비공개private 멤버나 보호된protected 멤버를 사용할 수 없다


**임포트와 확장 함수**

확장 함수를 사용하기 위해서는 임포트해야만 한다. 
코틀린에서는 클래스를 임포트할 때와 동일한 구문을 사용해 개별 함수를 임포트할 수 있다.

```kotlin
import strings.lastChar
// or 
import strings.*

val c = "Kotlin".lastChar()
```

as 키워드를 사용하면 임포트한 클래스나 함수를 다른 이름으로 부를 수 있다.

```kotlin
import strings.lastChar as last

val c = "Kotlin".last()
```

한 파일 안에서 다른 여러 패키지에 속해있는 **이름이 같은 함수**를 가져와 사용해야 하는 경우 이름을 바꿔서 임포트하면 이름 충돌을 막을 수 있다. 
물론 일반적인 클래스나 함수라면 그 전체 이름(FQN, Fully Qualified Name)을 써도 된다. 하지만 코틀린 문법상 확장 함수 는 반드시 짧은 이름을 써야 한다. 따라서 임포트할 때 이름을 바꾸는 것이 확장 함수 이름 충돌을 해결할 수 있는 유일한 방법

**자바에서 확장 함수 호출**

내부적으로 확장 함수는 수신 객체를 첫 번째 인자로 받는 정적 메소드다. 
그래서 확장 함수를 호출해도 다른 어댑터adapter 객체나 실행 시점 부가 비용이 들지 않는다.

```java
char c = StringUtilKt.lastChar("Java");
```



