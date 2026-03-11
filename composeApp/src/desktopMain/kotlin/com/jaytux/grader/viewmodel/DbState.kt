package com.jaytux.grader.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun <T> MutableState<T>.immutable(): State<T> = this

class RawDbState<T: Any>(private val loader: (Transaction.() -> List<T>)) {
    private val rawEntities by lazy {
        mutableStateOf(transaction { loader() })
    }

    val entities = rawEntities.immutable()
    fun refresh() {
        rawEntities.value = transaction { loader() }
    }
}

class RawDbFocusableSingleState<TIn, TOut: Any>(private val loader: (Transaction.(TIn) -> TOut?)) {
    private var _input: TIn? = null
    private val rawEntity by lazy {
        mutableStateOf<TOut?>(null)
    }

    val entity: State<TOut?> = rawEntity.immutable()

    fun focus(input: TIn) {
        _input = input
        rawEntity.value = transaction { loader(input) }
    }

    fun unfocus() {
        _input = null
        rawEntity.value = null
    }

    fun refresh() {
        rawEntity.value = transaction { _input?.let { loader(it) } }
    }
}

class RawDbFocusableState<TIn, TOut: Any>(private val loader: (Transaction.(TIn) -> List<TOut>)) {
    private var _input: TIn? = null
    private val rawEntities by lazy {
        mutableStateOf<List<TOut>?>(null)
    }

    val entities: State<List<TOut>?> = rawEntities.immutable()

    fun focus(input: TIn) {
        _input = input
        rawEntities.value = transaction { loader(input) }
    }

    fun unfocus() {
        _input = null
        rawEntities.value = null
    }

    fun refresh() {
        rawEntities.value = transaction { _input?.let { loader(it) } }
    }
}