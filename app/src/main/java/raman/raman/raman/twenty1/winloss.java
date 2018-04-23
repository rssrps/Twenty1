package raman.raman.raman.twenty1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import raman.raman.raman.twenty1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class winloss extends AppCompatActivity {

    private ImageView winorloss,playagain,quitgame;
    private DatabaseReference mdatabase;
    private DatabaseReference userref,opponentref;
    private String usergmid,oppgmid;
    private String userstart="false",opponentstart="false";
    private ProgressDialog waiting;
    private String useronline="false",opponentonline="false",strmoney="0",lastbug="false";
    private int toacounter=0,intmoney=0;
    private Vibrator v2;
    private MediaPlayer mpwin,mloss,btnclick;
    private int buttonpressed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winloss);

        winorloss=(ImageView)findViewById(R.id.winorloss);
        playagain=(ImageView)findViewById(R.id.playagain);
        quitgame=(ImageView)findViewById(R.id.quitgame);
        waiting=new ProgressDialog(this);
        v2 = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        waiting.setMessage("Waiting for opponent");
        btnclick = MediaPlayer.create(this, R.raw.cool);
        mdatabase= FirebaseDatabase.getInstance().getReference();

        Bundle b;
        b=getIntent().getExtras();
        String status=b.getString("status");
        usergmid=b.getString("usergmid");
        oppgmid=b.getString("oppgmid");

        buttonpressed=0;

        mpwin = MediaPlayer.create(this, R.raw.winsound);
        mloss= MediaPlayer.create(this, R.raw.loser);

        userref=mdatabase.child(usergmid);
        opponentref=mdatabase.child(oppgmid);


        userref.child("online").setValue("true");


        userref.child("lastbug").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lastbug=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(status.equals("won"))
        {
            winorloss.setImageResource(R.drawable.youwin);
            mpwin.start();
            userref.child("status").setValue("neutral");

        }
        else
        {
            winorloss.setImageResource(R.drawable.youlose);
            mloss.start();
            userref.child("status").setValue("neutral");
        }


        userref.child("emoji").setValue("none");
        opponentref.child("emoji").setValue("none");

        userref.child("startgame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userstart=dataSnapshot.getValue(String.class);

                if(userstart.equals("true")&&opponentstart.equals("true"))
                {
                    waiting.dismiss();

                    userref.child("myturn").setValue("true");
                    opponentref.child("myturn").setValue("false");

                    userref.child("current_game_sum").setValue("0");
                    opponentref.child("current_game_sum").setValue("0");

                    userref.child("playagain").setValue("false");

                    Intent i=new Intent(winloss.this,playgame.class);
                    if(lastbug.equals("false")&&buttonpressed==1)
                    startActivity(i);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        opponentref.child("startgame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                opponentstart=dataSnapshot.getValue(String.class);

                if(userstart.equals("true")&&opponentstart.equals("true"))
                {
                    waiting.dismiss();

                    userref.child("myturn").setValue("true");
                    opponentref.child("myturn").setValue("false");

                    userref.child("current_game_sum").setValue("0");
                    opponentref.child("current_game_sum").setValue("0");

                    userref.child("playagain").setValue("false");

                    Intent i=new Intent(winloss.this,playgame.class);
                    if(lastbug.equals("false")&&buttonpressed==1)
                    startActivity(i);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        opponentref.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                opponentonline=dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                v2.vibrate(150);
                btnclick.start();
                userref.child("lastbug").setValue("false");

                buttonpressed=1;
                playagain.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playagain.setAlpha((float) 1);


                        userref.child("emoji").setValue("none");
                        opponentref.child("emoji").setValue("none");

                        userref.child("status").setValue("neutral");
                        opponentref.child("status").setValue("neutral");

                        userref.child("playagain").setValue("true");
                        userref.child("startgame").setValue("true");

                        userref.child("current_game_sum").setValue("0");
                        opponentref.child("current_game_sum").setValue("0");


                        waiting.show();

                        if(opponentonline.equals("false"))
                        {

                            if(toacounter==0)
                            {
                                Toast.makeText(getApplicationContext(),"Opponent left the game!",Toast.LENGTH_SHORT).show();
                                toacounter=1;
                            }
                            userref.child("status").setValue("neutral");
                            opponentref.child("status").setValue("neutral");

                            userref.child("playagain").setValue("false");
                            userref.child("startgame").setValue("false");

                            userref.child("emoji").setValue("none");
                            opponentref.child("emoji").setValue("none");

                            userref.child("current_game_sum").setValue("0");
                            opponentref.child("current_game_sum").setValue("0");


                            userref.child("opponent_gamer_id").setValue("");

                            Intent i=new Intent(winloss.this, MainActivity.class);
                            startActivity(i);
                        }


                        userref.child("startgame").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                userstart=dataSnapshot.getValue(String.class);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        //Do something after 100ms
                    }
                }, 90);



            }
        });



        quitgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                v2.vibrate(150);
                btnclick.start();

                buttonpressed=1;

                userref.child("lastbug").setValue("false");

                quitgame.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        quitgame.setAlpha((float) 1);

                        userref.child("emoji").setValue("none");
                        opponentref.child("emoji").setValue("none");

                        userref.child("status").setValue("neutral");
                        opponentref.child("status").setValue("neutral");

                        userref.child("playagain").setValue("false");
                        userref.child("startgame").setValue("false");

                        userref.child("current_game_sum").setValue("0");
                        opponentref.child("current_game_sum").setValue("0");

                        userref.child("opponent_gamer_id").setValue("");

                        Intent i=new Intent(winloss.this, MainActivity.class);
                        startActivity(i);


                        //Do something after 100ms
                    }
                }, 90);


            }
        });




    }


    @Override
    public void onBackPressed() {

// make sure you have this outcommented
// super.onBackPressed();
        userref.child("startgame").setValue("false");
        opponentref.child("startgame").setValue("false");

        userref.child("playagain").setValue("false");
        userref.child("lastbug").setValue("false");

        userref.child("opponent_gamer_id").setValue("");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        userref.child("online").setValue("false");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userref.child("online").setValue("true");
    }

}
