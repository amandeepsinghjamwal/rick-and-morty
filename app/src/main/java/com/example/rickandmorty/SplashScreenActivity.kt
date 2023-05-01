package com.example.rickandmorty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.widget.TextView
import com.example.rickandmorty.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val animationSet= AnimationSet(false)
        val animationZoomIn= ScaleAnimation(.5f,1f,.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
        val animationZoomOut= ScaleAnimation(1.2f,1f,1.2f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
        animationZoomOut.startOffset = 500
        animationZoomIn.duration=1000
        animationZoomOut.duration=1000
        animationZoomOut.fillAfter=true
        animationSet.addAnimation(animationZoomIn)
        animationSet.addAnimation(animationZoomOut)
//        val animationZoomOut= AnimationUtils.loadAnimation(this,R.anim.fadeout_animation)
        binding.imageView.startAnimation(animationSet)
        binding.imageView3.startAnimation(animationSet)
        binding.textView.startAnimation(animationSet)
        val intent=Intent(this,MainActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()
        if(!sharedPreferences.getBoolean("false",false)){
            val welcomeText:TextView= findViewById(R.id.textView)
            welcomeText.text=getString(R.string.Welcome)
            myEdit.putBoolean("false",true).commit()
        }
        CoroutineScope(Dispatchers.Default).launch {
            delay(3000)
            startActivity(intent)
        }
    }
}