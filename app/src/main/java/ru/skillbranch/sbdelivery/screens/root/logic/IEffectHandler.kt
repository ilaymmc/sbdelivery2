package ru.skillbranch.sbdelivery.screens.root.logic

interface IEffHandler<E, M> {
    suspend fun handle(effect: E, commit: (M) -> Unit)
}