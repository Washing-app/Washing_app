package com.example.machines.ui

data class BookingFinishUi(
    val mode: BookingFinishMode,
    val bookingIdForPayment: String?,
    val date: String,
    val time: String,
    val machineName: String,
    val washTypeName: String,
    val price: Int
)