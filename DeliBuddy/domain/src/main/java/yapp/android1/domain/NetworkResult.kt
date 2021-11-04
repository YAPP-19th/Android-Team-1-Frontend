package yapp.android1.domain

import yapp.android1.domain.entity.NetworkError

sealed class NetworkResult<out T>() {
    class Success<T>(val data: T) : NetworkResult<T>()
    class Error(val errorType: NetworkError) : NetworkResult<Nothing>()
}