package struct;

import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ListIterator;

public class ListaCircular<E> extends AbstractSequentialList<E> {
	
	private class No {
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
	
	private class Iterador implements ListIterator<E> {
		
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
			new No(this.atual, e);
			this.ultRetorno = ListaCircular.this.cabeca;
		}

		@Override
		public boolean hasNext() {
			return ListaCircular.this.tamanho > 0;
		}

		@Override
		public boolean hasPrevious() {
			return ListaCircular.this.tamanho > 0;
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
			return (this.idx + ListaCircular.this.tamanho - 1) % ListaCircular.this.tamanho;
		}

		@Override
		public void remove() {
			if (this.ultRetorno == ListaCircular.this.cabeca)
				throw new IllegalStateException();
			this.atual = this.ultRetorno.ant;
            this.ultRetorno.remover();
            this.ultRetorno = ListaCircular.this.cabeca;
            this.idx = this.idx % ListaCircular.this.tamanho;
		}

		@Override
		public void set(E e) {
			if (this.ultRetorno != ListaCircular.this.cabeca)
                this.ultRetorno.val = e;
		}
	
	}
	
	private No cabeca;
	private int tamanho;

	public ListaCircular() {
		this.cabeca = new No();
		this.tamanho = 0;
	}
	
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
