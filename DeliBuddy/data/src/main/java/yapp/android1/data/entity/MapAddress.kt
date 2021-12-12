package yapp.android1.data.entity

import yapp.android1.domain.entity.AddressEntity

object MapAddress {
    fun toAddress(keywordDocument: KeywordDocument): AddressEntity {
        keywordDocument.apply {
            return AddressEntity(
                addressName = placeName,
                roadAddress = roadAddressName,
                address = addressName,
                addressDetail = "",
                lat = y.toDouble(),
                lng = x.toDouble()
            )
        }
    }

    fun toAddress(addressDocument: AddressDocument): AddressEntity {
        addressDocument.apply {
            return AddressEntity(
                addressName = addressName,
                roadAddress = roadAddress.addressName,
                address = address.addressName,
                addressDetail = "",
                lat = y.toDouble(),
                lng = x.toDouble()
            )
        }
    }

    fun toAddress(
        lat: Double,
        lng: Double,
        coordAddressDocument: CoordAddressDocument,
    ): AddressEntity {
        coordAddressDocument.apply {
            return AddressEntity(
                addressName = coordRoadAddress?.addressName ?: coordAddress.addressName,
                address = coordAddress.addressName,
                roadAddress = coordRoadAddress?.addressName ?: coordAddress.addressName,
                addressDetail = "",
                lat = lat,
                lng = lng
            )
        }
    }
}
