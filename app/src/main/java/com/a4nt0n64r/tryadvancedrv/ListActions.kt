package com.a4nt0n64r.tryadvancedrv

abstract class ListActions {

    //посчитать элементы
    abstract val count: Int

    //подвинуть элемент
    abstract fun changeItemPosition(fromPosition: Int, toPosition: Int)

    abstract class Data {
        abstract var isPinned: Boolean
        abstract var id: Long
        abstract var viewType: Int
        abstract var text: String
    }

    //получить элемент
    abstract fun getItem(index: Int): Data
}
