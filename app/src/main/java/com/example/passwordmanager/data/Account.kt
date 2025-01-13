package com.example.passwordmanager.data

class Account(
    private var name: String = "Untitled",
    private var password: String = ""
) {

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
