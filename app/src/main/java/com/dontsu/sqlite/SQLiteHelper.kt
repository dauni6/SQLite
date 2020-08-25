package com.dontsu.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//1. 관계형 데이터베이스 SQLite를 사용
//2. 테이블 사용하기 위함 그리고 정보를 저장
//3. 데이터베이스를 생성하려면 반드시 SQLiteOpenHelper 가 필요

/**
 * @context : 컨텍스트
 * @name : 데이터베이스명
 * @version : 버전 정보
 * 팩토리는 사용하지 않아서 null처리함
 * */
class SQLiteHelper(
    context: Context?,
    name: String?,
    version: Int
) : SQLiteOpenHelper(context, name, null, version) {
    
    //사용하게 될 데이터베이스가 전달됨
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블 생성하기 단, 데이터베이스가 생성되어 있다면 더 이상 실행되지 않음
        val create = "CREATE TABLE memo (" +
                "no INTEGER PRIMARY KEY," +
                "content TEXT," +
                "datetime INTEGER" +
                ")"

        db?.let {
            it.execSQL(create)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //전달되는 버전 정보가 변경되었을 때 현재 생성되어 있는 뎅터베이스의 버전과 비교해서 더 높으면 호출
    }

    fun insertMemo(memo: Memo) {
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()
    }

    fun selectMemo(): MutableList<Memo> {
        val list = mutableListOf<Memo>()
        val select = "select * from memo"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)

        while (cursor.moveToNext()) {
            //위치값 찾기
            val noIndex = cursor.getColumnIndex("no")
            val contentIndex = cursor.getColumnIndex("content")
            val datetimeIndex = cursor.getColumnIndex("datetime")
            //찾은 위치값으로 값 꺼내기
            val no = cursor.getLong(noIndex)
            val content = cursor.getString(contentIndex)
            val datetime = cursor.getLong(datetimeIndex)

            list.add(Memo(no, content, datetime))
        }

        cursor.close()
        rd.close()

        return list
    }

    fun updateMemo(memo: Memo) {
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)
        val wd = writableDatabase
        wd.update("memo", values, "no = ${memo.no}", null)
        wd.close()
    }

    fun deleteMemo(memo: Memo) {
        val delete = "delete from memo where no = ${memo.no}"
        val db = writableDatabase
        db.execSQL(delete)
        db.close()
    }


}
