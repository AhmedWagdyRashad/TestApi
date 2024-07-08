package com.ahmedwagdy.testapi.data.remote

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class DefaultApiClient : ApiClient {

    companion object {
        private const val TAG = "DefaultApiClient"
    }

    override fun request(url: String, requestMethod: String, callback: ApiResponseCallback) {
        ApiCallTask(callback).execute(url, requestMethod)
    }

    override fun request(
        url: String,
        requestMethod: String,
        requestBody: String,
        callback: ApiResponseCallback
    ) {
        ApiCallTask(callback).execute(url, requestMethod, requestBody)
    }

    private inner class ApiCallTask(private val callback: ApiResponseCallback) :
        AsyncTask<String, Void, ApiResponse>() {

        override fun doInBackground(vararg params: String): ApiResponse {
            val url = params[0]
            val method = params[1]
            val requestBody = if (params.size > 2) params[2] else null

            var urlConnection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            val response = ApiResponse()

            try {
                val apiUrl = URL(url)
                urlConnection = apiUrl.openConnection() as HttpURLConnection
                urlConnection.requestMethod = method

                if (requestBody != null) {
                    urlConnection.doOutput = true
                    urlConnection.setRequestProperty("Content-Type", "application/json")
                    val outputStream: OutputStream = urlConnection.outputStream
                    outputStream.write(requestBody.toByteArray())
                    outputStream.flush()
                    outputStream.close()
                }

                val inputStream: InputStream = urlConnection.inputStream
                val buffer = StringBuilder()
                reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    buffer.append(line).append("\n")
                }

                if (buffer.isEmpty()) {
                    response.statusCode = urlConnection.responseCode
                    response.message = "Empty response"
                    return response
                }

                response.statusCode = urlConnection.responseCode
                response.message = buffer.toString()
            } catch (e: IOException) {
                Log.e(TAG, "Error ", e)
                response.statusCode = 0
                response.message = "Error: ${e.message}"
            } finally {
                urlConnection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    Log.e(TAG, "Error closing stream", e)
                }
            }

            return response
        }

        override fun onPostExecute(result: ApiResponse) {
            super.onPostExecute(result)
            callback.onResponse(result)
        }
    }

}
