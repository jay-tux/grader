package com.jaytux.grader

sealed class Either<out E , out V> {
    class Error<E>(val error: E) : Either<E, Nothing>() {
        override fun toString(): String = "Error($error)"
    }
    class Value<V>(val value: V) : Either<Nothing, V>() {
        override fun toString(): String = "Value($value)"
    }

    fun isError() = this is Error<E>
    fun isValue() = this is Value<V>

    fun <R> map(f: (V) -> R) = when(this) {
        is Error -> this
        is Value -> Value(f(value))
    }

    suspend fun <R> mapSuspend(f: suspend (V) -> R) = when(this) {
        is Error -> this
        is Value -> Value(f(value))
    }

    fun <R> mapError(f: (E) -> R) = when(this) {
        is Error -> Error(f(error))
        is Value -> this
    }

    suspend fun <R> mapErrorSuspend(f: suspend (E) -> R) = when(this) {
        is Error -> Error(f(error))
        is Value -> this
    }

    fun <R> fold(fError: (E) -> R, fValue: (V) -> R): R = when(this) {
        is Error -> fError(error)
        is Value -> fValue(value)
    }

    suspend fun <R> foldSuspend(fError: suspend (E) -> R, fValue: suspend (V) -> R): R = when(this) {
        is Error -> fError(error)
        is Value -> fValue(value)
    }

    companion object {
        fun <E> E.error(): Either<E, Nothing> = Error(this)
        fun <V> V.value(): Either<Nothing, V> = Value(this)
    }
}