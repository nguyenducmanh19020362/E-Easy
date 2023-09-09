package com.example.mydictionary.screen.setting

sealed class Route(val route: String) {
    object Home: Route("home")
    object Words: Route("words")
}