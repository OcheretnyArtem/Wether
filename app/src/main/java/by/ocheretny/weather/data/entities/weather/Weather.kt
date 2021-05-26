package by.ocheretny.weather.data.entities.weather


data class Weather(
    val daily: List<Daily?>?,
    val lat: Number?,
    val lon: Number?,
    val timezone: String?,
) {
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