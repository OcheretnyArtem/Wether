package by.ocheretny.weather.widget

import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.ocheretny.weather.R
import by.ocheretny.weather.SettingsActivity
import by.ocheretny.weather.data.dto.weather.WeatherResponse
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.databinding.WeatherWidgetConfigureBinding
import by.ocheretny.weather.repository.weahter.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : AppCompatActivity() {

    companion object{
        const val PREFS_NAME = "by.ocheretny.weather.widget.WeatherWidget"
        const val LATITUDE_KEY = "latitude"
        const val LONGITUDE_KEY = "longitude"
    }
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

        Log.e("tag", "123")

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
//        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish()
//            return
//        }

        //appWidgetText.setText(loadTitlePref(this@WeatherWidgetConfigureActivity, appWidgetId))
    }

}





internal fun saveLatitude(context: Context, appWidgetId: Int, latitude: String) {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()
    prefs.putString(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId, latitude)
    prefs.apply()
}

internal fun loadLatitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE)
    val latitudeValue = prefs.getString(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId, "0")
    return latitudeValue ?: "0"
}

internal fun deleteLatitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()
    prefs.remove(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId)
    prefs.apply()
}

internal fun saveLongitude(context: Context, appWidgetId: Int, longitude: String) {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()
    prefs.putString(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId, longitude)
    prefs.apply()
}

internal fun loadLongitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE)
    val value = prefs.getString(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId, "0")
    return value ?: "0"
}

internal fun deleteLongitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE).edit()
    prefs.remove(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId)
    prefs.apply()
}


internal fun loadWeather(context: Context, appWidgetId: Int, block: (Weather) -> Unit) {
    val sp = context.getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME,Context.MODE_PRIVATE)
    val latitude = loadLatitude(context, appWidgetId)
    val longitude = loadLongitude(context, appWidgetId)
    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.Main) {   block(Weather.createEmptyWeather())}

        val weatherRepository = WeatherRepository()
        val unit = sp.getString(SettingsActivity.UNITS_KEY,"metric")
        val result = weatherRepository.loadData(
            latitude.toDouble(),
            longitude.toDouble(),
            units = unit.toString()

        )
        launch(Dispatchers.Main) {
            if (result != null) {
                block(result)
            }
        }
    }
}

