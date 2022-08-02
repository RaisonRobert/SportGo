package br.edu.ufam.pedro.sportgo.controller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.view.Util.MenuSquareItem


/**
 * Classe criada para a lista dos menus padrões
 * @param myDataset ArrayList<MenuSquareItem>
 * @param onItemClickListener OnItemClickListener
 */
class MenuSquareItemAdapter(private val myDataset: ArrayList<MenuSquareItem>,
                            private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MenuSquareItemAdapter.MyViewHolder>() {

    /**
     * Método criado para a seleção do Item
     * @param item MenuSquareItem
     */
    interface OnItemClickListener {
        fun onItemClick(item: MenuSquareItem)
    }

    /**
     * Classe que retorna um item ja customizado
     * @param view View
     */
    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    /**
     * Método criado para o momento da criação da view
     * @param parent ViewGroup
     * @param viewType Int
     * @return MyViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_square, parent, false) as View
        return MyViewHolder(view)
    }

    /**
     * Método criado para associar a view criada com os dadoPerguntas passados
     * @param holder MyViewHolder
     * @param position Int
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val tv_label = holder.view.findViewById<TextView>(R.id.tv_label)
        val iv_icon = holder.view.findViewById<AppCompatImageView>(R.id.iv_icon)

        tv_label.text = myDataset.get(position).label
        iv_icon.setImageDrawable(
            AppCompatResources.getDrawable(
                holder.view.context,
                myDataset.get(position).drawable
            )
        )

        tv_label.contentDescription = myDataset.get(position).label + " botão."

        holder.view.setOnClickListener {
            onItemClickListener.onItemClick(myDataset.get(position))
        }

    }

    /**
     * Método criado para coletar o tamanho da lista de dadoPerguntas
     * @return myDataset
     */
    override fun getItemCount() = myDataset.size
}