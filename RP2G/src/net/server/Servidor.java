package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import net.Mensagem;
import net.Mensagem.Evento;
import core.Jogo;
import core.database.DatabaseHandler;
import core.mapa.Posicao;
import core.personagem.Personagem;
import exception.DesyncException;

/**
 * Classe que faz a comunicação com o Cliente.
 */
public class Servidor {
	
	public static final int PORTA_PADRAO = 4479;
	
	private ServerSocket ss;
	private TratadorCliente[] clientes;
	private int porta;
	
	private Jogo jogo;
	private Random rng;
	
	/**
	 * Cria um novo servidor que rodará o jogo dado.
	 * @param jogo O jogo a ser utilizado.
	 */
	public Servidor(Jogo jogo, Random rng) {
		this(jogo, PORTA_PADRAO, rng);
	}
	
	/**
	 * Cria um novo servidor que rodará o jogo dado.
	 * @param jogo O jogo a ser utilizado.
	 * @param porta A porta de rede a ser utilizada.
	 */
	public Servidor(Jogo jogo, int porta, Random rng) {
		this.clientes = new TratadorCliente[Jogo.NRO_JOGADORES];
		this.jogo = jogo;
		this.porta = porta;
		this.rng = rng;
	}
	
	/**
	 * Conecta aos clientes e inicia o loop de jogo.
	 * @throws DesyncException Se houver dessincronia entre os clientes e o servidor.
	 * @throws IOException Se houver erros na transmissão de dados.
	 */
	public void start() throws DesyncException, IOException {
        this.ss = new ServerSocket(this.porta);
		int i = 0;
		System.err.println("Abrindo servidor.");
		while (i < this.clientes.length) {
			try {
				Socket s = this.ss.accept();
                System.err.println("Conexão nova com " + s.getInetAddress() + ";");
                this.clientes[i] = new TratadorCliente(s, i);
                i++;
			} catch(IOException e) {
				System.err.println("ERRO");
			}
		}
		System.err.println("Clientes conectados.");
		
		i = 1;
		for (TratadorCliente tc : this.clientes) {
            tc.enviar(new Mensagem(Evento.INICIO_CONEXAO, String.valueOf(i)));
            i++;
		}
		
		boolean conf = false;
		try {
            conf = this.confirmarTodos();
		} catch (DesyncException e) {
			this.notificarDessincronia();
		} catch (IOException e) {
			this.notificarQueda();
		}
		
		if (!conf)
			this.notificarDessincronia();
		
		this.enviar(rng);
		Personagem.D_20.setSeed(rng.nextLong());
	
		System.err.println("Enviando database.");
		for (TratadorCliente tc : this.clientes)
			DatabaseHandler.writeToStream(tc.getObjectOutputStream());
		
		System.err.println("Enviando informações do jogo.");
		try {
            this.enviar(this.jogo.getMapa());
            this.enviar(this.jogo.getPersonagensTime1());
            this.enviar(this.jogo.getPersonagensTime2());
		} catch (IOException e) {
			this.notificarQueda();
		}
		
		System.err.println("Informações transmitidas com êxito.");
		System.err.println("Iniciando jogo.");
		
		int vez = 0;
		Mensagem msg = null;
		
		this.clientes[0].notificar(Evento.INICIO_TURNO);
		boolean acabou = false;
		
		do {
			try {
                msg = this.clientes[vez].receber();
			} catch (IOException e) {
				this.notificarQueda();
			}
            
			Scanner s = new Scanner(msg.getMsg());
			int j;
			
			switch (msg.getEvento()) {
                case MOVIMENTO:
                	i = s.nextInt();
                	j = s.nextInt();
                	
                	if (this.jogo.mover(new Posicao(i, j))) {
                        this.sinalizarTodosExceto(msg, vez);
                        conf = false;
                        try {
                            conf = this.confirmarTodosExceto(vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else
                            this.notificarDessincronia();
                    } else {
                        this.notificarDessincronia();
                    }
                        
                    break;
                    
                case ATAQUE:
                	i = s.nextInt();
                	j = s.nextInt();
                	
                	if (this.jogo.atacar(new Posicao(i, j))) {
                        this.sinalizarTodosExceto(msg, vez);
                        conf = false;
                        try {
                            conf = this.confirmarTodosExceto(vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else
                            this.notificarDessincronia();
                    } else {
                        this.notificarDessincronia();
                    }
                	
                    break;
                    
                case USO:
                	if (this.jogo.usar(msg.getMsg())) {
                		this.sinalizarTodosExceto(msg, vez);
                        conf = false;
                        try {
                            conf = this.confirmarTodosExceto(vez);
                        } catch (DesyncException e) {
                        	this.notificarDessincronia();
                        } catch (IOException e) {
                        	this.notificarQueda();
                        }
                        if (conf)
                            this.clientes[vez].notificar(Evento.CONFIRMACAO);
                        else
                            this.notificarDessincronia();
                    } else {
                        this.notificarDessincronia();
                    }
                	
                	break;
                    
                case FIM_TURNO:
                	this.clientes[vez].notificar(Evento.CONFIRMACAO);
                	this.jogo.proximoPersonagem();
                	this.notificarTodosExceto(Evento.FIM_TURNO, vez);
                	vez = (vez+1) % this.clientes.length;
                	this.clientes[vez].notificar(Evento.INICIO_TURNO);
                	acabou = jogo.acabou();
                    break;
                    
                default:
                	// Pacote fora do protocolo
                	this.notificarDessincronia();
                    break;
			}
			s.close();
		} while (!acabou);
		
		for (TratadorCliente tc : this.clientes)
			tc.close();
		
		this.ss.close();
	}
	
	private void enviar(Object obj) throws IOException {
		for (TratadorCliente tc : this.clientes)
			tc.enviar(obj);
	}
	
	private void notificarTodosExceto(Evento e, int c) throws IOException {
		this.sinalizarTodosExceto(new Mensagem(e), c);
	}
	
	private void sinalizarTodosExceto(Mensagem m, int c) throws IOException {
		for (int i = 0; i < c; i++)
			this.clientes[i].enviar(m);
		for (int i = c+1; i < this.clientes.length; i++)
			this.clientes[i].enviar(m);
	}
	
	private boolean confirmarTodos() throws DesyncException, IOException {
		boolean confirmado = true;
		for (int i = 0; i < this.clientes.length; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
			
		return confirmado;
	}
	
	private boolean confirmarTodosExceto(int c) throws DesyncException, IOException {
		boolean confirmado = true;
		for (int i = 0; i < c; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
		for (int i = c+1; i < this.clientes.length; i++) {
			Mensagem m = this.clientes[i].receber();
			if (m.getEvento() != Evento.CONFIRMACAO) {
				confirmado = false;
                if (m.getEvento() == Evento.DESSINCRONIA)
                    throw new DesyncException();
			}
		}
		return confirmado;
	}
	
	private void notificarTodos(Evento e, boolean ignorarExcecao) throws IOException {
		for (TratadorCliente tc : this.clientes) {
			try {
				tc.notificar(e);
			} catch (IOException exc) {
				if (!ignorarExcecao)
					throw exc;
			}
		}
	}
	
	private void notificarDessincronia() throws DesyncException, IOException {
		this.notificarTodos(Evento.DESSINCRONIA, true);
		
		throw new DesyncException("Dessincronia detectada.");
	}
	
	private void notificarQueda() throws IOException {
		this.notificarTodos(Evento.QUEDA_CONEXAO, true);
		
		throw new IOException("Queda de um ou mais clientes.");
	}
	
}
