package com.gngsn.kotlindemo.ch4

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/*
데이터 클래스의 프로퍼티는 val가 아니어도 var를 써도 되지만,
불변 mutable 클래스를 권장: 데이터 클래스의 모든 프로퍼티를 읽기 전용 생성.

- HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적.
가령, 데이터 클래스 객체를 키로한 후 변경하면 컨테이너 상태가 잘못될 수 있다.

- 불변 객체를 사용하면 프로그램을 훨씬 쉽게 추론 가능.
특히 다중스레드 프로그램의 경우, 스레드가 사용 중인 데이터를 다른 스레드가 변경할 수 없으므로 스레드를 동기화해야 할 필요가 줄어듦.

**copy method**
: 객체를 복사copy 하면서 일부 프로퍼티를 바꿀 수 있게 해줌.
코틀린 컴파일러는 데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있도록 copy 메소드를 제공.

객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 나음.
복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도
프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않음.
 */
class CopyMethod {
    @Test
    fun copyTest() {
        val park = Client("박경선", 10580)
        val copy = park.copy()
        println(copy)

        Assertions.assertTrue(park == copy)
        Assertions.assertTrue(park !== copy)
    }
}