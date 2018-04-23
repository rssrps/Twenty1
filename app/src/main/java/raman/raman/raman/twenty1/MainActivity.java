package raman.raman.raman.twenty1;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import raman.raman.raman.twenty1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static raman.raman.raman.twenty1.R.attr.title;

public class MainActivity extends AppCompatActivity {

    private ImageView playgame;
    private ImageView myachievements,avatar;
    private ImageView message;
    private ImageView help,reseticon,tick,cross;
    private DatabaseReference mdatabase;
    private DatabaseReference userref,opponentref;
    private ProgressDialog loading;
    private String gamerid,avatartype,hasinvitation="false",opponentgamerid="",playagain="false";
    private TextView gmid,oppgamerid,paise;
    private int loadingcount=0,notifyonce=0;
    private String moneyuser="0";
    private Vibrator v1;
    private MediaPlayer mp,bclick;
    private int musicstart=0,musicisplaying=0;
    private int appusers;
    private MediaPlayer gamemusic,invitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playgame = (ImageView) findViewById(R.id.playgame);
        myachievements = (ImageView) findViewById(R.id.myachievements);
        message = (ImageView) findViewById(R.id.message);
        help = (ImageView) findViewById(R.id.help);
        loading = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference();
        gmid = (TextView) findViewById(R.id.gmid);
        avatar = (ImageView) findViewById(R.id.avatar);
        reseticon = (ImageView) findViewById(R.id.reseticon);
        oppgamerid = (TextView) findViewById(R.id.oppgamerid);
        tick = (ImageView) findViewById(R.id.tick);
        cross = (ImageView) findViewById(R.id.cross);
        paise = (TextView) findViewById(R.id.paise);

        mp = MediaPlayer.create(this, R.raw.check);
        invitation = MediaPlayer.create(this, R.raw.invitation);
        invitation.setVolume(70, 70);
        bclick = MediaPlayer.create(this, R.raw.cool);

        v1 = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        gamerid = pref.getString("gamerid", "");
        userref = mdatabase.child(gamerid);

        loading.setMessage("Getting game state");
        loading.show();

        gmid.setText(pref.getString("gamerid", "null value"));


        oppgamerid.setVisibility(View.INVISIBLE);
        tick.setVisibility(View.INVISIBLE);
        cross.setVisibility(View.INVISIBLE);


        mdatabase.child(pref.getString("gamerid", "")).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                avatartype = dataSnapshot.getValue(String.class);
                editor.putString("avatar", avatartype);
                editor.commit();
                loadingcount = loadingcount + 1;

                if (loadingcount > 3) {
                    loading.dismiss();

                    if (musicstart == 0) {
                        musicstart = 1;

                    }

                }

