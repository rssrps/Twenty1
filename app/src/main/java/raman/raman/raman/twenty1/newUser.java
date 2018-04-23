package raman.raman.raman.twenty1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import raman.raman.raman.twenty1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class newUser extends AppCompatActivity {

    private EditText username;
    private String usernmtext;
    private ImageView boy,girl;
    private ImageView gobtn;
    private String avatar="";
    private String count="1000";
    private boolean allok=false;
    private DatabaseReference mdatabase,thisuser;
    private int counter1;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        username=(EditText)findViewById(R.id.username);
        boy=(ImageView)findViewById(R.id.boy);
        girl=(ImageView)findViewById(R.id.girl);
        gobtn=(ImageView)findViewById(R.id.go_btn);
        mdatabase= FirebaseDatabase.getInstance().getReference();
        loading=new ProgressDialog(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();


        loading.setMessage("loading");
        loading.show();

        mdatabase.child("count").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            count=dataSnapshot.getValue(String.class);
                loading.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                gobtn.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gobtn.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 90);

                if(avatar.equals(""))
                    Toast.makeText(getApplicationContext(),"Choose an avatar!",Toast.LENGTH_SHORT).show();

                else {

                    if (username.getText().toString().trim().equals(""))
                        Toast.makeText(getApplicationContext(), "Username required!", Toast.LENGTH_SHORT).show();

                    else
                    {


                        usernmtext=username.getText().toString().trim();
                        mdatabase.child(usernmtext+count).setValue("");

                        editor.putString("gamerid",usernmtext+count);

                        thisuser=mdatabase.child(usernmtext+count);
                        thisuser.child("gameid").setValue(usernmtext+count);

                        counter1=Integer.parseInt(count);
                        counter1=counter1+1;
                        mdatabase.child("count").setValue(String.valueOf(counter1));


                        thisuser.child("username").setValue(usernmtext);
                        thisuser.child("avatar").setValue(avatar);
                        thisuser.child("totalgames").setValue("0");
                        thisuser.child("totalwins").setValue("0");

                        thisuser.child("myturn").setValue("false");
                        thisuser.child("gamecounter").setValue("0");
                        thisuser.child("user_age").setValue(count);
                        thisuser.child("current_game_sum").setValue("0");

                        thisuser.child("currentmove").setValue("0");
                        thisuser.child("opponent_gamer_id").setValue("");
                        thisuser.child("opponent_accepted").setValue("false");

                        thisuser.child("hasinvitation").setValue("false");
                        thisuser.child("startgame").setValue("false");
                        thisuser.child("status").setValue("neutral");

                        thisuser.child("playagain").setValue("false");
                        thisuser.child("consecutive").setValue("0");
                        thisuser.child("a3").setValue("false");

                        thisuser.child("online").setValue("false");
                        thisuser.child("money").setValue("100");
                        thisuser.child("emoji").setValue("none");

                        thisuser.child("lastbug").setValue("false");

                        editor.putString("first","no");

                        editor.commit();

                        Intent i=new Intent(newUser.this,MainActivity.class);
                        startActivity(i);

                    }
                }

            }

        });


        boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boy.setAlpha((float)1);
                avatar="boy";
                girl.setAlpha((float)0.6);

            }
        });

        girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                girl.setAlpha((float)1);
                avatar="girl";
                boy.setAlpha((float)0.6);

            }
        });


    }
}
