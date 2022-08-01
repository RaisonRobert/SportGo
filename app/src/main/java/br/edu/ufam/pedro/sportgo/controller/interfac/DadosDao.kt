package br.edu.ufam.pedro.sportgo.controller.interfac

import androidx.room.*
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin


@Dao
interface DadosDao{

    @Query("SELECT * FROM DadosLogin")
    fun buscarDados(): List<DadosLogin>

    @Insert
    fun salvaDados(vararg dadosUser : DadosLogin)

    @Delete
    fun delete(vararg dadosUser: DadosLogin)

    @Update
    fun alterarDados(dadosUser: DadosLogin)
}