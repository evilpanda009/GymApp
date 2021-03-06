package com.example.gymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    private Button addstaff;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String UserID;
    private Button logout;


    Button add,show,routine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashboard);
        add = findViewById(R.id.button4);
        show = findViewById(R.id.button8);
        routine = findViewById(R.id.button9);
        logout = findViewById(R.id.signOut);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMembers();
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMembers();
            }
        });
        routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showroutines();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showroutines();
            }
        });
        addstaff = findViewById(R.id.addstaffbtn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserID = user.getUid();

        logout = findViewById((R.id.signOut));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Logout();
            }
        });

        addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Loading Data", Toast.LENGTH_LONG).show();

                reference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userp = snapshot.getValue(User.class);

                        if (userp != null){
                            String email = userp.email;
                            String role = userp.role;
                            String gymid = userp.gymid;
                            Log.d("role", role);

                            if (role.equals("admin")) {
                                addstaff();
                            } else {
                                Toast.makeText(Dashboard.this, "You're not an admin!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Dashboard.this, "Couldn't connect to database!", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
    public void addMembers()
    {
        Intent intent = new Intent(this,AddMemberActivity.class);
        startActivity(intent);
    }
    public void showroutines()
    {
        Intent intent = new Intent(this,TrainingRoutines.class);
        startActivity(intent);
    }
    public void showMembers()
    {
        Intent intent = new Intent(this,memberList.class);
        startActivity(intent);
    }

    public void addstaff()
    {
        Intent i = new Intent(Dashboard.this, RegisterStaffActivity.class);
        startActivity(i);

    }

    public void Logout()
    {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}