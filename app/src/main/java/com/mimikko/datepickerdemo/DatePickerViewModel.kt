package com.mimikko.datepickerdemo

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import java.util.*

internal class DatePickerViewModel() {


    val selectedYear = mutableStateOf(0)
    val years = ArrayList<Pair<Int, String>>().apply {
        for (i in Date().getYearr() downTo 1980) {
            add(Pair(i, "${i}年"))
        }
    }

    val selectedMonth = mutableStateOf(0)
    val months = ArrayList<Pair<Int, String>>(12).apply {
        for (i in 1..12) {
            add(Pair(i, "${i}月"))
        }
    }

    val selectedDay = mutableStateOf(0)
    val days = mutableStateListOf<Pair<Int, String>>()

    val selectedHour = mutableStateOf(0)
    val hours = ArrayList<Pair<Int, String>>(24).apply {
        for (i in 0..23) {
            add(Pair(i, "${i}时"))
        }
    }

    val selectedMinute = mutableStateOf(0)
    val minutes = ArrayList<Pair<Int, String>>(60).apply {
        for (i in 0..59) {
            add(Pair(i, "${i}分"))
        }
    }

    val selectedSecond = mutableStateOf(0)
    val seconds = ArrayList<Pair<Int, String>>(60).apply {
        for (i in 0..59) {
            add(Pair(i, "${i}秒"))
        }
    }

    // MARK: ==== Event ====
    internal fun initDate(date: Date = Date()) {
        selectedYear.value = date.getYearr()
        selectedMonth.value = date.getMonthr()
        selectedDay.value = date.getDayOfMonth()
        selectedHour.value = date.getHour()
        selectedMinute.value = date.getMinute()
        selectedSecond.value = date.getSecond()
    }

    internal fun updateDays() {
        val monthCount = DateUtil.getDayCountOfMonth(selectedYear.value, selectedMonth.value)
        days.clear()
        days.addAll(
            when (monthCount) {
                28 -> day28
                29 -> day29
                30 -> day30
                else -> day31
            }
        )
    }

    // MARK: ==== Tools ====

    //  提前定义好
    private val day31 = ArrayList<Pair<Int, String>>().apply {
        for (i in 1..31)
            add(Pair(i, "${i}日"))
    }
    private val day30 = ArrayList<Pair<Int, String>>().apply {
        for (i in 1..30)
            add(Pair(i, "${i}日"))
    }
    private val day29 = ArrayList<Pair<Int, String>>().apply {
        for (i in 1..29)
            add(Pair(i, "${i}日"))
    }
    private val day28 = ArrayList<Pair<Int, String>>().apply {
        for (i in 1..28)
            add(Pair(i, "${i}日"))
    }

}