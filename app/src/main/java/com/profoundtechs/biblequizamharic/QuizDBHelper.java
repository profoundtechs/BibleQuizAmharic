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
        /*
        this.db = db;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CatagoriesTable.TABLE_NAME + "( " +
                CatagoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CatagoriesTable.COLUMN_NAME + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CatagoriesTable.TABLE_NAME + "(" + CatagoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTalbe();
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        db.execSQL("DROP TABLE IF EXISTS " + CatagoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
        */
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

/*        Cursor c = db.rawQuery("SELECT * FROM " + CatagoriesTable.TABLE_NAME, null);
        if (c.moveToFirst()){
            do {
                category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CatagoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CatagoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }
        c.close();*/
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

//    private void updateHighscore(int highscoreNew) {
//        highscore = highscoreNew;
//    UPDATE `quizCatagories` SET `high_score`=? WHERE _rowid_='1';
//
//
//        tvHighscore.setText("Highscore: " + highscore);
//
//        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(KEY_HIGHSCORE,highscore);
//        editor.apply();
//    }

    /*
    private void fillCategoriesTable(){
        Category c1 = new Category("Programming");
        addCategory(c1);
        Category c2 = new Category("Geography");
        addCategory(c2);
        Category c3 = new Category("Math");
        addCategory(c3);
    }

    private void addCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CatagoriesTable.COLUMN_NAME,category.getName());
        db.insert(CatagoriesTable.TABLE_NAME,null,cv);
    }

    private void fillQuestionsTalbe() {
        Question q1 = new Question("Programming: A is correct","A","B",
                "C",1,Category.PROGRAMMING);
        addQuestion(q1);
        Question q2 = new Question("Geography: B is correct","A","B",
                "C",2,Category.GEOGRAPHY);
        addQuestion(q2);
        Question q3 = new Question("Math: C is correct","A","B",
                "C",3,Category.MATH);
        addQuestion(q3);
        Question q4 = new Question("Math: A is correct","A","B",
                "C",1,Category.MATH);
        addQuestion(q4);
        Question q5 = new Question("Non existing: A is correct","A","B",
                "C",1,4);
        addQuestion(q5);
        Question q6 = new Question("Non existing: B is correct","A","B",
                "C",2,5);
        addQuestion(q6);
    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION,question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1,question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2,question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3,question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR,question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID,question.getCategoryId());
        db.insert(QuestionsTable.TABLE_NAME,null,cv);
    }
    */
}
