package com.MiyamizuSu.mymemo.classLibrary.Repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.MiyamizuSu.mymemo.classLibrary.Dao.MemoDao
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import com.MiyamizuSu.mymemo.classLibrary.Helpers.DateHelper
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
    suspend fun addNewMemo( newMemo: MemoItem) {
        _memoDao.addNewMemo(newMemo)
   }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateMemo(memo: MemoItem){
        var bet= DateHelper.calculateDateDifference(memo.unionDate)
        if(bet==0){
            memo.mIndex=MemoType.IMA
        }
        else if(bet<0){
            memo.mIndex=MemoType.EVER
        }
        _memoDao.updateMemoDate(uuid = memo.uuid, memoType = memo.mIndex)
    }

    suspend fun deleteMemo(memo:MemoItem){
        _memoDao.deleteMemoItem(memo)
    }


}