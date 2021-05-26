package by.ocheretny.weather.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.repository.weahter.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val _weather = MutableLiveData<Weather>()
    val weather: LiveData<Weather> = _weather

    private val _errorBus = MutableLiveData<String>()
    val errorBus: LiveData<String> = _errorBus

    fun loadData(lat: Double, lon: Double, appid: String, units: String) {
        ioScope.launch {
            try {
                _weather.postValue(weatherRepository.loadData(lat, lon, appid, units))
            } catch (e: Exception) {
                _errorBus.postValue(e.message)
            }
        }
    }
}