package com.gngsn.kotlindemo.ch4.ObjectKeyword

class ObjectKeyword {
}

class Person(val name: String, var isMarried: Boolean)

object Payroll {
    val allEmployees = arrayListOf<Person>()
    fun calculateSalary() {
        for (person in allEmployees) {
            // ...
        }
    }
}

fun main() {
    Payroll.allEmployees.add(Person("경선", false))
    Payroll.calculateSalary()
}