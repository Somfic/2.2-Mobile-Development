package com.example.mobile_development_2_2.map


import com.google.gson.JsonArray
import kotlinx.coroutines.delay
import org.osmdroid.util.GeoPoint
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class RouteRequest {


    companion object {
        suspend fun getRoute(origin: GeoPoint, destination: GeoPoint, apikey_: String?): String {

            var apikey : String = "5b3ce3597851110001cf62489aa6cbff57bc434dab2b62f3bd5b7861"
            if (apikey_ != null) {
                apikey = apikey_
            }

            val url: URL =
                URL("https://api.openrouteservice.org/v2/directions/foot-walking/geojson")
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty(
                "Authorization",
                apikey
            )
            conn.setRequestProperty(
                "Accept",
                "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8"
            )
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            conn.doOutput = true
//
//
            val payload: String = """{"coordinates":[[${origin.longitude},${origin.latitude}],[${destination.longitude},${destination.latitude}]]}"""
            println(payload)
            val dataOutputStream = DataOutputStream(conn.outputStream)
            dataOutputStream.writeBytes(payload)
            dataOutputStream.flush()
            dataOutputStream.close()


            val responseCode: Int = conn.responseCode
            if(responseCode != 200){
                println("Error: $responseCode")
                 var apikey__ = "5b3ce3597851110001cf62482fbd8d2e62ee41aab8811bbd5ae52f6a"
                delay(1000)
                return getRoute(origin, destination,apikey__)

            }
            println("Response Code: $responseCode")

            val inputStreamReader = InputStreamReader(conn.inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var inputLine: String?
            val response = StringBuilder()
            while (bufferedReader.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            bufferedReader.close()
            println(response)
            return (response.toString())
        }
    }
}