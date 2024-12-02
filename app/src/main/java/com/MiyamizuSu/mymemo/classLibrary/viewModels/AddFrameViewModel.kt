package com.MiyamizuSu.mymemo.classLibrary.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import com.MiyamizuSu.mymemo.classLibrary.DataBase.AppDatabase
import com.MiyamizuSu.mymemo.classLibrary.Entity.MemoItem
import com.MiyamizuSu.mymemo.classLibrary.Enums.MemoType
import com.MiyamizuSu.mymemo.classLibrary.Helpers.DateHelper.compareDate
import com.MiyamizuSu.mymemo.classLibrary.Helpers.DateHelper.convertMillisToDate
import com.MiyamizuSu.mymemo.classLibrary.Repository.MemoRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddFrameViewModel : ViewModelBase, ViewModel {
    private val _navToMain: () -> Unit
    private val _memoRepo: MemoRepo

    constructor(navToMain: () -> Unit, database: AppDatabase) {
        _navToMain = navToMain
        _memoRepo = MemoRepo(database.memoDao())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addNewMemo(
        memoTitle: String,
        memoDescription: String,
        memoUnionDate: String,
        memoImg: String = ""
    ) {
        val memoType: MemoType = compareDate(inputDate = memoUnionDate)
        CoroutineScope(Dispatchers.IO).launch {
            _memoRepo.addNewMemo(
                MemoItem(
                    title = memoTitle,
                    description = memoDescription,
                    unionDate = memoUnionDate,
                    memoImg = memoImg,
                    mIndex = memoType
                )
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun MainFrame() {
        var openDialog by remember { mutableStateOf(false) }
        var newMemoTitle by remember { mutableStateOf("") }
        var newMemoUnionDate by remember { mutableStateOf("") }
        var newMemoDescription by remember { mutableStateOf("") }
        var newMemoImg by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val focusRequester = FocusRequester()
        when {
            // ...
            openDialog -> {
                Dialog(onDismissRequest = { }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    )
                    {
                        Column {
                            Text(
                                text = "添加成功",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.3f)
                                    .wrapContentSize(Alignment.Center),
                                textAlign = TextAlign.Center,
                            )
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = "correct",
                                modifier = Modifier
                                    .wrapContentSize(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.6f)
                            )
                        }

                    }
                }
            }
        }
        Column(
            modifier = Modifier.pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            },

            )
        {
            ElevatedButton(
                onClick = _navToMain,
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "backToMainBtn")
            }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val boxWidth = maxWidth
                val boxHeight = maxHeight
                ElevatedCard(
                    modifier = Modifier
                        .padding(start = 0.025f * boxWidth, top = 0.05f * boxHeight)
                        .size(width = 0.95f * boxWidth, height = 0.85f * boxHeight),
                )
                {
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        val cardWidth = maxWidth
                        val cardHeight = maxHeight
                        Column(modifier = Modifier.padding(top = cardHeight*0.1f)) {
                            MyTextField(
                                newValue = newMemoTitle,
                                cardWidth = cardWidth,
                                cardHeight = cardHeight,
                                focusRequester = focusRequester,
                                onNewValueChange = { newMemoTitle = it },
                                textFieldTitle = "请输入标题"
                            )
                            MyDatePickerField(
                                modifier = Modifier
                                    .padding(start = 0.05 * cardWidth)
                                    .fillMaxWidth(0.9f),
                                cardWidth = cardWidth,
                                cardHeight = cardHeight,
                                onNewValueChange = { newMemoUnionDate = it }
                            )
                            MyTextField(
                                newValue = newMemoDescription,
                                cardWidth = cardWidth,
                                cardHeight = cardHeight,
                                focusRequester = focusRequester,
                                onNewValueChange = { newMemoDescription = it },
                                isFullLine = true,
                                textFieldTitle = "请输入简介"
                            )
                            HorizontalDivider(
                                thickness = 2.dp, modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 10.dp, top = 0.05f * cardHeight)
                            )
                            ElevatedButton(onClick =
                            {
                                addNewMemo(
                                    memoUnionDate = newMemoUnionDate,
                                    memoImg = newMemoImg,
                                    memoDescription = newMemoDescription,
                                    memoTitle = newMemoTitle
                                )
                                openDialog = true
                                CoroutineScope(Dispatchers.Main).launch {
                                    delay(1500)
                                    _navToMain()
                                }
                            },
                                modifier = Modifier.padding(
                                    top = 0.01f * cardHeight,
                                    start = 0.77 * cardWidth
                                )) {
                                Icon(Icons.Filled.Check, contentDescription = "check new MemoItem")
                            }
                        }
                    }

                }
            }

        }
    }


    @Composable
    private fun MyTextField(
        newValue: String,
        onNewValueChange: (String) -> Unit,
        cardWidth: Dp, cardHeight: Dp,
        focusRequester: FocusRequester,
        isFullLine: Boolean = false,
        textFieldTitle: String = ""
    ) {
        if (isFullLine) {
            Text(
                text = textFieldTitle,
                modifier = Modifier.padding(start = 0.045f * cardWidth, top = cardHeight * 0.05f)
            )
            OutlinedTextField(
                value = newValue,
                onValueChange = { onNewValueChange(it) },
                textStyle = TextStyle(
                    fontSize = 25.sp,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.surface,
                        offset = Offset(4f, 4f),
                        blurRadius = 2f
                    )
                ),
                minLines = 6,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(start = 0.05 * cardWidth)
                    .fillMaxWidth(0.9f)
            )
        } else {
            TextField(
                value = newValue,
                onValueChange = { onNewValueChange(it) },
                textStyle = TextStyle(
                    fontSize = 25.sp,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.surface,
                        offset = Offset(4f, 4f),
                        blurRadius = 2f
                    )
                ),
                label = {
                    Text(
                        text = textFieldTitle,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(start = 0.05 * cardWidth)
                    .fillMaxWidth(0.9f),
                colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent)
            )
        }

    }

    @Composable
    private fun MyDatePickerField(
        modifier: Modifier = Modifier,
        cardWidth: Dp,
        cardHeight: Dp,
        onNewValueChange: (String) -> Unit
    ) {
        var selectedDate by remember { mutableStateOf<Long?>(null) }
        var showModal by remember { mutableStateOf(false) }
        Text(
            text = "请选择日期",
            modifier = Modifier.padding(start = 0.045f * cardWidth, top = cardHeight * 0.05f)
        )
        OutlinedTextField(
            value = selectedDate?.let { convertMillisToDate(it) } ?: "",
            onValueChange = {
                onNewValueChange(it)
            },
            placeholder = { Text("例如：1970/01/01") },
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select date")
            },
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showModal = true
                        }
                    }
                }
        )

        // 显示日期选择对话框
        if (showModal) {
            MyDatePickerModal(
                onDateSelected = { dateMillis ->
                    selectedDate = dateMillis
                    dateMillis?.let { convertMillisToDate(it) }?.let { onNewValueChange(it) }
                    showModal = false
                },
                onDismiss = { showModal = false }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MyDatePickerModal(
        onDateSelected: (Long?) -> Unit,
        onDismiss: () -> Unit
    ) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

}