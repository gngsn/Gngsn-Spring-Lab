package com.gngsn.kotlindemo.ch5

class SamConstructure {
    fun createAllDoneRunnable(): Runnable {
        return Runnable {
            println("All done! ")
        }
    }

//    val listener = OnClickListener { view ->
//        val text = when (view.id) { // view.id를 사용해 어떤 버튼이 클릭됐는지 판단한다.
//            R.id.buttonl -> "First button"
//            R.id.button2 -> "Second button"
//            else -> "Unknown button"
//        }
//
//        toast(text)  // "text"의 값을 사용자에게 보여준다.
//    }
//
//    buttonl.setOnClickListener(listener)
//    button2.setOnClickListener(listener)
}