package dev.inkremental.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.swipeRefreshLayout
import dev.inkremental.Inkremental
import dev.inkremental.dsl.android.Size
import dev.inkremental.dsl.android.size
import dev.inkremental.dsl.android.text
import dev.inkremental.dsl.android.widget.textView
import dev.inkremental.dsl.androidx.recyclerview.RecyclerLayoutType
import dev.inkremental.dsl.androidx.recyclerview.list
import dev.inkremental.renderableContentView
import dev.inkremental.skip
import kotlin.concurrent.thread

class SwipeRefreshActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SwipeRefreshActivity"
    }

    private val mData = mutableListOf("1", "2", "3", "4", "5")
    private var onRefresh = false
    private var isFirst = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        renderableContentView {
            swipeRefreshLayout {

                size(Size.MATCH, Size.MATCH)

                onRefresh {
                    onRefresh = true
                    thread {
                        Thread.sleep(2000)
                        onRefresh = false
                        Inkremental.render()
                    }
                }

                refreshing(onRefresh)

                skip()

                list {
                    size(Size.MATCH, Size.MATCH)
                    layout(RecyclerLayoutType.Vertical())
                    items(mData) { index, itemValue ->
                        textView {
                            text(itemValue)
                        }
                    }
                }
            }
        }
    }
}