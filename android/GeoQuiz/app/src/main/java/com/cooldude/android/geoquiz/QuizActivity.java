package com.cooldude.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";

    private static final String KEY_INDEX = "index";
    private static final String KEY_CHEATS = "cheats";
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private TextView mNumberOfCheats;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null)
                return;
            else {
                mIsCheater = CheatActivity.wasAnswerShown(data);
                if(mIsCheater)
                    decreaseHint();
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    public  void onPause(){
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        String hints = mNumberOfCheats.getText().toString();
        hints = hints.replaceAll("[^0-9]", "");
        int hintsLeft = Integer.parseInt(hints);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_CHEATS, hintsLeft);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        int hintsLeft = 3;
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            hintsLeft = savedInstanceState.getInt(KEY_CHEATS, 3);
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button)findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(true);
            }
        });
        mFalseButton = (Button)findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton)findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex - 1 < 0) ? 0 : (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        if(hintsLeft <= 0){
            mCheatButton.setEnabled(false);
        }
        mNumberOfCheats = (TextView)findViewById(R.id.cheat_number);
        mNumberOfCheats.setText(getResources().getString(R.string.cheat_number, hintsLeft));
    }

    private void checkAnswer(boolean userPressedTrue){
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if(mIsCheater){
            messageResId = R.string.judgement_toast;
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void decreaseHint(){
        String hints = mNumberOfCheats.getText().toString();
        hints = hints.replaceAll("[^0-9]", "");
        int hintsLeft = Integer.parseInt(hints);

        if(hintsLeft > 0){
            hintsLeft -= 1;
            mNumberOfCheats.setText(getResources().getString(R.string.cheat_number, hintsLeft));
        }

        if(hintsLeft <= 0){
            mCheatButton = (Button)findViewById(R.id.cheat_button);
            mCheatButton.setEnabled(false);
        }
    }

    private void updateQuestion(){
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
}
