package com.villagevegetables.orders.Logins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.villagevegetables.orders.Activitys.DashBoardActivity
import com.villagevegetables.orders.Config.ViewController
import com.villagevegetables.orders.R
import com.villagevegetables.orders.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.loginBg), false)

        inits()
    }

    private fun inits() {
        binding.linearLogin.setOnClickListener {
            if (binding.editId.text.toString() == "") {
                ViewController.showToast(this@LoginActivity,"Enter your Id")
            }else{
                if (binding.editId.text.toString() == "VillageVegetables2025") {
                    val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                    startActivity(intent)
                }else{
                    ViewController.showToast(this@LoginActivity,"wrong ID")
                }
            }
        }

    }

}