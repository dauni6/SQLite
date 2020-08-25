package com.dontsu.sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val helper = SQLiteHelper(this, "memo", 1)
    private lateinit var memoListAdapter: MemoListAdapter

    init {
        //Timber initialize
        Timber.plant(Timber.DebugTree())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.apply {
            val list = helper.selectMemo()
            memoListAdapter = MemoListAdapter(list, helper)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = memoListAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

        saveBtn.setOnClickListener {
            if (editMemo.text.isNotEmpty()) {
                val memo = Memo(null, editMemo.text.toString(), System.currentTimeMillis())
                helper.insertMemo(memo)
                val list = helper.selectMemo()
                memoListAdapter.updateList(list)
                editMemo.setText("")
            }
        }

    }
}