package com.realm.imade.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.realm.imade.MainActivity
import com.realm.imade.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity(R.layout.splash) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<ImageView>(R.id.inage).apply {
            background =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.backgroud,
                        null
                    )
                else
                    AppCompatResources.getDrawable(this@Splash, R.drawable.backgroud)
        }


        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }
    }
}