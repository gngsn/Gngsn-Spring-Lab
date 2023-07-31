## Chapter 06. Kotlin Type System


자바 vs 코틀린
- 널이 될 수 있는 타입 nullable type
- 읽기 전용 컬렉션

## 1. 널 가능성


### 1.1 널이 될 수 있는 타입: 물음표 `?` 기호

<table>
<tr><th>Java</th><th>Kotlin</th></tr>
<tr>
<td>

```java
int strLen(String s) { 
    return s.length (); 
}
```

- `s` 인자 값으로 `null`을 넘기면 `NullPointerException` 발생
</td>
<td>

```kotlin
fun strLen(s: String) = s.length
```

- `strLen`에 `null`이거나 널이 될 수 있는 인자를 넘기는 것은 금지되며, 혹시 그런 값을 넘기면 **컴파일 시 오류** 발생

</td>
</tr>
</table>
<br/><br/>

`null`과 문자열을 인자로 받을 수 있게 하려면 타입 이름 뒤에 물음표(?)를 명시

```kotlin
fun strLenSafe(s: String?) = /* ... */
```

`String?`, `Int?`, `MyCustomType?` 과 같이 어떤 타입이든 타입 이름 뒤에 물음표를 붙이면,

그 타입의 변수나 프로퍼티에 `null` 참조를 저장할 수 있다는 뜻

즉, <b>`Type?` = `Type` or `null`</b> 이 성립

<br/>

null과 비교하고 나면 컴파일러는 그 사실을 기억.

null이 아님이 확실한 영역에서는 해당 값을 널이 될 수 없는 타입의 값처럼 사용 가능.


```kotlin
fun strLenSafe(s: String?): Int =
    if (s != null) s.length else 0
```


### 1.3 안전한 호출 연산자: `?.`

`?.`: <b>`null` 검사</b> 와 **메소드 호출**을 한 번의 연산으로 수행.

Example. `s?.toUpperCase()` 와 `if (s ! = null) s.toUpperCase() else null` 는 동일

- `String.toUpperCase` 👉🏻 `String` 타입 값 반환
- `s?.toUpperCase()` 👉🏻 `String?` 타입 값 반환
  - (`s`가 널이 될 수 있는 타입인 경우)





