package com.MiyamizuSu.mymemo.classLibrary.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import kotlinx.coroutines.flow.Flow


@Dao
interface MemoDao {
    @Insert
     fun addNewMemo(newMemo:MemoItem)

    @Insert
    fun addAllMemo(vararg memos:MemoItem)

    @Query("Select * from mymemo ")
    suspend fun getAllMyMemo () : List<MemoItem>

    @Query("select max(uuid) from mymemo where mIndex=:memoType")
    fun getMaxUid(memoType: MemoType):Int

}