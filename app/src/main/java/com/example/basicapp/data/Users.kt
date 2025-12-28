package com.example.basicapp.data

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
                gender = result.gender,
                dob = result.dob.age.toString(),
                email = result.email,
                ph = result.phone
            )
        )
    }
    return res
}