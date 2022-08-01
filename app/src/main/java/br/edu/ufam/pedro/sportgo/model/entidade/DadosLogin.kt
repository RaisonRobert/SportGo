package br.edu.ufam.pedro.sportgo.model.entidade

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DadosLogin(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    var nome : String,
    var email : String,
    var senha : String,
    var deficiente: Boolean
)