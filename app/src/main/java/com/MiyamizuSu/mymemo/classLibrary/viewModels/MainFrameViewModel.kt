package com.MiyamizuSu.mymemo.classLibrary.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import com.MiyamizuSu.mymemo.classLibrary.Helpers.DateHelper.calculateDaysFromToday
import com.MiyamizuSu.mymemo.classLibrary.Repository.MemoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainFrameViewModel : ViewModelBase, ViewModel {

    private val _memoRepo: MemoRepo
    private val _navToAdd:()->Unit


    constructor(database: AppDatabase,navToAdd:()->Unit) {
        this._navToAdd=navToAdd
        this._memoRepo = MemoRepo(database.memoDao())
    }

    /**
     * 上半部分组件容器
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun UpperFrame() {
        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(1.0f)
                .fillMaxHeight(0.5f),
        ) {
            AddButton()
            DateFrame()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun mainFrame(){
        var myMemos by remember { mutableStateOf<List<MemoItem>>(emptyList()) }
        LaunchedEffect(Unit) {
            myMemos = _memoRepo.getAllMemo()
            for (myMemo in myMemos){
                if(myMemo.mIndex==MemoType.EVER){

                }
                else{
                    _memoRepo.updateMemo(myMemo)
                }
            }
        }
        var openDialog by remember { mutableStateOf(false) }
        var memo by remember { mutableStateOf(MemoItem()) }
        when {
            // ...
            openDialog -> {
                Dialog(onDismissRequest = { openDialog=false }, properties = DialogProperties(dismissOnClickOutside = true)) {
                    Card(
                        modifier = Modifier
                            .width(700.dp)
                            .height(400.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    )
                    {
                        BoxWithConstraints(Modifier.fillMaxWidth().fillMaxHeight()) {
                            val cardWidth=maxWidth
                            val cardHeight=maxHeight
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .padding(top = cardHeight * 0.1f)
                                        .fillMaxHeight() // 确保 Column 占满可用高度
                                ) {
                                    Text(
                                        fontSize = 34.sp,
                                        text = memo.title,
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                    )
                                    Text(
                                        text = memo.unionDate,
                                        modifier = Modifier
                                            .padding(start = 0.65f * cardWidth, top = 0.1 * cardHeight)
                                    )
                                    Text(
                                        text = memo.description,
                                        modifier = Modifier.padding(start = 0.05 * cardWidth)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                                HorizontalDivider(
                                    thickness = 2.dp,
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 10.dp, top = 0.05f * cardHeight)
                                )
                                ElevatedButton(
                                    onClick = {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            _memoRepo.deleteMemo(memo)
                                            myMemos = myMemos.filterNot { it.uuid == memo.uuid }
                                        }
                                        openDialog=false
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(3.dp),
                                )
                                {
                                    Icon(Icons.Filled.Delete, contentDescription = "deleteItem.")
                                }
                            }
                        }

                    }
                }
            }
        }
        val updateMessage: (MemoItem) -> Unit = { myMemo->
            memo=myMemo
            openDialog=true
        }

        Column(modifier = Modifier
            .fillMaxSize(1.0f)
            .background(color = MaterialTheme.colorScheme.surface)
            .graphicsLayer {

            }
        )
        {
            UpperFrame()
            HorizontalDivider(thickness = 2.dp, modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
            )
            DownFrame(
                handleCardDoubleTap = updateMessage,
                myMemos = myMemos
            )
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
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun DownFrame(handleCardDoubleTap:(MemoItem)->Unit,myMemos:List<MemoItem>) {


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
                .fillMaxHeight(0.95f)
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

                    CardItem(
                        modifier = itemModifier,
                        unionDate = item.unionDate,
                        handleDoubleTap = { memo ->
                            handleCardDoubleTap(memo)
                        },
                        memo = item
                    )

            }
        }
    }


    /**
     * 透明卡片
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun CardItem(memo:MemoItem, modifier: Modifier,unionDate:String,handleDoubleTap:(MemoItem)->Unit) {
        val dayBet=calculateDaysFromToday(unionDate)
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent.copy(alpha = 0.05f)
            ),
            elevation = CardDefaults.cardElevation(
            ),
            modifier = modifier.pointerInput(Unit){
                detectTapGestures(onDoubleTap = {
                    offset: Offset ->
                    handleDoubleTap(memo)
                } )
            }

        )
        {
            Column(
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 左侧的 Text
                    Text(
                        text = memo.title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        overflow = TextOverflow.Clip ,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .wrapContentWidth(Alignment.End)
                    )
                    {
                        SuggestionChip(
                            onClick = { },
                            label = {
                                when (memo.mIndex) {
                                    MemoType.FUTURE -> {
                                        Text(text = "距今${dayBet}天", color = Color.White)
                                    }
                                    MemoType.IMA -> {
                                        Text(text = "现在！", color = Color.White,modifier=Modifier.fillMaxWidth(1.0f), textAlign = TextAlign.Center)
                                    }
                                    else -> {
                                        Text(text = "过去${dayBet}天", color = Color.White)
                                    }
                                }
                            },
                            modifier = Modifier.size(width = 90.dp, height = 20.dp),
                            border = BorderStroke(0.dp, Color.Transparent),
                            colors = when (memo.mIndex) {
                                MemoType.FUTURE -> SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0x808FCEE3),
                                )
                                MemoType.IMA -> SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0x806EC02D),
                                )
                                else -> SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0x80FFA500),
                                )
                            }
                        )
                        Text(
                            text = unionDate,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Text(
                    text = memo.description,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 9.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }


    /**
     * 增加按钮
     */
    @Composable
    private fun AddButton() {
        ElevatedButton(onClick = _navToAdd, modifier = Modifier.padding(start = 330.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
        }
    }
    data class DoubleTapData(
        val offset: Offset,
        val memoDetail:MemoItem
    )
}