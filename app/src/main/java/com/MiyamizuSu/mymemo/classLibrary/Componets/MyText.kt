package com.MiyamizuSu.mymemo.classLibrary.Componets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

interface MyText {
    @Composable
    public fun myText(text: String,modifier: Modifier,fontSize:TextUnit)
}