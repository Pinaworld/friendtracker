package com.example.pinbe.friendtracker.Database;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.pinbe.friendtracker.Widget.FriendTrackerWidget;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class Database {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase(Context context) {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
            setAppointmentListener(context);
        }
        return mDatabase;
    }

    private static void setAppointmentListener(final Context context) {
        FirebaseDatabase.getInstance().getReference().child("Appointment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateWidget(context);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateWidget(context);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                updateWidget(context);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                updateWidget(context);
            }
        });
    }

    private static void updateWidget(Context context) {
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, FriendTrackerWidget.class));
        FriendTrackerWidget myWidget = new FriendTrackerWidget();
        myWidget.onUpdate(context, AppWidgetManager.getInstance(context),ids);
    }
}
