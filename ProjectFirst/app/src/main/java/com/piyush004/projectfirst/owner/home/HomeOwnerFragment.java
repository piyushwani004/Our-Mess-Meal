package com.piyush004.projectfirst.owner.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piyush004.projectfirst.LoginKey;
import com.piyush004.projectfirst.R;
import com.piyush004.projectfirst.owner.map.MapsOwnerActivity;
import com.piyush004.projectfirst.owner.messmenu.MessMenuActivity;
import com.piyush004.projectfirst.owner.profile.ProfileOwnerActivity;

public class HomeOwnerFragment extends Fragment {

    private TextView textView;
    private String MessHeading;
    private String login_name, messName;
    private DatabaseReference databaseReference;
    private ToggleButton toggleButton;
    private ImageView imageViewLocation, imageViewTodaysMenu, imageViewProfile, imageViewCustomer, imageViewMessMenu;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment_owner, container, false);

        textView = view.findViewById(R.id.textViewProfileHeading);
        toggleButton = view.findViewById(R.id.toggleButton);
        imageViewLocation = view.findViewById(R.id.locationHome);
        imageViewTodaysMenu = view.findViewById(R.id.imageViewTodaysMenu);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageViewCustomer = view.findViewById(R.id.imageViewCustomer);
        imageViewMessMenu = view.findViewById(R.id.imageViewMessMenu);

        login_name = LoginKey.loginKey;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisterType").child(login_name).child("MessLocation");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messName = snapshot.child("MessName").getValue(String.class);
                if (messName == null) {
                    textView.setText("Not Set Mess Name");
                } else {
                    textView.setText(messName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("RegisterType").child(login_name).child("MessStatus");
                Toast.makeText(getContext(), toggleButton.getText(), Toast.LENGTH_SHORT).show();
                String status = toggleButton.getText().toString();
                databaseReference.child("Status").setValue(status);
                switch (status) {
                    case "Open":
                        toggleButton.setTextColor(Color.GREEN);
                        break;
                    case "Closed":
                        toggleButton.setTextColor(Color.RED);
                        break;
                }
            }
        });

        imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ImageView Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MapsOwnerActivity.class);
                startActivity(intent);
            }
        });

        imageViewTodaysMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ImageView Click", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ImageView Click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ProfileOwnerActivity.class);
                startActivity(intent);
            }
        });

        imageViewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ImageView Click", Toast.LENGTH_SHORT).show();
            }
        });

        imageViewMessMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessMenuActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
