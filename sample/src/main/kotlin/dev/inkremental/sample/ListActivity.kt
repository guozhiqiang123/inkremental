package dev.inkremental.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.inkremental.dsl.android.*
import dev.inkremental.dsl.android.Size.MATCH
import dev.inkremental.dsl.android.Size.WRAP
import dev.inkremental.dsl.android.widget.button
import dev.inkremental.dsl.android.widget.frameLayout
import dev.inkremental.dsl.android.widget.linearLayout
import dev.inkremental.dsl.android.widget.textView
import dev.inkremental.dsl.androidx.recyclerview.*
import dev.inkremental.dsl.androidx.recyclerview.RecyclerLayoutType.*
import dev.inkremental.renderableContentView

class ListActivity : AppCompatActivity() {

    var items = mutableListOf<Any>(1, 2, 3, 4, 5, 6, 7, 8, 9, "Section 10", 11, 12, 13, 14, 15, 16, 17, 18, 19, "Section 20").toMutableList()
    var listStyle = 1
    val intDiffCallback = IntDiffCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        renderableContentView {
            frameLayout {
                size(MATCH, MATCH)

                linearLayout {
                    size(MATCH, 50.dpSize)

                    button {
                        size(WRAP, MATCH)
                        text("Vertical")
                        onClick {
                            listStyle = 1
                        }
                    }
                    button {
                        size(WRAP, MATCH)
                        text("Horizontal")
                        onClick {
                            listStyle = 2
                        }
                    }
                    button {
                        size(WRAP, MATCH)
                        text("Grid")
                        onClick {
                            listStyle = 3
                        }
                    }
                    button {
                        size(WRAP, MATCH)
                        text("Add")
                        onClick {
                            val size = items.size + 1
                            //reverse positions of last two elements and add next twenty elements. This is done to show animation from diffutils
                            items = (items.dropLast(2) + listOf(items[items.lastIndex], items[items.lastIndex - 1]) + (size..(size + 19)) + listOf("Section $size")).toMutableList()
                        }
                    }
                }
                list {
                    size(MATCH, MATCH)
                    margin(top = 50.dp)

                    when (listStyle) {
                        1 -> layout(Vertical())
                        2 -> layout(Horizontal())
                        3 -> layout(Grid(spanCount = 3, spanSizeLookUp = { item -> when (item) {
                            is Int -> 1
                            else -> 3
                        } }))
                    }

                    //uses Recycler underneath
                    items(items) { index: Int, itemValue: Any ->
//                    itemsDiffable(items, intDiffCallback) { index: Int, itemValue: Int ->
                        size(if (listStyle != 2) MATCH else WRAP, WRAP)
                        //adapter item
                        when(itemValue) {
                            is Int -> itemWidget(itemValue)
                            is String -> sectionWidget(itemValue)
                        }
                    }
                }
            }

        }
    }

    private fun itemWidget(itemValue: Int) {
        frameLayout {
            size(if (listStyle != 2) MATCH else 100.dpSize, if (listStyle == 3) 150.dpSize else 50.dpSize)
            margin(value = 1.dp)
            backgroundResource(R.color.children_stroke)

            frameLayout {
                size(MATCH, MATCH)
                margin(value = 2.dp)

                backgroundResource(R.color.white)
                textView {
                    size(WRAP, WRAP)
                    layoutGravity(CENTER)
                    text("item: $itemValue")
                }
            }
        }
    }

    private fun sectionWidget(itemValue: String) {
        frameLayout {
            size(MATCH, if (listStyle == 3) 150.dpSize else 50.dpSize)
            margin(value = 1.dp)
            backgroundResource(R.color.children_stroke)

            frameLayout {
                size(MATCH, MATCH)
                margin(value = 2.dp)

                backgroundResource(R.color.white)
                textView {
                    size(WRAP, WRAP)
                    layoutGravity(CENTER)
                    text(itemValue)
                }
            }
        }
    }
}


class IntDiffCallback : InkrementalDiffCallback<Int>() {

    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        val old: Int = oldItems[oldItem]
        val new: Int = newItems[newItem]
        return old == new
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        val old: Int = oldItems[oldItem]
        val new: Int = newItems[newItem]
        return old == new
    }

}
