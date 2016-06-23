package net.client;

import core.Jogo;
import core.Ordem;

/**
 * Interface para controlar o cliente.
 * O método {@link Controlador#proximaOrdem(Jogo)} deve retornar a ordem a ser executada,
 * e é chamado em loop até que:
 * 	1) O jogo acabe; ou
 *  2) Uma ordem de comando ENCERRAR seja retornada.
 */
@FunctionalInterface
public interface Controlador {

	public Ordem proximaOrdem(Jogo j);
	
}
