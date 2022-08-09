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
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.controller.ui.Ui
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import kotlinx.android.synthetic.main.layout_fragment_cadastrar_local.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

@Suppress("DEPRECATION")
class CadastrarLocalFragment : Fragment() {
    private lateinit var userDao: DadosDao
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
        return inflater.inflate(R.layout.layout_fragment_cadastrar_local, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        botoes(view)
    }

    private fun botoes(view: View) {
        profile_photo.setOnClickListener{
            dispatchTakePictureIntent()
        }
        btnCadastrar.setOnClickListener {
//            montarBody()
                userDao.salvaLocal(montarBody())
        }
    }


    private fun montarBody(): DadosLocal {
        var foto: String? = null
        photo?.let {
            Ui.convertToBase64(photo!!)?.let {
                foto = it
            }
        }
        val cadastro = DadosLocal(
            foto = foto,
            nomelocal = nomefieldLocal.text.toString(),
            horario = nomefieldHorario.text.toString(),
            linklocal = nomefieldLinkMaps.text.toString(),
            esporte = nomefieldDesc.text.toString(),
            descricao = nomefieldEsporte.text.toString()
        )
        return cadastro
    }

    /**
     * Método criado para realizar a foto de perfil do usuário
     */
    @Suppress("DEPRECATION")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
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
                profile_photo.setImageBitmap(photo)
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
            300, 300, true
        )
    }

    private fun setHeader(view: View) {
        this.profile_photo = view.findViewById(R.id.imageButtonTirarFoto) as ImageView
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText(getString(R.string.cadastrar_local))
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.home_admin, false)
        }
    }

}