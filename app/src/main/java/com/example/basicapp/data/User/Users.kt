package com.example.basicapp.data.User

import com.example.basicapp.db.userdetail.UserDetailEntity

data class Users(
    val info: Info,
    val results: List<Result>
)

fun Users.toEntity(results: List<Result>): List<UserDetailEntity> {
    val res = mutableListOf<UserDetailEntity>()
    for (result in results) {
        res.add(
            UserDetailEntity(
                profilePic = result.picture.large,
                fullName = result.name.first + " " + result.name.last,
                firstName = result.name.first,
                lastName = result.name.last,
                gender = result.gender,
                age = result.dob.age.toString(),
                email = result.email,
                ph = result.phone,
                lat = result.location.coordinates.latitude,
                lon = result.location.coordinates.longitude
            )
        )
    }
    return res
}