package com.pedrosant.cashtrack.extensions

fun String.extractTokenValue():String = this.substringAfter("Bearer ")