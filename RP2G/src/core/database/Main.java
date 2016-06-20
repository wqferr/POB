package core.database;

import core.mapa.Mapa;
import core.mapa.Posicao;

public class Main {

	public static void main(String[] args){
		JanelaMain win = new JanelaMain();
		win.setVisible(true);
		
		Mapa m = Mapa.get("Mapa2");
		//System.out.println(m.getNColunas());
		for (int i=0; i<m.getNLinhas(); i++){
			for (int j=0; j<m.getNColunas(); j++){
				if (m.getQuadrado(new Posicao(i, j)).isTransponivel()){
					if (m.getSpawnPoints().contains(new Posicao(i ,j))){
						System.out.printf("2");
					}
					else System.out.printf("1");
				}
				else System.out.printf("0");
			}
			System.out.printf("\n");
		}
	}

}
