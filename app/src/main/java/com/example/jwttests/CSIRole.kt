package com.example.jwttests

import android.util.Log

enum class CSIRole (val role: String) {
    USER("user"), VOLUNTEER("volunteer"), ADMIN("admin");

    fun isAdmin(): Boolean = this == ADMIN
}

fun tokenRoleToCsiRole(tokenRole: String): CSIRole {
    Log.d("TAG", tokenRole)
    return when (tokenRole) {
        CSIRole.USER.role -> CSIRole.USER
        CSIRole.VOLUNTEER.role -> CSIRole.VOLUNTEER
        CSIRole.ADMIN.role -> CSIRole.ADMIN
        else -> CSIRole.USER
    }
}
