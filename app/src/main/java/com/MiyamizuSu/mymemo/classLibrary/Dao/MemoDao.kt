package com.MiyamizuSu.mymemo.classLibrary.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import kotlinx.coroutines.flow.Flow


@Dao
interface MemoDao {
    @Insert
    suspend fun addNewMemo(newMemo:MemoItem)

    @Query("Select * from mymemo ")
    suspend fun getAllMyMemo () : List<MemoItem>

    @Query("select max(uuid) from mymemo where mIndex=:memoType")
    fun getMaxUid(memoType: MemoType):Int

    @Query("UPDATE mymemo SET mIndex=:memoType WHERE uuid = :uuid   ")
    suspend fun updateMemoDate(uuid: Int, memoType: MemoType)
    @Delete
    suspend fun deleteMemoItem(memoItem: MemoItem)
}