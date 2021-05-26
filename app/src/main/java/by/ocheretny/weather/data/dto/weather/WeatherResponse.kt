package by.ocheretny.weather.data.dto.weather


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("alerts")
    val alerts: List<Alert?>?,
    @SerializedName("current")
    val current: Current?,
    @SerializedName("daily")
    val daily: List<Daily?>?,
    @SerializedName("hourly")
    val hourly: List<Hourly?>?,
    @SerializedName("lat")
    val lat: Number?,
    @SerializedName("lon")
    val lon: Number?,
    @SerializedName("minutely")
    val minutely: List<Minutely?>?,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_offset")
    val timezoneOffset: Number?
) {
    data class Alert(
        @SerializedName("description")
        val description: String?,
        @SerializedName("end")
        val end: Number?,
        @SerializedName("event")
        val event: String?,
        @SerializedName("sender_name")
        val senderName: String?,
        @SerializedName("start")
        val start: Number?,
        @SerializedName("tags")
        val tags: List<String?>?
    )

    data class Current(
        @SerializedName("clouds")
        val clouds: Number?,
        @SerializedName("dew_point")
        val dewPoint: Number?,
        @SerializedName("dt")
        val dt: Number?,
        @SerializedName("feels_like")
        val feelsLike: Number?,
        @SerializedName("humidity")
        val humidity: Number?,
        @SerializedName("pressure")
        val pressure: Number?,
        @SerializedName("sunrise")
        val sunrise: Number?,
        @SerializedName("sunset")
        val sunset: Number?,
        @SerializedName("temp")
        val temp: Number?,
        @SerializedName("uvi")
        val uvi: Number?,
        @SerializedName("visibility")
        val visibility: Number?,
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind_deg")
        val windDeg: Number?,
        @SerializedName("wind_gust")
        val windGust: Number?,
        @SerializedName("wind_speed")
        val windSpeed: Number?
    ) {
        data class Weather(
            @SerializedName("description")
            val description: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("id")
            val id: Number?,
            @SerializedName("main")
            val main: String?
        )
    }

    data class Daily(
        @SerializedName("clouds")
        val clouds: Number?,
        @SerializedName("dew_point")
        val dewPoint: Number?,
        @SerializedName("dt")
        val dt: Number?,
        @SerializedName("feels_like")
        val feelsLike: FeelsLike?,
        @SerializedName("humidity")
        val humidity: Number?,
        @SerializedName("moon_phase")
        val moonPhase: Number?,
        @SerializedName("moonrise")
        val moonrise: Number?,
        @SerializedName("moonset")
        val moonset: Number?,
        @SerializedName("pop")
        val pop: Number?,
        @SerializedName("pressure")
        val pressure: Number?,
        @SerializedName("rain")
        val rain: Number?,
        @SerializedName("sunrise")
        val sunrise: Number?,
        @SerializedName("sunset")
        val sunset: Number?,
        @SerializedName("temp")
        val temp: Temp?,
        @SerializedName("uvi")
        val uvi: Number?,
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind_deg")
        val windDeg: Number?,
        @SerializedName("wind_gust")
        val windGust: Number?,
        @SerializedName("wind_speed")
        val windSpeed: Number?
    ) {
        data class FeelsLike(
            @SerializedName("day")
            val day: Number?,
            @SerializedName("eve")
            val eve: Number?,
            @SerializedName("morn")
            val morn: Number?,
            @SerializedName("night")
            val night: Number?
        )

        data class Temp(
            @SerializedName("day")
            val day: Number?,
            @SerializedName("eve")
            val eve: Number?,
            @SerializedName("max")
            val max: Number?,
            @SerializedName("min")
            val min: Number?,
            @SerializedName("morn")
            val morn: Number?,
            @SerializedName("night")
            val night: Number?
        )

        data class Weather(
            @SerializedName("description")
            val description: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("id")
            val id: Number?,
            @SerializedName("main")
            val main: String?
        )
    }

    data class Hourly(
        @SerializedName("clouds")
        val clouds: Number?,
        @SerializedName("dew_point")
        val dewPoint: Number?,
        @SerializedName("dt")
        val dt: Number?,
        @SerializedName("feels_like")
        val feelsLike: Number?,
        @SerializedName("humidity")
        val humidity: Number?,
        @SerializedName("pop")
        val pop: Number?,
        @SerializedName("pressure")
        val pressure: Number?,
        @SerializedName("rain")
        val rain: Rain?,
        @SerializedName("temp")
        val temp: Number?,
        @SerializedName("uvi")
        val uvi: Number?,
        @SerializedName("visibility")
        val visibility: Number?,
        @SerializedName("weather")
        val weather: List<Weather?>?,
        @SerializedName("wind_deg")
        val windDeg: Number?,
        @SerializedName("wind_gust")
        val windGust: Number?,
        @SerializedName("wind_speed")
        val windSpeed: Number?
    ) {
        data class Rain(
            @SerializedName("1h")
            val h: Number?
        )

        data class Weather(
            @SerializedName("description")
            val description: String?,
            @SerializedName("icon")
            val icon: String?,
            @SerializedName("id")
            val id: Number?,
            @SerializedName("main")
            val main: String?
        )
    }

    data class Minutely(
        @SerializedName("dt")
        val dt: Number?,
        @SerializedName("precipitation")
        val precipitation: Number?
    )
}