package yapp.android1.delibuddy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import yapp.android1.domain.entity.AddressEntity

@Parcelize
data class Address(
    val addressName: String,
    val roadAddress: String,
    val address: String,
    var addressDetail: String,
    val lat: Double,
    val lng: Double
) : Parcelable {
    companion object {
        fun mapToAddress(entity: AddressEntity): Address {
            return Address(
                addressName = entity.addressName,
                roadAddress = entity.roadAddress,
                address = entity.address,
                addressDetail = "",
                lat = entity.lat,
                lng = entity.lng
            )
        }
    }
}