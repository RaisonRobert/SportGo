package br.edu.ufam.pedro.sportgo.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.edu.ufam.pedro.sportgo.R
import br.edu.ufam.pedro.sportgo.controller.interfac.DadosDao
import br.edu.ufam.pedro.sportgo.model.banco.AppDatabase
import br.edu.ufam.pedro.sportgo.model.banco.BancodeDados
import kotlinx.android.synthetic.main.layout_webview_fragment.view.*

class WebViewFragment : Fragment() {
    private lateinit var userDao: DadosDao
    lateinit var ws: WebSettings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.instancia(requireContext())
        userDao = db.userDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.layout_webview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHeader(view)
        startWebView(view)
    }
    @Suppress("DEPRECATION")
    private fun setHeader(view: View) {
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val headerLayout = view.findViewById<View>(R.id.headerView)
        val titlePage = headerLayout.findViewById<TextView>(R.id.title)
        titlePage.setText("Visualizar Mapa do local")
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.detalhes_local, false)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun startWebView(view: View) {
        val webView = view.webViewMAPS
        BancodeDados.dadosLocal.linklocal?.let {
            webView.loadUrl(it)
            //        ABRIR NO CELULAR
            webView.webViewClient = WebViewClient()
//        ABRIR NO GOOGLE CHROME
//        webView.webChromeClient = WebChromeClient()
            webViewSettings(webView)
        }
    }

    private fun webViewSettings(webView: WebView?) {
        ws = webView!!.settings
        ws.javaScriptEnabled = true
        ws.setSupportZoom(false)
        ws.setUseWideViewPort(true)
        ws.setDomStorageEnabled(true);
//        ws.setAppCacheEnabled(true);
        ws.setLoadsImagesAutomatically(true);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

    }

}