package by.ocheretny.weather.networking

import by.ocheretny.weather.data.dto.weather.WeatherResponse
import retrofit2.Response
import retrofit2.http.*

interface WeatherService {

    @GET("data/2.5/onecall")
    suspend fun loadData(
            @Query("lat")
            lat: Double,
            @Query("lon")
            lon: Double,
            @Query("appid")
            appid: String,
            @Query("units")
            units: String
    ) : Response<WeatherResponse>
}