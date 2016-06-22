package net.client;

import core.Jogo;
import core.Ordem;

@FunctionalInterface
public interface Controlador {

	public Ordem proximaOrdem(Jogo j);
	
}
