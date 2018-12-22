package com.profoundtechs.biblequizamharic;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    private static final long COUNTDOWN_IN_MILLS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView tvQuestion;
    private TextView tvScore;
    private TextView tvQuestionCount;
    private TextView tvCategory;
    private TextView tvCountDown;
    private Button btnOption1,btnOption2,btnOption3,btnOption4;
    private ViewFlipper vfQuiz;
//    private RadioGroup rgChoises;
//    private RadioButton rb1;
//    private RadioButton rb2;
//    private RadioButton rb3;
//    private RadioButton rb4;
//    private Button btnConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList getTextColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMills;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;
    int categoryId;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        tvScore = findViewById(R.id.tvScore);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvCategory = findViewById(R.id.tvCategory);
        tvCountDown = findViewById(R.id.tvCountDown);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        vfQuiz = findViewById(R.id.vfQuiz);
        getTextColorDefaultCd = tvCountDown.getTextColors();

        Intent intent = getIntent();
        categoryId = intent.getIntExtra(QuizCatagoryActivity.EXTRA_CATEGORY_ID,0);
        String catagoryName = intent.getStringExtra(QuizCatagoryActivity.EXTRA_CATEGORY_NAME);

        if (savedInstanceState == null){
            QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryId);
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMills = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered){
                startCountDown();
            } else {
                updateCountDownText();
            }
        }

        btnOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==1){
                    score++;
                    tvScore.setText("Score: " + score);
                    btnOption1.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption1.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption2.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption1.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption3.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption1.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption4.setBackgroundColor(Color.rgb(63,190,195));
                }

                checkAnswer();
            }
        });

        btnOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==2){
                    score++;
                    tvScore.setText("Score: " + score);
                    btnOption2.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption2.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption1.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption2.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption3.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption2.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption4.setBackgroundColor(Color.rgb(63,190,195));
                }

                checkAnswer();
            }
        });

        btnOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==3){
                    score++;
                    tvScore.setText("Score: " + score);
                    btnOption3.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption3.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption1.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption3.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption2.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption3.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption4.setBackgroundColor(Color.rgb(63,190,195));
                }

                checkAnswer();
            }
        });

        btnOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==4){
                    score++;
                    tvScore.setText("Score: " + score);
                    btnOption4.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption4.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption1.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption4.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption2.setBackgroundColor(Color.rgb(63,190,195));
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption4.setBackgroundColor(Color.rgb(170,120,120));
                    btnOption3.setBackgroundColor(Color.rgb(63,190,195));
                }

                checkAnswer();
            }
        });
    }

    private void showNextQuestion() {
        btnOption1.setBackgroundColor(Color.rgb(63,135,195));
        btnOption2.setBackgroundColor(Color.rgb(63,135,195));
        btnOption3.setBackgroundColor(Color.rgb(63,135,195));
        btnOption4.setBackgroundColor(Color.rgb(63,135,195));

        if (questionCounter<questionCountTotal){
            currentQuestion=questionList.get(questionCounter);

            tvQuestion.setText(currentQuestion.getQuestion());
            btnOption1.setText(currentQuestion.getOption1());
            btnOption2.setText(currentQuestion.getOption2());
            btnOption3.setText(currentQuestion.getOption3());
            btnOption4.setText(currentQuestion.getOption4());

            questionCounter++;
            tvQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;

            timeLeftInMills = COUNTDOWN_IN_MILLS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMills,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMills = 0;
                updateCountDownText();
                showAnswer();
                checkAnswer();
            }
        }.start();
    }

    private void showAnswer() {
        if (currentQuestion.getAnswerNr()==1){
            btnOption1.setBackgroundColor(Color.rgb(63,190,195));
            btnOption2.setBackgroundColor(Color.rgb(170,120,120));
            btnOption3.setBackgroundColor(Color.rgb(170,120,120));
            btnOption4.setBackgroundColor(Color.rgb(170,120,120));
        } else if (currentQuestion.getAnswerNr()==2){
            btnOption1.setBackgroundColor(Color.rgb(170,120,120));
            btnOption2.setBackgroundColor(Color.rgb(63,190,195));
            btnOption3.setBackgroundColor(Color.rgb(170,120,120));
            btnOption4.setBackgroundColor(Color.rgb(170,120,120));
        } else if (currentQuestion.getAnswerNr()==3){
            btnOption1.setBackgroundColor(Color.rgb(170,120,120));
            btnOption2.setBackgroundColor(Color.rgb(170,120,120));
            btnOption3.setBackgroundColor(Color.rgb(63,190,195));
            btnOption4.setBackgroundColor(Color.rgb(170,120,120));
        } else if (currentQuestion.getAnswerNr()==4){
            btnOption1.setBackgroundColor(Color.rgb(170,120,120));
            btnOption2.setBackgroundColor(Color.rgb(170,120,120));
            btnOption3.setBackgroundColor(Color.rgb(170,120,120));
            btnOption4.setBackgroundColor(Color.rgb(63,190,195));
        }
    }

    private void updateCountDownText() {
        int minutes = (int) ((timeLeftInMills/1000)/60);
        int seconds = (int) ((timeLeftInMills/1000)%60);

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        tvCountDown.setText(timeFormatted);

        if (timeLeftInMills<10000){
            tvCountDown.setTextColor(Color.RED);
        } else {
            tvCountDown.setTextColor(getTextColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

        if (questionCounter<questionCountTotal){
            currentQuestion=questionList.get(questionCounter);

            Handler quizHandler = new Handler();
            Runnable codeToRun = new Runnable() {
                @Override
                public void run() {
                    btnOption1.setBackgroundColor(Color.rgb(63,135,195));
                    btnOption2.setBackgroundColor(Color.rgb(63,135,195));
                    btnOption3.setBackgroundColor(Color.rgb(63,135,195));
                    btnOption4.setBackgroundColor(Color.rgb(63,135,195));

                    tvQuestion.setText(currentQuestion.getQuestion());
                    btnOption1.setText(currentQuestion.getOption1());
                    btnOption2.setText(currentQuestion.getOption2());
                    btnOption3.setText(currentQuestion.getOption3());
                    btnOption4.setText(currentQuestion.getOption4());

                    questionCounter++;
                    tvQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
                    answered = false;

                    vfQuiz.showNext();

                    timeLeftInMills = COUNTDOWN_IN_MILLS;
                    startCountDown();
                }
            };
            quizHandler.postDelayed(codeToRun,2000);

        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE,score);
        resultIntent.putExtra(EXTRA_CATEGORY_ID, categoryId);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();
        } else {
            Toast.makeText(this,"Press back again to finish",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,score);
        outState.putInt(KEY_QUESTION_COUNT,questionCounter);
        outState.putLong(KEY_MILLIS_LEFT,timeLeftInMills);
        outState.putBoolean(KEY_ANSWERED,answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST,questionList);
    }
}
