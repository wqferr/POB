package net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import net.Mensagem;
import net.Mensagem.Evento;
import net.client.Comando.Ordem;
import net.server.Servidor;
import core.Jogo;
import core.mapa.Posicao;
import exception.DesyncException;

public class Cliente {
	
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private Controlador controlador;
	
	public Cliente(Controlador con) {
		this.controlador = con;
	}
	
	public void conectar(InetAddress ip) throws IOException {
		this.conectar(ip, Servidor.PORTA_PADRAO);
	}

	public void conectar(InetAddress ip, int porta) throws IOException {
		this.conexao = new Socket(ip, porta);
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		Mensagem msg = this.receber();
		if (msg.getEvento() != Evento.INICIO_CONEXAO)
			this.notificarDessincronia();
		this.notificar(Evento.CONFIRMACAO);
		Jogo jogo = null;
		try {
			jogo = (Jogo) this.in.readObject();
		} catch (ClassNotFoundException e) {}
		// TODO receber itens
		// TODO receber personagens
		boolean acabou = false;
		
		while (!acabou) {
			msg = this.receber();
			
            Scanner s = new Scanner(msg.getMsg());
            int i, j;
			
			switch (msg.getEvento()) {
                case INICIO_TURNO:
                	Comando c;
                	do {
                        c = this.controlador.proximoComando();
                        this.enviar(c.empacotar());
                        if (!this.confirmar())
                        	this.notificarDessincronia();
                	} while (c.getOrdem() != Ordem.ENCERRAR);
                    break;
                    
                case MOVIMENTO:
                	i = s.nextInt();
                	j = s.nextInt();
                	if (jogo.mover(new Posicao(i, j)))
                		this.notificar(Evento.CONFIRMACAO);
                	else
                		this.notificarDessincronia();
                    break;
                    
                case ATAQUE:
                	i = s.nextInt();
                	j = s.nextInt();
                	if (jogo.atacar(new Posicao(i, j)))
                		this.notificar(Evento.CONFIRMACAO);
                	else
                		this.notificarDessincronia();
                	// TODO verificar se jogo acabou
                    break;
                    
                case FIM_TURNO:
                	jogo.proximoPersonagem();
                    break;
                    
                case QUEDA_CONEXAO:
                	this.notificarQueda();
                    break;
                    
                default:
                	// mensagem fora do protocolo
                	this.notificarDessincronia();
                    break;
			}
			s.close();
		}
		this.conexao.close();
	}
	
	private Mensagem receber() throws IOException {
		try {
			return (Mensagem) this.in.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	private boolean confirmar() throws DesyncException, IOException {
		Mensagem m = this.receber();
		if (m.getEvento() == Evento.DESSINCRONIA)
			throw new DesyncException();
		return m.getEvento() == Evento.CONFIRMACAO;
	}
	
	private void enviar(Mensagem m) throws IOException {
		this.out.writeObject(m);
	}
	
	private void notificar(Evento e) throws IOException {
		this.enviar(new Mensagem(e));
	}
	
	private void notificarDessincronia() throws DesyncException, IOException {
		this.notificar(Evento.DESSINCRONIA);
		throw new DesyncException();
	}
	
	private void notificarQueda() throws IOException {
		throw new IOException("Queda de conexão");
	}

}
