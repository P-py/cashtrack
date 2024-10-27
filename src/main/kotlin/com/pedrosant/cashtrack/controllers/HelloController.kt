package com.pedrosant.cashtrack.controllers

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
@CrossOrigin
class HelloController {
    @GetMapping
    fun hello():String{
        return "Hello World! The application is UP AND RUNNING!!"
    }
}