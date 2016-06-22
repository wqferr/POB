package net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import net.Mensagem;
import net.Mensagem.Evento;
import net.client.Ordem.Comando;
import net.server.Servidor;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.personagem.Personagem;
import exception.DesyncException;

public class Cliente {
	
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private InetAddress ip;
	private int porta;
	private Controlador controlador;
	
	public Cliente(Controlador con, InetAddress ip) {
		this(con, ip, Servidor.PORTA_PADRAO);
	}
	
	public Cliente(Controlador con, InetAddress ip, int porta) {
		this.controlador = con;
		this.ip = ip;
		this.porta = porta;
	}
	
	public void conectar() throws IOException {
		this.conexao = new Socket(this.ip, this.porta);
		this.conexao.getOutputStream().flush();
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		this.out.flush(); // mandar header de inicialização
		
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		Mensagem msg = this.receber();
		if (msg.getEvento() != Evento.INICIO_CONEXAO)
			this.notificarDessincronia();
		this.notificar(Evento.CONFIRMACAO);
		
		System.err.println("Recebendo database.");
		DatabaseHandler.readAllStream(this.in);
		
		System.err.println("Recebendo jogo.");
		Jogo jogo = null;
		try {
			Mapa m = (Mapa) this.in.readObject();
			@SuppressWarnings("unchecked")
			List<Personagem> p1 = (List<Personagem>) this.in.readObject();
			@SuppressWarnings("unchecked")
			List<Personagem> p2 = (List<Personagem>) this.in.readObject();
			
			jogo = new Jogo(m, p1, p2);
		} catch (ClassNotFoundException e) {}
		System.err.println("Informações transmitidas com êxito.");
		
		while (!jogo.acabou()) {
			msg = this.receber();
			
            Scanner s = new Scanner(msg.getMsg());
            int i, j;
			
			switch (msg.getEvento()) {
                case INICIO_TURNO:
                	Ordem o;
                	do {
                        o = this.controlador.proximaOrdem(jogo);
                        this.enviar(o.empacotar());
                        if (!this.confirmar())
                        	this.notificarDessincronia();
                	} while (o.getComando() != Comando.ENCERRAR);
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
			jogo.exibir(System.out::print);
		}
		this.conexao.close();
	}
	
	private Mensagem receber() throws IOException {
		try {
			Mensagem msg = (Mensagem) this.in.readObject();
			System.err.println(msg);
			return msg;
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
