package com.cyz.askaday

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.cyz.baseproject.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.app.FragmentPagerAdapter


class MainActivity : AppCompatActivity() , HomeFragment.OnFragmentInteractionListener{


    private var mHomeFragment: HomeFragment? = null
    private var mExploreFragment: HomeFragment? = null
    private var mProfileFragment: HomeFragment? = null
    private val mFragments: ArrayList<Fragment> = ArrayList<Fragment>()
    private var mAdapter: ViewPagerAdapter? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        mHomeFragment = HomeFragment.newInstance("","")
        mExploreFragment = HomeFragment.newInstance("","")
        mProfileFragment = HomeFragment.newInstance("","")

        mFragments.add(mHomeFragment!!)
        mFragments.add(mExploreFragment!!)
        mFragments.add(mProfileFragment!!)


        customViewPager.setOffscreenPageLimit(3)
        mAdapter = ViewPagerAdapter(this.supportFragmentManager, mFragments)
        customViewPager.setAdapter(mAdapter)
    }

    inner class ViewPagerAdapter(fm: FragmentManager, private val fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var title = ""
            when (position) {
                0 -> title = "Home"
                1 -> title = "Dashboard"
                2 -> title = "Notifications"
            }
            return title
        }
    }
}
