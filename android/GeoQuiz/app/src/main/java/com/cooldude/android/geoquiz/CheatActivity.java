package com.cooldude.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.cooldude.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.cooldude.android.geoquiz.answer_shown";
    private static final String KEY_INDEX = "index";

    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private TextView mVersionTextView;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

        if(savedInstanceState != null){
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_INDEX);
            if(mAnswerIsTrue){
                mAnswerTextView.setText(R.string.true_button);
            }
            else{
                mAnswerTextView.setText(R.string.false_button);
            }
            setAnswerShownResult(mAnswerIsTrue);
        }
        else {
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        }

        mVersionTextView = (TextView)findViewById(R.id.build_version);
        mVersionTextView.setText(getResources().getString(R.string.build_version, android.os.Build.VERSION.SDK_INT));

        mShowAnswerButton = (Button)findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }
                else{
                    mAnswerTextView.setText(R.string.false_button);
                }

                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_INDEX, mAnswerIsTrue);
    }
}
