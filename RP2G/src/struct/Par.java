package struct;

public class Par<U, V> {
	
	private U v1;
	private V v2;

	public Par(U v1, V v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public U getV1() {
		return this.v1;
	}
	
	public V getV2() {
		return this.v2;
	}

}
