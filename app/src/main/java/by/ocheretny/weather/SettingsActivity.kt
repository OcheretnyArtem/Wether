package by.ocheretny.weather

import android.content.Context
import android.icu.number.Notation.simple
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import by.ocheretny.weather.widget.WeatherWidget
import by.ocheretny.weather.widget.WeatherWidgetConfigureActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    companion object{
        const val UNITS_KEY = "units"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switch = findViewById<SwitchMaterial>(R.id.switchMaterial2)

        val sp = getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME,Context.MODE_PRIVATE)

        val units = findViewById<Spinner>(R.id.units_spinner)
        ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item)
            .also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                units.adapter = it
            }
        units.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val spinnerUnits = resources.getStringArray(R.array.units)
                sp.edit().putString(UNITS_KEY,spinnerUnits[position].toString()).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}