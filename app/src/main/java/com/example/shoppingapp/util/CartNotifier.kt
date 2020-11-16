package com.example.shoppingapp.util

import com.here.sdk.search.Place

interface CartNotifier {
    fun notifyChange()
}

interface MapNotifier{
    fun notifyChange(places: List<Place?>? =null)
}