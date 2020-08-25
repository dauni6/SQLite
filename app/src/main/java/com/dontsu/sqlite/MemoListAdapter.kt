package com.dontsu.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler.view.*
import timber.log.Timber
import java.text.SimpleDateFormat

class MemoListAdapter(var listData: MutableList<Memo>, var helper: SQLiteHelper) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateList(newList : MutableList<Memo>) {
        listData.clear()
        listData.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_recycler, parent, false)
        return MemoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       when (holder) {
           is MemoViewHolder -> {
               Timber.d("---onBindViewHolder : $position")
               val memo = listData[position]
               holder.setMemo(memo)
           }
       }
    }

    inner class MemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mMemo: Memo? = null

        init {
            itemView.deleteBtn.setOnClickListener {
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)
                notifyDataSetChanged()
            }

            itemView.updateBtn.setOnClickListener {
                val memo = Memo(mMemo!!.no, "수정됨", System.currentTimeMillis())
                helper?.updateMemo(memo)
                val list = helper?.selectMemo()
                updateList(list)
            }
        }

        fun setMemo(memo: Memo) {
            itemView.textNo.text = "${memo.no}"
            itemView.textContent.text = "${memo.content}"
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            itemView.textDatetime.text = sdf.format(memo.datetime)

            this.mMemo = memo
        }

    }

}
