package com.profoundtechs.biblequizamharic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    // Views
    TextView tvLinkBibleQuiz;
    TextView tvLinkLifeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvLinkBibleQuiz = findViewById(R.id.tvLinkBibleQuiz);
        tvLinkBibleQuiz.setText("https://lifeline-techs.com");
        Linkify.addLinks(tvLinkBibleQuiz,Linkify.ALL);

        tvLinkLifeline = findViewById(R.id.tvLinkLifeline);
        tvLinkLifeline.setText("https://play.google.com/store/apps/details?id=com.dozcore.mathew.biblequiz");
        Linkify.addLinks(tvLinkLifeline,Linkify.ALL);
    }
}
