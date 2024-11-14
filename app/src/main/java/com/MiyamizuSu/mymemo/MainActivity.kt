package com.MiyamizuSu.mymemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
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
            Scaffold(
                topBar = {

                },
                bottomBar = {
                    BottomAppBar(
                        actions = {

                        },
                        floatingActionButton = {

                        },
                        modifier = Modifier.height(57.dp)
                    )
                },
                floatingActionButton = {

                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    MyMemoTheme {
                        Column(modifier = Modifier
                            .fillMaxSize(1.0f)
                            .background(color = MaterialTheme.colorScheme.surface)
                            .graphicsLayer {

                            }
                        )
                        {
                            viewModel.UpperFrame()
                            HorizontalDivider(thickness = 2.dp, modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 10.dp)
                            )
                            viewModel.DownFrame()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        AppDatabase.getMyDb(this.applicationContext).close()
        super.onDestroy()
    }
}
