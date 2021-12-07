package com.moringaschool.myrestaurants.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private SharedPreferences mSharedPreferences;
//    private SharedPreferences.Editor mEditor;

    private DatabaseReference mSearchedLocationReference;
    private ValueEventListener mSearchedLocationReferenceListener;

    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.locationEditText) EditText mLocationEditText;
    @BindView(R.id.appNameTextView)  TextView mAppNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mEditor = mSharedPreferences.edit();

        mSearchedLocationReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

        mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    String location = locationSnapshot.getValue().toString();
                    Log.d("Locations Updated", "location : " + location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mFindRestaurantsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == mFindRestaurantsButton) {
            String location = mLocationEditText.getText().toString();
//            if (!(location).equals("")) {
//                addToSharedPreference(location);
//            }

            saveLocationToFireBase(location);

            Intent intent = new Intent(MainActivity.this, RestaurantsListActivity.class);
            intent.putExtra("location",location);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }

    public void saveLocationToFireBase(String location) {
        mSearchedLocationReference.push().setValue(location);
    }

//    private void addToSharedPreference(String location) {
//        mEditor.putString(Constants.PREFERENCE_LOCATION_KEY, location).apply();
//    }

}