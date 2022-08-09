package br.edu.ufam.pedro.sportgo.controller.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin
import kotlinx.android.synthetic.main.dialog_modal.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

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
     * Método que converte um bitmap para uma string de Base64
     * @param imagem Bitmap
     */
    fun convertToBase64(imagem: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        imagem.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val imageString: String = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
        return imageString
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

    /**
     * Método que cria a imagem de perfil do usuário
     * @param context Contexto
     */
//    lateinit var currentPhotoPath: String
    fun createImageFile(context: Context): File {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_LOCAL", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
//            .apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
    }




}