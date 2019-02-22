package com.example.pinbe.friendtracker.Fragments;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pinbe.friendtracker.Models.Appointment;
import com.example.pinbe.friendtracker.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

import static com.example.pinbe.friendtracker.Database.Database.getDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentCreationFragment extends Fragment {

    private Button createAppointmentButton;
    private EditText appointmentName;
    private EditText appointmentAdress;
    private EditText appointmentCity;
    private EditText appointmentPostalCode;
    private EditText appointmentDescription;
    private DatePicker appointmentDate;
    private TimePicker appointmentTime;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private Boolean saved=null;
    private ViewGroup inflatedView;

    private String groupId;
    private String userId;

    public AppointmentCreationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.inflatedView = container;
        return inflater.inflate(R.layout.fragment_appointment_creation, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mFirebaseInstance = getDatabase();
        mFirebaseDatabase =  mFirebaseInstance.getReference().child("Appointment");

        createAppointmentButton = inflatedView.findViewById(R.id.createAppointmentButton);
        appointmentName = inflatedView.findViewById(R.id.appointmentName);
        appointmentAdress = inflatedView.findViewById(R.id.appointmentAddress);
        appointmentCity = inflatedView.findViewById(R.id.appointmentCity);
        appointmentPostalCode = inflatedView.findViewById(R.id.appointmentPostalCode);
        appointmentDescription = inflatedView.findViewById(R.id.appointmentDescription);
        appointmentDate = inflatedView.findViewById(R.id.appointmentDate);
        appointmentTime = inflatedView.findViewById(R.id.appointmentTime);

        createAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment();
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.M)
    private void createAppointment(){
        String name = appointmentName.getText().toString();
        String description = appointmentDescription.getText().toString();
        String address = appointmentAdress.getText().toString();
        String city = appointmentCity.getText().toString();
        String postalCode = appointmentPostalCode.getText().toString();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, appointmentDate.getYear());
        cal.set(Calendar.MONTH, appointmentDate.getMonth());
        cal.set(Calendar.DATE, appointmentDate.getDayOfMonth());
        cal.set(Calendar.HOUR_OF_DAY, appointmentTime.getHour());
        cal.set(Calendar.MINUTE, appointmentTime.getMinute());
        cal.set(Calendar.MILLISECOND, 0);
        Date date = cal.getTime();


        if(validateInputs(name, address, city, postalCode, date)){
            int postalCodeInt = Integer.parseInt(postalCode);

            Boolean result =  save(name, address, city, postalCodeInt, date, description);
            if(result){
                Toast.makeText(getContext(), "Evènement Créé.",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Error during appointment creation.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validateInputs(String name,String address, String city, String postalCode, Date date){

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Veuillez indiquer le nom de l'évènement", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Veuillez indiquer l'adresse de l'évènement", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(getContext(), "Veuillez indiquer la ville de l'évènement", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(postalCode)) {
            Toast.makeText(getContext(), "Veuillez indiquer le code postal de la ville", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (postalCode.length() > 5 && isInteger(postalCode)) {
            Toast.makeText(getContext(), "Le format du code postal n'est pas valide", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(date.compareTo(new Date()) < 0){
            Toast.makeText(getContext(), "L'évènement ne peut pas avoir de date antérieure à celle d'aujourd'hui.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public Boolean save(String name,String address, String city, int postalCode,Date date, String description)
    {
        try
        {
            String appointmentId = mFirebaseDatabase.push().getKey();

            Appointment appointment = new Appointment(name, address, postalCode, city, description, date, groupId, userId);
            appointment.setId(appointmentId);

            mFirebaseDatabase.child(appointmentId).setValue(appointment);
            addAppointmentChangeListener(appointment);

            saved=true;
        }catch (DatabaseException e)
        {
            e.printStackTrace();
            saved=false;
        }
        return saved;
    }

    private void addAppointmentChangeListener(Appointment appointment) {

        final Fragment fragment = AppointmentCreationFragment.this;
        // User data change listener
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Appointment appointment = dataSnapshot.getValue(Appointment.class);

                // Check for null
                if (appointment == null) {
                    Log.e("APPOINTMENT_CREATION", "Appointment data is null!");
                    return;
                }

                Log.e("APPOINTMENT_CREATION", "Appointment data is changed: " + appointment.getName() + ", " + appointment.getDescription());

                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("APPOINTMENT_CREATION", "Failed to read appointùent", error.toException());
            }
        });
    }

    public void getGroupAndUserId(String groupId, String userId){
        this.groupId = groupId;
        this.userId = userId;

    }

    public boolean isInteger( String input ) {
        try {
            Integer.parseInt( input );
            return true;
        }
        catch( Exception e ) {
            return false;
        }
    }
}
