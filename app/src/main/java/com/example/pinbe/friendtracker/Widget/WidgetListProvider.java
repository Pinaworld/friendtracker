package com.example.pinbe.friendtracker.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.Models.Group;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private int appWidgetId;

    private ArrayList<String> widgetList = new ArrayList<>();
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String userId;


    public WidgetListProvider(Context context, Intent intent)
    {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                                        AppWidgetManager.INVALID_APPWIDGET_ID);


    }

    private void updateWidgetListView()
    {
        getAllappointments();
    }


    private void getAllappointments() {

        Query query = mFirebaseDatabase.child("Group").orderByChild("members");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Group group = ds.getValue(Group.class);
                        if(group.getMembersId().contains(userId)){
                            setGroupAppointments(group.getId(), group.getName());
                        }
                    }

                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setGroupAppointments(String groupId, final String groupName) {
        Query query = mFirebaseDatabase.child("Appointment").orderByChild("groupId").equalTo(groupId);

        if(widgetList.size() > 0){
            widgetList.clear();
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try{
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Appointment appointment = ds.getValue(Appointment.class);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                        Date date = appointment.getAppointmentDate();
                        widgetList.add(appointment.getName() + " - " + groupName + ": " +
                                        dateFormat.format(appointment.getAppointmentDate()) + " - " +
                                        appointment.getPostalCode() + " " + appointment.getCity());
                    }
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appWidgetListView);
                }catch(Exception e){
                    Log.i("ERROR", e.getMessage());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public int getCount()
    {
        return widgetList.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_row_model);

        remoteView.setTextViewText(R.id.appointmentTxt, widgetList.get(position));

        return remoteView;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public void onCreate()
    {
        mFirebaseInstance = getDatabase(context);
        mFirebaseDatabase = mFirebaseInstance.getReference();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        updateWidgetListView();
    }

    @Override
    public void onDataSetChanged()
    {
    }

    @Override
    public void onDestroy()
    {
        widgetList.clear();
    }

}
