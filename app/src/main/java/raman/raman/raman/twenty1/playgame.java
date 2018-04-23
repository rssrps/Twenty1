package raman.raman.raman.twenty1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import raman.raman.raman.twenty1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class playgame extends AppCompatActivity {

    private EditText opponent_gmid;
    private ImageView invite_button,user_avatar,opponent_avatar,one,two,three;
    private DatabaseReference mdatabase,userref,opponentref;
    private String stropponent="",struser="";
    private TextView userid,opponentid,tv3,optimer,ustimer;
    private String oppid="";
    private TextView gamesum;
    private ProgressDialog loading;
    private String gamestart="false",oppavatar="";
    private int loadingcount=0;
    private ImageView makemove,coin;
    private String whichselected="0";
    private ImageView uturn,oturn,uemoji,oemoji,e1,e2,e3,e4;
    private ProgressBar probar;
    private TextView optext,ustext,minus10;
    private int opponentconsecutive=0,toastcount=0,countertoast=0,callonce=0;
    private boolean playing=false;

    private int usersum=0,opponentsum=0,money=0;
    private String userturn="true",opponentturn="false";
    int usertotalgames=0,opponenttotalgames=0,winsuser=0,winsopponent=0;
    private String userstatus="neutral",oppstatus="neutral";
    private String useronline="false",opponentonline="false";
    private String uem="none",oem="none",stringmoney="0";

    private Vibrator v;
    private String opstrmoney="0";
    private int opintmoney=0,writeonce=0,playonce=0;
    private MediaPlayer clicksound,bell,gamemusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);

        opponent_gmid=(EditText)findViewById(R.id.opponent_gmid);
        invite_button=(ImageView)findViewById(R.id.invite_button);
        user_avatar=(ImageView)findViewById(R.id.user_avatar);
        opponent_avatar=(ImageView)findViewById(R.id.opponent_avatar);
        one=(ImageView)findViewById(R.id.one);
        two=(ImageView)findViewById(R.id.two);
        three=(ImageView)findViewById(R.id.three);
        tv3=(TextView)findViewById(R.id.textView3);
        gamesum=(TextView)findViewById(R.id.gamesum);
        userid=(TextView)findViewById(R.id.user_gamerid);
        opponentid=(TextView)findViewById(R.id.opponent_gamerid);
        loading=new ProgressDialog(this);
        uturn=(ImageView)findViewById(R.id.uturn);
        oturn=(ImageView)findViewById(R.id.oturn);
        makemove=(ImageView)findViewById(R.id.makemove);
        probar=new ProgressBar(this);
        optext=(TextView)findViewById(R.id.thinking);
        ustext=(TextView)findViewById(R.id.yourtuen);
        uemoji=(ImageView)findViewById(R.id.emojiuser);
        oemoji=(ImageView)findViewById(R.id.emojiopponent);

        v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        clicksound = MediaPlayer.create(this, R.raw.cool);
        bell = MediaPlayer.create(this, R.raw.bell);


        coin=(ImageView)findViewById(R.id.coin);
        minus10=(TextView)findViewById(R.id.minusten);

        e1=(ImageView)findViewById(R.id.e1);
        e2=(ImageView)findViewById(R.id.e2);
        e3=(ImageView)findViewById(R.id.e3);
        e4=(ImageView)findViewById(R.id.e4);


        uemoji.setVisibility(View.INVISIBLE);
        oemoji.setVisibility(View.INVISIBLE);

        e1.setVisibility(View.INVISIBLE);
        e2.setVisibility(View.INVISIBLE);
        e3.setVisibility(View.INVISIBLE);
        e4.setVisibility(View.INVISIBLE);

        probar.setVisibility(View.INVISIBLE);

        one.setVisibility(View.INVISIBLE);
        two.setVisibility(View.INVISIBLE);
        three.setVisibility(View.INVISIBLE);
        uturn.setVisibility(View.INVISIBLE);

        ustext.setVisibility(View.INVISIBLE);

        loading.setMessage("loading...Please wait");
        loading.show();

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        struser=pref.getString("gamerid","");

        mdatabase= FirebaseDatabase.getInstance().getReference();
        userref=mdatabase.child(struser);

        userref.child("online").setValue("true");


        userid.setText(pref.getString("gamerid",""));

        if(pref.getString("avatar","boy").equals("boy"))
            user_avatar.setImageResource(R.drawable.boy);
        else
            user_avatar.setImageResource(R.drawable.girl);


        oturn.setVisibility(View.INVISIBLE);
        optext.setVisibility(View.INVISIBLE);



        userref.child("opponent_gamer_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                oppid=dataSnapshot.getValue(String.class);
                opponentid.setText(oppid);

                loadingcount=loadingcount+1;
                if(loadingcount>4)
                loading.dismiss();

                if(oppid.equals(""))
                {
                   loading.dismiss();
                    oturn.setVisibility(View.INVISIBLE);
                    optext.setVisibility(View.INVISIBLE);

                    //do nothing
                }
                else
                {
                    opponentref=mdatabase.child(oppid);

                    opponentref.child("avatar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            oppavatar=dataSnapshot.getValue(String.class);
                            if(oppavatar.equals("boy"))
                                opponent_avatar.setImageResource(R.drawable.boy);
                            else
                                opponent_avatar.setImageResource(R.drawable.girl);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    opponentref.child("avatar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            oppavatar = dataSnapshot.getValue(String.class);
                            loadingcount=loadingcount+1;
                            if(loadingcount>4)
                                loading.dismiss();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    opponentref.child("myturn").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            opponentturn=dataSnapshot.getValue(String.class);
                            loadingcount=loadingcount+1;
                            if(loadingcount>4)
                                loading.dismiss();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    opponentref.child("current_game_sum").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String cursum=dataSnapshot.getValue(String.class);
                            loadingcount=loadingcount+1;
                            if(loadingcount>4)
                                loading.dismiss();

                            opponentsum=Integer.parseInt(cursum);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    opponentref.child("totalgames").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            opponenttotalgames=Integer.parseInt(dataSnapshot.getValue(String.class));
                            loadingcount=loadingcount+1;
                            if(loadingcount>4)
                                loading.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                   opponentref.child("totalwins").addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                            winsopponent=Integer.parseInt(dataSnapshot.getValue(String.class));
                           loadingcount=loadingcount+1;
                           if(loadingcount>4)
                               loading.dismiss();

                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });



                    opponentref.child("status").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            oppstatus=dataSnapshot.getValue(String.class);
                            loadingcount=loadingcount+1;
                            if(loadingcount>4)
                                loading.dismiss();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    opponentref.child("consecutive").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                       opponentconsecutive=Integer.parseInt(dataSnapshot.getValue(String.class));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    opponentref.child("startgame").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String lastbug=dataSnapshot.getValue(String.class);
                            if(lastbug.equals("false"))
                            {
                                opponentref.child("myturn").setValue("false");
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
                            if(opponentonline.equals("false"))
                            {
                                if(gamestart.equals("true"))
                                {
                                    userref.child("status").setValue("won");
                                    opponentref.child("status").setValue("lost");

                                    userref.child("startgame").setValue("false");
                                    if(toastcount==0)
                                    {
                                        Toast.makeText(getApplicationContext(),"Opponent left the game!",Toast.LENGTH_SHORT).show();
                                        toastcount=1;
                                    }
                                    userref.child("myturn").setValue("false");
                                    userref.child("current_game_sum").setValue("0");

                                    opponentref.child("startgame").setValue("false");
                                    opponentref.child("myturn").setValue("false");
                                    opponentref.child("current_game_sum").setValue("0");



                                    usertotalgames=usertotalgames+1;
                                    userref.child("totalgames").setValue(String.valueOf(usertotalgames));

                                    winsuser=winsuser+1;
                                    opponenttotalgames=opponenttotalgames+1;
                                    opponentref.child("totalgames").setValue(String.valueOf(opponenttotalgames));

                                    opponentref.child("consecutive").setValue("0");

                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


      if(oppid.equals(""))
      {

          loading.dismiss();
          //do nothing

      }
        else {
          opponentref.child("avatar").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                  oppavatar = dataSnapshot.getValue(String.class);
                  loadingcount=loadingcount+1;
                  if(loadingcount>4)
                      loading.dismiss();


              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });

          opponentref.child("myturn").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                 opponentturn=dataSnapshot.getValue(String.class);
                  loadingcount=loadingcount+1;
                  if(loadingcount>4)
                      loading.dismiss();


              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });


          opponentref.child("current_game_sum").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                  String cursum=dataSnapshot.getValue(String.class);
                  loadingcount=loadingcount+1;
                  if(loadingcount>4)
                      loading.dismiss();

                  opponentsum=Integer.parseInt(cursum);

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });


          opponentref.child("emoji").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {

                  oem=dataSnapshot.getValue(String.class);
                  if(oem.equals("none"))
                  {
                      oemoji.setVisibility(View.INVISIBLE);
                  }
                  else
                  {
                      oemoji.setVisibility(View.VISIBLE);

                      if(oem.equals("e1"))
                        oemoji.setImageResource(R.drawable.angry);
                      if(oem.equals("e2"))
                          oemoji.setImageResource(R.drawable.sunglasses);
                      if(oem.equals("e3"))
                          oemoji.setImageResource(R.drawable.heart);
                      if(oem.equals("e4"))
                          oemoji.setImageResource(R.drawable.laughing);


                  }

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });



      }


        userref.child("startgame").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               gamestart=dataSnapshot.getValue(String.class);
                loadingcount=loadingcount+1;
                if(loadingcount>4)
                    loading.dismiss();


                if(gamestart.equals("true"))
                {

                    tv3.setVisibility(View.INVISIBLE);
                    opponent_gmid.setVisibility(View.INVISIBLE);
                    invite_button.setVisibility(View.INVISIBLE);
                    opponentid.setText(oppid);

                    e1.setVisibility(View.VISIBLE);
                    e2.setVisibility(View.VISIBLE);
                    e3.setVisibility(View.VISIBLE);
                    e4.setVisibility(View.VISIBLE);


                    if (oppavatar.equals("boy"))
                        opponent_avatar.setImageResource(R.drawable.boy);
                    else
                        opponent_avatar.setImageResource(R.drawable.girl);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userref.child("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                useronline=dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userref.child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                     stringmoney=dataSnapshot.getValue(String.class);
                     money=Integer.parseInt(stringmoney);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userref.child("myturn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userturn=dataSnapshot.getValue(String.class);
                loadingcount=loadingcount+1;
                if(loadingcount>4)
                    loading.dismiss();


                if(userturn.equals("true"))
                {
                    one.setVisibility(View.VISIBLE);

                    if(usersum<20)
                    two.setVisibility(View.VISIBLE);

                    if(usersum<19)
                    three.setVisibility(View.VISIBLE);

                    uturn.setVisibility(View.VISIBLE);
                    ustext.setVisibility(View.VISIBLE);

                    oturn.setVisibility(View.INVISIBLE);
                    optext.setVisibility(View.INVISIBLE);

                    probar.setVisibility(View.INVISIBLE);

                    whichselected="0";
                    one.setAlpha((float)0.6);
                    two.setAlpha((float)0.6);
                    three.setAlpha((float)0.6);



                }

                else
                {
                    one.setVisibility(View.INVISIBLE);
                    two.setVisibility(View.INVISIBLE);
                    three.setVisibility(View.INVISIBLE);

                    if(oppid.equals("")==false)
                    {
                        oturn.setVisibility(View.VISIBLE);
                        optext.setVisibility(View.VISIBLE);
                    }

                    uturn.setVisibility(View.INVISIBLE);
                    ustext.setVisibility(View.INVISIBLE);

                    probar.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userref.child("current_game_sum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String cursum=dataSnapshot.getValue(String.class);
                loadingcount=loadingcount+1;
                if(loadingcount>4)
                    loading.dismiss();

                usersum=Integer.parseInt(cursum);

                if(usersum==19)
                    three.setVisibility(View.INVISIBLE);
                if(usersum==20)
                {
                    two.setVisibility(View.INVISIBLE);
                    three.setVisibility(View.INVISIBLE);
                }


                String printsum;
                if(usersum<10)
                {
                    printsum="0"+String.valueOf(usersum);
                    gamesum.setTextColor(getResources().getColor(R.color.golden));
                }

                else
                {
                    printsum = String.valueOf(usersum);

                    if(usersum<21)
                    gamesum.setTextColor(getResources().getColor(R.color.newcolor));

                    else
                        gamesum.setTextColor(getResources().getColor(R.color.colorPrimary
                        ));
                }
                gamesum.setText(printsum);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        userref.child("emoji").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                uem=dataSnapshot.getValue(String.class);
                if(uem.equals("none"))
                {
                    uemoji.setVisibility(View.INVISIBLE);
                }
                else
                {
                    uemoji.setVisibility(View.VISIBLE);

                    if(uem.equals("e1"))
                        uemoji.setImageResource(R.drawable.angry);
                    if(uem.equals("e2"))
                        uemoji.setImageResource(R.drawable.sunglasses);
                    if(uem.equals("e3"))
                        uemoji.setImageResource(R.drawable.heart);
                    if(uem.equals("e4"))
                        uemoji.setImageResource(R.drawable.laughing);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        userref.child("totalgames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usertotalgames=Integer.parseInt(dataSnapshot.getValue(String.class));
                loadingcount=loadingcount+1;
                if(loadingcount>4)
                    loading.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userref.child("totalwins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                winsuser=Integer.parseInt(dataSnapshot.getValue(String.class));
                loadingcount=loadingcount+1;
                if(loadingcount>4)
                    loading.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userref.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userstatus=dataSnapshot.getValue(String.class);

                if(userstatus.equals("won"))
                {
                    Intent i3=new Intent(playgame.this,winloss.class);
                    i3.putExtra("status","won");

                    i3.putExtra("usergmid",struser);
                    i3.putExtra("oppgmid",oppid);

                //    userref.child("status").setValue("neutral");
                    opponentref.child("status").setValue("neutral");
                    startActivity(i3);
                }

                if(userstatus.equals("lost"))
                {
                    Intent i=new Intent(playgame.this,winloss.class);
                    i.putExtra("status","lost");

                    i.putExtra("usergmid",struser);
                    i.putExtra("oppgmid",oppid);

                    userref.child("status").setValue("neutral");
                    opponentref.child("status").setValue("neutral");
                    startActivity(i);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        invite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                clicksound.start();
                invite_button.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        invite_button.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 90);

                stropponent=opponent_gmid.getText().toString();
                opponentid.setText(stropponent);

                if(stropponent.equals(""))
                    Toast.makeText(getApplicationContext(),"Opponent Gamer_id required!",Toast.LENGTH_SHORT).show();
                else
                {

                    mdatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild(stropponent)&&stropponent.equals(pref.getString("gamerid",""))==false)
                            {

                                if(callonce==0)
                                {
                                    callonce = 1;

                                    opponentref = mdatabase.child(stropponent);
                                    opponent_gmid.setText("");

                                    userref.child("opponent_gamer_id").setValue(stropponent);

                                    opponentref.child("opponent_gamer_id").setValue(struser);
                                    opponentref.child("hasinvitation").setValue("true");

                                    opponentid.setText(oppid);

                                    bell.start();
                                    Toast.makeText(getApplicationContext(), "waiting for opponent", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(playgame.this, MainActivity.class);
                                    startActivity(i);

                                }

                            }

                            else
                            {

                                opponent_gmid.setText("");
                                opponentid.setText("");
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }



            }
        });



        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                one.setAlpha((float)1);
                two.setAlpha((float)0.6);
                three.setAlpha((float)0.6);
                whichselected="1";

                String printsum;
                if(usersum<10)
                    printsum="0"+String.valueOf(usersum);
                else
                    printsum=String.valueOf(usersum);

                gamesum.setText(printsum);



            }
        });


        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                two.setAlpha((float)1);
                one.setAlpha((float)0.6);
                three.setAlpha((float)0.6);
                whichselected="2";

                String printsum;
                if(usersum<10)
                    printsum="0"+String.valueOf(usersum);
                else
                    printsum=String.valueOf(usersum);

                gamesum.setText(printsum);


            }
        });


        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                three.setAlpha((float)1);
                two.setAlpha((float)0.6);
                one.setAlpha((float)0.6);
                whichselected="3";

                String printsum;
                if(usersum<10)
                    printsum="0"+String.valueOf(usersum);
                else
                    printsum=String.valueOf(usersum);

                gamesum.setText(printsum);



            }
        });


        makemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                makemove.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makemove.setAlpha((float) 1);

                        //Do something after 100ms
                    }
                }, 90);



                if(whichselected.equals("0"))
                {
                    //do nothing
                }
                else
                {

                    if(userturn.equals("true"))
                    {

                        v.vibrate(50);

                        if(whichselected.equals("1"))
                        {
                            if(usersum==20)
                            {

                                usertotalgames=usertotalgames+1;
                                userref.child("status").setValue("lost");
                                userref.child("totalgames").setValue(String.valueOf(usertotalgames));

                                winsopponent=winsopponent+1;
                                opponenttotalgames=opponenttotalgames+1;
                                opponentref.child("status").setValue("won");
                                opponentref.child("totalgames").setValue(String.valueOf(opponenttotalgames));
                                opponentref.child("totalwins").setValue(String.valueOf(winsopponent));

                                userref.child("myturn").setValue("false");
                                opponentref.child("myturn").setValue("false");

                                userref.child("current_game_sum").setValue("21");
                                opponentref.child("current_game_sum").setValue("21");

                                userref.child("startgame").setValue("false");
                                opponentref.child("startgame").setValue("false");


                                userref.child("consecutive").setValue("0");
                                opponentconsecutive++;
                                if(opponentconsecutive>=5)
                                    opponentref.child("a3").setValue("true");

                                opponentref.child("consecutive").setValue(String.valueOf(opponentconsecutive));







                                opponentref.child("money").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        opstrmoney=dataSnapshot.getValue(String.class);
                                        opintmoney=Integer.parseInt(opstrmoney);

                                        if(writeonce==0)
                                        {
                                            writeonce=1;
                                            opintmoney = opintmoney + 100;
                                            opponentref.child("money").setValue(String.valueOf(opintmoney));

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });






                            }
                            else
                            {
                                usersum=usersum+1;
                                opponentsum=opponentsum+1;

                                userref.child("current_game_sum").setValue(String.valueOf(usersum));
                                opponentref.child("current_game_sum").setValue(String.valueOf(usersum));

                                userref.child("myturn").setValue("false");
                                opponentref.child("myturn").setValue("true");


                            }

                        }


                        if(whichselected.equals("2"))
                        {

                            if(usersum==19)
                            {
                                usertotalgames=usertotalgames+1;
                                userref.child("status").setValue("lost");
                                userref.child("totalgames").setValue(String.valueOf(usertotalgames));

                                winsopponent=winsopponent+1;
                                opponenttotalgames=opponenttotalgames+1;
                                opponentref.child("status").setValue("won");
                                opponentref.child("totalgames").setValue(String.valueOf(opponenttotalgames));
                                opponentref.child("totalwins").setValue(String.valueOf(winsopponent));

                                userref.child("myturn").setValue("false");
                                opponentref.child("myturn").setValue("false");

                                userref.child("current_game_sum").setValue("21");
                                opponentref.child("current_game_sum").setValue("21");

                                userref.child("startgame").setValue("false");
                                opponentref.child("startgame").setValue("false");



                                userref.child("consecutive").setValue("0");
                                opponentconsecutive++;
                                if(opponentconsecutive>=5)
                                    opponentref.child("a3").setValue("true");

                                opponentref.child("consecutive").setValue(String.valueOf(opponentconsecutive));


                                opponentref.child("money").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        opstrmoney=dataSnapshot.getValue(String.class);
                                        opintmoney=Integer.parseInt(opstrmoney);

                                        if(writeonce==0)
                                        {
                                            writeonce=1;
                                            opintmoney = opintmoney + 100;
                                            opponentref.child("money").setValue(String.valueOf(opintmoney));

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }

                            else
                            {
                                if(usersum<19)
                                {
                                    usersum=usersum+2;
                                    opponentsum=opponentsum+2;

                                    userref.child("current_game_sum").setValue(String.valueOf(usersum));
                                    opponentref.child("current_game_sum").setValue(String.valueOf(usersum));

                                    userref.child("myturn").setValue("false");
                                    opponentref.child("myturn").setValue("true");


                                }

                            }




                        }


                        if(whichselected.equals("3"))
                        {


                            if(usersum==18)
                            {
                                usertotalgames=usertotalgames+1;
                                userref.child("status").setValue("lost");
                                userref.child("totalgames").setValue(String.valueOf(usertotalgames));

                                winsopponent=winsopponent+1;
                                opponenttotalgames=opponenttotalgames+1;
                                opponentref.child("status").setValue("won");
                                opponentref.child("totalgames").setValue(String.valueOf(opponenttotalgames));
                                opponentref.child("totalwins").setValue(String.valueOf(winsopponent));

                                userref.child("myturn").setValue("false");
                                opponentref.child("myturn").setValue("false");

                                userref.child("current_game_sum").setValue("21");
                                opponentref.child("current_game_sum").setValue("21");

                                userref.child("startgame").setValue("false");
                                opponentref.child("startgame").setValue("false");



                                userref.child("consecutive").setValue("0");
                                opponentconsecutive++;
                                if(opponentconsecutive>=5)
                                    opponentref.child("a3").setValue("true");

                                opponentref.child("consecutive").setValue(String.valueOf(opponentconsecutive));


                                opponentref.child("money").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        opstrmoney=dataSnapshot.getValue(String.class);
                                        opintmoney=Integer.parseInt(opstrmoney);

                                        if(writeonce==0)
                                        {
                                            writeonce=1;
                                            opintmoney = opintmoney + 100;
                                            opponentref.child("money").setValue(String.valueOf(opintmoney));

                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });




                            }

                            else
                            {
                                if(usersum<18)
                                {
                                    usersum=usersum+3;
                                    opponentsum=opponentsum+3;

                                    userref.child("current_game_sum").setValue(String.valueOf(usersum));
                                    opponentref.child("current_game_sum").setValue(String.valueOf(usersum));

                                    userref.child("myturn").setValue("false");
                                    opponentref.child("myturn").setValue("true");



                                }

                            }




                        }


                    }



                }

            }
        });


        opponent_avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if(gamestart.equals("true"))
                {
                    userref.child("status").setValue("won");
                    userref.child("startgame").setValue("false");
                    userref.child("myturn").setValue("false");
                    userref.child("emoji").setValue("none");
                    userref.child("current_game_sum").setValue("21");


                    opponentref.child("current_game_sum").setValue("21");
                    opponentref.child("myturn").setValue("false");
                    opponentref.child("startgame").setValue("false");
                    opponentref.child("emoji").setValue("none");
                    opponentref.child("status").setValue("lost");


                }

                return true;
            }
        });


        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e1.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        e1.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 150);

                if(money<10)
                {
                    Toast.makeText(getApplicationContext(),"Not enough Money!",Toast.LENGTH_SHORT).show();
                }

                else
                {

                    coin.setImageResource(R.drawable.angry);
                    coin.setAlpha((float) 1);
                    minus10.setAlpha((float) 1);

                    bell.start();
                    opponentref.child("emoji").setValue("e1");
                    oemoji.setVisibility(View.VISIBLE);
                    oemoji.setImageResource(R.drawable.angry);
                    money=money-10;
                    userref.child("money").setValue(String.valueOf(money));
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coin.setAlpha((float) 0);
                            minus10.setAlpha((float) 0);

                            //Do something after 100ms
                        }
                    }, 1000);


                }
            }
        });


        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e2.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        e2.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 150);

                if (money < 10) {
                    Toast.makeText(getApplicationContext(), "Not enough Money!", Toast.LENGTH_SHORT).show();
                } else {


                    coin.setImageResource(R.drawable.sunglasses);
                    coin.setAlpha((float) 1);
                    minus10.setAlpha((float) 1);

                    bell.start();
                    opponentref.child("emoji").setValue("e2");
                    oemoji.setVisibility(View.VISIBLE);
                    oemoji.setImageResource(R.drawable.sunglasses);
                    money = money - 10;
                    userref.child("money").setValue(String.valueOf(money));
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coin.setAlpha((float) 0);
                            minus10.setAlpha((float) 0);

                            //Do something after 100ms
                        }
                    }, 1000);

                }
            }
        });



        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e3.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        e3.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 150);

                if(money<10)
                {
                    Toast.makeText(getApplicationContext(),"Not enough Money!",Toast.LENGTH_SHORT).show();
                }

                else
                {

                    coin.setImageResource(R.drawable.heart);
                    coin.setAlpha((float) 1);
                    minus10.setAlpha((float) 1);


                    bell.start();
                    opponentref.child("emoji").setValue("e3");
                    oemoji.setVisibility(View.VISIBLE);
                    oemoji.setImageResource(R.drawable.heart);
                    money=money-10;
                    userref.child("money").setValue(String.valueOf(money));
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coin.setAlpha((float) 0);
                            minus10.setAlpha((float) 0);

                            //Do something after 100ms
                        }
                    }, 1000);


                }

            }
        });



        e4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                e4.setAlpha((float) 0.6);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        e4.setAlpha((float) 1);
                        //Do something after 100ms
                    }
                }, 150);


                if(money<10)
                {
                    Toast.makeText(getApplicationContext(),"Not enough Money!",Toast.LENGTH_SHORT).show();
                }

                else
                {

                    coin.setImageResource(R.drawable.laughing);
                    coin.setAlpha((float) 1);
                    minus10.setAlpha((float) 1);

                    bell.start();
                    opponentref.child("emoji").setValue("e4");
                    oemoji.setVisibility(View.VISIBLE);
                    oemoji.setImageResource(R.drawable.laughing);
                    money=money-10;
                    userref.child("money").setValue(String.valueOf(money));
                    final Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            coin.setAlpha((float) 0);
                            minus10.setAlpha((float) 0);

                            //Do something after 100ms
                        }
                    }, 1000);


                }
            }


        });


    }




    @Override
    public void onBackPressed() {

// make sure you have this outcommented

        super.onBackPressed();

        if (gamestart.equals("true")) {


            usertotalgames = usertotalgames + 1;
            userref.child("status").setValue("lost");
            userref.child("totalgames").setValue(String.valueOf(usertotalgames));


            opponentref.child("lastbug").setValue("true");


            userref.child("emoji").setValue("none");
            opponentref.child("emoji").setValue("none");

            winsopponent = winsopponent + 1;
            opponenttotalgames = opponenttotalgames + 1;
            opponentref.child("status").setValue("won");
            opponentref.child("totalgames").setValue(String.valueOf(opponenttotalgames));
            opponentref.child("totalwins").setValue(String.valueOf(winsopponent));

            userref.child("myturn").setValue("false");
            opponentref.child("myturn").setValue("false");

            userref.child("current_game_sum").setValue("21");
            opponentref.child("current_game_sum").setValue("21");

            userref.child("startgame").setValue("false");
            opponentref.child("startgame").setValue("false");

            userref.child("consecutive").setValue("0");
            opponentconsecutive++;
            if (opponentconsecutive >= 5)
                opponentref.child("a3").setValue("true");

            opponentref.child("consecutive").setValue(String.valueOf(opponentconsecutive));



            opponentref.child("money").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    opstrmoney=dataSnapshot.getValue(String.class);
                    opintmoney=Integer.parseInt(opstrmoney);

                    if(writeonce==0)
                    {
                        writeonce=1;
                        opintmoney = opintmoney + 100;
                        opponentref.child("money").setValue(String.valueOf(opintmoney));

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            opponentref.child("myturn").setValue("false");
            opponentref.child("startgame").setValue("false");

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        else
        {

            userref.child("emoji").setValue("none");

            Intent i = new Intent(playgame.this, MainActivity.class);
            startActivity(i);
        }

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
        gamemusic.setVolume(70,70);
        gamemusic.seekTo(Integer.parseInt(pref.getString("position","0")));
        gamemusic.start();
        super.onResume();
        super.onResume();
    }


}
