package com.example.ex

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val firstFragment = FirstFragment()
        val secondFragment = SecondFragment()
        val thirdFragment = ThirdFragment()
        val thourdFragment = ThourdFragment()


        setCurrentFragment(firstFragment)

        bottomNavigationView2.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.miTv -> setCurrentFragment(firstFragment)
                R.id.miFeed -> setCurrentFragment(secondFragment)
                R.id.miColl -> setCurrentFragment(thirdFragment)
                R.id.miPerm -> setCurrentFragment(thourdFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()

        }

    private fun FirstFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.viewPager_ImageSlider, fragment)
            commit()
            viewPager2 = findViewById(R.id.viewPager_ImageSlider)

            val sliderItems: MutableList<Slideritem> = ArrayList()
            sliderItems.add(Slideritem(R.drawable.ic))
            sliderItems.add(Slideritem(R.drawable.spl))
            sliderItems.add(Slideritem(R.drawable.wheel))

            viewPager2.adapter = SliderAdapter(sliderItems, viewPager2)
        }
}