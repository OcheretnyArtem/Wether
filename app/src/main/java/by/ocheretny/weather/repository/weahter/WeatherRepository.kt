package by.ocheretny.weather.repository.weahter

import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.mappers.weather.WeatherResponseMapper
import by.ocheretny.weather.networking.weather.WeatherApi

class WeatherRepository {

    private val api = WeatherApi.provideRetrofit()
    private val weatherMapper = WeatherResponseMapper()

    suspend fun loadData(lat: Double, lon: Double, appid: String, units: String): Weather? {

        val response = api.loadData(lat, lon, appid, units)

        return if (response.isSuccessful) {

            response.body()?.let { weatherMapper.map(it) }

        } else
            throw Throwable(response.errorBody().toString())
    }
}