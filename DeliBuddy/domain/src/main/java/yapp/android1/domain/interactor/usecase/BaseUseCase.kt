package yapp.android1.domain.interactor.usecase


abstract class BaseUseCase<out Type : Any, in Params> {

    abstract suspend fun run(params: Params): Type

    suspend operator fun invoke(params: Params) = run(params)

    class None

}