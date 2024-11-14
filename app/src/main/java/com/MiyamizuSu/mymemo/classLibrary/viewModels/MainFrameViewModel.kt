package com.MiyamizuSu.mymemo.classLibrary.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import com.MiyamizuSu.mymemo.classLibrary.Repository.MemoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainFrameViewModel : ViewModelBase, ViewModel {

    private val _memoRepo: MemoRepo

    constructor(database: AppDatabase) {
        this._memoRepo = MemoRepo(database.memoDao())
    }

    /**
     * 上半部分组件容器
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun UpperFrame() {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(1.0f)
                .fillMaxHeight(0.5f),
        ) {
            addButton()
            DateFrame()
        }
    }

    /**
     * 展示备忘录日期
     */
    @OptIn(ExperimentalLayoutApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun DateFrame() {
        val currentDate = LocalDate.now()
        val year = currentDate.year
        val month = currentDate.monthValue
        val day = currentDate.dayOfMonth
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
        {
            val boxWidth = maxWidth
            val boxHeight = maxHeight
            FlowColumn(
                modifier = Modifier
                    .padding(start = boxWidth * 0.22f, top = boxHeight * 0.5f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.large
                    ),

                )
            {
                Text(
                    text = year.toString(),
                    modifier = Modifier
                        .padding(start = boxWidth * 0.3f * 0.8f),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 2.sp,
                    style = TextStyle(

                    )
                )
                Text(
                    text = month.toString() + "月" + day.toString() + "日",
                    modifier = Modifier,
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 14.sp,
                )
            }
        }
    }

    /**
     * 下半部分组件容器
     */
    @Composable
    fun DownFrame() {
        var myMemos by remember { mutableStateOf<List<MemoItem>>(emptyList()) }
        LaunchedEffect(Unit) {
            myMemos = _memoRepo.getAllMemo()
        }
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        val cardHeight = 70.dp
        val density = LocalDensity.current
        val cardHeightPx = with(density) { cardHeight.toPx() }

        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemScrollOffset }
                .collect { scrollOffset ->
                    if (scrollOffset >= cardHeightPx.toInt()) {
                        val firstVisibleItemIndex = listState.firstVisibleItemIndex
                        coroutineScope.launch {
                            listState.scrollToItem(firstVisibleItemIndex + 1, 0)
                        }
                    }
                }
        }
            Text(
                text = "列表清单",
                modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                style = LocalTextStyle.current.merge(
                    TextStyle(
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        )
                    )
                )
            )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            verticalArrangement = Arrangement.Top
        ) {
            itemsIndexed(myMemos) { index, item ->
                    val isFirstItem = index == listState.firstVisibleItemIndex
                    val isFifthItem  = index == listState.firstVisibleItemIndex + 4
                    val targetScale = when {
                        isFirstItem || isFifthItem -> 0.9f
                        index < listState.firstVisibleItemIndex + 5 -> 1f
                        else -> 0f
                    }
                    val animatedScale by animateFloatAsState(
                        targetValue = targetScale,
                        animationSpec = tween(durationMillis = 200)
                    )

                    val itemModifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                        .padding(start = 5.dp, end = 5.dp, top = 10.dp)
                        .graphicsLayer {
                            scaleX = animatedScale
                            scaleY = animatedScale
                        }

                    cardItem(
                        title = item.title,
                        description = item.description.orEmpty(),
                        modifier = itemModifier
                    )

            }
        }
    }


    /**
     * 透明卡片
     */
    @Composable
    private fun cardItem(title: String, description: String) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent.copy(alpha = 0.05f)// 设置透明背景
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp // 取消阴影效果，若仍需轻微阴影可以调整此值
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(start = 5.dp, end = 5.dp, top = 10.dp)
        )
        {
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 3.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = description,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 3.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }

    @Composable
    private fun cardItem(title: String, description: String, modifier: Modifier) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent.copy(alpha = 0.05f)
            ),
            elevation = CardDefaults.cardElevation(
            ),
            modifier = modifier

        )
        {
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 3.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = description,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 3.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }


    /**
     * 增加按钮
     */
    @Composable
    private fun addButton() {
        ElevatedButton(onClick = {

        }, modifier = Modifier.padding(start = 330.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
        }
    }
}