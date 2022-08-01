package br.edu.ufam.pedro.sportgo.view


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.view.activity.LoginActivity

class BemVindoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bem_vindo)
        time()
    }
    private fun time() {
        val intent = Intent(this, LoginActivity::class.java)
        val timer = object : CountDownTimer(5000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                startActivity(intent)
                finish()
            }

        }
        timer.start()

    }
}