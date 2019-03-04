package com.profoundtechs.biblequizamharic;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StartingScreenActivity extends AppCompatActivity {
    private QuizDBHelper databaseHelper;

    private TextView tvHighscore;
    private TextView tvAcknowledgment;
    private Spinner spCategory;

    private int highscore;

    private Button btnStartQuiz;
    private Animation btn_start_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
        setTitle("");

        spCategory = findViewById(R.id.spCategory);
//        tvAcknowledgment = findViewById(R.id.tvAcknowledgment);
//        tvAcknowledgment.setText("<a href='https://lifeline-techs.com'>My Website</a>");
//        Linkify.addLinks(tvAcknowledgment,Linkify.ALL);

        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btn_start_anim = AnimationUtils.loadAnimation(this,R.anim.btn_start_anim);
        btnStartQuiz.startAnimation(btn_start_anim);

        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        //Code for copying the database to the device
        databaseHelper=new QuizDBHelper(this);
        File database=getApplicationContext().getDatabasePath(QuizDBHelper.DATABASE_NAME);
        if (!database.exists()){
            databaseHelper.getReadableDatabase();
            Toast.makeText(this, "Database copied successfully", Toast.LENGTH_LONG).show();
            if (copyDatabase(this)){
            }else {
                Toast.makeText(this, "Error occurred during copying database", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    //Method for copying database (Used above)
    public boolean copyDatabase(Context context){
        try{
            InputStream inputStream=context.getAssets().open(QuizDBHelper.DATABASE_NAME);
            String outFileName=QuizDBHelper.DB_LOCATION+QuizDBHelper.DATABASE_NAME;
            OutputStream outputStream=new FileOutputStream(outFileName);
            byte[] buff=new byte[1024];
            int length=0;
            while ((length=inputStream.read(buff))>0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(StartingScreenActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void startQuiz() {
        Intent intent = new Intent(StartingScreenActivity.this,QuizCatagoryActivity.class);
        startActivity(intent);
        finish();
    }
}
