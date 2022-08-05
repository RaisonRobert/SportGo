package br.edu.ufam.pedro.sportgo.controller.interfac

import br.edu.ufam.pedro.sportgo.model.entidade.DadosLocal

interface itemClickListenerCadastro {
    fun itemClick(dado: DadosLocal, position: Int)
}