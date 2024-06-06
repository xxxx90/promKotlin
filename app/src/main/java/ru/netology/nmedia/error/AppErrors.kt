package ru.netology.nmedia.error

sealed class AppErrors : RuntimeException()

data class ApiError (val code: Int): AppErrors()

object NetworkException : AppErrors()
object UnknownException : AppErrors()