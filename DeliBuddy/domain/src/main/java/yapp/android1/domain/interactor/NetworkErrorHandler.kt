package yapp.android1.domain.interactor

import yapp.android1.domain.entity.NetworkError


interface NetworkErrorHandler {
    fun getError(exception: Throwable): NetworkError
}