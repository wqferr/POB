package struct;

/**
 * Interface que armazena um par de Objetos.
 * @param <U>
 * @param <V>
 */

public class Par<U, V> {
	
	private U v1;
	private V v2;
	
	/**
	 * Construtor padrão
	 * @param v1
	 * @param v2
	 */
	public Par(U v1, V v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	/**
	 * Retorna o primeiro objeto
	 * @return
	 */
	public U getV1() {
		return this.v1;
	}
	
	/**
	 * Retorna o segundo objeto
	 * @return
	 */
	public V getV2() {
		return this.v2;
	}

}
