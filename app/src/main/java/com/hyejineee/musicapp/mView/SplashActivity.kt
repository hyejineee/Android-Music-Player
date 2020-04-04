package com.hyejineee.musicapp.mView

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.hyejineee.musicapp.R
import com.hyejineee.musicapp.databinding.SplashActivityBinding
import com.hyejineee.musicapp.mVIewModel.SongInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val songInfoViewModel: SongInfoViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewDataBinding: SplashActivityBinding = setContentView(this, R.layout.splash_activity)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.imageFile = getString(R.string.splash_image)

        val i = Intent(this, PlayerService::class.java)
        startService(i)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}