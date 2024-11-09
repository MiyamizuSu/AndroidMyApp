package com.MiyamizuSu.mymemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.viewModels.MainFrameViewModel
import com.MiyamizuSu.mymemo.ui.theme.MyMemoTheme

// 应用程序入口
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getMyDb(this.applicationContext)
        var viewModel=MainFrameViewModel(db)
        enableEdgeToEdge()
        setContent {
            MyMemoTheme {
                Column(modifier = Modifier
                    .border(width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp))
                    .fillMaxSize(1.0f)
                )
                {
                    viewModel.UpperFrame()
                    viewModel.DownFrame()
                }
            }
        }
    }

    override fun onDestroy() {
        AppDatabase.getMyDb(this.applicationContext).close()
        super.onDestroy()
    }
}
