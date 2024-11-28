package com.MiyamizuSu.mymemo.classLibrary.Helpers

import androidx.room.TypeConverter
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType

class TypeHelper {
    @TypeConverter
    fun fromMemoType(memoType: MemoType): Int {
        return memoType.value
    }
    @TypeConverter
    fun toMemoType(value: Int): MemoType {
        return MemoType.values().firstOrNull { it.value == value } ?: MemoType.IMA
    }

}