package com.example.myapplication

import com.google.gson.annotations.SerializedName

// Data Classes
data class ApiResponse(
    @SerializedName("devices") val devices: List<DeviceData>? = null
)

data class DeviceData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("line") val line: LineData? = null,
    @SerializedName("product") val product: ProductData? = null,
    @SerializedName("shortnames") val shortnames: List<String>? = null,
    @SerializedName("images") val image: ImageData? = null
)

data class LineData(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null
)

data class ProductData(
    @SerializedName("name") val name: String? = null,
    @SerializedName("abbreviation") val abbreviation: String? = null
)

data class ImageData(
    @SerializedName("default") val default: String? = null
)
