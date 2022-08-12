package br.edu.ufam.pedro.sportgo.view.fragment

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.adapter.RecyclerViewListaEsporte
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.controller.interfac.itemClickListenerCadastro
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.banco.Preferences
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import kotlinx.android.synthetic.main.layout_lista_esporte.view.*

class ListaEsporteSelecionadoFragment: Fragment(), itemClickListenerCadastro{
    private lateinit var userDao: DadosDao
    private lateinit var recycler_lista: RecyclerView
    private lateinit var adapterLista: RecyclerViewListaEsporte
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_lista_esporte, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        adapterLista = RecyclerViewListaEsporte(this)
        setupRecyclerView(view)
        addDados()

    }
    private fun addDados() {
        val esporte = Preferences.getEsporte(requireContext())
        esporte?.let {
            val lista = userDao.buscarListaEsporte(it).toMutableList()
            adapterLista.popularLista(lista)
        }
    }
    private fun setupRecyclerView(view: View) {
        recycler_lista = view.findViewById(R.id.rv_menu_esporte)
        recycler_lista.layoutManager = LinearLayoutManager(requireContext())
        recycler_lista.adapter = adapterLista
    }
    @SuppressLint("SetTextI18n")
    private fun setHeader(view: View) {
        view.txtSaudacoesEsporte.text = "Locais com (${Preferences.getEsporte(requireContext())})"
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText("Lista de locais")
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.home, false)
        }
    }

    override fun itemClick(dado: DadosLocal, position: Int) {
        Log.i("teste","Lista Esporte click: ${dado}")
        BancodeDados.dadosLocal = dado
        findNavController().navigate(R.id.action_lista_de_esporte_selecionado_to_detalhes_local)
    }
}