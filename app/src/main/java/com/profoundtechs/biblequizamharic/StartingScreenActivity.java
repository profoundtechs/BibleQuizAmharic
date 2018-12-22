package com.profoundtechs.biblequizamharic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class StartingScreenActivity extends AppCompatActivity {
//    private static final int REQUEST_CODE_QUIZ = 1;
//    public static final String EXTRA_CATEGORY_ID = "extraCategoryId";
//    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
//
//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String KEY_HIGHSCORE = "keyHighscore";
//
    private QuizDBHelper databaseHelper;

    private TextView tvHighscore;
    private Spinner spCategory;

    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);
        setTitle("");

//        tvHighscore = findViewById(R.id.tvHighscore);
        spCategory = findViewById(R.id.spCategory);

        Button btnStartQuiz = findViewById(R.id.btnStartQuiz);
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

//        loadCategories();
//        loadHighscore();
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
//        Category selectedCategory = (Category) spCategory.getSelectedItem();
//        int catagoryId = selectedCategory.getId();
//        String categoryName = selectedCategory.getName();

        Intent intent = new Intent(StartingScreenActivity.this,QuizCatagoryActivity.class);
        startActivity(intent);
        finish();
//        intent.putExtra(EXTRA_CATEGORY_ID,catagoryId);
//        intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
//        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }

//    private void loadCategories() {
//        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
//        List<Category> categories = dbHelper.getAllCategories();
//        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,categories);
//        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCategory.setAdapter(adapterCategories);
//    }
}
