package br.edu.ufam.pedro.sportgo.controller.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.itemClickListenerCadastro
import br.edu.ufam.pedro.sportgo.controller.ui.Ui
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import br.edu.ufam.pedro.sportgo.view.fragment.ListaEsporteSelecionadoFragment
import kotlinx.android.synthetic.main.item_lista_cadastro.view.*

class RecyclerViewListaEsporte (var clickListener: ListaEsporteSelecionadoFragment) : RecyclerView.Adapter<RecyclerViewListaEsporte.ViewHolder>()  {
    private var listar: MutableList<DadosLocal> = mutableListOf()
    var itemListener: itemClickListenerCadastro? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bindView(listAdapter: DadosLocal, action : itemClickListenerCadastro) {
            itemView.textViewNome.text = "Local: " + listAdapter.nomelocal.toString()
            itemView.txtDescLocal.text = listAdapter.descricao.toString()
            itemView.textViewEsporte.text = "Esporte Praticado: "+listAdapter.esporte.toString()
            listAdapter.foto?.let {
                itemView.imageViewLocal.setImageBitmap(Ui.convertBase64ToBitmap(listAdapter.foto)?.let { reduzBitmap(it) })
            }

            itemView.setOnClickListener{
                action.itemClick(listAdapter, adapterPosition)
            }
        }
        private fun reduzBitmap(bmpFotoRotation: Bitmap): Bitmap? {
            return Bitmap.createScaledBitmap(
                bmpFotoRotation,
                250, 300
                , true
            )
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_lista_cadastro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listr = listar[position]
        holder.bindView(listr, clickListener)
        holder.let {
            itemListener?.itemClick(listar[position], position)
        }
    }

    override fun getItemCount(): Int {
        return listar.size
    }
    fun popularLista(dado: MutableList<DadosLocal>) {
        this.listar.clear()
        this.listar.addAll(dado)
        Log.i("teste", "lista ---- >> $listar")
        notifyDataSetChanged()
    }
}