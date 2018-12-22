package com.profoundtechs.biblequizamharic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuizCatagoryActivity extends AppCompatActivity {

    RecyclerView rvCategoryMain;

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryId";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String KEY_HIGHSCORE = "keyHighscore";

    private QuizDBHelper dbHelper;

    private CategoryAdapter adapter;

    private int highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_catagory);
//        setTitle("Select Category");

//        loadHighscore();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbHelper = new QuizDBHelper(this);
        List<Category> categoryList = dbHelper.getAllCategories();

        rvCategoryMain = findViewById(R.id.rvCategoryMain);
        rvCategoryMain.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(categoryList, new CategoryAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                int catagoryId = category.getId();
                String categoryName = category.getName();

                Intent intent = new Intent(QuizCatagoryActivity.this,QuizActivity.class);
                intent.putExtra(EXTRA_CATEGORY_ID,catagoryId);
                intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
                startActivityForResult(intent,REQUEST_CODE_QUIZ);

                startActivity(new Intent(QuizCatagoryActivity.this,QuizActivity.class));
            }
        });
        rvCategoryMain.setAdapter(adapter);
        rvCategoryMain.setItemAnimator(new DefaultItemAnimator());
        rvCategoryMain.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE,0);
                int categoryID = data.getIntExtra(QuizActivity.EXTRA_CATEGORY_ID,0);
                if (score > highscore){
                    dbHelper.updateHighScore(score,categoryID);
                }
            }
        }
    }

//    private void loadHighscore(){
//        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//        highscore = prefs.getInt(KEY_HIGHSCORE,0);
//        tvHighscore.setText("Highscore: " + highscore);
//    }

}
