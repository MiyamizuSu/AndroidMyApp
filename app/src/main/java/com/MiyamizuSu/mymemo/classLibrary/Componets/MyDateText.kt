package com.MiyamizuSu.mymemo.classLibrary.Componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class MyDateText : MyText {

    @Composable
    override fun myText(text: String,modifier: Modifier,fontSize:TextUnit) {
        Text(
            text = text,
            fontSize = fontSize,
            modifier = modifier
        )
    }

}