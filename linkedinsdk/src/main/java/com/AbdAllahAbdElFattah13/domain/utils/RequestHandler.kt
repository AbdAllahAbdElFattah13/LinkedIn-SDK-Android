package com.AbdAllahAbdElFattah13.domain.utils

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

object RequestHandler {
    private const val TAG = "RequestHandler"

    /**
     * Method that handles Post request
     *
     * @return String - response from API, null if failed
     */
    @JvmStatic
    @Throws(IOException::class, JSONException::class)
    fun sendPost(urlStr: String, postDataParams: JSONObject): String? {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 20000
        conn.connectTimeout = 20000
        conn.requestMethod = "POST"
        conn.doInput = true
        conn.doOutput = true
        val os = conn.outputStream
        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(encodeParams(postDataParams))
        writer.flush()
        writer.close()
        os.close()
        val responseCode = conn.responseCode // To Check for 200
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))
            val sb = StringBuilder()
            var line: String
            while (bufferedReader.readLine().also { line = it } != null) {
                sb.append(line)
                break
            }
            bufferedReader.close()
            return sb.toString()
        } else {
            val err = BufferedReader(InputStreamReader(conn.errorStream))
            val sb = StringBuilder()
            var line: String
            while (err.readLine().also { line = it } != null) {
                sb.append(line)
                break
            }
            err.close()
            Log.e(TAG, "Error Code " + conn.responseCode)
            Log.e(TAG, "Error Message $sb")
        }
        return null
    }

    /**
     * Method that handles Get request
     *
     * @return String - response from API, null if failed
     */
    @JvmStatic
    @Throws(IOException::class)
    fun sendGet(url: String?, accessToken: String): String? {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.setRequestProperty("Authorization", "Bearer $accessToken")
        con.setRequestProperty("cache-control", "no-cache")
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0")
        val responseCode = con.responseCode
        Log.d(TAG, "Response Code :: $responseCode")
        return if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
            var inputLine: String?
            val response = StringBuilder()
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()
            response.toString()
        } else {
            val err = BufferedReader(InputStreamReader(con.errorStream))
            val sb = StringBuilder()
            var line: String
            while (err.readLine().also { line = it } != null) {
                sb.append(line)
                break
            }
            err.close()
            Log.e(TAG, "Error Code " + con.responseCode)
            Log.e(TAG, "Error Message $sb")
            null
        }
    }

    /**
     * Method that encodes parameters
     *
     * @return String - url encoded string
     */
    @Throws(UnsupportedEncodingException::class, JSONException::class)
    private fun encodeParams(params: JSONObject): String {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }
}