package by.ocheretny.weather.mappers

import by.ocheretny.weather.data.dto.weather.WeatherResponse

interface Mapper<F, T> {
    fun map(from: F): T
}