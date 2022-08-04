package br.edu.ufam.pedro.sportgo.controller.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.view.fragment.HomeAdminFragment

class RecyclerViewCadastro (var clickListener: HomeAdminFragment) :
    RecyclerView.Adapter<RecyclerViewCadastro.ViewHolder>()  {
    private var listar: MutableList<DadosLogin> = mutableListOf()
    var itemListener: itemClickListenerRank? = null
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun bindView(listAdapter: DadosLogin, action : itemClickListenerRank, position: Int) {
            itemView.textViewNome.text = listAdapter.nome.toString()
            itemView.txtPosicao.text = (position + 1).toString()
//            itemView.txtTempo.text = "Tempo: " + listAdapter.tempo.toString()
            itemView.textViewPontos.text = "Pontuação: " + listAdapter.pontos.toString()
            itemView.setOnClickListener{
                action.itemClick(listAdapter, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rank, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listr = listar[position]
        holder.bindView(listr, clickListener, position)
        holder.let {
            itemListener?.itemClick(listar[position], position)
        }
    }

    override fun getItemCount(): Int {
        return listar.size
    }
    fun popularLista(dado: MutableList<DadosLogin>) {
        this.listar.clear()
        this.listar.addAll(dado)
        Log.i("teste", "lista ---- >> $listar")
        notifyDataSetChanged()
    }
}