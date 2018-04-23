package raman.raman.raman.twenty1;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import raman.raman.raman.twenty1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class achievements extends AppCompatActivity {

    private TextView totalplayed,totalwon,totallost,userid,consecutive;
    private ImageView play10,win10,fiverow;
    private String gamerid;
    private Bundle b;
    private String totalgames;
    private ProgressDialog loading;
    private DatabaseReference userref;
    private int wins,played,lost;
    int loadingcount=0;
    private MediaPlayer gamemusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        totalplayed=(TextView)findViewById(R.id.totalplayed);
        totallost=(TextView)findViewById(R.id.totallost);
        totalwon=(TextView)findViewById(R.id.totalwon);
        userid=(TextView)findViewById(R.id.userid);
        consecutive=(TextView)findViewById(R.id.consecutive);

        play10=(ImageView)findViewById(R.id.play10);
        win10=(ImageView)findViewById(R.id.win10);
        fiverow=(ImageView)findViewById(R.id.fiverow);

        b=getIntent().getExtras();
        gamerid=b.getString("userid");

        userid.setText(gamerid);
        loading=new ProgressDialog(this);
        loading.setMessage("Loading");
        loading.show();


        userref= FirebaseDatabase.getInstance().getReference().child(gamerid);

        userref.child("online").setValue("true");

        userref.child("totalgames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalgames=dataSnapshot.getValue(String.class);
                loadingcount++;
                if(loadingcount>3)
                    loading.dismiss();
                totalplayed.setText(totalgames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userref.child("a3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String a3=dataSnapshot.getValue(String.class);
                loadingcount++;
                if(loadingcount>3)
                    loading.dismiss();

                if(a3.equals("false"))
                    fiverow.setAlpha((float) 0.4);
                else
                    fiverow.setAlpha((float) 1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        userref.child("totalwins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String totalwins=dataSnapshot.getValue(String.class);

                loadingcount++;
                if(loadingcount>3)
                    loading.dismiss();

                totalwon.setText(totalwins);
                wins=Integer.parseInt(totalwins);
                played=Integer.parseInt(totalgames);
                lost=played-wins;

                totallost.setText(String.valueOf(lost));

                if(played<10)
                    play10.setAlpha((float) 0.4);
                else
                    play10.setAlpha((float) 1 );


                if(wins<10)
                    win10.setAlpha((float) 0.4);
                else
                    win10.setAlpha((float) 1 );


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


userref.child("consecutive").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        String consec=dataSnapshot.getValue(String.class);
        loadingcount++;
        if(loadingcount>3)
            loading.dismiss();

        consecutive.setText(consec);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});



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


    @Override
    protected void onResume() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        gamemusic = MediaPlayer.create(this, R.raw.gamemusic);
        gamemusic.setLooping(true);
        gamemusic.setVolume(20,20);
        gamemusic.seekTo(Integer.parseInt(pref.getString("position","0")));

        gamemusic.start();
        super.onResume();
        super.onResume();
    }
}
