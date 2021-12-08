package yapp.android1.domain.entity

data class AddressEntity(
    val addressName: String,
    val roadAddress: String,
    val address: String,
    val addressDetail: String,
    val lat: Double,
    val lng: Double
)