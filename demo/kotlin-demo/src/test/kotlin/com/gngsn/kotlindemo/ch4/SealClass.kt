sealed class Expr {     // 기반 클래스를 sealed로 봉인한다.
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

// 기반 클래스의 모든 하위 클래스를 중첩 클래스로 나열한다.
fun eval(e: Expr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
        }