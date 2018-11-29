package com.sultan.utils.backendprovider.model

import com.google.gson.annotations.SerializedName

class DataResponse<T> constructor(
        @SerializedName("code")
        val code: Int = 400,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("data")
        val data: T? = null,
        @SerializedName("isSuccess")
        val isSuccess : Boolean = false){
}