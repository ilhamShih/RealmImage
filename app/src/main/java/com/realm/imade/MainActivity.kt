package com.realm.imade

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.realm.imade.api.DataService
import com.realm.imade.api.RetrofitResponse.retrofitInstanceServer
import com.realm.imade.databinding.ActivityMainBinding
import com.realm.imade.ui.model.PageViewModel
import com.realm.imade.ui.tabs.TabsAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var pager: ViewPager2
    private lateinit var binding: ActivityMainBinding
    var dataService: DataService? = null
    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pageViewModel = ViewModelProvider(this)[PageViewModel::class.java]
        dataService = retrofitInstanceServer!!.create(DataService::class.java)

        val tabsAdapter = TabsAdapter(supportFragmentManager, lifecycle)
        pager = binding.viewPager
        pager.adapter = tabsAdapter
        pager.offscreenPageLimit = 1
        pager.adapter = tabsAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.icon =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_baseline_list,
                                null
                            )
                        else
                            AppCompatResources.getDrawable(this, R.drawable.ic_baseline_list)
                }
                1 -> {
                    tab.icon =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_baseline_favorite,
                                null
                            )
                        else
                            AppCompatResources.getDrawable(this, R.drawable.ic_baseline_favorite)
                }
            }
        }.attach()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)

    }

}