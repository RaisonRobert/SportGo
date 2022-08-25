package br.edu.ufam.pedro.sportgo.view.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.view.ui.Ui
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import kotlinx.android.synthetic.main.layout_visualiza_dados_fragment.view.*

class VisualizarDadosMapLocalFragment : Fragment(){
    private lateinit var userDao: DadosDao
    private val NAO_POSSUI = "Não Possui Informação"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_visualiza_dados_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        setDadosVisualizar(view)
        botao(view)
    }

    private fun botao(view: View) {
        view.btnLocal.setOnClickListener {
            if(!BancodeDados.dadosLocal.linklocal.isNullOrEmpty()){
                findNavController().navigate(R.id.action_detalhes_local_to_web_view)
            }else
                Toast.makeText(requireContext(),"Link do local não está disponível",Toast.LENGTH_SHORT).show()

        }
    }

    @Suppress("DEPRECATION")
    private fun setHeader(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText("Visualizar Detalhes")
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.lista_de_esporte_selecionado, false)
        }
    }
    private fun setDadosVisualizar(view: View) {
        val foto = Ui.convertBase64ToBitmap(BancodeDados.dadosLocal.foto.toString())
        foto?.let {
            view.imageButtonTirarFotoVisualizarEsporte.setImageBitmap(it)
        }
        BancodeDados.dadosLocal.esporte?.let {
            if (it.isNotEmpty()) {
                view.textView.setText("Esporte Praticado: $it")
            } else
                view.textView.setText(NAO_POSSUI)

        }
        BancodeDados.dadosLocal.horario?.let {
            if (it.isNotEmpty()) {
                view.txtHorario.setText(it)
            } else
                view.txtHorario.setText(NAO_POSSUI + " de Horario")

        }
        BancodeDados.dadosLocal.descricao?.let {
            if (it.isNotEmpty()) {
                view.txtDescLocalEsporte.setText(it)
            } else
                view.txtDescLocalEsporte.setText(NAO_POSSUI+ " de descrição")

        }
    }
}