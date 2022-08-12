package br.edu.ufam.pedro.sportgo.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.adapter.RecyclerViewCadastro
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.Preferences
import kotlinx.android.synthetic.main.layout_lista_esporte.view.*

class ListaEsporteSelecionado: Fragment() {
    private lateinit var userDao: DadosDao
    private lateinit var recycler_lista: RecyclerView
    private lateinit var adapterLista: RecyclerViewCadastro
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
//        adapterLista = RecyclerViewCadastro(this)
//        setupRecyclerView(view)
//        addDados()
//        botoes(view)

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
}