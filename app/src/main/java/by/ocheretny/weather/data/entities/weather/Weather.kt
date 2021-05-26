package by.ocheretny.weather.data.entities.weather


data class Weather(
    val daily: List<Daily?>?,
    val lat: Number?,
    val lon: Number?,
    val timezone: String?,
) {

    companion object {
        fun createEmptyWeather(): Weather {
            return Weather(ArrayList<Weather.Daily?>().apply {
                add(
                    Weather.Daily(null, null, Weather.Daily.Temp(
                        null, null
                    ), null, ArrayList<Weather.Daily.Weather>().apply {
                        add(
                            Weather.Daily.Weather(
                                null, null, null, null
                            )
                        )
                    })
                )
            }, null, null, null)
        }
    }

    data class Daily(
        val dt: Number?,
        val pop: Number?,
        val temp: Temp?,
        val uvi: Number?,
        val weather: List<Weather?>?,
    ) {

        data class Temp(
            val max: Number?,
            val min: Number?,
        )

        data class Weather(
            val description: String?,
            val icon: String?,
            val id: Number?,
            val main: String?
        )
    }

}