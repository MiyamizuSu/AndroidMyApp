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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Enums.Screen
import com.MiyamizuSu.mymemo.classLibrary.viewModels.AddFrameViewModel
import com.MiyamizuSu.mymemo.classLibrary.viewModels.MainFrameViewModel
import com.MiyamizuSu.mymemo.ui.theme.MyMemoTheme

// 应用程序入口
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun myMemo(){
        val db = AppDatabase.getMyDb(this.applicationContext)
        val navController = rememberNavController()
        val mainViewModel=MainFrameViewModel(database =  db, navToAdd = {
            navController.navigate("add")
            println("toadd")
        })
        val addViewModel=AddFrameViewModel(navToMain = {navController.navigate("main")})
        NavHost(
            navController = navController,
            startDestination = "main" // 设置起始页面为 "main"
        ) {
            // Main 页面
            composable("main") {
                mainViewModel.mainFrame()
            }
            // Add 页面
            composable("add") {
                addViewModel.mainFrame()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        myMemo()
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
