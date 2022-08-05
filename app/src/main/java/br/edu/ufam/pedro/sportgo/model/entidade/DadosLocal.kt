package br.edu.ufam.pedro.sportgo.model.entidade

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DadosLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val foto: String,
    val nomelocal: String,
    val horario: String,
    val linklocal: String,
    val esporte: String,
    val descricao: String
)
