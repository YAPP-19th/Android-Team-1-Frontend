package yapp.android1.domain.entity

import java.io.Serializable

data class Address(
    val addressName: String,
    val roadAddress: String,
    val address: String,
    val lat: Double,
    val lng: Double
) : Serializable