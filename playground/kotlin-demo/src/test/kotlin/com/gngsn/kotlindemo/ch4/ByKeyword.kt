package com.gngsn.kotlindemo.ch4

/*
by : 인터페이스를 구현할 때 by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있음

대규모 객체지향 시스템의 취약점의 원인 중 하나는 구현 상속 implementation inheritance에 의해 발생.

하위 클래스가 상위 클래스의 메소드 중 일부를 오버라이드하면 하위 클래스는 상위 클래스의 세부 구현 사항에 의존.
시스템 이 변함에 따라 상위 클래스의 구현이 바뀌거나 상위 클래스에 새로운 메소드가 추가.
하위 클래스가 상위 클래스에 대해 갖고 있던 가정이 깨져서 코드가 정상적으로 작동하지 못하는 경우가 발생할 수 있음

-> 코틀린은 기본적으로 클래스를 final로 취급
모든 클래스를 기본적으로 final로 취급하면 상속을 염두에 두고 open 변경자로 열어둔 클래스만 확장할 수 있다.
열린 상위 클래스의 소스코드를 변경할 때는 open 변경자를 보고 해당 클래스를 다른 클래스가 상속하리라 예상할 수 있으므로,
변경 시 하위 클래스를 깨지 않기 위해 좀 더 조심할 수 있다.

하지만 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있는데, 이럴 때에는 데코레이터decorator 패턴을 사용하곤 함.
이 패턴의 핵심은 상속 을 허용하지 않는 클래스(기존 클래스) 대신 사용할 수 있는 새로운 클래스(데코레이터)를 만들되
기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고, 기존 클래스를 데코레이터 내부에 필드로 유지하는 것.

이때 새로 정의해야 하는 기능은 데코레이 터의 메소드에 새로 정의하고(물론 이때 기존 클래스의 메소드나 필드를 활용할 수도 있다)
기존 기능이 그대로 필요한 부분은 데코레이터의 메소드가 기존 클래스의 메소드에게 요청을 전달forwarding.

이런 접근 방법의 단점은 준비 코드가 상당히 많이 필요하다는 점
예를 들어 Collection 같이 비교적 단순한 인터페이스를 구현하면서 아무 동작도 변경하지 않는 데코레이터를 만들 때조차도 다음과 같이 복잡한 코드를 작성해야 한다.

메소드 중 일부의 동작을 변경하고 싶은 경우 메소드를 오버라이드하면 컴파일러가 생성한 메소드 대신 오버라이드한 메소드가 쓰인다.
기존 클래스의 메소드에 위임하는 기본 구현으로 충분한 메소드는 따로 오버라이드할 필요가 없다
*/

class DelegatingCollection<T>(innerList: Collection<T> = ArrayList<T>()) : Collection<T> by innerList {}


/* by 키워드를 통해 아래의 무의미한 오버라이드 메소드를 적지않아도 된다!

class DelegatingCollectionBefore<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}
 */


/*
by 키워드로 클래스 안에 있던 모든 메소드 정의 제거.
-> 컴파일러가 자동으로 생성하며 자동 생성한 코드의 구현은 DelegatingCollection에 있던 구현과 비슷

메소드 중 일부의 동작을 변경하고 싶은 경우 메소드를 오버라이드하면 컴파일러가 생성한 메소드 대신 오버라이드한 메소드가 호출.


> Example.
중복을 제거하는 프로세스를 설계하는 중이라면 원소 추가 횟수를 기록하는 컬렉션을 통해
최종 컬렉션 크기와 원소 추가 시도 횟수 사이의 비율을 살펴봄으로써 중복 제거 프로세스의 효율성을 판단할 수 있음.
- CountingSet에 MutableCollection의 구현 방식에 대한 의존관계가 생기지 않는다는 점이 중요
    클라이언트 코드가 CountingSet의 코드를 호출할 때 발생하는 일은 CountingSet 안에서 마음대로 제어할 수 있지만,
    CountingSet 코드는 위임 대상 내부 클래스인 Mutable Collection에 문서화된 API를 활용.
    그러므로 내부 클래스 MutableCollection 이 문서화된 API를 변경하지 않는 한 CountingSet 코드가 계속 잘 작동할 것임을 확신
 */
class CountingSet<T>(val innerSet: MutableCollection<T> = HashSet<T>()) :
    MutableCollection<T> by innerSet { // MutableC이lection의 구현을 innerSet에게 위임

    var objectsAdded = 0

    // 아래 두 메소드는 위임하지 않고 새로운 구현을 제공한다.
    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}