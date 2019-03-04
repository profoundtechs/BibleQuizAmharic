package com.profoundtechs.biblequizamharic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

    private QuizDBHelper dbHelper;
    private CategoryAdapter adapter;
    private int highscore;

    // Views
    Toolbar toolbarCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_catagory);

        toolbarCategory = findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbarCategory);
        getSupportActionBar().setTitle("የመፅሃፍ ቅዱስ ክፍል ምረጥ");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.aboutMain){
            startActivity(new Intent(QuizCatagoryActivity.this,AboutActivity.class));
        }

        return true;
    }
}
