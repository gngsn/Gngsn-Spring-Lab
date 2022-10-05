## 컬렉션 함수형 API

컬렉션을 다룰 때 함수형 프로그래밍을 사용하면 굉장히 편리하다. 
대부분의 로직에 라이브러리 함수를 활용하여 코드를 아주 간결하게 만들 수 있다.


### 필수적인 함수: filter와 map

filter와 map은 컬렉션을 활용할 때 기반이 되는 함수다. 대부분의 컬렉션 연산을 이 두 함수를 통해 표현할 수 있다.
숫자를 사용한 예제와 Person을 사용한 예제를 통해 이 두 함수를 자세히 


> 고차 함수(HOF, High Order Function)
> : 함수형 프로그래밍에서 람다나 함수를 인자로 받거나, 혹은 함수를 반환하는 함수
고차 함수는 기본 함수를 조합해서 새로운 연산을 정의하거나, 고차 함수를 여러번 조합해 복잡한 연산을 쉽게 정의할 수 있다. 
>  이런 식으로 고차 함수와 단순한 함수를 조합하는 방식을 컴비네이터 패턴(combinator pattern)이라 부르고, 고차함수로 구현한 컴비네이터 패턴을 컴비네이터(combinator) 라고 부른다.

### Example: .filter(/* lambda */)

``` kotlin
data class Person(val name: String, val age: Int)
```

filter 함수는 filter함수는 컬렉션에서 원치 않는 원소를 제거하며, 주어진 조건에 따라 컬렉션을 필터링한다. 

즉, 컬렉션을 이터레이션하면서 주어진 람다에 각 원소를 넘겨서 람다가 true를 반환하는 원소만 모은다.

아래는 짝수만 필터링하는 코드이다.


``` kotlin
val list = listOf (1, 2, 3, 4)
println(list.filter { it % 2 == 0 }) // [2, 4]
```

이 때, 람다로 입력한 참/거짓을 반환하는 구문을 술어predicate라고 한다.

30살 이상인 사람만 필요하다면 filter를 사용한다.
그림 5.3 filter 함수는 주어진 술어를 만족하는 모든 원소를 선택한다.

``` kotlin
val people = listOf(Person(“Alice”, 29), Person (“Bob”, 31))
println(people.filter { it .age > 30 }) // [Person(name=Bob, age=31)]
```



### Example: .map(/* lambda */)

원소를 변환하려면 map 함수를 사용해야 한다. map 함수는 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만든다. 

``` kotlin
val list = listOf (1, 2, 3, 4) println(list.map{ it * it }) // [1, 4, 9, 16]
```

결과는 원본 리스트와  동일한 원소의 개수를 갖지만, 각 원소는 주어진 조건에 맞게 새롭게  변환된 컬렉션이다. 

// 그림 5.4

``` kotlin 
val people = listOf(Person(“Alice”, 29), Person(“Bob”, 31))
println(people.map { it.name }) // [Alice, Bob]
```

``` kotlin
people.map(Person::name)
```

이런 호출을 쉽게 연쇄시킬 수 있다. 예를 들어 30살 이상인 사람의 이름을 출력해보자.

``` kotlin
people.filter { it.age > 30 }.map(Person::name) // [Bob]

// (people.filter({ it.age > 30 })).map(Person::name) 
```


이제 이 목록에서 가장 나이 많은 사람의 이름을 알고 싶다고 하자. 먼저 목록에 있는 사람들의 나이의 최댓값을 구하고 나이가 그 최댓값과 같은 모든 사람을 반환하면 된다. 람다를 사용하면 쉽게 그런 코드를 작성할 수 있다.


### all, any, count, find: 컬렉션에 술어 적용

컬렉션에 대해 자주 수행하는 연산으로 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 또는 그 변종으로 컬렉션 안에 어떤 조건을 만족하는 원소가 있는지 판단하는) 연산이 있다. 
코틀린에서는 all과 any가 이런 연산이다. count 함수는 조건을 만족하는 원소의  개수를 반환하며, find 함수는 조건을 만족하는 첫 번째 원소를 반환한다.

이런 함수를 살펴보기 위해 어떤 사람의 나이가 27살 이하인지 판단하는 술어 함수
`canBeInClub27` 를 만들자.

``` kotlin
val canBeInClub27 = { p: Person -> p.age <= 27 }
```

모든 원소가 이 술어를 만족하는지 궁금하다면 all 함수를 쓴다.

