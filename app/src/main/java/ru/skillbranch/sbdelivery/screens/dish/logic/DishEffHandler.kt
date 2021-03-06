package ru.skillbranch.sbdelivery.screens.dish.logic

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import ru.skillbranch.sbdelivery.repository.DishRepository
import ru.skillbranch.sbdelivery.screens.root.logic.Eff
import ru.skillbranch.sbdelivery.screens.root.logic.IEffHandler
import ru.skillbranch.sbdelivery.screens.root.logic.Msg
import java.lang.IllegalStateException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class DishEffHandler @Inject constructor(
    private val repository: DishRepository,
    private val notifyChannel: Channel<Eff.Notification>,
//    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : IEffHandler<DishFeature.Eff, Msg> {

    override suspend fun handle(effect: DishFeature.Eff, commit: (Msg) -> Unit) {

//        if (localJob == null) localJob = Job()
//        withContext(localJob!! + dispatcher) {
            when (effect) {
                is DishFeature.Eff.AddToCart -> {
                    repository.addToCart(effect.id, effect.count)
                    val count = repository.cartCount()
                    commit(Msg.UpdateCartCount(count))
                    notifyChannel.send(Eff.Notification.Error("В корзину добавлено $count товаров"))
                }
                is DishFeature.Eff.LoadDish -> {
                    val dish = repository.findDish(effect.dishId)
                    commit(DishFeature.Msg.ShowDish(dish).toMsg())
                }
                is DishFeature.Eff.LoadReviews -> {
                    try {
                        val reviews = repository.loadReviews(effect.dishId)
                        commit(DishFeature.Msg.ShowReviews(reviews).toMsg())
                    } catch (t: Throwable) {
                        notifyChannel.send(Eff.Notification.Error(t.message ?: "something error"))
                    }
                }
                is DishFeature.Eff.SendReview -> {
                    try {
                        repository.sendReview(effect.id, effect.rating, effect.review)
                        notifyChannel.send(Eff.Notification.Text("Отзыв успешно отправлен"))
                    } catch (t: Throwable) {
                        notifyChannel.send(Eff.Notification.Error(t.message ?: "something error"))
                    }
                }
//                is DishFeature.Eff.Terminate -> {
//                    localJob?.cancel("Terminate coroutine scope")
//                    localJob = null
//                }
//            }
        }

    }

//    companion object  {
//        private var localJob: Job? = null
//    }
    private fun DishFeature.Msg.toMsg(): Msg = Msg.Dish(this)

}



