package by.ocheretny.weather.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import by.ocheretny.weather.R
import by.ocheretny.weather.data.entities.weather.Weather
import by.ocheretny.weather.repository.weahter.WeatherRepository
import com.google.android.gms.location.*
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "by.ocheretny.weather.widget.WeatherWidget"
        const val LATITUDE_KEY = "latitude"
        const val LONGITUDE_KEY = "longitude"
        const val UNITS_KEY = "units"
    }

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private lateinit var units: Spinner
    private lateinit var switch: SwitchMaterial
    private lateinit var selectedUnit: String
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private var onClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity

        saveUnit(context, appWidgetId, selectedUnit)

        val latitudeEdit = latitude.text.toString()
        val longitudeEdit = longitude.text.toString()

        saveLatitude(context, appWidgetId, latitudeEdit)
        saveLongitude(context, appWidgetId, longitudeEdit)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        setContentView(R.layout.weather_widget_configure)

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_received),
                        Toast.LENGTH_LONG
                    ).show()
                    getLocation()
                    latitude.isEnabled = false
                    longitude.isEnabled = false
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.permission_not_received),
                        Toast.LENGTH_SHORT
                    ).show()

                    switch.isChecked = false
                }
            }

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        latitude = findViewById(R.id.latitude)
        longitude = findViewById(R.id.longitude)
        findViewById<View>(R.id.save_button).setOnClickListener(onClickListener)
        units = findViewById(R.id.units_spinner)
        switch = findViewById(R.id.your_location)

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        getLocation()
                        latitude.isEnabled = false
                        longitude.isEnabled = false
                    }
                    else -> {
                        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            } else {
                latitude.text.clear()
                longitude.text.clear()
                latitude.isEnabled = true
                longitude.isEnabled = true
            }
        }

        ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item)
            .also{
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            units.adapter = it
        }

        units.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedUnit = resources.getStringArray(R.array.units)[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


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


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProvider.lastLocation.addOnCompleteListener {
            latitude.setText(it.result.latitude.toString())
            longitude.setText(it.result.longitude.toString())
        }
    }
}

internal fun saveLatitude(context: Context, appWidgetId: Int, latitude: String) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.putString(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId, latitude)
    prefs.apply()
}

internal fun loadLatitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    )
    val latitudeValue =
        prefs.getString(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId, "0")
    return latitudeValue ?: "0"
}

internal fun deleteLatitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.remove(WeatherWidgetConfigureActivity.LATITUDE_KEY + appWidgetId)
    prefs.apply()
}

internal fun saveLongitude(context: Context, appWidgetId: Int, longitude: String) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.putString(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId, longitude)
    prefs.apply()
}

internal fun loadLongitude(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    )
    val value = prefs.getString(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId, "0")
    return value ?: "0"
}

internal fun deleteLongitude(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.remove(WeatherWidgetConfigureActivity.LONGITUDE_KEY + appWidgetId)
    prefs.apply()
}

internal fun saveUnit(context: Context, appWidgetId: Int, unit: String) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.putString(WeatherWidgetConfigureActivity.UNITS_KEY + appWidgetId, unit)
    prefs.apply()
}

internal fun loadUnit(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    )
    val value = prefs.getString(WeatherWidgetConfigureActivity.UNITS_KEY + appWidgetId, "metric")
    return value ?: "metric"
}

internal fun deleteUnit(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(
        WeatherWidgetConfigureActivity.PREFS_NAME,
        Context.MODE_PRIVATE
    ).edit()
    prefs.remove(WeatherWidgetConfigureActivity.UNITS_KEY + appWidgetId)
    prefs.apply()
}

internal fun loadWeather(context: Context, appWidgetId: Int, block: (Weather) -> Unit) {
    val latitude = loadLatitude(context, appWidgetId)
    val longitude = loadLongitude(context, appWidgetId)
    val unit = loadUnit(context, appWidgetId)
    CoroutineScope(Dispatchers.IO).launch {
        launch(Dispatchers.Main) { block(Weather.createEmptyWeather()) }
        val weatherRepository = WeatherRepository()
        val result = weatherRepository.loadData(
            latitude,
            longitude,
            units = unit
        )
        launch(Dispatchers.Main) {
            if (result != null) {
                block(result)
            }
        }
    }
}