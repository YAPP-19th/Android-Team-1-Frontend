package yapp.android1.domain.entity


sealed class NetworkError {
    object Network : NetworkError()
    object Timeout : NetworkError()
    object InternalServer : NetworkError()
    object EmptyItems : NetworkError()
    class BadRequest(val code: Int, val message: String) : NetworkError()
    class Unknown(val throwable: Throwable) : NetworkError()
}