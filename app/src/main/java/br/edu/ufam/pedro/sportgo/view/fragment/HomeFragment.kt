package br.edu.ufam.pedro.sportgo.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.adapter.MenuSquareItemAdapter
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.banco.Preferences
import br.edu.ufam.pedro.sportgo.view.Util.MenuSquareItem
import br.edu.ufam.pedro.sportgo.view.activity.LoginActivity
import kotlinx.android.synthetic.main.dialog_apaga_conta.view.*
import kotlinx.android.synthetic.main.layout_fragment_criar_conta.view.*

class HomeFragment: Fragment() {
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
        return inflater.inflate(R.layout.layout_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        setupMenu()
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
    private fun setupMenu() {
        val menuItens = ArrayList<MenuSquareItem>()
        menuItens.add(MenuSquareItem("Futebol", R.drawable.ic_futebol))
        menuItens.add(MenuSquareItem("Futebol \nAmericano", R.drawable.ic_american_football))
        menuItens.add(MenuSquareItem("Basketball", R.drawable.ic_basketball))
        menuItens.add(MenuSquareItem("Voleyball", R.drawable.ic_voleyball))
        menuItens.add(MenuSquareItem("Tennis", R.drawable.ic_tennis))
        menuItens.add(MenuSquareItem("Ping Pong", R.drawable.ic_ping_pong))
        viewManager = GridLayoutManager(context, 3)
        viewAdapter = MenuSquareItemAdapter(menuItens, this.menuSquareItemClickListener())
        recyclerView = requireActivity().findViewById<RecyclerView>(R.id.rv_menu).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    /**
     * Método criado para definir os itens do Menu
     * @return OnItemClickListener
     */
    private fun menuSquareItemClickListener(): MenuSquareItemAdapter.OnItemClickListener = object :
        MenuSquareItemAdapter.OnItemClickListener {

        /**
         * Método criado para as ações dos Itens do menu
         * @param item MenuSquareItem
         */
        override fun onItemClick(item: MenuSquareItem) {
            when (item.drawable) {
                R.drawable.ic_futebol -> {
//                    Toast.makeText(activity, "Clicou em futebol!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Futebol")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
                R.drawable.ic_american_football -> {
//                    Toast.makeText(activity, "Clicou em futebol Americano!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Futebol Americano")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
                R.drawable.ic_basketball -> {
//                    Toast.makeText(activity, "Clicou em basketball!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Basketball")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
                R.drawable.ic_voleyball -> {
//                    Toast.makeText(activity, "Clicou em voleyball!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Voleyball")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
                R.drawable.ic_tennis -> {
//                    Toast.makeText(activity, "Clicou em tennis!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Tennis")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
                R.drawable.ic_ping_pong -> {
//                    Toast.makeText(activity, "Clicou em ping pong!", Toast.LENGTH_LONG).show()
                    Preferences.setEsporte(requireContext(),"Ping Pong")
                    findNavController().navigate(R.id.action_home_to_lista_de_esporte_selecionado)
                }
            }
        }
    }
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSair -> {
                startLogin()
            }
            R.id.menuApagarConta -> {
                dialogApagaConta()
            }
            R.id.menuAlterarDados -> {
                findNavController().navigate(R.id.action_home_to_alterar)
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