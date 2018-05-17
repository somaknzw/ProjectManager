package com.lifeistech.android.projectmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static io.realm.log.RealmLog.clear;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public ListView ListView;
    public EditText titleEditText2;
    ProjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //realmを開く
        realm = Realm.getDefaultInstance();
        ListView = (ListView)findViewById(R.id.listView);



        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Project detail = (Project) parent.getItemAtPosition(position);


                Intent intent = new Intent(MainActivity.this, FeedbackList.class);
                intent.putExtra("title", detail.title);
                intent.putExtra("updateDate", detail.updateDate);
                intent.putExtra("achievement", detail.achievement);
                startActivity(intent);

            }
        });


        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("このプロジェクトを削除しますか？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                adapter = (ProjectAdapter) ListView.getAdapter();

                                Project detail = adapter.getItem(position);

                                //データを消して、リストを並べなおす
                                adapter.remove(detail);

                                adapter.notifyDataSetChanged();

                                //realmからの消去
                                RealmResults<Project> list = realm.where(Project.class)
                                        .equalTo("updateDate",detail.updateDate).findAll();

                                //begin-commitに挟むことで更新
                                realm.beginTransaction();

                                list.deleteFirstFromRealm();

                                realm.commitTransaction();
                                Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                            } })
                        .setNegativeButton("キャンセル",null).setCancelable(true);

                builder.show();
                return true;
            }
        });




    }
    


    public void setProjectList(){

        //realmから読み取る
        RealmResults<Project> results = realm.where(Project.class).isNotNull("updateDate").findAll();
        List<Project> items = realm.copyFromRealm(results);

        ProjectAdapter adapter = new ProjectAdapter(this, R.layout.project_title_layout, items);

        ListView.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();

        setProjectList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //データをrealmに保存というメソッド
    public void save(final String title, final String updateDate){

        //メモを保存
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Project detail = realm.createObject(Project.class);
                detail.title = title;
                detail.updateDate = updateDate;
            }
        });

    }




    public void create(View v){


        //カスタムビューの設定
        final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View view = inflater.inflate(R.layout.dialog_1, null);

        //AlertDialog生成
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//タイトル設定
        builder.setTitle("新規プロジェクト作成");
//レイアウト設定
        builder.setView(view);

//ＯＫボタン設定
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){


                titleEditText2 = (EditText) view.findViewById(R.id.titleEditText2);
                String title = titleEditText2.getText().toString();

                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
                String updateDate = sdf.format(date);

                save(title, updateDate);

                Toast toast = Toast.makeText(MainActivity.this, "保存しました", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();

                setProjectList();


            }
        });

//Cancelボタン設定
        builder.setNegativeButton("cancel",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
//キャンセルなので何もしない
            }
        });
//ダイアログの表示
        builder.create().show();

    }




//        Intent intent = new Intent(this, CreateActivity.class);
//        startActivity(intent);


}