                if (avatartype.equals("boy"))
                    avatar.setImageResource(R.drawable.boy);
                else
                    avatar.setImageResource(R.drawable.girl);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mdatabase.child(gamerid).child("hasinvitation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                hasinvitation = dataSnapshot.getValue(String.class);
                loadingcount = loadingcount + 1;

                if (loadingcount > 3)
                    loading.dismiss();

                if (hasinvitation.equals("true")) {
                    message.setImageResource(R.mipmap.onemessage);

                    tick.setVisibility(View.VISIBLE);
                    cross.setVisibility(View.VISIBLE);
                    oppgamerid.setVisibility(View.VISIBLE);

                    if (notifyonce == 0) {
                        notifyonce = 1;
                        notifyThis("Twenty1 Invitation!", opponentgamerid + " invited you");
                        v1.vibrate(400);
                        invitation.start();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mdatabase.child(gamerid).child("current_game_sum").setValue("0");
        mdatabase.child(gamerid).child("online").setValue("true");

        mdatabase.child(gamerid).child("startgame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String startgame = dataSnapshot.getValue(String.class);
                loadingcount = loadingcount + 1;

                if (loadingcount > 3)
                    loading.dismiss();

                if (startgame.equals("true") && playagain.equals("false")) {
                    Intent i2 = new Intent(MainActivity.this, playgame.class);
                    startActivity(i2);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mdatabase.child(gamerid).child("opponent_gamer_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                opponentgamerid = dataSnapshot.getValue(String.class);
                oppgamerid.setText(opponentgamerid);
                opponentref = mdatabase.child(opponentgamerid);


                loadingcount = loadingcount + 1;
                if (loadingcount > 3)
                    loading.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mdatabase.child(gamerid).child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                moneyuser = dataSnapshot.getValue(String.class);
                paise.setText("$ " + moneyuser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mdatabase.child(gamerid).child("playagain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                playagain = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        playgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mp.start();
                playgame.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        playgame.setAlpha((float) 1);

                        Intent i = new Intent(MainActivity.this, playgame.class);
                        startActivity(i);

                        //Do something after 100ms
                    }
                }, 250);


            }


        });


        myachievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bclick.start();

                myachievements.setAlpha((float) 1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myachievements.setAlpha((float) 0.7);
                        //Do something after 100ms

                        editor.putString("position", String.valueOf(gamemusic.getCurrentPosition()));
                        editor.commit();
                        Intent i = new Intent(MainActivity.this, achievements.class);
                        i.putExtra("userid", gamerid);
                        startActivity(i);

                    }
                }, 200);


            }
        });


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bclick.start();
                message.setAlpha((float) 1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        message.setAlpha((float) 0.7);
                        //Do something after 100ms
                    }
                }, 200);

                message.setImageResource(R.mipmap.nomessage);
                if (hasinvitation.equals("false"))
                    Toast.makeText(getApplicationContext(), "No invitations yet!", Toast.LENGTH_SHORT).show();

            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bclick.start();
                help.setAlpha((float) 1);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        help.setAlpha((float) 0.7);
                        //Do something after 100ms


                    }
                }, 200);

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                final TextView input = new TextView(MainActivity.this);
                input.setTextSize(14);

                input.setText("\n\n  -> Your Gamer_id is " + gamerid + " \n  -> Invite your friend using their Gamer_id\n\n  Game rules :\n\n\t->  The game starts with SUM 0\n\t->  Atmost 3 steps allowed from current SUM.\n\t->  The player who moves to 21 loses.\n\n    Hint: Learn as you play!");
                builder.setView(input);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // User clicked Yes button

                    }
                });


                builder.setTitle("Help");

                android.app.AlertDialog dialog = builder.create();
                dialog.setIcon(R.drawable.helpericon);
                dialog.show();


            }
        });


        reseticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bclick.start();
                reseticon.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reseticon.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 200);


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                final TextView input = new TextView(MainActivity.this);
                input.setTextSize(14);

                input.setText("\n    Are you sure you want to permanently delete \n    this account and create a new user?");
                builder.setView(input);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // User clicked Yes button

                        Intent i = new Intent(MainActivity.this, newUser.class);
                        startActivity(i);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // do nothing if user clicked NO

                    }
                });


                builder.setTitle("Reset all data");

                android.app.AlertDialog dialog = builder.create();
                dialog.setIcon(R.drawable.reset_icon);
                dialog.show();


            }
        });


        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bclick.start();
                userref.child("startgame").setValue("true");
                opponentref.child("startgame").setValue("true");

                userref.child("myturn").setValue("true");
                opponentref.child("myturn").setValue("false");

                tick.setVisibility(View.INVISIBLE);
                cross.setVisibility(View.INVISIBLE);
                oppgamerid.setVisibility(View.INVISIBLE);

                message.setImageResource(R.mipmap.nomessage);
                userref.child("hasinvitation").setValue("false");

                userref.child("current_game_sum").setValue("0");
                opponentref.child("current_game_sum").setValue("0");


            }
        });


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bclick.start();

                userref.child("opponent_gamer_id").setValue("");
                opponentref.child("opponent_gamer_id").setValue("");

                userref.child("startgame").setValue("false");
                opponentref.child("startgame").setValue("false");

                userref.child("myturn").setValue("false");
                opponentref.child("myturn").setValue("false");

                userref.child("hasinvitation").setValue("false");

                tick.setVisibility(View.INVISIBLE);
                cross.setVisibility(View.INVISIBLE);
                oppgamerid.setVisibility(View.INVISIBLE);

                message.setImageResource(R.mipmap.nomessage);

                userref.child("current_game_sum").setValue("0");
                opponentref.child("current_game_sum").setValue("0");


            }
        });




    }

    public void notifyThis(String title, String message) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(getApplicationContext());
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setTicker("{your tiny message}")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("INFO");

        NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }







    @Override
    public void onBackPressed() {

// make sure you have this outcommented
// super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        userref.child("online").setValue("false");

        userref.child("emoji").setValue("none");
        opponentref.child("emoji").setValue("none");

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        userref.child("online").setValue("false");
        userref.child("opponent_gamer_id").setValue("");
        userref.child("startgame").setValue("false");
        userref.child("myturn").setValue("false");
        userref.child("current_game_sum").setValue("0");

        userref.child("emoji").setValue("none");
        opponentref.child("emoji").setValue("none");

        super.onDestroy();

    }

    @Override
    protected void onResume() {

        userref.child("online").setValue("true");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        gamemusic = MediaPlayer.create(this, R.raw.gamemusic);
        gamemusic.setLooping(true);
        gamemusic.setVolume(70,70);
        gamemusic.seekTo(Integer.parseInt(pref.getString("position","1")));
        gamemusic.start();
        super.onResume();
        super.onResume();


    }


    @Override
    protected void onPause() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        editor.putString("position",String.valueOf(gamemusic.getCurrentPosition()));
        editor.commit();

        gamemusic.stop();
        gamemusic.release();
        super.onPause();

    }


}



