package com.kyana.projectmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

import android.widget.Toast;

public class CreateFeedback extends AppCompatActivity{

    public Realm realm;
    public EditText commentEditText;
    public EditText titleEditText;
    public float manzoku;
    public RatingBar ratingBar;
    public String kari;
    public SeekBar seekBar;
    public int tassei;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kyana.projectmanager.R.layout.activity_fb_create);

        //realmを開く
        realm = Realm.getDefaultInstance();

        //関連付け
        commentEditText = (EditText)findViewById(com.kyana.projectmanager.R.id.comment);
        titleEditText = (EditText)findViewById(com.kyana.projectmanager.R.id.text_fbtitle);
        ratingBar = (RatingBar) findViewById(com.kyana.projectmanager.R.id.ratingbar);
        ratingBar.setNumStars(5);


        Intent intent = getIntent();
        kari = intent.getStringExtra("title");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                manzoku = ratingBar.getRating();




            }
        });


        seekBar = (SeekBar) findViewById(com.kyana.projectmanager.R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //ツマミがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar, int progress, boolean fromUser) {
                        tassei = seekBar.getProgress();

                    }

                    //ツマミがタッチされた時に呼ばれる
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    //ツマミがリリースされた時に呼ばれる
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public void save3(){
        final Project detail = realm.where(Project.class).equalTo("updateDate",
                getIntent().getStringExtra("updateDate")).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                detail.achievement = tassei;
            }
        });

    }



    public void save2(final String title, final String comment, final float satisfaction, final String logdate, final String dayid, final String fb_title){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm bgrealm) {
                Project detail = realm.createObject(Project.class);
                detail.title = title;
                detail.comment = comment;
                detail.satisfaction = satisfaction;
                detail.logdate = logdate;
                detail.dayid = dayid;
                detail.fb_title = fb_title;

            }
        });
    }



    public void create(View v){

        String title = kari;
        String comment = commentEditText.getText().toString();
        String fb_title = titleEditText.getText().toString();
        float satisfaction = manzoku;




        Date today = new Date();
        SimpleDateFormat day = new SimpleDateFormat("M月d日", Locale.JAPANESE);
        String logdate = day.format(today);

        Date id = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String dayid = date.format(id);

        save2(title, comment, satisfaction, logdate, dayid, fb_title);
        save3();

        Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //realmを閉じる
        realm.close();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }



}
