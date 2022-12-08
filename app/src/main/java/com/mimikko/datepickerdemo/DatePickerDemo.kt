package com.mimikko.datepickerdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

@Composable
fun DatePickerColumn(
    pairList: List<Pair<Int, String>>,
    defaultIndex: Int = 0,
    itemHeight: Dp = 50.dp,
    itemWidth: Dp = 50.dp,
    focusColor: Color = MaterialTheme.colors.primary,
    unfocusedColor: Color = Color(0xFFC5C7CF),
    changeAction: (Int) -> Unit
) {
    val dataPickerCoroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()


    LaunchedEffect(defaultIndex) {
        var initIndex = 0
        for (index in pairList.indices) {
            if (defaultIndex == pairList[index].first) {
                initIndex = index
                break
            }
        }
        dataPickerCoroutineScope.launch {
            listState.scrollToItem(initIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(top = itemHeight / 2, bottom = itemHeight / 2)
            .height(itemHeight * 5)
    ) {
        item {
            Spacer(Modifier.height(itemHeight))
        }
        item {
            Spacer(Modifier.height(itemHeight))
        }
        itemsIndexed(items = pairList, key = { _, pair -> pair.first }) { index, pair ->
            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .width(itemWidth)
                    .clickable {
                        changeAction(pair.first)
                    }
                    .padding(horizontal = 5.dp),
                Alignment.Center
            ) {
                Text(
                    text = pair.second,
                    color = if (listState.firstVisibleItemIndex == index) focusColor
                    else unfocusedColor
                )
            }
        }
        item {
            Spacer(Modifier.height(itemHeight))
        }
        item {
            Spacer(Modifier.height(itemHeight))
        }
    }

    if (listState.isScrollInProgress) {
        DisposableEffect(Unit) {
            onDispose {
                changeAction(pairList[listState.firstVisibleItemIndex].first)
                dataPickerCoroutineScope.launch {
                    listState.animateScrollToItem(listState.firstVisibleItemIndex)
                }
            }
        }
    }
}

@Composable
internal fun SelectDateTime(modifier: Modifier = Modifier) {
    var dateTime by remember {
        mutableStateOf("--")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .offset(y = 120.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp)
    )
    {
        Text(
            text = dateTime,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally)
        )
        DataTimePicker(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            dateTime = it
        }
    }
}

@Composable
fun DataTimePicker(
    modifier: Modifier = Modifier,
    updateAction:(String)->Unit
) {

    val itemHeight = 50.dp
    val viewModel = DatePickerViewModel()

    var selectedYear by remember { viewModel.selectedYear }
    var selectedMonth by remember { viewModel.selectedMonth }
    var selectedDay by remember { viewModel.selectedDay }
    var selectedHour by remember { viewModel.selectedHour }
    var selectedMinute by remember { viewModel.selectedMinute }
    var selectedSecond by remember { viewModel.selectedSecond }

    LaunchedEffect(Unit) {
        viewModel.initDate()
    }
    LaunchedEffect(key1 = selectedMonth) {
        viewModel.updateDays()
    }

    // 显示信息
    val timeStr = "${selectedYear}-${selectedMonth}-${selectedDay} ${selectedHour}:${selectedMinute}:${selectedSecond}"

    LaunchedEffect(key1 = timeStr) {
        updateAction(timeStr)
    }

    Box(
        modifier = modifier,
        Alignment.Center
    ) {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface),
            Arrangement.SpaceEvenly,
            Alignment.CenterVertically
        ) {

            DatePickerColumn(pairList = viewModel.years, itemWidth = 70.dp, defaultIndex = selectedYear) {
                selectedYear = it
            }

            DatePickerColumn(pairList = viewModel.months, itemWidth = 50.dp, defaultIndex = selectedMonth) {
                selectedMonth = it
            }

            DatePickerColumn(pairList = viewModel.days.toImmutableList(), itemWidth = 50.dp, defaultIndex = selectedDay) {
                selectedDay = it
            }

            DatePickerColumn(pairList = viewModel.hours, itemWidth = 50.dp, defaultIndex = selectedHour) {
                selectedHour = it
            }

            DatePickerColumn(pairList = viewModel.minutes, itemWidth = 50.dp, defaultIndex = selectedMinute) {
                selectedMinute = it
            }

            DatePickerColumn(pairList = viewModel.seconds, itemWidth = 50.dp, defaultIndex = selectedSecond) {
                selectedSecond = it
            }
        }

        //  放在后面使得不会被遮住
        Column {
            Divider(
                Modifier.padding(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = itemHeight
                ),
                thickness = 1.dp
            )
            Divider(
                Modifier.padding(
                    start = 15.dp,
                    end = 15.dp
                ),
                thickness = 1.dp
            )
        }
    }
}
