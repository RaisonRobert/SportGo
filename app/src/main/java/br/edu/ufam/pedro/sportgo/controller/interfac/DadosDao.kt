package br.edu.ufam.pedro.sportgo.controller.interfac

import androidx.room.*
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal
import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin


@Dao
interface DadosDao{
//    Dados User
    @Query("SELECT * FROM DadosLogin WHERE id = :id")
    fun buscarAdmin(id: Long): DadosLogin?

    @Query("SELECT * FROM DadosLogin")
    fun buscarDados(): List<DadosLogin>

    @Insert
    fun salvaDados(vararg dadosUser : DadosLogin)

    @Delete
    fun delete(vararg dadosUser: DadosLogin)

    @Update
    fun alterarDados(dadosUser: DadosLogin)

// Locais Cadastrado
    @Query("SELECT * FROM DadosLocal")
    fun buscarLocais(): List<DadosLocal>

    @Insert
    fun salvaLocal(vararg dadosLocal : DadosLocal)

}