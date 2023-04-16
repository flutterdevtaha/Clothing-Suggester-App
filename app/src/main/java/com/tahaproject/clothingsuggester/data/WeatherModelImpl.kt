package com.tahaproject.clothingsuggester.data

import com.google.gson.Gson
import com.tahaproject.clothingsuggester.data.models.response.WeatherData
import okhttp3.*
import java.io.IOException

class WeatherModelImpl(private val apiRequest: ApiRequest, private val apiKey: String) : WeatherModel {
    val gson = Gson()
    override fun fetchCurrentWeatherData(
        lat: String,
        long: String,
        callback: (WeatherData) -> Unit,
        errorCallback: (String) -> Unit
    ) {
        return apiRequest.getCurrentWeather(lat, long, apiKey, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback(e.message ?: "Error fetching weather data")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val weatherData = gson.fromJson(responseBody, WeatherData::class.java)
                    weatherData?.let(callback) ?: errorCallback("Error parsing weather data")
                } ?: errorCallback("Error fetching weather data")
            }
        })
    }
}