package by.ocheretny.weather

import android.content.Context
import android.icu.number.Notation.simple
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.get
import by.ocheretny.weather.widget.WeatherWidget
import by.ocheretny.weather.widget.WeatherWidgetConfigureActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val UNITS_KEY = "units"
        const val LATITUDE_KEY = "latitude key"
        const val LONGITUDE_KEY = "longitude key"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sp =
            getSharedPreferences(WeatherWidgetConfigureActivity.PREFS_NAME, Context.MODE_PRIVATE)


        val switch = findViewById<SwitchMaterial>(R.id.your_location)
        val latitude = findViewById<EditText>(R.id.edit_latitude)
        val longitude = findViewById<EditText>(R.id.edit_longitude)
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
                sp.edit().putString(UNITS_KEY, spinnerUnits[position].toString()).apply()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                latitude.visibility = View.GONE
                longitude.visibility = View.GONE
            } else {
                latitude.visibility = View.VISIBLE
                longitude.visibility = View.VISIBLE
            }
        }
    }
}