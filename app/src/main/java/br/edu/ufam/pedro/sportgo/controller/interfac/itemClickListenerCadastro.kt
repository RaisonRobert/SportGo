package br.edu.ufam.pedro.sportgo.controller.interfac

import br.edu.ufam.pedro.sportgo.model.entidade.DadosLogin

interface itemClickListenerCadastro {
    fun itemClick(dado: DadosLogin, position: Int)
}