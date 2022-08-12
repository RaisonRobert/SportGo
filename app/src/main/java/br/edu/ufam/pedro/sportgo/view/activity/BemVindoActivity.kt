package br.edu.ufam.pedro.sportgo.view.activity


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin

class BemVindoActivity : AppCompatActivity() {
    val PERMISSION_REQUEST_COARSE_LOCATION = 1
    private lateinit var userDao: DadosDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bem_vindo)
        val db = AppDatabase.instancia(this)
        userDao = db.userDao()
        time()
        criaAdmin()
        testeCadastrarLocal()
    }
    private fun time() {
        val intent = Intent(this, LoginActivity::class.java)
        val timer = object : CountDownTimer(6000, 1000) {
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
        ActivityCompat.requestPermissions(
            this@BemVindoActivity,
            arrayOf(
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_COARSE_LOCATION
        )

        if (ActivityCompat.checkSelfPermission(
                this@BemVindoActivity,
                android.Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@BemVindoActivity,
                arrayOf(android.Manifest.permission.CAMERA),
                0
            )
        }
    }
    private fun criaAdmin() {
        if(userDao.buscarDados().isEmpty()){
            userDao.salvaDados(
                DadosLogin(
                id = 1,
                nome = "Conta Administrador",
                email = "admin@ufam.edu.br",
                senha = "admin",
                deficiente = false
                )
            )
            userDao.salvaDados(
                DadosLogin(
                    id = 2,
                    nome = "Raison",
                    email = "raison@ufam.edu.br",
                    senha = "123",
                    deficiente = true
                )
            )
        }
    }
    private fun testeCadastrarLocal() {
        if (userDao.buscarLocais().isEmpty()) {
            userDao.salvaLocal(
                DadosLocal(
                    id = 1,
                    foto = BancodeDados.foto,
                    nomelocal = "SEJEL",
                    horario = "08:00 até 22:00",
                    linklocal = "https://goo.gl/maps/9aphYVFfA2uUv9t1A",
                    esporte = "Futebol",
                    descricao = "A SEJEL foi criada em 2002, tendo como objetivo o amparo ao desporto, a " +
                            "promoção, a difusão das atividades desportivas e a promoção do esporte amador."
                )
            )
        }
    }
}