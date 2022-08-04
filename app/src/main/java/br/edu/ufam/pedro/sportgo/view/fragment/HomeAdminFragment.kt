package br.edu.ufam.pedro.sportgo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.banco.Preferences
import br.edu.ufam.pedro.sportgo.view.activity.LoginActivity
import kotlinx.android.synthetic.main.dialog_apaga_conta.view.*

class HomeAdminFragment : Fragment() {
    private lateinit var userDao: DadosDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_home_admin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        adapterLista = RecyclerViewCadastro(this)
        setupRecyclerView(view)
        addDados()
    }
    /**
     * Método criado para setar os itens da Header
     * @param view View
     */
    @Suppress("DEPRECATION")
    private fun setHeader(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        setHasOptionsMenu(true)
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText(getString(R.string.aprendendo_libraas))
    }
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_admin, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                startLogin()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogApagaConta() {
        val alertDialogExibir = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_apaga_conta, null)
        view.iconDialog.setImageResource(R.drawable.ic_fail)
        view.titleDialog.text = getString(R.string.poxa)
        view.subtitleDialog.text = getString(R.string.apaga_conta)
        view.descriptionDialog.text = ""
        alertDialogExibir.setView(view)
        val dialog = alertDialogExibir.create()
        dialog.show()
        view.buttonCancela.setOnClickListener {
            dialog.dismiss()
        }
        view.buttonOK.setOnClickListener {
            dialog.dismiss()
            userDao.delete(BancodeDados.dadosUser)
            startLogin()
        }
    }
    private fun startLogin() {
        setPreferencesLogin("", "")
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
    private fun setPreferencesLogin(email: String, senha: String) {
        Preferences.setEmail(requireContext(), email)
        Preferences.setSenha(requireContext(), senha)
    }
}