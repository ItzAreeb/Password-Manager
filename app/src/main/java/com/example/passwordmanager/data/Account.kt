package com.example.passwordmanager.data

import java.io.Serializable

class Account(
    private var name: String = "Untitled",
    private var password: String = ""
) : Serializable {
    fun setName(name: String) {
        this.name = name
    }
    fun getName(): String {
        return name
    }
    fun setPassword(password: String) {
        this.password = password
    }
    fun getPassword(): String {
        return password
    }
}