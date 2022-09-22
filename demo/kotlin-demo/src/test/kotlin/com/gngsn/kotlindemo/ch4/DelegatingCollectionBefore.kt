package com.gngsn.kotlindemo.ch4

// 아무 동작도 변경하지 않는 데코레이터
class DelegatingCollectionBefore<T> : Collection<T> {
    private val innerList = arrayListOf<T>()

    override val size: Int
        get() = innerList.size

    override fun isEmpty(): Boolean = innerList.isEmpty()

    override fun contains(element: T): Boolean = innerList.contains(element)

    override fun iterator(): Iterator<T> = innerList.iterator()

    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)
}

class DelegatingCollection<T>(innerList: Collection<T> = ArrayList<T>()) : Collection<T> by innerList {}

class CountingSet<T>(val innerSet: MutableCollection<T> = HashSet<T>()) :
    MutableCollection<T> by innerSet { // MutableC이lection의 구현을 innerSet에게 위임

    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}