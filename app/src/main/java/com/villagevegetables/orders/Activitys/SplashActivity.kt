package com.villagevegetables.orders.Activitys

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.villagevegetables.orders.Config.Preferences
import com.villagevegetables.orders.Config.ViewController
import com.villagevegetables.orders.Logins.LoginActivity
import com.villagevegetables.orders.R
import com.villagevegetables.orders.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }


    val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.splashW), false)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("45", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            //token = task.result.toString()
            Log.e("FCM_TOKEN", "FCM Token: ${task.result}")
        })
        val title= intent.getStringExtra("title")
        val notificationMessage= intent.getStringExtra("isNotification").toString()

        Log.e("notificationMessage","notificationMessage "+notificationMessage+ "title "+title)
        askNotificationPermission()
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Handler(Looper.getMainLooper()).postDelayed({
            }, 1500)
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
        }

        methodRun()

    }


    private fun methodRun() {
        handler.postDelayed({
            val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")
            if (loginCheck.equals("Login")) {
                startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            }
        }, 3000)
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.e("On New INtent","OnNew Intent");
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.isNotEmpty()) {
                Handler(Looper.getMainLooper()).postDelayed({
                }, 1500)
            }
        }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Handler(Looper.getMainLooper()).postDelayed({

                }, 1500)
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                 ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    200
                );
            } else {
                ActivityCompat.requestPermissions(
                    this@SplashActivity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    200
                );
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Handler(Looper.getMainLooper()).postDelayed({
        }, 1500)
    }

}