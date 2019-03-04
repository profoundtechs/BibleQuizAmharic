package com.profoundtechs.biblequizamharic;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
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
    private Animation btn_answer_anim;

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
        btn_answer_anim = AnimationUtils.loadAnimation(this,R.anim.btn_answer_anim);

        Intent intent = getIntent();
        categoryId = intent.getIntExtra(QuizCatagoryActivity.EXTRA_CATEGORY_ID,0);
        String catagoryName = intent.getStringExtra(QuizCatagoryActivity.EXTRA_CATEGORY_NAME);

        //Set the category name
        tvCategory.setText("የጥያቄ ክፍል: " + catagoryName);

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

        //Media player
        final MediaPlayer rightAnswer = MediaPlayer.create(this,R.raw.right);
        final MediaPlayer wrongAnswer = MediaPlayer.create(this,R.raw.wrong);

        btnOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==1){
                    score++;
                    tvScore.setText("ውጤት: " + score);
                    btnOption1.setBackgroundResource(R.drawable.button_correct);
                    rightAnswer.start();
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption1.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption2.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption1.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption3.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption1.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption4.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                }

                checkAnswer();

                btnOption1.startAnimation(btn_answer_anim);
            }
        });

        btnOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==2){
                    score++;
                    tvScore.setText("ውጤት: " + score);
                    btnOption2.setBackgroundResource(R.drawable.button_correct);
                    rightAnswer.start();
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption2.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption1.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption2.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption3.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption2.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption4.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                }

                checkAnswer();

                btnOption2.startAnimation(btn_answer_anim);
            }
        });

        btnOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==3){
                    score++;
                    tvScore.setText("ውጤት: " + score);
                    btnOption3.setBackgroundResource(R.drawable.button_correct);
                    rightAnswer.start();
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption3.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption1.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption3.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption2.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==4){
                    btnOption3.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption4.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                }

                checkAnswer();

                btnOption3.startAnimation(btn_answer_anim);
            }
        });

        btnOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestion.getAnswerNr()==4){
                    score++;
                    tvScore.setText("ውጤት: " + score);
                    btnOption4.setBackgroundResource(R.drawable.button_correct);
                    rightAnswer.start();
                } else if (currentQuestion.getAnswerNr()==1){
                    btnOption4.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption1.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==2){
                    btnOption4.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption2.setBackgroundResource(R.drawable.button_correct);

                    wrongAnswer.start();
                } else if (currentQuestion.getAnswerNr()==3){
                    btnOption4.setBackgroundResource(R.drawable.button_incorrect);
                    btnOption3.setBackgroundResource(R.drawable.button_correct);
                    wrongAnswer.start();
                }

                checkAnswer();

                btnOption4.startAnimation(btn_answer_anim);
            }
        });
    }

    private void showNextQuestion() {
        btnOption1.setBackgroundResource(R.drawable.button_default);
        btnOption2.setBackgroundResource(R.drawable.button_default);
        btnOption3.setBackgroundResource(R.drawable.button_default);
        btnOption4.setBackgroundResource(R.drawable.button_default);

        if (questionCounter<questionCountTotal){
            currentQuestion=questionList.get(questionCounter);

            tvQuestion.setText(currentQuestion.getQuestion());
            btnOption1.setText(currentQuestion.getOption1());
            btnOption2.setText(currentQuestion.getOption2());
            btnOption3.setText(currentQuestion.getOption3());
            btnOption4.setText(currentQuestion.getOption4());

            questionCounter++;
            tvQuestionCount.setText("ጥያቄ: " + questionCounter + "/" + questionCountTotal);
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
            btnOption1.setBackgroundResource(R.drawable.button_correct);
            btnOption2.setBackgroundResource(R.drawable.button_default);
            btnOption3.setBackgroundResource(R.drawable.button_default);
            btnOption4.setBackgroundResource(R.drawable.button_default);
        } else if (currentQuestion.getAnswerNr()==2){
            btnOption1.setBackgroundResource(R.drawable.button_default);
            btnOption2.setBackgroundResource(R.drawable.button_correct);
            btnOption3.setBackgroundResource(R.drawable.button_default);
            btnOption4.setBackgroundResource(R.drawable.button_default);
        } else if (currentQuestion.getAnswerNr()==3){
            btnOption1.setBackgroundResource(R.drawable.button_default);
            btnOption2.setBackgroundResource(R.drawable.button_default);
            btnOption3.setBackgroundResource(R.drawable.button_correct);
            btnOption4.setBackgroundResource(R.drawable.button_default);
        } else if (currentQuestion.getAnswerNr()==4){
            btnOption1.setBackgroundResource(R.drawable.button_default);
            btnOption2.setBackgroundResource(R.drawable.button_default);
            btnOption3.setBackgroundResource(R.drawable.button_default);
            btnOption4.setBackgroundResource(R.drawable.button_correct);
        }
    }

    private void updateCountDownText() {
        int minutes = (int) ((timeLeftInMills/1000)/60);
        int seconds = (int) ((timeLeftInMills/1000)%60);

        String timeFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);

        tvCountDown.setText(timeFormatted);

        if (timeLeftInMills<10000){
            tvCountDown.setTextColor(Color.rgb(255,0,102));
        } else {
            tvCountDown.setTextColor(Color.WHITE);
        }
    }

    private void checkAnswer() {
        answered = true;

        countDownTimer.cancel();

            btnOption1.setEnabled(false);
            btnOption2.setEnabled(false);
            btnOption3.setEnabled(false);
            btnOption4.setEnabled(false);

            Handler quizHandler = new Handler();
            Runnable codeToRun = new Runnable() {
                @Override
                public void run() {
                    if (questionCounter<questionCountTotal){
                        currentQuestion=questionList.get(questionCounter);
                    btnOption1.setBackgroundResource(R.drawable.button_default);
                    btnOption2.setBackgroundResource(R.drawable.button_default);
                    btnOption3.setBackgroundResource(R.drawable.button_default);
                    btnOption4.setBackgroundResource(R.drawable.button_default);

                    tvQuestion.setText(currentQuestion.getQuestion());
                    btnOption1.setText(currentQuestion.getOption1());
                    btnOption2.setText(currentQuestion.getOption2());
                    btnOption3.setText(currentQuestion.getOption3());
                    btnOption4.setText(currentQuestion.getOption4());

                    btnOption1.setEnabled(true);
                    btnOption2.setEnabled(true);
                    btnOption3.setEnabled(true);
                    btnOption4.setEnabled(true);

                    questionCounter++;
                    tvQuestionCount.setText("ጥያቄ: " + questionCounter + "/" + questionCountTotal);
                    answered = false;

                    vfQuiz.showNext();

                    timeLeftInMills = COUNTDOWN_IN_MILLS;
                    startCountDown();
                    } else {
                        finishQuiz();
                    }
                }
            };
            quizHandler.postDelayed(codeToRun,2000);

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
            Toast.makeText(this,"ወደኋላ ለመመለስ በድጋሚ ይጫኑ",Toast.LENGTH_SHORT).show();
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
