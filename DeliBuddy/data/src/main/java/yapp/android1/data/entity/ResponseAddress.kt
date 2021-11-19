package yapp.android1.data.entity

import com.google.gson.annotations.SerializedName

data class ResponseAddress(
    @SerializedName("documents")
    val documents: List<AddressDocument>,
    @SerializedName("meta")
    val meta: AddressMeta
)

data class AddressDocument(
    @SerializedName("address")
    val address: Address,
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("address_type")
    val addressType: String,
    @SerializedName("road_address")
    val roadAddress: RoadAddress,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String
)

data class Address(
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("b_code")
    val bCode: String,
    @SerializedName("h_code")
    val hCode: String,
    @SerializedName("main_address_no")
    val mainAddressNo: String,
    @SerializedName("mountain_yn")
    val mountainYn: String,
    @SerializedName("region_1depth_name")
    val region1depthName: String,
    @SerializedName("region_2depth_name")
    val region2depthName: String,
    @SerializedName("region_3depth_h_name")
    val region3depthHName: String,
    @SerializedName("region_3depth_name")
    val region3depthName: String,
    @SerializedName("sub_address_no")
    val subAddressNo: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String
)

data class RoadAddress(
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("building_name")
    val buildingName: String,
    @SerializedName("main_building_no")
    val mainBuildingNo: String,
    @SerializedName("region_1depth_name")
    val region1depthName: String,
    @SerializedName("region_2depth_name")
    val region2depthName: String,
    @SerializedName("region_3depth_name")
    val region3depthName: String,
    @SerializedName("road_name")
    val roadName: String,
    @SerializedName("sub_building_no")
    val subBuildingNo: String,
    @SerializedName("underground_yn")
    val undergroundYn: String,
    @SerializedName("x")
    val x: String,
    @SerializedName("y")
    val y: String,
    @SerializedName("zone_no")
    val zoneNo: String
)


data class AddressMeta(
    @SerializedName("is_end")
    val isEnd: Boolean,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)
