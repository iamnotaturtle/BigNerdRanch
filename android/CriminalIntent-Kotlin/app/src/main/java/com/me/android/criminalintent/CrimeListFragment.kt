package com.me.android.criminalintent

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val SAVED_SUBTITLE_VISIBLE = "subtite"

class CrimeListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null
    private var subtitleVisible: Boolean = false

    inner class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_crime, parent, false)), View.OnClickListener {
        private var titleTextView = itemView.findViewById<TextView>(R.id.crime_title)
        private var dateTextView = itemView.findViewById<TextView>(R.id.crime_date)
        private var solvedImageView = itemView.findViewById<ImageView>(R.id.crime_solved)

        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = CrimePagerActivity.newIntent(activity, crime.id)
            startActivity(intent)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            solvedImageView.visibility = if (this.crime.isSolved) View.VISIBLE else View.GONE
        }
    }

    inner class CrimeAdapter(private var crimes: List<Crime>): RecyclerView.Adapter<CrimeHolder>() {
        override fun getItemCount(): Int = crimes.size

        fun setCrimes(_crimes: List<Crime>) {
            crimes = _crimes
        }

        override fun onBindViewHolder(p0: CrimeHolder, p1: Int) {
            val crime: Crime = crimes[p1]
            p0.bind(crime)
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return CrimeHolder(layoutInflater, p0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        recyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)

        savedInstanceState?.let {
            subtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val crimeLab: CrimeLab = CrimeLab.getInstance(activity)
        val crimes = crimeLab.getCrimes()

        if (adapter == null) {
            adapter = CrimeAdapter(crimes)
            recyclerView.adapter = adapter
        } else {
            adapter?.setCrimes(crimes)
            adapter?.notifyDataSetChanged()
        }

        updateSubtitle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu?.findItem(R.id.show_subtitle)
        if (subtitleVisible) {
            subtitleItem?.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem?.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab.getInstance(activity).addCrime(crime)
                val intent = CrimePagerActivity.newIntent(activity, crime.id)
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                subtitleVisible = !subtitleVisible
                activity?.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab.getInstance(activity)
        val count = crimeLab.crimes.size
        var subtitle: String? = resources.getQuantityString(R.plurals.subtitle_format, count, count)

        if (!subtitleVisible) {
            subtitle = null
        }
        (activity as AppCompatActivity).supportActionBar?.subtitle = subtitle
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, subtitleVisible)
    }

}