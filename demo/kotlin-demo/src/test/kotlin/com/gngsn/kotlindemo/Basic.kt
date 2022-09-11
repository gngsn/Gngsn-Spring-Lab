package com.gngsn.kotlindemo;


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random
import com.gngsn.kotlindemo.Basic.Color.*

class Basic {

    // ========== function ============
    fun ex1_max(a: Int, b: Int) : Int {
        return if (a > b) a else b
    }

    // 바로 Return 가능 (fyi. kotlin의 if 절은 식(expression)이기 때문에 값 return)
    fun ex2_max(a: Int, b: Int): Int = if (a > b) a else b

    // Return Type 생략 가능
    fun ex3_max(a: Int, b: Int) = if (a > b) a else b


    // ========== variable ============

    @Test
    fun ex_variable() {
        var message = "코틀린을 언제 다 뗄 수 있을까 ~.~"
        message = if (Random(3).nextInt() > 10) "아마 5일 뒤.." else "지금은 아님"

        val answer = 12
        Assertions.assertEquals(Int.javaClass, answer.javaClass)
        val yearsToCompute = 7.5e6 // 타입 추론: Double
        Assertions.assertEquals(Double.javaClass, yearsToCompute.javaClass)

//        Assertions.assertThrows(TypeCastException.class, ()->{answer = "String은 안넣어짐"})
        val lang = arrayListOf<String>("Java", "Kotlin", "Javascript")
        lang.add("MySQL")
        lang[2] = "Typescript"

        println("My major is ${lang[0]}")
    }

    // ========== Class ============

    class SimplePerson(val name: String)

    class Person(val name: String, var isMarried: Boolean)

    class Person2(var name: String) {
        constructor() : this(name = "default")
    }


    @Test
    fun test() {
        val p = Person2()
        println(p.name)

        val p2 = Person2("const")
        println(p2.name)
    }

    class Rectangle(val height: Int, val width: Int) {
        val isSquare: Boolean
            get() = height == width
    }

    @Test
    fun test_properties() {
        val person = Person("gngsn", false)
        println(person.name) // Getter! gngsn
        println(person.isMarried) // Getter! fasle

        person.isMarried = false;

        val rect = Rectangle(200, 200)
        println(rect.isSquare)
        // rect.isSquare = true  !ERROR
    }

    // ========== Enum & when ============

    enum class Color(
        val r: Int, val g: Int, val b: Int // 상수의 프로퍼티를 정의한다.
    ) {
        RED(255, 0, 0), ORANGE(255, 165, 0),  // 각 상수를 생성할 때 그에 대한 프로퍼티 값을 지정한다.
        YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
        INDIGO(75, 0, 130), VIOLET(238, 130, 238);  // 여기 반드시 세미콜론을 사용해야 함

        fun rgb() = (r * 256 + g) * 256 + b     // enum 클래스 안에서 메소드를 정의한다.
    }

    fun getMnemonic(color: Color): String {
        return when (color) {
            Color.RED -> "Richard"
            Color.ORANGE -> "Of"
            Color.YELLOW -> "York"
            Color.GREEN -> "Gave"
            Color.BLUE -> "Battle"
            Color.INDIGO -> "In"
            Color.VIOLET -> "Vain"
        }
    }

    fun getWarmth(color: Color) =
        when (color) {
            Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
            Color.GREEN -> "neutral"
            Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold"
        }

    fun mix(cl: Color, c2: Color) = // when 식의 인자로 아무 객체나 사용할 수 있다. when은 이렇게 인자로 받은 객체가 각 분기 조건에 있는 객체와 같은지 테스트한다.
        when (setOf(cl, c2)) {
            setOf(RED, YELLOW) -> ORANGE
            setOf(YELLOW, BLUE) -> GREEN
            setOf(BLUE, VIOLET) -> INDIGO
            else -> throw Exception("Dirty color")
        } // 호출될 때마다 함수 인자로 주어진 두 색이 when의 분기 조건에 있는 다른 두 색과 같은지 비교하기 위해 여러 Set 인스턴스를 생성

    @Test
    fun test_enum_when() {
        Assertions.assertEquals(255, Color.BLUE.rgb())
        Assertions.assertEquals("In", getMnemonic(Color.INDIGO))
        Assertions.assertEquals("warm", getWarmth(Color.YELLOW))
    }
}
