package br.edu.ufam.pedro.sportgo.controller.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin
import kotlinx.android.synthetic.main.dialog_modal.view.*

object Ui {
    /**
     *
     * Sobrecarga do método que cria modal padrão do load
     * @param context Contexto
     * @param show boolean
     * @return AlertDialog
     *
     * */
    fun createLoadDialog(context: Context, show: Boolean): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(R.layout.dialog_load)
        builder.setCancelable(false)
        val modal = builder.create()
        modal.setCanceledOnTouchOutside(false)
        if (show) modal.show()
        return modal
    }
    /**
     *
     * Sobrecarga do método que cria modal padrão do aplicativo recebendo uma ação
     * @param context Contexto
     * @param icon Ícone
     * @param title Título do modal
     * @param subtitle Subtítulo do modal
     * @param description Descrição do modal
     * @return AlertDialog
     *
     * */
    fun createModal(
        context: Context,
        icon: Int,
        title: String,
        subtitle: String,
        description: String?
    ): AlertDialog? {

        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_modal, null)

        var modalIcon = mDialogView.findViewById<ImageView>(R.id.iconDialog)
        var modalTitle = mDialogView.findViewById<TextView>(R.id.titleDialog)
        var modalSubtitle = mDialogView.findViewById<TextView>(R.id.subtitleDialog)
        var modalDescription = mDialogView.findViewById<TextView>(R.id.descriptionDialog)

        modalIcon.setImageResource(icon)
        modalTitle.text = title
        modalSubtitle.text = subtitle
        modalDescription.text = description

        val mBuilder = AlertDialog.Builder(context)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()

        mDialogView.buttonOK.setOnClickListener{
            mAlertDialog.dismiss()

        }
        return mAlertDialog
    }

    /**
     * Método que converte a string Base64 em um bitmap
     * @param base64 String a ser convertida
     */
    fun convertBase64ToBitmap(base64: String): Bitmap? {
        val data = android.util.Base64.decode(base64, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }
//    fun AlteraDados(dados: DadosLogin) {
//        BancodeDados.arquivosDadosCadastrado.forEach{
//            if(it.email == dados.email){
//                it.pontos = dados.pontos
//                return
//            }
//        }
//    }

    fun atualizaLista(salvaDados: List<DadosLogin>) {
        BancodeDados.arquivosDadosCadastrado = salvaDados.toMutableList()
    }

}