package by.ocheretny.weather.entities.weather


data class Weather(
    val daily: List<Daily?>?,
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
) {
    data class Daily(
        val dt: Int?,
        val pop: Int?,
        val temp: Temp?,
        val uvi: Double?,
        val weather: List<Weather?>?,
    ) {

        data class Temp(
            val max: Double?,
            val min: Double?,
        )

        data class Weather(
            val description: String?,
            val icon: String?,
            val id: Int?,
            val main: String?
        )
    }

}