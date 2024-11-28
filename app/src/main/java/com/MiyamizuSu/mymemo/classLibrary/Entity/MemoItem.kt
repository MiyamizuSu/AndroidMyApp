package com.MiyamizuSu.mymemo.classLibrary.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType


/**
 *  @param uuid 与mIndex形成联合主键
 *  @param mIndex 与uuid形成联合主键，mIndex类型为MemoType
 *  @param unionDate 事件关联日期,需要用户输入
 *  @param description 事件描述//事件注意事项，需要用户输入
 *  @param memoImg 事件关联图片Url，需要用户输入
 *  @param title 事件标题，需要用户输入
 */
@Entity(
    primaryKeys = ["uuid", "mIndex"],
    indices = [Index(value = ["uuid", "mIndex"], unique = true)],
    tableName = "mymemo"
)
data class MemoItem(

    @ColumnInfo(name = "uuid")
    var uuid: Int=0,
    @ColumnInfo(name = "mIndex")
    var mIndex: MemoType=MemoType.IMA,

    @ColumnInfo(name = "unionDate")
    var unionDate: String="1970.01.01",

    @ColumnInfo(name = "memoTitle")
    var title: String = "默认标题",

    @ColumnInfo(name = "memoDescription")
    var description: String?=null,

    @ColumnInfo(name = "memoImg")
    var memoImg: String?=null,
)
