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
import net.server.Servidor;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.database.DatabaseHandler;
import core.mapa.Mapa;
import core.mapa.Posicao;
import core.personagem.Personagem;
import exception.DesyncException;

/**
 * Classe que faz a comunicação com o Servidor.
 */
public class Cliente {
	
	private Socket conexao;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	private InetAddress ip;
	private int porta;
	private Controlador controlador;
	private Jogo jogo;
	private int time;
	
	/**
	 * Contrutor padrao do cliente
	 * @param con
	 * @param ip
	 */
	public Cliente(Controlador con, InetAddress ip) {
		this(con, ip, Servidor.PORTA_PADRAO);
	}
	
	/**
	 * Construtor completo do cliente
	 * @param con
	 * @param ip
	 * @param porta
	 */
	public Cliente(Controlador con, InetAddress ip, int porta) {
		this.controlador = con;
		this.ip = ip;
		this.porta = porta;
		this.jogo = null;
	}
	
	/**
	 * Define o controlador.
	 * @param con
	 */
	public void setControlador(Controlador con) {
		this.controlador = con;
	}
	
	/**
	 * Retorna qual time é controlado por este cliente.
	 * @return O time controlado pelo cliente.
	 */
	public int getTime() {
		return this.time;
	}
	
	/**
	 * Estabelece a conexão com um servidor.
	 * @throws IOException Se houver erros na transmissão de dados.
	 */
	public void conectar() throws IOException {
		this.conexao = new Socket(this.ip, this.porta);
		this.conexao.getOutputStream().flush();
		this.out = new ObjectOutputStream(this.conexao.getOutputStream());
		this.out.flush(); // mandar header de inicialização
		
		this.in = new ObjectInputStream(this.conexao.getInputStream());
		Mensagem msg = this.receber();
		if (msg.getEvento() != Evento.INICIO_CONEXAO)
			this.notificarDessincronia();
		
		this.time = Integer.parseInt(msg.getMsg());
		this.notificar(Evento.CONFIRMACAO);
		
		System.err.println("Recebendo database.");
		DatabaseHandler.readAllStream(this.in);
		
		System.err.println("Recebendo jogo.");
		try {
			Mapa m = (Mapa) this.in.readObject();
			@SuppressWarnings("unchecked")
			List<Personagem> p1 = (List<Personagem>) this.in.readObject();
			@SuppressWarnings("unchecked")
			List<Personagem> p2 = (List<Personagem>) this.in.readObject();
			
			jogo = new Jogo(m, p1, p2);
		} catch (ClassNotFoundException e) {}
		try {
			Personagem.D_20.setSeed((long) this.in.readObject());
		} catch (ClassNotFoundException e) {}
		System.err.println("Informações transmitidas com êxito.");
	}
	
	/**
	 * Inicia o loop de jogo.
	 * Deve ser chamado após {@link Cliente#conectar()}.
	 * @throws IOException Se houver erros na transmissão de dados.
	 */
	public void start() throws IOException {
		Mensagem msg;
		while (!jogo.acabou()) {
			msg = this.receber();
			
            Scanner s = new Scanner(msg.getMsg());
            int i, j;
			
			switch (msg.getEvento()) {
                case INICIO_TURNO:
                	Ordem o;
                	do {
                        o = this.controlador.proximaOrdem(jogo);
                        if (jogo.executar(o)) {
                            this.enviar(new Mensagem(o));
                            if (!this.confirmar())
                                this.notificarDessincronia();
                        }
                        if (jogo.acabou()) {
                        	o = new Ordem(Comando.ENCERRAR);
                        	this.enviar(new Mensagem(o));
                        	if (!this.confirmar())
                        		this.notificarDessincronia();
                        }
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
                
                case USO:
                	if (jogo.usar(s.next()))
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
		}
		this.conexao.close();
	}
	
	/**
	 * Retorna o Jogo obtido do servidor.
	 * Se chamado antes de {@link Cliente#conectar()}, retorna null.
	 * @return O estado atual do jogo.
	 */
	public Jogo getJogo() {
		return this.jogo;
	}
	
	private Mensagem receber() throws IOException {
		try {
			Mensagem msg = (Mensagem) this.in.readObject();
			System.err.println("Recebido: " + msg);
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
		this.out.flush();
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
