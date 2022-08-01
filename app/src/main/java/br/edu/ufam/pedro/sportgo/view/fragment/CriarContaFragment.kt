package br.edu.ufam.pedro.sportgo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.controller.ui.Ui
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin
import br.edu.ufam.pedro.sportgo.view.activity.HomeActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.layout_fragment_criar_conta.view.*

class CriarContaFragment: Fragment() {
    private lateinit var loading: AlertDialog
    private lateinit var userDao: DadosDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_fragment_criar_conta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading = Ui.createLoadDialog(requireContext(), false)
        botoes(view)
    }



    private fun botoes(view: View) {
        view.btnCriarConta.setOnClickListener {
            cadastro(view)
        }
    }


    private fun cadastro(view: View) {
        val nome = view.nomefield.text.toString()
        val email = view.emailfieldtextcriarConta.text.toString()
        val senha = view.passwordfieldCriarConta.text.toString()
        val nomeLayout = view.findViewById<TextInputLayout>(R.id.nomeLayout)
        val emailLayout = view.findViewById<TextInputLayout>(R.id.emailLayoutcriarconta)
        val senhaLayout  = view.findViewById<TextInputLayout>(R.id.passwordLayoutCriarConta)
        nomeLayout.isErrorEnabled = true
        emailLayout.isErrorEnabled = true
        senhaLayout.isErrorEnabled = true
        if(nome.isNotEmpty()&&email.isNotEmpty()&&senha.isNotEmpty()) {
            if (emailValid(email)) {
                nomeLayout.isErrorEnabled = false
                emailLayout.isErrorEnabled = false
                senhaLayout.isErrorEnabled = false
                if (validaConta(email)){
                    val user = DadosLogin(
                        nome = nome,
                        email = email,
                        senha = senha,
                        deficiente = true
                    )
//                    INSERE DADOS
                    userDao.salvaDados(user)
                    Ui.atualizaLista(userDao.buscarDados())
//                    BancodeDados.arquivosDadosCadastrado.add(user)
                    BancodeDados.arquivosDadosCadastrado.forEach {
                        if (it.email == email){
                            BancodeDados.dadosUser = it
                        }
                    }
//                    BancodeDados.dadosUser.nome = nome
//                    BancodeDados.dadosUser.email = email
//                    BancodeDados.dadosUser.senha = senha
                    setPreferencesLogin(email, senha)
                    dialogContaCadastrada()
//                    Log.i("teste","banco de dados: ${userDao.buscarDados()}")
                }else {
                    emailLayout.error = getString(R.string.emailCadastrado)
                }
            } else {
                emailLayout.error = getString(R.string.notValidEmail)
            }
        }else
            if (nome.isEmpty()){
                emailLayout.isErrorEnabled = false
                senhaLayout.isErrorEnabled = false
                nomeLayout.error = getString(R.string.vazio)
            }else
                if(email.isEmpty()){
                    nomeLayout.isErrorEnabled = false
                    senhaLayout.isErrorEnabled = false
                    emailLayout.error = getString(R.string.vazio)
                }else
                    if (senha.isEmpty()){
                        nomeLayout.isErrorEnabled = false
                        emailLayout.isErrorEnabled = false
                        senhaLayout.error = getString(R.string.vazio)
                    }

    }

    private fun dialogContaCadastrada() {
        Ui.createModal(requireContext(),
            R.drawable.ic_sucesso,
            getString(R.string.sucessoTitle),
            getString(R.string.conta_cadastrada_sucesso),
            getString(R.string.desc_bem_vindo_libras))!!
            .setOnDismissListener {
                startHome()
            }
    }

    private fun validaConta(email: String): Boolean {
        BancodeDados.arquivosDadosCadastrado.forEach{
            if(email == it.email){
                return false
            }
        }
        return true
    }

    /**
     * Método criado para validação do Email
     * @return Boolean
     */
    private fun emailValid(email: String): Boolean {
        return !(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }
    private fun startHome() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()
    }
    private fun setPreferencesLogin(email: String, senha: String) {
        br.edu.ufam.pedro.sportgo.model.banco.Preferences.setEmail(requireContext(), email)
        br.edu.ufam.pedro.sportgo.model.banco.Preferences.setSenha(requireContext(), senha)
//        Log.i(
//            "teste",
//            "Preferences Armazenado>>> email: ${Preferences.getEmail(requireContext())} Senha: ${
//                Preferences.getSenha(requireContext())
//            }"
//        )
    }


}