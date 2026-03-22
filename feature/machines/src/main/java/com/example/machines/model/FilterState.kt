package com.example.machines.model

data class FilterState(
    val temperatures: Set<Int> = emptySet(),
    val speeds: Set<Int> = emptySet(),
    val minDuration: Int = 15,
    val maxDuration: Int = 165
)