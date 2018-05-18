package com.kyana.projectmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FeedbackList extends AppCompatActivity {

    public Realm realm;
    public ListView feed_back_list;
    public ProgressBar progressBar;

    public String test;
    public String updateDate;
    public TextView text;
    public int per;
    FeedbackAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kyana.projectmanager.R.layout.activity_feedbackview);

        realm = Realm.getDefaultInstance();

        feed_back_list = (ListView)findViewById(com.kyana.projectmanager.R.id.listView2);
        progressBar = (ProgressBar)findViewById(com.kyana.projectmanager.R.id.progressBar);
        text = (TextView)findViewById(com.kyana.projectmanager.R.id.textView2);

        ViewCompat.setNestedScrollingEnabled(feed_back_list, true);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle(getIntent().getStringExtra("title"));

        feed_back_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Project detail = (Project) parent.getItemAtPosition(position);
                Intent intent = new Intent(FeedbackList.this, DetailofFeedBackActivity.class);
                intent.putExtra("dayid", detail.dayid);
                intent.putExtra("date", detail.logdate);
                startActivity(intent);

            }
        });





        feed_back_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackList.this);
                builder.setMessage("このフィードバックを削除しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                adapter = (FeedbackAdapter) feed_back_list.getAdapter();

                                Project detail = adapter.getItem(position);

                                //データを消して、リストを並べなおす
                                adapter.remove(detail);

                                adapter.notifyDataSetChanged();

                                //realmからの消去
                                RealmResults<Project> list = realm.where(Project.class).equalTo("dayid",detail.dayid).findAll();

                                //begin-commitに挟むことで更新
                                realm.beginTransaction();

                                list.deleteFirstFromRealm();

                                realm.commitTransaction();
                                Toast.makeText(FeedbackList.this, "削除しました", Toast.LENGTH_SHORT).show();
                            } })
                        .setNegativeButton("キャンセル",null).setCancelable(true);

                builder.show();
                return true;
            }
        });



    }

    public void setFeedbacktList(){

        //realmから読み取る ここでタイトルから絞らせる
        RealmResults<Project> results = realm.where(Project.class).equalTo("title", getIntent().getStringExtra("title")).isNull("updateDate").findAll();
        List<Project> items = realm.copyFromRealm(results);

        Intent intent = getIntent();
        test = intent.getStringExtra("title");
        updateDate = intent.getStringExtra("updateDate");

        Collections.reverse(items);
        FeedbackAdapter adapter = new FeedbackAdapter(this, com.kyana.projectmanager.R.layout.project_layout, items);

        feed_back_list.setAdapter(adapter);

//      今からachievementから数値入手、それをバーに表示
        progressBar.setMax(100); // 水平プログレスバーの最大値を設定
        per = intent.getIntExtra("achievement", 0);
        progressBar.setProgress(per); // 水平プログレスバーの値を設定　
        text.setText("達成率"+(String.valueOf(per))+"%");

    }

    /*    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()) {
                case android.R.id.home:
                    finish();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    @Override
    protected void onResume() {
        super.onResume();

        setFeedbacktList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void gocreatefb(View v){
        Intent intent = new Intent(FeedbackList.this, CreateFeedback.class);
        intent.putExtra("title", test);
        intent.putExtra("updateDate", updateDate);
        startActivity(intent);

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
