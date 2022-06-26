package com.rov.waterrov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.unicodelabs.kdgaugeview.KdGaugeView;

public class MainActivity extends AppCompatActivity {
    KdGaugeView pressure_Show, temp, tds;
    ProgressBar ph_values;
    String  Ph_value, TDS, Pressure, Temperature, turbidity, Battery_life;
    TextView ph_value_show, battery_LIFE, turbidity_Show;
    DatabaseReference reff;
    ImageView pressureimg, turbidityimg, low_battery, normal_battery;
LinearLayout phlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
confirmation();
        pressure_Show = findViewById(R.id.pressures);


        temp = findViewById(R.id.temp);


        tds = findViewById(R.id.tds);


        ph_values = findViewById(R.id.ph_value);
        ph_value_show = findViewById(R.id.ph);
        battery_LIFE = findViewById(R.id.batteryLife);

        turbidity_Show = findViewById(R.id.turbidity_show);



        low_battery = findViewById(R.id.battery_low);
        normal_battery = findViewById(R.id.battery_normal);

        phlayer = findViewById(R.id.phlayer);
        // ph_values.setMax(100);

//       ph_values.setRotation(90);


        reff = FirebaseDatabase.getInstance().getReference();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Ph_value = dataSnapshot.child("Phvalue").getValue().toString();
                TDS = dataSnapshot.child("TDS").getValue().toString();
                Pressure = dataSnapshot.child("pressure").getValue().toString();
                Temperature = dataSnapshot.child("temperature").getValue().toString();
                turbidity = dataSnapshot.child("turbidity").getValue().toString();
                Battery_life = dataSnapshot.child("battery").getValue().toString();


                pressure_Show.setSpeed((int) Double.parseDouble(Pressure));
                temp.setSpeed((int) Double.parseDouble(Temperature));
                tds.setSpeed((int) Double.parseDouble(TDS));

                ph_values.setProgress((int) Double.parseDouble(Ph_value));
                ph_value_show.setText(Ph_value);

                turbidity_Show.setText(turbidity);

//battery check
                battery_check(Battery_life);
//temperature check
                temperature_check(Temperature);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ToggleButton Switch1 = findViewById(R.id.lights1);
        Switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Switch1.isChecked()) {

                    DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("switch one");
                    df.setValue(1);


                } else {
                    DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("switch one");
                    df.setValue(0);


                }
            }
        });


        ToggleButton Switch2 = findViewById(R.id.lights2);
        Switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Switch2.isChecked()) {

                    DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("switch two");
                    df.setValue(1);


                } else {
                    DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("switch two");
                    df.setValue(0);


                }
            }
        });


// checking turbidity of water

        turbidityimg = findViewById(R.id.img_turbidity);
        turbidityimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(turbidity) <= 15 ){
                    show_notifications("EXCELLENT WATER");
                }
                if (Integer.parseInt(turbidity) > 15 & Integer.parseInt(turbidity) < 30  ){
                    show_notifications("FAIR WATER");
                }
                if (Integer.parseInt(turbidity) > 30){
                    show_notifications("POOR WATER");
                }


            }
        });

// checking ph of water

        phlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Double.parseDouble(Ph_value) <= 5 && Double.parseDouble(Ph_value) > 0){
                    show_notifications("Death Zone");
                }
                if (Double.parseDouble(Ph_value) > 5 && Double.parseDouble(Ph_value) <= 7){
                    show_notifications("Warning");
                }
                if (Double.parseDouble(Ph_value) > 7 && Double.parseDouble(Ph_value) < 8.5){
                    show_notifications("Ideal (Good range)");
                }
                if (Double.parseDouble(Ph_value) > 8.5 && Double.parseDouble(Ph_value) <= 11){
                    show_notifications("Warning");
                }
                if (Double.parseDouble(Ph_value) > 11 && Double.parseDouble(Ph_value) < 14){
                    show_notifications("Death Zone");
                }

            }
        });

    }
    // checking temperature of water
    private void temperature_check(String temperature) {

        if (Double.parseDouble(temperature) >= 42.0) {

            Button cancel;
            TextView note;
            //will create a view of our custom dialog layout
            View alertCustomdialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.temerature_warning, null);
            //initialize alert builder.
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

            //set our custom alert dialog to tha alertdialog builder
            alert.setView(alertCustomdialog);
            cancel = alertCustomdialog.findViewById(R.id.cancel_button);

            final AlertDialog dialog = alert.create();
            //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //finally show the dialog box in android all
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


    }
    }
    // checking battery life
    private void battery_check(String battery_life) {

        battery_LIFE.setText(battery_life + "%");

        if (Integer.parseInt(battery_life) <= 30) {
            battery_animations();
            low_battery.setVisibility(View.VISIBLE);
            normal_battery.setVisibility(View.INVISIBLE);
            if (Integer.parseInt(battery_life) == 30 || Integer.parseInt(battery_life) == 20 || Integer.parseInt(battery_life) == 10) {

                show_battery_warning(battery_life);

            }
        } else {
            low_battery.clearAnimation();
            low_battery.setVisibility(View.INVISIBLE);
            normal_battery.setVisibility(View.VISIBLE);
        }
    }

    public void battery_animations() {
        low_battery = findViewById(R.id.battery_low);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        low_battery.startAnimation(animation);
    }

    void show_notifications(String massage) {
        Button cancel;
        TextView note;
        //will create a view of our custom dialog layout
        View alertCustomdialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop, null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        cancel = alertCustomdialog.findViewById(R.id.cancel_button);
        note = alertCustomdialog.findViewById(R.id.notifiy);
        note.setText(massage);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //finally show the dialog box in android all
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    void show_battery_warning(String precentage) {
        Button cancel;
        TextView note;
        //will create a view of our custom dialog layout
        View alertCustomdialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.battery_warning, null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        cancel = alertCustomdialog.findViewById(R.id.cancel_button);
        note = alertCustomdialog.findViewById(R.id.notifiy);
        note.setText(precentage + "% battery remaining");
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //finally show the dialog box in android all
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }


    void confirmation() {
        Button cancel;
        TextView note;
        //will create a view of our custom dialog layout
        View alertCustomdialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.confirmation_start, null);
        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);
        cancel = alertCustomdialog.findViewById(R.id.cancel_button);
        final AlertDialog dialog = alert.create();
        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //finally show the dialog box in android all
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

}
