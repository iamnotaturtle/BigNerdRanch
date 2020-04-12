package com.me.geoquiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {
    private val KEY_NUM_CHEATED = 0
    private var answerIsTrue: Boolean? = null
    private var answerView: TextView? = null
    private var answerButton: Button? = null


    companion object {
        private const val EXTRA_ANSWER_TRUE = "com.me.geoquiz.answer_is_true"
        private const val EXTRA_ANSWER_SHOWN = "com.me.geoquiz.answer_is_shown"

        fun newIntent(context: Context, answerIsTrue: Boolean): Intent {
            val intent = Intent(context, CheatActivity::class.java)
            intent.putExtra(EXTRA_ANSWER_TRUE, answerIsTrue)
            return intent
        }

        fun wasAnswerShown(result: Intent): Boolean {
            return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(RESULT_OK, data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_TRUE, false)
        answerView = findViewById(R.id.answerText)
        answerButton = findViewById(R.id.answerButton)
        answerButton?.setOnClickListener {
            if (answerIsTrue == true) {
                answerView?.setText(R.string.true_button)
            } else {
                answerView?.setText(R.string.false_button)
            }

            setAnswerShownResult(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val cx = it.width / 2
                val cy = it.height / 2
                val radius: Float = it.width.toFloat()
                val anim: Animator = ViewAnimationUtils.createCircularReveal(it, cx, cy, radius, 0.toFloat())
                anim.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        it.visibility = View.INVISIBLE
                    }
                })
                anim.start()
            } else {
                it.visibility = View.INVISIBLE
            }
        }

    }
}
