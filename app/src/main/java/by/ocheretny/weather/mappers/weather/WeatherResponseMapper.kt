package by.ocheretny.weather.mappers.weather

import android.util.Log
import by.ocheretny.weather.data.dto.weather.WeatherResponse
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.mappers.Mapper

class WeatherResponseMapper : Mapper<WeatherResponse, Weather> {
    override fun map(from: WeatherResponse): Weather {

        Log.e("mapping", from.toString())

        val daily = ArrayList<Weather.Daily>()
        from.daily?.forEach { dl ->
            daily.add(
                Weather.Daily(
                    dt = dl?.dt,
                    pop = dl?.pop,
                    temp = Weather.Daily.Temp(
                        max = dl?.temp?.max,
                        min = dl?.temp?.min
                    ),
                    uvi = dl?.uvi,
                    weather = ArrayList<Weather.Daily.Weather>().apply {
                        dl?.weather?.forEach { wea ->
                            add(
                                Weather.Daily.Weather(
                                    description = wea?.description,
                                    icon = wea?.icon,
                                    id = wea?.id,
                                    main = wea?.main
                                )
                            )
                        }
                    }
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