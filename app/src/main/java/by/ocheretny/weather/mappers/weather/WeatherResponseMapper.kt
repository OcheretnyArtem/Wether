package by.ocheretny.weather.mappers.weather

import by.ocheretny.weather.data.dto.weather.WeatherResponse
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.mappers.Mapper

class WeatherResponseMapper : Mapper<WeatherResponse, Weather> {
    override fun map(from: WeatherResponse): Weather {

        val daily = ArrayList<Weather.Daily>()

        from.daily?.forEach { bufferDaily ->

            val temp = Weather.Daily.Temp(bufferDaily?.temp?.max, bufferDaily?.temp?.min)

            val weather = ArrayList<Weather.Daily.Weather>()

            bufferDaily?.weather?.forEach {bufferWeather ->
                weather.add(Weather.Daily.Weather(
                        description = bufferWeather?.description,
                        icon = bufferWeather?.icon,
                        id = bufferWeather?.id,
                        main = bufferWeather?.main))
            }

            daily.add(
                    Weather.Daily(
                            bufferDaily?.dt,
                            bufferDaily?.pop,
                            temp,
                            bufferDaily?.uvi,
                            weather
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