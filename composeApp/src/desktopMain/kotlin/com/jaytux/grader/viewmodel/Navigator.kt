package com.jaytux.grader.viewmodel

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jaytux.grader.ui.ChevronLeft
import kotlin.reflect.KClass

class Navigator private constructor(
    private var _start: IDestination,
    private val _typeMap: Map<KClass<out IDestination>, RenderData>
) : ViewModel() {
    interface IDestination

    private data class Entry<T : IDestination>(val dest: T, val token: NavToken)
    inner class NavToken {
        fun navTo(target: IDestination) { this@Navigator.navTo(target) }
        fun back() { this@Navigator.back() }
        inline fun <reified T : IDestination> backTo() { this@Navigator.backTo<T>() }
        fun rewriteHistory(t: IDestination) { this@Navigator.rewriteHistory(t) }
    }
    internal data class RenderData(
        val header: @Composable (IDestination) -> Unit,
        val renderer: @Composable (IDestination, NavToken) -> Unit
    )

    private val _stack = mutableStateOf(listOf<Entry<*>>(Entry(_start, NavToken())))

    fun navTo(target: IDestination) {
        _stack.value += Entry(target, NavToken())
    }

    fun back() {
        if(_stack.value.size > 1) _stack.value = _stack.value.dropLast(1)
    }

    fun <T : IDestination> backTo(cls: KClass<T>) {
        val idx = _stack.value.indexOfLast { entry -> cls.isInstance(entry.dest) }
        if(idx != -1 && idx != _stack.value.lastIndex) {
            _stack.value = _stack.value.take(idx + 1)
        }
    }

    fun rewriteHistory(t: IDestination) {
        _stack.value = listOf(Entry(t, NavToken()))
        _start = t
    }

    inline fun <reified T : IDestination> backTo() = backTo(T::class)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun DisplayScaffold() {
        val state = remember { SnackbarHostState() }
        val stack by _stack
        val (top, render) = remember(stack) {
            val top = stack.last()
            val render = _typeMap[top.dest::class]
                ?: throw IllegalStateException("No renderer for destination of type ${top.dest::class.simpleName}")
            top to render
        }
        val snackVM = viewModel<SnackVM>()
        snackVM.Launcher(state)

        BackHandler { back() }
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    title = { render.header(top.dest) },
                    navigationIcon = {
                        IconButton({ back() }, enabled = top != _start) {
                            Icon(ChevronLeft, contentDescription = "Back")
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(state)
            }
        ) { insets ->
            Surface(Modifier.padding(insets), color = MaterialTheme.colorScheme.surface) {
                render.renderer(top.dest, top.token)
            }
        }
    }

    @DslMarker
    annotation class NavigatorDslMarker

    @NavigatorDslMarker
    class Builder internal constructor(
        private val _onBuild: (IDestination, Map<KClass<out IDestination>, RenderData>) -> Navigator
    ) {
        private val _typeMap = mutableMapOf<KClass<out IDestination>, RenderData>()
        private lateinit var _start: IDestination

        fun <T : IDestination> composable(cls: KClass<T>, title: @Composable (T) -> Unit, renderer: @Composable (T, NavToken) -> Unit) {
            _typeMap[cls]?.let {
                throw IllegalArgumentException("Destination of type ${cls.simpleName} is already registered.")
            } ?: run {
                _typeMap[cls] = RenderData({
                    @Suppress("UNCHECKED_CAST")
                    title(it as T)
                }) { d, t ->
                    @Suppress("UNCHECKED_CAST")
                    renderer(d as T, t)
                }
            }
        }

        inline fun <reified T : IDestination> composable(noinline title: @Composable (T) -> Unit, noinline renderer: @Composable (T, NavToken) -> Unit) {
            composable(T::class, title, renderer)
        }

        fun start(start: IDestination) {
            if(this::_start.isInitialized) throw IllegalStateException("Start destination is already set.")
            _start = start
        }

        internal fun build(): Navigator {
            if(!this::_start.isInitialized) throw IllegalStateException("Start destination is not set.")
            return _onBuild(_start, _typeMap)
        }
    }

    companion object {
        fun build(block: Builder.() -> Unit): Navigator =
            Builder { start, map -> Navigator(start, map) }
                .apply { block() }.build()

        @Composable
        fun NavHost(initial: IDestination, block: Builder.() -> Unit) {
            val vm = viewModel {
                build {
                    block()
                    start(initial)
                }
            }

            vm.DisplayScaffold()
        }
    }
}