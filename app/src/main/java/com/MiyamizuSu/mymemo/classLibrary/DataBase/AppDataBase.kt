package com.MiyamizuSu.mymemo.classLibrary.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.MiyamizuSu.mymemo.classLibrary.Dao.MemoDao
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Helpers.TypeHelper

@Database(entities = [MemoItem::class], version = 1)
@TypeConverters(TypeHelper::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDao
    /**
     * 创建database的代价高，故应获取database单例
     */
    companion object {
        private var db: AppDatabase? = null
        private val dbName = "mydb3.db"
        fun getMyDb(context: Context): AppDatabase = if (db == null) {
            Room.databaseBuilder(context, AppDatabase::class.java, dbName)
                .setJournalMode(JournalMode.TRUNCATE)
                .build()
        } else {
            db!!
        }
    }

}