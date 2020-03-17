package com.hyejineee.flochallenge.mView

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.hyejineee.flochallenge.R
import com.hyejineee.flochallenge.databinding.SplashActivityBinding

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewDataBinding: SplashActivityBinding = setContentView(this, R.layout.splash_activity)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.imageFile = getString(R.string.splash_image)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 2000)
    }
}