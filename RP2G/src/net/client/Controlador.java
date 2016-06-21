package net.client;

import core.Jogo;

@FunctionalInterface
public interface Controlador {

	public Ordem proximaOrdem(Jogo j);
	
}
