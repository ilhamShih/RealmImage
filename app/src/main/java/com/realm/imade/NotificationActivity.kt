package com.realm.imade

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class NotificationActivity : AppCompatActivity(R.layout.notification_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.backgroud))

        val bundle: Bundle? = intent.extras
        val intent = intent
        if (bundle != null) {
            val username = intent.getStringExtra("notificationUrl")
            val imageView = findViewById<ImageView>(R.id.images)
            Glide.with(this).load(username).into(imageView)
        }

    }
}