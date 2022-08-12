package br.edu.ufam.pedro.sportgo.model.banco

import android.content.Context
import android.content.SharedPreferences


object Preferences {
    private var preference: SharedPreferences? = null
    private val PREFS_FILENAME = "br.edu.ufam.pedro.sportgo.preferences"
    private var preferencesEmail: String? = null
    private const val preferenceEMAIL = "email"
    private var preferencesSenha: String? = null
    private var preferencesEsporte: String? = null
    private const val preferenceSENHA = "senha"
    private const val preferenceESPORTE = "esporte"
    /**
     * Método para criar preferências compartilhadas na memória interna do device
     * @param context Contexto
     * @return SharedPreferences
     */
    private fun preferences(context: Context): SharedPreferences {
        if (preference == null) {
            preference = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        }
        return preference!!
    }
    /**
     * Método para salvar token do usuário na memória do device
     * @param context Contexto
     * @return String
     */

    fun setEmail(context: Context, email: String) {
        preferences(context).edit().putString(preferenceEMAIL, email).apply()
        preferencesEmail = email
    }
    fun setSenha(context: Context, login: String) {
        preferences(context).edit().putString(preferenceSENHA, login).apply()
        preferencesSenha = login
    }
    fun setEsporte(context: Context, esporte: String) {
        preferences(context).edit().putString(preferenceESPORTE, esporte).apply()
        preferencesEsporte = esporte
    }
    /**
     * Método para retornar token do usuário na memória do device
     * @param context Contexto
     * @return String
     */
    fun getEmail(context: Context): String? {
        if(preferencesEmail.isNullOrBlank()){
            val email = preferences(context)
                .getString(preferenceEMAIL, null)
            return email
        } else{
            return preferencesEmail
        }
    }
    fun getSenha(context: Context): String? {
        if(preferencesSenha.isNullOrBlank()){
            val senha = preferences(context)
                .getString(preferenceSENHA, null)
            return senha
        } else{
            return preferencesSenha
        }
    }
    fun getEsporte(context: Context): String? {
        if(preferencesEsporte.isNullOrBlank()){
            val esporte = preferences(context)
                .getString(preferenceESPORTE, null)
            return esporte
        } else{
            return preferencesEsporte
        }
    }
}