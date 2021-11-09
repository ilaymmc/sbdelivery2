package ru.skillbranch.sbdelivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.skillbranch.sbdelivery.screens.demo.logic.DemoEffHandler
import ru.skillbranch.sbdelivery.screens.demo.logic.DemoFeature

class DemoViewModel: ViewModel() {
    val feature = DemoFeature

    init {
        feature.listen(viewModelScope, DemoEffHandler())
    }

    fun accept(msg: DemoFeature.Msg) {
        feature.mutate(msg)
    }

}