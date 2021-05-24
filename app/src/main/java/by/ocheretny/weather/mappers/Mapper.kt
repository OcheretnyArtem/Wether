package by.ocheretny.weather.mappers

import by.ocheretny.weather.dto.weather.WeatherResponse

interface Mapper<F, T> {
    fun map(from: WeatherResponse): T
}