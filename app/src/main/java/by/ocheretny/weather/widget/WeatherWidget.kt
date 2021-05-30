package by.ocheretny.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import by.ocheretny.weather.R
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteLatitude(context, appWidgetId)
            deleteLongitude(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {

    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    val dateFormat = SimpleDateFormat("dd.MM")

    val intent = Intent(context, WeatherWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, WeatherWidget::class.java))
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

    loadWeather(context, appWidgetId) {
        try {
            val city = it.timezone.toString()
            views.setTextViewText(R.id.city_name,city)

            val time1 = it.daily?.get(0)?.dt?.toLong()?.times(1000) ?: 0
            views.setTextViewText(R.id.date_one, dateFormat.format(Date(time1)))

            val time2 = it.daily?.get(1)?.dt?.toLong()?.times(1000) ?: 0
            views.setTextViewText(R.id.date_two, dateFormat.format(Date(time2)))

            val time3 = it.daily?.get(2)?.dt?.toLong()?.times(1000) ?: 0
            views.setTextViewText(R.id.date_three, dateFormat.format(Date(time3)))

            val time4 = it.daily?.get(3)?.dt?.toLong()?.times(1000) ?: 0
            views.setTextViewText(R.id.date_four, dateFormat.format(Date(time4)))

            it.daily?.get(0)?.weather?.get(0)?.icon?.let {
                loadImage(views, R.id.img_one, ids, "https://openweathermap.org/img/wn/${it}.png")
            }

            it.daily?.get(1)?.weather?.get(0)?.icon?.let {
                loadImage(views, R.id.img_two, ids, "https://openweathermap.org/img/wn/${it}.png")
            }
            it.daily?.get(2)?.weather?.get(0)?.icon?.let {
                loadImage(views, R.id.img_three, ids, "https://openweathermap.org/img/wn/${it}.png")
            }
            it.daily?.get(3)?.weather?.get(0)?.icon?.let {
                loadImage(views, R.id.img_four, ids, "https://openweathermap.org/img/wn/${it}.png")
            }

            val d1Max = it.daily?.get(0)?.temp?.max?.toInt() ?: "--"
            val d1Min = it.daily?.get(0)?.temp?.min?.toInt() ?: "--"

            views.setTextViewText(R.id.temper_one, "$d1Max/$d1Min")

            val d2Max = it.daily?.get(1)?.temp?.max?.toInt() ?: "--"
            val d2Min = it.daily?.get(1)?.temp?.min?.toInt() ?: "--"
            views.setTextViewText(R.id.temper_two, "$d2Max/$d2Min")

            val d3Max = it.daily?.get(2)?.temp?.max?.toInt() ?: "--"
            val d3Min = it.daily?.get(2)?.temp?.min?.toInt() ?: "--"
            views.setTextViewText(R.id.temper_three, "$d3Max/$d3Min")

            val d4Max = it.daily?.get(3)?.temp?.max?.toInt() ?: "--"
            val d4Min = it.daily?.get(3)?.temp?.min?.toInt() ?: "--"
            views.setTextViewText(R.id.temper_four, "$d4Max/$d4Min")

        } catch (e: Exception) {
            views.setTextViewText(R.id.city_name,"----")
            views.setTextViewText(R.id.date_one, "--.--")
            views.setTextViewText(R.id.date_two, "--.--")
            views.setTextViewText(R.id.date_three, "--.--")
            views.setTextViewText(R.id.date_four, "--.--")

            views.setImageViewResource(R.id.img_one, R.drawable.ic_baseline_remove_24)
            views.setImageViewResource(R.id.img_two, R.drawable.ic_baseline_remove_24)
            views.setImageViewResource(R.id.img_three, R.drawable.ic_baseline_remove_24)
            views.setImageViewResource(R.id.img_four, R.drawable.ic_baseline_remove_24)

            views.setTextViewText(R.id.temper_one, "--/--")
            views.setTextViewText(R.id.temper_two, "--/--")
            views.setTextViewText(R.id.temper_three, "--/--")
            views.setTextViewText(R.id.temper_four, "--/--")
        }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


    val updateIntent = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    views.setOnClickPendingIntent(
        R.id.button_setting,
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, WeatherWidgetConfigureActivity::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            },
            0
        )
    )
    views.setOnClickPendingIntent(R.id.button_refresh, updateIntent)
}

private fun loadImage(views: RemoteViews, id: Int, widgetIds: IntArray, link: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Picasso.get()
            .load(link)
            .error(R.drawable.ic_baseline_remove_24)
            .into(views, id, widgetIds)
    }
}