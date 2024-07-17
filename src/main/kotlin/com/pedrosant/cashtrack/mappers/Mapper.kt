package com.pedrosant.cashtrack.mappers

interface Mapper<C, V, E>{
    // C -> class
    // V -> view
    // E -> entry
    fun mapView(c:C):V
    fun mapEntry(e:E):C
}