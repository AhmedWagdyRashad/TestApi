package com.ahmedwagdy.testapi.data.remote

interface ApiClient {
    fun request(url: String, requestMethod: String, callback: ApiResponseCallback)
    fun request(
        url: String,
        requestMethod: String,
        requestBody: String,
        callback: ApiResponseCallback
    )
}
