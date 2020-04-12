package com.me.android.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CrimePagerActivity: AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var crimes: List<Crime>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)

        viewPager = findViewById(R.id.crime_view_pager)
        crimes = CrimeLab.getInstance(this).getCrimes()

        val fragmentManager = supportFragmentManager
        viewPager.adapter = object: FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(p0: Int): Fragment {
                return CrimeFragment.newIntent(crimes[p0].id)
            }

            override fun getCount(): Int {
                return crimes.size
            }
        }

        val crimeId = intent.getSerializableExtra(EXTRA_CRIME_ID)
        val crimeIndex = crimes.indexOfFirst { it.id == crimeId }
        viewPager.currentItem = crimeIndex
    }

    companion object {
        @JvmStatic
        val EXTRA_CRIME_ID = "com.ygaberman.android.criminalintent.crime_id"

        @JvmStatic
        fun newIntent(packageContext: Context?, crimeId: UUID): Intent {
            val intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }

    }
}