``` kotlin
val people = listOf (Person (“Alice”, 27), Person (“Bob”, 31))
println(people.all(canBeInClub27)) // false
```

술어를 만족하는 원소가 하나라도 있는지 궁금하면 any를 쓴다.

```
println(people.any (canBeInClub27)) // true
```


어떤 조건에 대해 `!all`을 수행한 결과와 그 조건의 부정에 대해 any를 수행한 결과는 같다(드 모르강의 법칙De Morgan’s Theorem). 또 어떤 조건에 대해 `!any`를  수행한 결과와 그 조건의 부정에 대해 `all`을 수행한 결과도 같다. 가독성을 높이려면 any와 all 앞에 `!`를 붙이지 않는 편이 낫다.

한국어로 풀어보면 “모든 원소가 조건에 해당하지  않음 = 조건에 해당하지 않는 것이 단 하나도 없음”이기 때문이다.

``` kotlin
val list = listOf (1, 2, 3)
println(!list.all { it == 3 }) //`!`를 눈치 채지 못하는 경우가 자주 있다. 따라서 이런 식보다는 any를 사용하는 식이 더 낫다.
// true
println(list.any { it != 3 }) // any를 사용하려면 술어를 부정해야 한다. 
// true
```

> 모든 원소가 3이 아님 == 3인 원소가 단 하나도 없음 


### count(/* lambda */)

술어를 만족하는 원소의 개수를 구하려면 count를 사용할 수 있다.

``` kotlin
val people = listOf (Person (’’Alice”r 27), Person (’’Bob”, 31)) 
println(people.count(canBeInClub27)) // 1
```

> count VS size
: count가 있다는 사실을 잊어버리고, 컬렉션을 필터링한 결과의 크기를 가져오는 경우가 있다.

``` kotlin
println(people.filter(canBeInClub27).size) // 1
```

하지만 이렇게 처리하면 조건을 만족하는 모든 원소가 들어가는 **중간 컬렉션**이 생긴다. 반면 count는 조건을 만족하는 원소의 개수만을 추적하지 조건을 만족하는 원소를 따로 저장하지 않는다. 따라서 count가 훨씬 더 효율적이다.


술어를 만족하는 원소를 하나 찾고 싶으면 find 함수를 사용한다.

``` kotlin
val people = listOf (Person (“Alice”, 27), Person (“Bob”, 31))
println(people.find(canBeInClub27)) // Person(name=Alicez age=27)
```

이 식은 조건을 만족하는 원소가 하나라도 있는 경우 가장 먼저 조건을 만족한다고 확인 된 원소를 반환하며, 만족하는 원소가 전혀 없는 경우 null을 반환한다. find는 `firstOrNull`과 같다. 조건을 만족하는 원소가 없으면 null이 나온다는 사실을 더 명확 히 하고 싶다면 `firstOrNull`을 쓸 수 있다.


### .groupBy()

: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶다고 하자. 예를 들어 사람을 나이에 따라 분류해보자. 특성을 파라미터로 전달하면 컬렉션을 자동으로 구분  해주는 함수가 있으면 편리할 것이다. groupBy 함수가 그런 역할을 한다.

``` kotlin
val people = listOf (Person(“Alice”, 31), Person(“Bob”, 29), Person(“Carol”, 31))
println(people.groupBy { it .age })
```

이 연산의 결과는 컬렉션의 원소를 구분하는 특성(이 예제에서는 age)이 키이고, 키 값에 따른 각 그룹(이 예제에서는 Person 객체의 모임)이 값인 맵이다. 

// 그림 5.5

이 예제의 경우 출력은 다음과 같다.

``` kotlin
{29=[Person(name=Bob, age=29)],
31=[Person(name=Alice, age=31), Person(name=Carol, age=31)]}
```

각 그룹은 리스트다. 따라서 groupBy의 결과 타입은 `Map<Int, List<Person>>`이다. 필요하면 이 맵을 mapKeys나 mapValues 등을 사용해 변경할 수 있다.

다른 예로 멤버 참조를 활용해 문자열을 첫 글자에 따라 분류하는 코드를 보자.

``` kotlin 
val list = listOf (“a”, “ab”, “b”)
println(list.groupBy(String::first)) // {a=[a, ab], b=[b]}
```

first는 String의 멤버가 아니라 **확장 함수**지만 여전히 멤버 참조를 사용해 first에 접근할 수 있다.