package com.MiyamizuSu.mymemo.classLibrary.Helpers

import android.os.Build
import androidx.annotation.RequiresApi
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
object  DateHelper {
    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("YYYY/MM/dd", Locale.getDefault())
        return formatter.format(Date(millis))
    }
    fun compareDate(inputDate: String): MemoType {
        val currentDate = LocalDate.now()

        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

        val inputDateParsed = LocalDate.parse(inputDate, formatter)

        return when {
            currentDate.isBefore(inputDateParsed) -> MemoType.FUTURE
            currentDate.isEqual(inputDateParsed) -> MemoType.IMA
            else -> MemoType.EVER
        }
    }
    fun calculateDaysFromToday(inputDate: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val date = LocalDate.parse(inputDate, formatter)
        val currentDate = LocalDate.now()
        val daysDifference = ChronoUnit.DAYS.between(date, currentDate)
        return Math.abs(daysDifference).toString()
    }
    fun calculateDateDifference(inputDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val inputLocalDate = LocalDate.parse(inputDate, formatter)
        val currentDate = LocalDate.now()
        return ChronoUnit.DAYS.between(currentDate, inputLocalDate).toInt()
    }

}