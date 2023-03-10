package com.barebrains.leciel19;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class event_details extends AppCompatActivity {

    TextView title,desc;
    ImageView eveimage;
    ToggleButton favtb;
    DatabaseReference reference,reg;
    Intent intent;
    String child,tag;
    TabLayout dtab;
    SharedPreferences sp;
    Button bb2;
    String tab1,tab2,tab3;
    Context context;
    String id="",img;
    AlertDialog.Builder a;
    AlertDialog vi;


    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //| View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        if(hasWindowFocus())
            hideSystemUI();
    }


    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
       // getWindow().setEnterTransition(new Explode());
       // getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_event_details);
        a=new AlertDialog.Builder(this);


        sp= this.getSharedPreferences("com.barebrains.leciel19",MODE_PRIVATE);

        bb2=findViewById(R.id.backbut2);
        intent = getIntent();
        child=intent.getStringExtra("category");
        tag= intent.getStringExtra("tag");
        title=findViewById(R.id.evedttitle);
        desc=findViewById(R.id.evedesc);
        dtab=findViewById(R.id.dtab);
        context =this;

        eveimage=findViewById(R.id.eveimv);
        favtb=findViewById(R.id.favButton);


        bb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child(child).child(tag);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    title.setText(dataSnapshot.child("name").getValue().toString());
                    img=dataSnapshot.child("imgurl").getValue().toString();
                    for(DataSnapshot snapshot:dataSnapshot.child("detail").getChildren()){
                        if(snapshot.getKey().toString().charAt(0)=='1'){
                            tab1=snapshot.getValue().toString();
                            desc.setText(tab1);
                        }
                        if(snapshot.getKey().toString().charAt(0)=='2'){
                            dtab.getTabAt(1).setText(snapshot.getKey().substring(1));
                            tab2=snapshot.getValue().toString();
                        }
                        if(snapshot.getKey().toString().charAt(0)=='3'){
                            tab3=snapshot.getValue().toString();
                        }
                    }
                Glide.with(context).load(img).centerCrop().into(eveimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final ImageView f=(ImageView)findViewById(R.id.fh) ;

        favtb.setChecked(sp.getBoolean(tag,false));
        favtb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sp.edit().putBoolean(tag,favtb.isChecked()).commit();
                if(isChecked){
                    ObjectAnimator fa=ObjectAnimator.ofFloat(f,"alpha",1,0);
                    fa.setDuration(500);
                    fa.start();
                    ObjectAnimator fa1=ObjectAnimator.ofFloat(f,"scaleX",1,5);
                    fa1.setDuration(500);
                    fa1.start();
                    ObjectAnimator fa2=ObjectAnimator.ofFloat(f,"scaleY",1,5);
                    fa2.setDuration(500);
                    fa2.start();

                }
            }
        });




        dtab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int a=tab.getPosition();
                if(a==0){
                    desc.setText(tab1);
                }
                if(a==1){
                    desc.setText(tab2);
                }
                if(a==2){
                    desc.setText(tab3);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });





/*removed register button*/
        ((Button)findViewById(R.id.reg)).setVisibility(View.GONE);
                                                                            /*removed register button*/






        reg = FirebaseDatabase.getInstance().getReference().child(child).child(tag);

        reg.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String tm="1";
                try {
                    tm = dataSnapshot.child("tm").getValue().toString();
                }catch(Exception e){}
                Button b[] = new Button[2];
                LinearLayout linlay = new LinearLayout(context);
                linlay.setOrientation(LinearLayout.VERTICAL);
                Log.i("alert",tm);
                for (int i = 0; i < tm.length(); i++) {
                    b[i] = new Button(context);
                    b[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
                    b[i].setText("Register for " + tm.charAt(i));
                    linlay.addView(b[i]);
                    final int i1 = i;
                    b[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, register.class);
                            try {
                                id = dataSnapshot.child("id").getValue().toString();
                                id = id.substring(3 * i1, 3 * (i1 + 1));
                            }
                            catch(Exception e){}
                            intent.putExtra("id", id);
                            intent.putExtra("token", "");
                            if (tag.equals("W7") || tag.equals("Tg"))
                                intent.putExtra("ex", tag);
                            else
                                intent.putExtra("ex", "");
                            startActivity(intent);
                        }
                    });
                }

                a.setTitle("Register");
                a.setView(linlay);
                vi=a.create();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });






        ((Button)findViewById(R.id.reg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vi.show();
            }
        });



    }
}
