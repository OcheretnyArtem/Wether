package by.ocheretny.weather.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import by.ocheretny.weather.R
import by.ocheretny.weather.databinding.WeatherWidgetConfigureBinding

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : Activity() {
    companion object {
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
    }

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var latitude: EditText
    private lateinit var longitude: EditText
    private var onClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity

        // When the button is clicked, store the string locally
        val latitude = latitude.text.toString()
        val longitude = longitude.text.toString()
        //saveTitlePref(context, appWidgetId, widgetText)

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        updateAppWidget(context, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        resultValue.putExtra(LATITUDE,latitude)
        resultValue.putExtra(LONGITUDE,longitude)
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
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
    prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
    return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(PREF_PREFIX_KEY + appWidgetId)
    prefs.apply()
}