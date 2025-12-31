package com.example.basicapp.data.User

data class Location(
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val state: String,
    val street: Street,
    val timezone: Timezone
)