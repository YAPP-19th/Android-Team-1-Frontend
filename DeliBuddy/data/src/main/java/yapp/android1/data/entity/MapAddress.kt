package yapp.android1.data.entity

import yapp.android1.domain.entity.Address

object MapAddress {
    fun toAddress(keywordDocument: KeywordDocument): Address {
        keywordDocument.apply {
            return Address(
                addressName = placeName,
                roadAddress = roadAddressName,
                address = addressName,
                lat = x.toDouble(),
                lon = y.toDouble()
            )
        }
    }

    fun toAddress(addressDocument: AddressDocument): Address {
        addressDocument.apply {
            return Address(
                addressName = addressName,
                roadAddress = roadAddress.addressName,
                address = address.addressName,
                lat = x.toDouble(),
                lon = y.toDouble()
            )
        }
    }
}