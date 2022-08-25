package br.edu.ufam.pedro.sportgo.view.fragment

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.view.ui.Ui
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import kotlinx.android.synthetic.main.dialog_apaga_conta.view.*
import kotlinx.android.synthetic.main.layout_visualiza_admin_fragment.*
import kotlinx.android.synthetic.main.layout_visualiza_admin_fragment.nomeLayoutDesc
import kotlinx.android.synthetic.main.layout_visualiza_admin_fragment.nomeLayoutLocal
import kotlinx.android.synthetic.main.layout_visualiza_admin_fragment.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class VisualizarLocalFragment : Fragment() {
    private lateinit var userDao: DadosDao
    private val NAO_POSSUI = "Não Possui Dados Cadastrado"
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var profile_photo: ImageView
    private var photo: Bitmap? = null
    private lateinit var photoFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_visualiza_admin_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        setDadosVisualizar(view)
        botoesAlterar()
    }

    private fun botoesAlterar() {
            imageButtonTirarFotoVisualizar.setOnClickListener {
                dispatchTakePictureIntent()
            }
        btnAlterar.setOnClickListener {
            val body = montarBody()


                if((body.esporte!!.isNotEmpty() && body.nomelocal!!.isNotEmpty()) && body.descricao!!.isNotEmpty()){
                    BancodeDados.dadosLocal = body
//                    Toast.makeText(requireContext(),"salvou", Toast.LENGTH_SHORT).show()
//                    Log.i("teste","dado update: ${BancodeDados.dadosLocal}")
                    userDao.alterarDadosLocal(BancodeDados.dadosLocal)
                    findNavController().popBackStack(R.id.home_admin, false)
                }
                if(body.nomelocal.isNullOrEmpty()) {
                    nomeLayoutLocal.error = "Digite o Nome do Local"
                }
                if(body.esporte.isNullOrEmpty()){
                    nomeLayoutEsporteVisualizar.error = "Escolha o Esporte"
//                Toast.makeText(requireContext(),"Escolha o Esporte", Toast.LENGTH_SHORT).show()
                }
                if(body.descricao.isNullOrEmpty()){
                    nomeLayoutDesc.error = "Digite uma breve descrição"
                }



        }
    }

    private fun montarBody(): DadosLocal {
        var foto: String? = BancodeDados.dadosLocal.foto
        photo?.let {
            foto = Ui.convertToBase64(reduzBitmap(it))
        }

        val cadastro = DadosLocal(
            id = BancodeDados.dadosLocal.id,
            foto = foto,
            nomelocal = nomefieldLocalVisualizar.text.toString(),
            horario = nomefieldHorarioVisualizar.text.toString(),
            linklocal = nomefieldLinkMapsVisualizar.text.toString(),
            esporte = menu_esporte_visualizar.text.toString(),
            descricao = nomefieldDescVisualizar.text.toString()

        )
        return cadastro
    }

    /**
     * Método criado para realizar a foto de perfil do usuário
     */
    @Suppress("DEPRECATION")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            try {
                photoFile = Ui.createImageFile(requireContext())
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "br.edu.ufam.pedro.sportgo",
                    photoFile
                )
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoURI
                )
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    /**
     * Método criado para capturar o resultado da Activitu e realizar uma ação
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent
     */
    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == REQUEST_IMAGE_CAPTURE) && (resultCode == Activity.RESULT_OK)) {
            val uri = Uri.fromFile(photoFile)
            photo = carrega(uri)
            photo?.let {
                profile_photo.setImageBitmap(it)
            }
        }
    }


    fun carrega(caminhoFoto: Uri): Bitmap {
        val inputStream =
            requireActivity().contentResolver.openInputStream(caminhoFoto) // extrai dadoPerguntas exif da foto
        val exif = androidx.exifinterface.media.ExifInterface(inputStream!!)
        val orientacao = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val codigoOrientacao = orientacao;
        val bmp = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, caminhoFoto)
        val path = bitmapToFile(bmp).path.toString()
        var orientacaoAjustada = when (codigoOrientacao) {
            ExifInterface.ORIENTATION_NORMAL ->
                abreFotoERotaciona(path, 0)
            ExifInterface.ORIENTATION_ROTATE_90 ->
                abreFotoERotaciona(path, 90)
            ExifInterface.ORIENTATION_ROTATE_180 ->
                abreFotoERotaciona(path, 180)
            ExifInterface.ORIENTATION_ROTATE_270 ->
                abreFotoERotaciona(path, 270)
            else -> abreFotoERotaciona(path, 0)
        }
        return orientacaoAjustada
    }

    private fun abreFotoERotaciona(caminhoFoto: String?, angulo: Int): Bitmap {
        val bitmap = BitmapFactory.decodeFile(caminhoFoto);
        val matrix = Matrix();
        matrix.postRotate(angulo.toFloat())
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.getWidth(),
            bitmap.getHeight(),
            matrix,
            true
        );
    }

    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(requireContext())
        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file) as OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    /**
     * Método criado para envio da foto
     * @return AlertDialogMessage
     */

    private fun reduzBitmap(bmpFotoRotation: Bitmap): Bitmap? {
        return Bitmap.createScaledBitmap(
            bmpFotoRotation,
            250, 300, true
        )
    }

    private fun setDadosVisualizar(view: View) {
        btnAlterar.visibility = View.GONE
        val foto = Ui.convertBase64ToBitmap(BancodeDados.dadosLocal.foto.toString())
        foto?.let {
            view.imageButtonTirarFotoVisualizar.setImageBitmap(it)
        }
        BancodeDados.dadosLocal.nomelocal?.let {
            if (it.isNotEmpty()) {
                view.nomefieldLocalVisualizar.setText(it)
            } else
                view.nomefieldLocalVisualizar.setText(NAO_POSSUI)
            view.nomefieldLocalVisualizar.isEnabled = false
        }
        BancodeDados.dadosLocal.horario?.let {
            if (it.isNotEmpty()) {
                view.nomefieldHorarioVisualizar.setText(it)
            } else
                view.nomefieldHorarioVisualizar.setText(NAO_POSSUI)
            view.nomefieldHorarioVisualizar.isEnabled = false
        }
        BancodeDados.dadosLocal.linklocal?.let {
            if (it.isNotEmpty()) {
                view.nomefieldLinkMapsVisualizar.setText(it)
            } else
                view.nomefieldLinkMapsVisualizar.setText(NAO_POSSUI)
            view.nomefieldLinkMapsVisualizar.isEnabled = false
        }
        BancodeDados.dadosLocal.esporte?.let {
            if (it.isNotEmpty()) {
                view.menu_esporte_visualizar.setText(it)
            } else
                view.menu_esporte_visualizar.setText(NAO_POSSUI)
            view.menu_esporte_visualizar.isEnabled = false
        }
        BancodeDados.dadosLocal.descricao?.let {
            if (it.isNotEmpty()) {
                view.nomefieldDescVisualizar.setText(it)
            } else
                view.nomefieldDescVisualizar.setText(NAO_POSSUI)
            view.nomefieldDescVisualizar.isEnabled = false
        }

    }

    @Suppress("DEPRECATION")
    private fun setHeader(view: View) {

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        this.profile_photo = view.findViewById(R.id.imageButtonTirarFotoVisualizar) as ImageView
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText("Visualizar Cadastro")
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.home_admin, false)
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_cadastra, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAlterarDados -> {
                ativarCampos()
            }
            R.id.menuApagarConta -> {
                dialogApagaConta()
            }


        }
        return super.onOptionsItemSelected(item)
    }
    private fun dialogApagaConta() {
        val alertDialogExibir = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.dialog_apaga_conta, null)
        view.iconDialog.setImageResource(R.drawable.ic_fail)
        view.titleDialog.text = "Excluir Local!"
        view.subtitleDialog.text = "Você tem certeza que deseja excluir este local?"
        view.descriptionDialog.text = ""
        alertDialogExibir.setView(view)
        val dialog = alertDialogExibir.create()
        dialog.show()
        view.buttonCancela.setOnClickListener {
            dialog.dismiss()
        }
        view.buttonOK.setOnClickListener {
            dialog.dismiss()
//            Log.i("teste", "dados estatico: ${BancodeDados.dadosUser}")
            userDao.deleteLocal(BancodeDados.dadosLocal)
//            Log.i("teste", "banco de dados: ${userDao.buscarDados()}")
//            Ui.atualizaLista(userDao.buscarDados())
//                BancodeDados.arquivosDadosCadastrado.remove(BancodeDados.dadosUser)
            findNavController().popBackStack(R.id.home_admin, false)
        }
    }
    private fun ativarCampos() {
        nomefieldLocalVisualizar.isEnabled = true
        nomefieldHorarioVisualizar.isEnabled = true
        nomefieldLinkMapsVisualizar.isEnabled = true
        menu_esporte_visualizar.isEnabled = true
        nomefieldDescVisualizar.isEnabled = true
        if (nomefieldLocalVisualizar.text.toString() == NAO_POSSUI) {
            nomefieldLocalVisualizar.setText("")
        }
        if (nomefieldHorarioVisualizar.text.toString() == NAO_POSSUI) {
            nomefieldHorarioVisualizar.setText("")
        }
        if (nomefieldLinkMapsVisualizar.text.toString() == NAO_POSSUI) {
            nomefieldLinkMapsVisualizar.setText("")
        }
        if (menu_esporte_visualizar.text.toString() == NAO_POSSUI) {
            menu_esporte_visualizar.setText("")
        }
            val items = listOf("Futebol", "Futebol Americano", "Basketball", "Voleyball", "Tennis", "Ping Pong")
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            (nomeLayoutEsporteVisualizar.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        if (nomefieldDescVisualizar.text.toString() == NAO_POSSUI) {
            nomefieldDescVisualizar.setText("")
        }
        btnAlterar.visibility = View.VISIBLE
    }


}