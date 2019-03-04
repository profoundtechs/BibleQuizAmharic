package com.profoundtechs.biblequizamharic;

import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.profoundtechs.biblequizamharic.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "bibleQuizAmharic.db";
    public static final String DB_LOCATION = "/data/data/com.profoundtechs.biblequizamharic/databases/";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private static QuizDBHelper instance;

    private SQLiteDatabase db;

    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized QuizDBHelper getInstance(Context context){
        if (instance==null){
            instance=new QuizDBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public void openDatabase(){
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        if (db!=null && db.isOpen()){
            return;
        }
        db = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase(){
        if (db!=null){
            db.close();
        }
    }

    public List<Category> getAllCategories(){
        Category category = null;
        openDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CatagoriesTable.TABLE_NAME,null);
        c.moveToFirst();
        List<Category> categoryList = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                category = new Category();
                category.setId(c.getInt(0));
                category.setName(c.getString(1));
                category.setHighScore(c.getInt(2));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions(){
        Question question = null;
        openDatabase();
        ArrayList<Question> questionList = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()){
            do {
                question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setCategoryId(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);

            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryId){
        Question question = null;
        openDatabase();
        ArrayList<Question> questionList = new ArrayList<>();
        String slection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_CATEGORY_ID + " = ?", selectionArgs);

        if (c.moveToFirst()){
            do {
                question = new Question();
                question.setId(c.getInt(0));
                question.setQuestion(c.getString(1));
                question.setOption1(c.getString(2));
                question.setOption2(c.getString(3));
                question.setOption3(c.getString(4));
                question.setOption4(c.getString(5));
                question.setAnswerNr(c.getInt(6));
                question.setCategoryId(c.getInt(7));
                questionList.add(question);

            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

    public void updateHighScore(int highscoreNew, int categoryID) {
        openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CatagoriesTable.COLUMN_HIGH_SCORE,highscoreNew);
        db.update(CatagoriesTable.TABLE_NAME,cv,"id = ?", new String[]{String.valueOf(categoryID)});
    }
}
