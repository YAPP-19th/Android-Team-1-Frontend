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
        val DEFAULT = Address(
            addressName = "서울역",
            roadAddress = "서울특별시 중구 한강대로 405",
            address = "",
            addressDetail = "",
            lat = 37.5283169,
            lng = 126.9294254
        )

        fun mapToAddress(entity: AddressEntity): Address {
            return Address(
                addressName = entity.addressName,
                roadAddress = entity.roadAddress,
                address = entity.address,
                addressDetail = entity.addressDetail,
                lat = entity.lat,
                lng = entity.lng
            )
        }
    }
}