package com.me.android.criminalintent

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.DatePicker
import java.util.*

class DatePickerFragment: DialogFragment() {
    private lateinit var datePicker: DatePicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val view = View.inflate(context, R.layout.dialog_date, null)
        datePicker = view.findViewById(R.id.dialog_date_picker)
        datePicker.init(year, month, day, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            val time = GregorianCalendar(datePicker.year, datePicker.month, datePicker.dayOfMonth, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)).time
            sendResult(Activity.RESULT_OK, time)
        }
        return builder
            .setView(view)
            .setTitle(R.string.date_picker_title)
            .create()
    }

    private fun sendResult(resultCode: Int, date: Date) {
        if (targetFragment == null) {
            return
        }

        val intent = Intent()
        intent.putExtra(EXTRA_DATE, date)

        targetFragment?.onActivityResult(targetRequestCode, resultCode, intent)
    }

    companion object {
        @JvmStatic
        private val ARG_DATE = "date"

        @JvmStatic
        val EXTRA_DATE = "com.ygaberman.android.criminalintent.date"

        @JvmStatic
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)

            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}