package com.sultan.utils.backendprovider.model

import com.google.gson.annotations.SerializedName

class GeneralResponse constructor(
        @SerializedName("code")
        val code: Int = 400,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("isSuccess")
        val isSuccess : Boolean = false){
}