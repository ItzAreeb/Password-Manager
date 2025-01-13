package com.example.passwordmanager.data

class AccountList(
    private val accounts: MutableList<Account> = mutableListOf()
) {
    fun addAccount(account: Account) {
        accounts.add(account)
    }
    fun removeAccount(account: Account) {
        accounts.remove(account)
    }
    fun getAccounts(): List<Account> {
        return accounts.toList()
    }
    fun getAccount(index: Int): Account {
        return accounts[index]
    }
}