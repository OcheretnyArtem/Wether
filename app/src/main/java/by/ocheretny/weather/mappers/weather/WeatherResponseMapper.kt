package by.ocheretny.weather.mappers.weather

import by.ocheretny.weather.data.dto.weather.WeatherResponse
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.mappers.Mapper

class WeatherResponseMapper : Mapper<WeatherResponse, Weather> {

    override fun map(from: WeatherResponse): Weather {

        val daily = ArrayList<Weather.Daily>()

        from.daily?.forEach { bufferDaily ->

            val temp = Weather.Daily.Temp(
                max = bufferDaily?.temp?.max ?: 0.0,
                min = bufferDaily?.temp?.min ?: 0.0
            )

            val weather = ArrayList<Weather.Daily.Weather>()

            bufferDaily?.weather?.forEach { bufferWeather ->
                weather.add(
                    Weather.Daily.Weather(
                        description = bufferWeather?.description.orEmpty(),
                        icon = bufferWeather?.icon.orEmpty(),
                        id = bufferWeather?.id ?: 0,
                        main = bufferWeather?.main.orEmpty()
                    )
                )
            }

            daily.add(
                Weather.Daily(
                    dt = bufferDaily?.dt ?: 0,
                    pop = bufferDaily?.pop ?: 0,
                    temp = temp,
                    uvi = bufferDaily?.uvi ?: 0.0,
                    weather = weather
                )
            )
        }
        return Weather(
            daily = daily,
            lat = from.lat ?: 0.0,
            lon = from.lon ?: 0.0,
            timezone = from.timezone.orEmpty(),
        )
    }
}