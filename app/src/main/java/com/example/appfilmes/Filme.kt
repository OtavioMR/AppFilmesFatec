package com.example.appfilmes

data class Filme(
    val id: Long = 0,
    val titulo: String,
    val tipo: String,
    val inicio: String,
    val fim: String,
    val opiniao: String,
    val nota: Int? = null
)
