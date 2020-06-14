/**
 * Sadrzi elemente potrebne za igranje igrice u konzoli.
 */
package konzola;

import java.util.Scanner;

import logika.Igra2048;

/**
 * Klasa koja sluzi za igranje igrice u konzoli. 
 * @author Adisa Bolic
 *
 */
public class Konzola_Igraj2048 {
	
	/**
	 * Trazi od igraca da unosi poteze sve dok pobijedi (u tom slucaju
	 * mu se nudi opcije nastavka igranja ili igranja ispocetka) ili izgubi (u tom slucaju mu se samo nudi opcija
	 * igranja ispocetka).
	 * Izmedju svaka dva poteza se prikazuje trenutno stanje tabele.
	 * @param args
	 */
	public static void main(String[] args) {
		Igra2048 igra = new Igra2048();
		Scanner ulaz = new Scanner(System.in);
		int potez;
		char nova_igra, nastavak;
		prikaziTabelu(igra);
		
		while(!igra.kraj()) {
			System.out.println("Unesite potez: (8 - gore, 6 - desno, 4 - lijevo, 2 - dole)");
			potez = ulaz.nextInt();
			if(potez == 8)
				igra.uradiPotez(Igra2048.DIREKCIJA_GORE);
			else if(potez == 6)
				igra.uradiPotez(Igra2048.DIREKCIJA_DESNO);
			else if(potez == 4)
				igra.uradiPotez(Igra2048.DIREKCIJA_LIJEVO);
			else if(potez == 2)
				igra.uradiPotez(Igra2048.DIREKCIJA_DOLE);
			else
				System.out.println("Unesite validan potez!");
			prikaziTabelu(igra);
			if(igra.pobjeda()) {
				System.out.println("Pobijedili ste :)");
				System.out.println("Zelite li nastaviti igrati, iako ste pobijedili? (y/n)");
				nastavak = ulaz.next().charAt(0);
				if(nastavak == 'y' || nastavak == 'Y') {
					igra.nastaviIgru();
					prikaziTabelu(igra);
				} else {
					System.out.println("Zelite li ponovo igrati ispocetka? (y/n)");
					nova_igra = ulaz.next().charAt(0);
					if(nova_igra == 'y' || nova_igra == 'Y') {
						igra.novaIgra();
						prikaziTabelu(igra);
					}					
					else
						break;
				}
			}
		}
		if(igra.kraj()) {
			prikaziTabelu(igra);
			System.out.println("Izgubili ste :(");
		}
		ulaz.close();
	}
	
	/**
	 * Ispisuje tabelu u konzolu, vodi racuna o formatiranju ispisa radi vecih brojeva u tabeli.
	 * @param igra Instanca objekta Igra2048 ciju tabelu ispisuje
	 */
	public static void prikaziTabelu(Igra2048 igra) {
		int[][] tabela = igra.stanjeTabele();
		for (int i = 0 ; i < 4 ; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.printf("%6d ", tabela[i][j]);
			}
			System.out.println();
		}
	}
}
