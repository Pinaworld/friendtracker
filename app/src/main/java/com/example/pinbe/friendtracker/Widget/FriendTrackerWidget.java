package com.example.pinbe.friendtracker.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.pinbe.friendtracker.R;

public class FriendTrackerWidget extends AppWidgetProvider {

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context)
    {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context)
    {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {
        Log.i("WIDGET_INFO", "UPDATING");

        for(int i=0;i<appWidgetIds.length;i++)
        {
            Intent intent = new Intent(context, WidgetService.class);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.friend_tracker_appointments_widget);

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            rv.setRemoteAdapter(R.id.appWidgetListView, intent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.appWidgetListView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

}

