package by.ocheretny.weather.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import by.ocheretny.weather.R
import by.ocheretny.weather.data.dto.weather.WeatherResponse
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.databinding.WeatherWidgetConfigureBinding
import by.ocheretny.weather.repository.weahter.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : Activity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private var onClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity

        // Когда нажимаем кнопку создать, вычитываем данные
        val latitude = latitude.text.toString()
        val longitude = longitude.text.toString()

        //сохроняем их в базу данных
        saveLatitude(context, appWidgetId, latitude)
        saveLongitude(context, appWidgetId, longitude)

        // Дальше магия
        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
    private lateinit var binding: WeatherWidgetConfigureBinding


    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        binding = WeatherWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        latitude = binding.latitude as EditText
        longitude = binding.longitude as EditText
        binding.addButton.setOnClickListener(onClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        //appWidgetText.setText(loadTitlePref(this@WeatherWidgetConfigureActivity, appWidgetId))
    }

}

private const val PREFS_NAME = "by.ocheretny.weather.widget.WeatherWidget"
private const val LATITUDE_KEY = "latitude"
private const val LONGITUDE_KEY = "longitude"


internal fun saveLatitude(context: Context, appWidgetId: Int, latitude: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(LATITUDE_KEY + appWidgetId, latitude)
    prefs.apply()
}

internal fun loadLatitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val latitudeValue = prefs.getString(LATITUDE_KEY + appWidgetId, "0")
    return latitudeValue ?: "0"
}

internal fun deleteLatitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(LATITUDE_KEY + appWidgetId)
    prefs.apply()
}

internal fun saveLongitude(context: Context, appWidgetId: Int, longitude: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(LONGITUDE_KEY + appWidgetId, longitude)
    prefs.apply()
}

internal fun loadLongitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val value = prefs.getString(LONGITUDE_KEY + appWidgetId, "0")
    return value ?: "0"
}

internal fun deleteLongitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(LONGITUDE_KEY + appWidgetId)
    prefs.apply()
}


fun loadWeather(context: Context, appWidgetId: Int, block: (Weather) -> Unit) {
    val latitude = loadLatitude(context, appWidgetId)
    val longitude = loadLongitude(context, appWidgetId)
    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.Main) {   block(Weather.createEmptyWeather())}

        val weatherRepository = WeatherRepository()
        val result = weatherRepository.loadData(
            latitude.toDouble(),
            longitude.toDouble()
        )
        launch(Dispatchers.Main) {
            if (result != null) {
                block(result)
            }
        }
    }
}