package yapp.android1.data.entity

import com.google.gson.annotations.SerializedName

data class CoordAddressModel(
    @SerializedName("documents")
    val coordAddressDocument: CoordAddressDocument,
    @SerializedName("meta")
    val meta: CoordAddressMeta
)

data class CoordAddressDocument(
    @SerializedName("address")
    val coordAddress: CoordAddress,
    @SerializedName("road_address")
    val coordRoadAddress: CoordRoadAddress
)

data class CoordAddress(
    @SerializedName("address_name")
    val addressName: String,
    @SerializedName("main_address_no")
    val mainAddressNo: String,
    @SerializedName("mountain_yn")
    val mountainYn: String,
    @SerializedName("region_1depth_name")
    val region1depthName: String,
    @SerializedName("region_2depth_name")
    val region2depthName: String,
    @SerializedName("region_3depth_name")
    val region3depthName: String,
    @SerializedName("sub_address_no")
    val subAddressNo: String
)

data class CoordRoadAddress(
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
    @SerializedName("zone_no")
    val zoneNo: String
)

data class CoordAddressMeta(
    @SerializedName("total_count")
    val totalCount: Int
)
