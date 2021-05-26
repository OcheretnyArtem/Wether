package by.ocheretny.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import by.ocheretny.weather.data.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val check = findViewById<TextView>(R.id.check_text_view)

        viewModel.loadData(33.44,-04.58,"066684c224288ec83f079c8017eb1057","metric")

        check.text = viewModel.weather.value?.timezone.toString()

    }
}