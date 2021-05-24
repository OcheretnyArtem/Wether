package by.ocheretny.weather.mappers.weather

import by.ocheretny.weather.dto.weather.WeatherResponse
import by.ocheretny.weather.entities.weather.Weather
import by.ocheretny.weather.mappers.Mapper

class WeatherResponseMapper : Mapper<WeatherResponse, Weather> {
    override fun map(from: WeatherResponse): Weather {
        val daily = ArrayList<Weather.Daily>()
        val temp = ArrayList<Weather.Daily.Temp>()

        from.daily?.forEach {
            var max = it?.temp?.max
            var min = it?.temp?.min
            var Weather.
            daily.add(
                Weather.Daily(
                    it?.dt,
                    it?.pop,
                    max,
                    min,
                    it?.uvi,
                    it?.weather
                )
            )
        }
        return Weather(
            daily = from.daily[1].,
            lat = from.lat ?: 0.0,
            lon = from.lon ?: 0.0,
            timezone = from.timezone.orEmpty(),

            )
    }
}