package com.realm.imade.ui.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.realm.imade.MainActivity
import com.realm.imade.notification.NotificationM.createChannel
import com.realm.imade.R
import com.realm.imade.config.Config
import com.realm.imade.config.Config.mainSplash
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
            if (!mainSplash) {
                createChannel(Config.ON_CHANNEL)
                mainSplash = true
            }
            delay(2000)
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }
    }
}