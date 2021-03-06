package com.piyush004.projectfirst.customer.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.piyush004.projectfirst.LoginKey;
import com.piyush004.projectfirst.R;
import com.piyush004.projectfirst.customer.schedule.MessScheduleActivity;
import com.piyush004.projectfirst.customer.search_mess.SearchMessLocation;
import com.squareup.picasso.Picasso;


public class CustomerHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private TextView textViewCurrentMess, textViewProfile;
    private ImageView imageViewLocation, imageViewProfile, imageViewSchedule, imageViewMessMenu;
    private Thread threadMessName = null;
    private Thread threadProfile = null;
    private String login_name, key;
    private String messCurrentMess;
    private ProgressBar progressBar;
    private AlertDialog.Builder builder;
    private DatabaseReference dff;
    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    public static CustomerHomeFragment newInstance(String param1, String param2) {
        CustomerHomeFragment fragment = new CustomerHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_customer_home, container, false);

        login_name = LoginKey.loginKey;
        textViewCurrentMess = view.findViewById(R.id.cust_current_mess_c);
        imageViewLocation = view.findViewById(R.id.locationHome_c);
        imageViewProfile = view.findViewById(R.id.imageViewProfile_c);
        imageViewSchedule = view.findViewById(R.id.imageViewMessSchedule_c);
        imageViewMessMenu = view.findViewById(R.id.imageViewMessMenu_c);
        textViewProfile = view.findViewById(R.id.textCustProfile_c);
        progressBar = view.findViewById(R.id.homeProgressbar);
        progressBar.setVisibility(View.VISIBLE);


        threadMessName = new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {

                        DatabaseReference dfmt = FirebaseDatabase.getInstance().getReference().child("Customer").child(login_name);
                        dfmt.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messCurrentMess = snapshot.child("CustCurrentMess").getValue(String.class);
                                System.out.println(messCurrentMess);
                                if (messCurrentMess == null) {
                                    textViewCurrentMess.setText("NOT Set ");
                                } else {
                                    DatabaseReference dfmt = FirebaseDatabase.getInstance().getReference().child("Mess").child(messCurrentMess);
                                    dfmt.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String messName = snapshot.child("MessName").getValue(String.class);
                                            textViewCurrentMess.setText(messName);
                                            System.out.println(messName);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });
        threadMessName.start();

        threadProfile = new Thread(new Runnable() {

            @Override
            public void run() {

                DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Customer").child(login_name);
                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String imgprofile = snapshot.child("ImageURl").getValue(String.class);
                        String nameProfile = snapshot.child("CustName").getValue(String.class);

                        if (imgprofile == null) {
                            Picasso.get().load(R.drawable.profile_home).into(imageViewProfile);
                        } else {
                            Picasso.get().load(imgprofile).into(imageViewProfile);
                        }
                        if (nameProfile == null) {
                            textViewProfile.setText("Profile");
                        } else {
                            textViewProfile.setText(nameProfile);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        threadProfile.start();

        imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLocation = new Intent(getActivity(), SearchMessLocation.class);
                startActivity(intentLocation);
            }
        });

        imageViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSch = new Intent(getActivity(), MessScheduleActivity.class);
                startActivity(intentSch);
            }
        });

        textViewCurrentMess.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Customer").child(login_name);
                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        key = snapshot.child("CustCurrentMess").getValue(String.class);
                        System.out.println(key);
                        dff = FirebaseDatabase.getInstance().getReference().child("ManageCustomer").child(key);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete current Mess ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                df.child("CustCurrentMess").removeValue();
                                df.child("CustJoinDay").removeValue();
                                df.child("CustJoinMonth").removeValue();
                                df.child("CustJoinYear").removeValue();
                                dff.child(login_name).removeValue();
                                textViewCurrentMess.setText("Not Set");
                                Toast.makeText(getContext(), "Remove Mess Name Successfully", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("Mess Delete Page");
                alert.show();

                return true;
            }
        });

        progressBar.setVisibility(View.GONE);
        return view;
    }
}