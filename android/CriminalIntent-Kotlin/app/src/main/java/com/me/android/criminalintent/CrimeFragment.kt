package com.me.android.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import java.util.*

class CrimeFragment: Fragment(){
    private lateinit var crime: Crime
    private lateinit var dateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crime = CrimeLab.getInstance(activity).getCrime(crimeId) ?: Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_crime, container, false)

        val titleField: EditText = view.findViewById(R.id.crime_title)
        titleField.setText(crime.title)
        titleField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                crime.title = s.toString()
            }

            override fun afterTextChanged(s: Editable) {}

        })

        dateButton = view.findViewById(R.id.crime_date)
        dateButton.setOnClickListener {
            val dialog = DatePickerFragment.newInstance(crime.date)
            dialog.setTargetFragment(this, REQUEST_DATE)
            dialog.show(fragmentManager, DIALOG_DATE)
        }
        updateDate()

        val checkBox: CheckBox = view.findViewById(R.id.crime_solved)
        checkBox.isChecked = crime.isSolved
        checkBox.setOnCheckedChangeListener {_, isChecked ->  crime.isSolved = isChecked}

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_DATE) {
            val date = data?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            crime.date = date
            updateDate()
        }
    }

    override fun onPause() {
        super.onPause()
        CrimeLab.getInstance(activity).updateCrime(crime)
    }

    private fun updateDate() {
        dateButton.text = crime.date.toString()
    }

    companion object {
        @JvmStatic
        val ARG_CRIME_ID = "crime_id"

        @JvmStatic
        val DIALOG_DATE = "DialogDate"

        @JvmStatic
        private val REQUEST_DATE = 0

        @JvmStatic
        fun newIntent(crimeId: UUID): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)

            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}