package ru.skillbranch.sbdelivery.screens.demo.logic

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.skillbranch.sbdelivery.screens.root.logic.IEffHandler

object DemoFeature {
    fun initialState(): State = State()
    fun initialEffects(): Set<Eff> = emptySet()

    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState())
    val state
        get() = _state.asStateFlow()
    private lateinit var _scope: CoroutineScope

    private val mutations: MutableSharedFlow<Msg> = MutableSharedFlow()

    fun mutate(mutation: Msg) {
        _scope.launch {
            mutations.emit(mutation)
        }
    }

    fun listen(scope: CoroutineScope, effHandler: IEffHandler<Eff, Msg>) {
        _scope = scope
        _scope.launch {
            mutations
                .onEach { Log.e("DemoEffHandler", "MUTATION $it") }
                .scan(initialState() to initialEffects()) { (s, _), m ->
                    s.reduce(m)
                }
                .collect { (s, effs) ->
                    _state.emit(s)
                    effs.forEach { eff ->
                        launch {
                            effHandler.handle(eff, ::mutate)
                        }
                    }
                }
        }
    }

    data class State(val count: Int = 0, val isLoading:Boolean = false)

    sealed class Msg {
        object NextRandom: Msg()
        object Clear: Msg()
        data class ShowValue(val value: Int): Msg()
    }

    sealed class Eff {
        object NextGenerate: Eff()
        object StopGenerate: Eff()
    }
}