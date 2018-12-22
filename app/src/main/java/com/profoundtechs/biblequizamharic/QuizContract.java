package com.profoundtechs.biblequizamharic;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract(){}

    public static class CatagoriesTable implements BaseColumns{
        public static final String TABLE_NAME = "quizCatagories";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_HIGH_SCORE = "high_score";
    }

    public static class QuestionsTable implements BaseColumns{
        public static final String TABLE_NAME = "quizQuestions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NR = "answerNr";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
    }

}
