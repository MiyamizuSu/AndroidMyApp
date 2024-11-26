package com.MiyamizuSu.mymemo.classLibrary.viewModels

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

class AddFrameViewModel :ViewModelBase,ViewModel {
    private val _navToMain:()->Unit

    constructor(navToMain:()->Unit){
        _navToMain=navToMain
    }

    @Composable
    fun mainFrame(){
        Button(
            onClick = _navToMain
        ) {
            Text(
                text = "去主页"
            )
        }
    }
}