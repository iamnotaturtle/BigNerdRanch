package com.me.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuizActivity : AppCompatActivity() {
    private val REQUEST_CODE_CHEAT = 0
    private val KEY_INDEX: String = "index"
    private val KEY_CHEATER: String = "com.me.geoquiz.cheater"
    private val KEY_NUM_CHEATED: String = "com.me.geoquiz.num_cheated"
    private val TAG: String = "QuizActivity"
    private val questionBank: Array<Question> = arrayOf(
        Question(R.string.question_life, false),
        Question(R.string.question_oceans, true)
    )
    private var currentIndex: Int = 0
    private var numberCorrect: Int = 0
    private var isCheater: Boolean = false
    private var numberCheated: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreated(Bundle) called")

        setContentView(R.layout.activity_quiz)

        val buildVersion: TextView = findViewById(R.id.buildVersion)
        buildVersion.text = String.format(resources.getString(R.string.build_version), Build.VERSION.SDK_INT)

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            isCheater = savedInstanceState.getBoolean(KEY_CHEATER, false)
            numberCheated = savedInstanceState.getInt(KEY_NUM_CHEATED, 0)
        }

        val questionView: TextView = findViewById(R.id.question)
        questionView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)

        trueButton.setOnClickListener {
            checkAnswer(true)
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            isCheater = false
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        val previousButton: ImageButton = findViewById(R.id.previous_button)
        previousButton.setOnClickListener {
            currentIndex = if (currentIndex == 0) 0 else (currentIndex - 1) % questionBank.size
            updateQuestion()
        }

        val numberCheatedView: TextView = findViewById(R.id.numberCheated)
        numberCheatedView.text = String.format(resources.getString(R.string.number_cheated), numberCheated)

        val cheatButton: Button = findViewById(R.id.cheatButton)
        cheatButton.setOnClickListener {
            val answerIsTrue = questionBank[currentIndex].answer
            val intent = CheatActivity.newIntent(this@QuizActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        disableCheatButton()

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putBoolean(KEY_CHEATER, isCheater)
        outState.putInt(KEY_NUM_CHEATED, numberCheated)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionView: TextView = findViewById(R.id.question)
        val question: Int = questionBank[currentIndex].id
        questionView.setText(question)
        setTrueFalseButtons(true)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        questionBank[currentIndex].answered = true
        setTrueFalseButtons(false)
        val answerTrue = questionBank[currentIndex].answer

        if (userPressedTrue == answerTrue && !isCheater) {
            numberCorrect += 1
        }
        var messageId = if (userPressedTrue == answerTrue) R.string.correct_toast else R.string.incorrect_toast
        if (isCheater) {
            messageId = R.string.judgement
        }

        Toast.makeText(this@QuizActivity, messageId, Toast.LENGTH_SHORT).show()

        if (currentIndex == questionBank.size - 1) {
            Toast.makeText(this@QuizActivity, "You got ${(numberCorrect / questionBank.size.toDouble()) * 100}%!", Toast.LENGTH_SHORT).show()
            numberCorrect = 0
        }
    }

    private fun setTrueFalseButtons(enabled: Boolean) {
        val trueButton: Button = findViewById(R.id.true_button)
        val falseButton: Button = findViewById(R.id.false_button)
        trueButton.isClickable = enabled
        falseButton.isClickable = enabled
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return
            }

            isCheater = CheatActivity.wasAnswerShown(data)

            if (isCheater) {
                numberCheated += 1
            }

            disableCheatButton()

            val numberCheatedView: TextView = findViewById(R.id.numberCheated)
            numberCheatedView.text = String.format(resources.getString(R.string.number_cheated), numberCheated)
        }
    }

    private fun disableCheatButton() {
        if (numberCheated > 0) {
            val cheatButton: Button = findViewById(R.id.cheatButton)
            cheatButton.isEnabled = false
        }
    }
}
