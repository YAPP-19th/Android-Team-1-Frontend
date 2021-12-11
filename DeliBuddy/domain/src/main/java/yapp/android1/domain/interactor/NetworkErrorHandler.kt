package yapp.android1.domain.interactor

import yapp.android1.domain.entity.NetworkError


interface DeliBuddyNetworkErrorHandler {
    fun getError(exception: Throwable): NetworkError
}

interface KakaoNetworkErrorHandler {
    fun getError(exception: Throwable): NetworkError
}