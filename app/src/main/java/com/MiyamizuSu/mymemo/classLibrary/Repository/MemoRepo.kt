package com.MiyamizuSu.mymemo.classLibrary.Repository

import android.util.Log
import com.MiyamizuSu.mymemo.classLibrary.Dao.MemoDao
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList

class MemoRepo {

    private var _memoDao:MemoDao

    constructor( memoDao: MemoDao){
        this._memoDao=memoDao
    }

    suspend fun getAllMemo() : List<MemoItem>{
        var res=_memoDao.getAllMyMemo()
        return res
    }

    suspend fun addNewMemo( newMemos: List<MemoItem>) {
        var maxUid= _memoDao.getMaxUid(newMemos[0].mIndex)
        for(item in newMemos){
            maxUid++
            item.uuid=maxUid
        }
        _memoDao.addAllMemo(*newMemos.toTypedArray())
    }

   suspend fun addNewMemo( newMemo: MemoItem) {
        var maxUid= _memoDao.getMaxUid(newMemo.mIndex)
        maxUid++
        newMemo.uuid=maxUid
        _memoDao.addNewMemo(newMemo)
    }

}