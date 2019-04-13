package com.a4nt0n64r.tryadvancedrv

import java.util.*

//источник данных наследуется от действий с данными, но можно наверное в нашем проекте так не делать

class DataSource : ListActions() {
    private val books: MutableList<BooksData>

    override val count: Int
        get() = books.size


    //заполнение books который состоит из BookData
    init {
        val names = arrayListOf<String>("Item1", "Item2", "Item3")

        books = LinkedList()


        for (j in 0 until names.size) {
            val id = books.size.toLong()
            val viewType = 0
            val text = names[j]
            books.add(BooksData(false, id, viewType, text))
        }

    }

    //возвращает книгу по индексу
    override fun getItem(index: Int): Data {
        if (index < 0 || index >= count) {
            throw IndexOutOfBoundsException("index = $index")
        }

        return books[index]
    }

    //изменяет положение элемента (FROM_POS,TO_POS)
    override fun changeItemPosition(fromPosition: Int, toPosition: Int) {

        if (fromPosition == toPosition) {
            return
        }

        val item = books.removeAt(fromPosition)

        books.add(toPosition, item)
    }


    //просто класс нашей книги
    class BooksData(
        override var isPinned: Boolean,
        override var id: Long,
        override var viewType: Int,
        override var text: String
    ) : Data() {

        //возвращает String из id и названия книги
        private fun makeText(id: Long, text: String): String {
            return "$id - $text"
        }
    }
}