package ru.skillbranch.sbdelivery.screens.demo.logic

import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.skillbranch.sbdelivery.screens.root.logic.IEffHandler
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class DemoEffHandler: IEffHandler<DemoFeature.Eff, DemoFeature.Msg> {

    private var localJob: Job = Job()

    override suspend fun handle(effect: DemoFeature.Eff, commit: (DemoFeature.Msg) -> Unit) {
        Log.e("DemoEff", "EFF $effect")
        when(effect){
            is DemoFeature.Eff.NextGenerate -> {
                withContext(coroutineContext + localJob) {
                    delay(3000)
                    val rnd = (0..100).random()
                    commit(DemoFeature.Msg.ShowValue(value = rnd))
                }
            }

            is DemoFeature.Eff.StopGenerate -> {
                localJob.cancel()
                localJob = Job()
            }
        }
    }
}