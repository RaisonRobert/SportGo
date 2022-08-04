package br.edu.ufam.pedro.sportgo.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.controller.ui.Ui
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.banco.Preferences
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.layout_fragment_alterar_dados.view.*
import kotlinx.android.synthetic.main.layout_fragment_alterar_dados.view.btnDeficiente
import kotlinx.android.synthetic.main.layout_fragment_criar_conta.view.*

class AlterarDadosFragment : Fragment(){
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
        return inflater.inflate(R.layout.layout_fragment_alterar_dados, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDadosAlterar(view)
        botao(view)
        setHeader(view)
    }

    private fun botao(view: View) {

        view.btnAlterar.setOnClickListener {
            salvaDadosAlterados(view)
        }
    }


    private fun salvaDadosAlterados(view: View) {
        val nome = view.nomefieldAlterar.text.toString()
        val senha = view.passwordfieldAlterar.text.toString()
//        Log.i("teste", "nome: $nome")
//        Log.i("teste", "senha: $senha")
        val nomeLayout = view.findViewById<TextInputLayout>(R.id.nomeLayoutAlterar)
        val senhaLayout = view.findViewById<TextInputLayout>(R.id.passwordLayoutAlterar)
        nomeLayout.isErrorEnabled = true
        senhaLayout.isErrorEnabled = true
        nomeLayout.error = null
        senhaLayout.error = null
        if (validaCampos(nome, senha)) {

            val switch = verificaBotaoDef(view)
            BancodeDados.dadosUser.nome = nome
            BancodeDados.dadosUser.senha = senha
            BancodeDados.dadosUser.deficiente = switch
            userDao.alterarDados(BancodeDados.dadosUser)
            Ui.atualizaLista(userDao.buscarDados())
            setPreferencesLogin(senha)
            dialogLogin()
        } else if (nome.isEmpty()) {
            nomeLayout.isErrorEnabled = false
            nomeLayout.error = getString(R.string.digite_seu_nome)
        } else {
            senhaLayout.isErrorEnabled = false
            senhaLayout.error = getString(R.string.digite_a_senha)
        }
    }
    private fun verificaBotaoDef(view: View): Boolean {
        var resp = false
        if (view.btnDeficiente.isChecked) {
            resp = true
        }
        return resp
    }
    private fun dialogLogin() {
        Ui.createModal(
            requireContext(),
            R.drawable.ic_sucesso,
            getString(R.string.sucessoTitle),
            getString(R.string.sucesso),
            ""
        )!!
            .setOnDismissListener {
                startHome()
            }
    }

    private fun startHome() {
        findNavController().popBackStack(R.id.home, false)
    }
    private fun validaCampos(nome: String, senha: String): Boolean {
        if (nome.isNotEmpty() && senha.isNotEmpty()) {
            return true
        }
        return false
    }

    private fun setPreferencesLogin(senha: String) {
        Preferences.setSenha(requireContext(), senha)
    }
    private fun setDadosAlterar(view: View) {
        view.nomefieldAlterar.setText(BancodeDados.dadosUser.nome)
        view.emailfieldtextAlterar.setText(BancodeDados.dadosUser.email)
        view.emailfieldtextAlterar.isEnabled = false
        view.passwordfieldAlterar.setText(BancodeDados.dadosUser.senha)
    }
    private fun setHeader(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText(getString(R.string.alterar_dados))
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.home, false)
        }
    }
}