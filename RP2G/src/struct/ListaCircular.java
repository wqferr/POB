package struct;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ListIterator;

/**
 * @author William Quelho Ferreira
 * Uma lista ordenada em que o último precede o primeiro.
 * Uma chamada a {@link ListIterator#next()} após o último elemento ter sido
 * retornado resulta no iterador voltar ao primeiro elemento. Analogamente,
 * uma chamada a {@link ListIterator#previous()} após o primeiro elemento ter
 * sido retornado, ou em seu estado inicial, resulta no iterador avançar ao
 * último elemento.
 * {@link ListIterator#hasNext()} e {@link ListIterator#hasPrevious()} sempre
 * retornam {@code true}.
 *
 * @param <E> A classe dos elementos da lista.
 */
public class ListaCircular<E> extends AbstractSequentialList<E> implements Serializable {
	
	private static final long serialVersionUID = 606785502187027264L;

	private class No implements Serializable {
		static final long serialVersionUID = 6772146105120541965L;
		
		No ant;
		No prx;
		E val;
		
		No() {
			this.val = null;
			
			this.prx = this;
			this.ant = this;
		}
		
		No(No ant, E val) {
			this.val = val;
			
			this.ant = ant;
			this.prx = ant.prx;
			ant.prx.ant = this;
			ant.prx = this;
			
			ListaCircular.this.tamanho++;
		}
		
		E remover() {
			this.ant.prx = this.prx;
			this.prx.ant = this.ant;
			ListaCircular.this.tamanho--;
			return this.val;
		}
	}
	
	private class Iterador implements ListIterator<E>, Serializable {
		
		private static final long serialVersionUID = -7623555157932589100L;
		
		No atual;
		No ultRetorno;
		int idx;
		
		Iterador() {
			this.atual = cabeca;
			this.idx = -1;
			this.ultRetorno = ListaCircular.this.cabeca;
		}

		@Override
		public void add(E e) {
			if (e == null)
				throw new NullPointerException();
			new No(this.atual, e);
			this.ultRetorno = ListaCircular.this.cabeca;
		}

		@Override
		public boolean hasNext() {
			return this.atual.prx.val != null;
		}

		@Override
		public boolean hasPrevious() {
			return this.atual.val != null;
		}

		@Override
		public E next() {
			this.atual = this.atual.prx;
			if (this.atual == ListaCircular.this.cabeca)
                this.atual = this.atual.prx;
			ultRetorno = this.atual;
			this.idx = this.nextIndex();
			return this.atual.val;
		}

		@Override
		public int nextIndex() {
			if (ListaCircular.this.tamanho == 0)
				return -1;
			return (this.idx + 1) % ListaCircular.this.tamanho;
		}

		@Override
		public E previous() {
			if (this.atual == ListaCircular.this.cabeca)
				this.atual = this.atual.ant;
			E val = this.atual.val;
			ultRetorno = this.atual;
			this.atual = this.atual.ant;
			this.idx = this.previousIndex();
			return val;
		}

		@Override
		public int previousIndex() {
			if (ListaCircular.this.tamanho == 0)
				return -1;
			return (this.idx + ListaCircular.this.tamanho - 1) % ListaCircular.this.tamanho;
		}

		@Override
		public void remove() {
			if (this.ultRetorno == ListaCircular.this.cabeca)
				throw new IllegalStateException();
			this.atual = this.ultRetorno.ant;
            this.ultRetorno.remover();
            this.ultRetorno = ListaCircular.this.cabeca;
            if (ListaCircular.this.tamanho == 0)
            	this.idx = -1;
            else
                this.idx = this.idx % ListaCircular.this.tamanho;
		}

		@Override
		public void set(E e) {
			if (e == null)
				throw new NullPointerException();
			if (this.ultRetorno != ListaCircular.this.cabeca)
                this.ultRetorno.val = e;
		}
	
	}
	
	private No cabeca;
	private int tamanho;

	/**
	 * Constrói uma lista circular vazia.
	 */
	public ListaCircular() {
		this.cabeca = new No();
		this.tamanho = 0;
	}
	
	/**
	 * Constrói uma lista circular com os elementos de {@code c}.
	 * Os elementos são adicionados na ordem gerada por {@link Collection#forEach(java.util.function.Consumer)}.
	 * @param c A coleção dos elementos a serem adicionados.
	 */
	public ListaCircular(Collection<? extends E> c) {
		this();
		No n = this.cabeca;
		for (E e : c)
			n = new No(n, e);
		this.tamanho = c.size();
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		if (arg0 < 0 || arg0 > this.tamanho)
			throw new IndexOutOfBoundsException(String.valueOf(arg0));
		ListIterator<E> li = new Iterador();
		while (arg0 > 0) {
			li.next();
			arg0--;
		}
		return li;
	}

	@Override
	public int size() {
		return this.tamanho;
	}
	
	@Override
	public void clear() {
		this.cabeca = new No();
		this.tamanho = 0;
	}

}
