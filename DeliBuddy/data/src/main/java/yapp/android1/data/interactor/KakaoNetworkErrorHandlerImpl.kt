package yapp.android1.data.interactor

import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import yapp.android1.domain.entity.NetworkError
import yapp.android1.domain.interactor.KakaoNetworkErrorHandler
import java.net.SocketTimeoutException
import javax.inject.Inject

class KakaoNetworkErrorHandlerImpl @Inject constructor(
    private val retrofit: Retrofit
) : KakaoNetworkErrorHandler {

    override fun getError(exception: Throwable): NetworkError {
        return when (exception) {
            is SocketTimeoutException -> NetworkError.Timeout
            is HttpException -> {
                when (exception.code()) {
                    in 500..599 -> NetworkError.InternalServer
                    in 400..499 -> {
                        val code = exception.code()
                        val message = extractErrorMessage(exception.response())
                        NetworkError.BadRequest(code, message)
                    }
                    else -> NetworkError.Unknown
                }
            }
            else -> NetworkError.Unknown
        }
    }

    private fun extractErrorMessage(response: Response<*>?): String {
        val converter = retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            arrayOfNulls(0)
        )
        val baseResponse = converter.convert(response?.errorBody()!!)
        return baseResponse?.message.orEmpty()
    }

    private data class ErrorResponse(val message: String, val documentation_url: String)
}