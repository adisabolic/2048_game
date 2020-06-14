/**
 * Sadrzi sve logicke elemente igrice.
 */
package logika;

import java.util.Random;

//  0
// 2 1
//  3

/**
 * Ovo je klasa koja sadrži svu logiku iza igre 2048.
 * @author Adisa Bolic
 *
 */

public class Igra2048 {
	
	public static final int DIREKCIJA_GORE = 0;
	public static final int DIREKCIJA_LIJEVO = 2;
	public static final int DIREKCIJA_DESNO = 1;
	public static final int DIREKCIJA_DOLE = 3;
	
	/**
	 * Atribut oznacava broj preostalih slobodnih (praznih) polja u tabeli.
	 */
	public int broj_slobodnih;
	
	/**
	 * Klasa koja se koristi za nasumicno generisanje polja (sa vrijednosti 2 ili 4) koje se dodaje nakon svakog poteza.
	 */
	Random rnd;
	
	/**
	 * Tabela cuva trenutne numericke vrijednosti polja (prazno polje ima vrijednost 0).
	 */
	int[][] tabela;
	
	/**
	 * Atribut govori da li je igrac pobijedio (uspio dobiti polje sa brojem 2048).
	 */
	boolean pobjeda; 
	
	/**
	 * Atribut govori da li je igrac nakon pobjede odlucio nastaviti igru. Koristi se kako se ne bi uvijek pojavljivala poruka 
	 * da je igrac pobijedio sa pitanjem da li zeli igrati ispocetka ili nastaviti igru.
	 */
	boolean igrajPoslijePobjede;
	
	/**
	 * Konstrukor bez parametara koji incijalizira tabelu (4x4) na sve nule, postavlja broj slobodnih polja na 16 te postavlja ostale
	 * atribute na njihove podrazumijevane vrijednosti na pocetku igre. Generisu se dva polja (sadrze ili 2 ili 4).
	 */
	public Igra2048() {
		tabela = new int[4][4];
		broj_slobodnih = 16;
		rnd = new Random();
		pobjeda = false;
		igrajPoslijePobjede = false;
		
		generisiPolje();
		generisiPolje();
	}
	
	/**
	 * Metoda generise nasumicno broj izmedju 0 i broja slobodnih polja, a zatim se slobodno polje sa tim rednim brojem
	 * nasumicno postavlja ili na vrijednost 2 ili 4 (sa omjerom vjerovatnoca 3:1). Takodjer se umanji broj slobodnih polja za 1.
	 */
	private void generisiPolje() {
		int polje = rnd.nextInt(broj_slobodnih);
		int vrijednost = rnd.nextInt(4);
		if(vrijednost == 0)
			vrijednost = 4;
		else
			vrijednost = 2;
		
		int brojac = 0;
		for(int i = 0 ; i < 4 ; i++)
			for(int j = 0 ; j < 4 ; j++) {
				if(tabela[i][j] == 0) {
					if(brojac == polje)
						tabela[i][j] = vrijednost;
					brojac++;
				}
			}
		broj_slobodnih--;
	}
	
	/**
	 * Funkcija poziva funkcije pomjeri() i spoji(), a zatim ukoliko je bilo nekih promjena tabeli u nekoj od te dvije funkcije,
	 * ponovo poziva funkciju pomjeri(). Na ovaj nacin se uradi jedan validan potez pomjeranja svih polja
	 * u odgovarajucoj direkciji te spajanja eventualnih jednakih susjednih polja. Na kraju se poziva funkcija
	 * generisiPolje() koja nasumicno dodaje novo polje sa vrijednoscu 2 ili 4.
	 * Ukoliko nije bilo promjena u potezu, ne generise se novo polje.
	 * @param direkcija Direkcija moze biti 0 - gore, 1 - desno, 2 - lijevo, 3 - dole
	 */
	public void uradiPotez(int direkcija) {
		boolean promjena1 = pomjeri(direkcija);
		boolean promjena2 = spoji(direkcija);
		if(promjena1 || promjena2) { //ukoliko nije bilo promjene ni u pomjeranju ni spajanju, ne generise novo polje
			pomjeri(direkcija);
			generisiPolje();
		}
	}
		
	/**
	 * Funkcija pomjera sva polja u smjeru direkcije. Pomjeranje se razdvaja u dva slucaja (horizontalno i vertikalno). Iz
	 * vrijednosti parametra direkcije izracuna se pocetno polje (ako je npr. pomjeranje ulijevo, pocetno polje ce biti zadnje
	 * polje u redu), pomak (u slucaju pomjeranja ulijevo, pomak je -1) te trenutnaPozicija koja oznacava prvo
	 * prazno polje pocev od pocetnog na koje se moze pomjeriti iduce neprazno polje.
	 * @param direkcija Direkcija moze biti 0 - gore, 1 - desno, 2 - lijevo, 3 - dole
	 * @return Vraca true ako se desila neka promjena u tabeli nakon pomjeranja, a u suprotnom vraca false.
	 */
	private boolean pomjeri(int direkcija) {	
		/**
		 * Atribut predstavlja zadnje polje u smjeru direkcije u redu ili koloni (zavisno od toga da li je
		 * pomjeranje horizontalno ili vertikalno).
		 */
		int pocetna;
		/**
		 * Atribut govori u kojem smjeru se pomjera trenutnaPozicija (smjer je suprotan direkciji poteza).
		 */
		int pomak;
		/**
		 * Atribut predstavlja prvo prazno polje na koje se moze pomjeriti iduce neprazno polje.
		 */
		int trenutnaPozicija;
		/**
		 * Atribut se postavlja na true ukoliko dodje do nekog pomjeranja (promjene u odnosu na prijasnje stanje tabele).
		 */
		boolean bilo_promjene = false;
		
		// pomjeranje desno i lijevo
		if(direkcija == 1 || direkcija == 2) {
			pocetna = 6 - 3*direkcija; // pocetna je ili 3 ili 0
			pomak = 2*direkcija - 3; // pomak je ili -1 ili 1
			trenutnaPozicija = pocetna;
			
			// ovaj dio vrsi samo pomjeranje polja
			for(int i = 0 ; i < 4 ; i++) {
				for(int j = pocetna ; j != pocetna + 4 * pomak ; j += pomak) {
					if(tabela[i][trenutnaPozicija] != 0) // ukoliko se na pocetku desi da to polje nije slobodno
						trenutnaPozicija += pomak;
					else if (tabela[i][j] != 0) { // pomjeri to polje u smjeru direkcija
						tabela[i][trenutnaPozicija] = tabela[i][j];
						tabela[i][j] = 0;
						trenutnaPozicija += pomak;
						bilo_promjene = true;
					}
				}
				trenutnaPozicija = pocetna;
			}
		}
		
		// pomjeranje gore i dole
		else {
			pocetna = direkcija; // pocetna je ili 3 ili 0
			pomak = 1 - (2*direkcija)/3; // pomak je ili -1 ili 1
			trenutnaPozicija = pocetna;
			
			// ovaj dio vrsi samo pomjeranje polja
			for(int j = 0 ; j < 4 ; j++) {
				for(int i = pocetna ; i != pocetna + 4 * pomak ; i += pomak) {
					if(tabela[trenutnaPozicija][j] != 0) // ukoliko se na pocetku desi da to polje nije slobodno
						trenutnaPozicija += pomak;
					else if (tabela[i][j] != 0) { // pomjeri to polje u smjeru direkcija
						tabela[trenutnaPozicija][j] = tabela[i][j];
						tabela[i][j] = 0;
						trenutnaPozicija += pomak;
						bilo_promjene = true;
					}
				}
				trenutnaPozicija = pocetna;
			}
		}
		return bilo_promjene;
	}
	/**
	 * Funkcija radi slicno kao i funkcija pomjeri(), samo umjesto pomjeranja vrsi spajanje susjednih polja nakon pomjeranja.
	 * Varijable pocetna, pomak i bilo_promjene imaju isto znacenje kao i u funkciji pomjeri(). 
	 * Varijabla spojeno sluzi da se pri spajanju obezbijedi da se ne spaja neko novonostalo polje (npr. ako 
	 * se cetiri dvice pomjere u stranu, ne treba da nastane jedna 8, vec dvije 4). Azurira se i atribut
	 * broj slobodnih polja ukoliko dodje do spajanja te ukoliko nastane broj 2048, atribut pobjeda
	 * se postavlja na true.
	 * @param Direkcija Direkcija moze biti 0 - gore, 1 - desno, 2 - lijevo, 3 - dole
	 * @return Vraca true ako se desila neka promjena u tabeli nakon pomjeranja, a u suprotnom vraca false.
	 */
	private boolean spoji(int direkcija) {
		int pocetna;
		int pomak;
		boolean spojeno = false;
		boolean bilo_promjene = false;
		
		// pomjeranje desno i lijevo
		if(direkcija == 1 || direkcija == 2) {
			pocetna = 6 - 3*direkcija; // pocetna je ili 3 ili 0
			pomak = 2*direkcija - 3; // pomak je ili -1 ili 1
			
			// ovaj dio vrsi samo spajanje polja
			for(int i = 0 ; i < 4 ; i++)
				for(int j = pocetna ; j != pocetna + 3 * pomak ; j += pomak)
					if(spojeno)
						spojeno = false;
					else if(tabela[i][j] != 0 && tabela[i][j] == tabela[i][j + pomak]) {
						tabela[i][j] = 2 * tabela[i][j];
						if(!igrajPoslijePobjede && tabela[i][j] == 8) // ako je igrac pobijedio (po prvi put, tj. nije bio nastavak igre)
							pobjeda = true;
						tabela[i][j + pomak] = 0;
						spojeno = true;
						bilo_promjene = true;
						broj_slobodnih++;
					}
		}
		
		// pomjeranje gore i dole
		else {
			pocetna = direkcija; // pocetna je ili 3 ili 0
			pomak = 1 - (2*direkcija)/3; // pomak je ili -1 ili 1
			
			// ovaj dio vrsi samo spajanje polja
			for(int j = 0 ; j < 4 ; j++) {
				for(int i = pocetna ; i != pocetna + 3 * pomak ; i += pomak)
					if(spojeno)
						spojeno = false;
					else if(tabela[i][j] != 0 && tabela[i][j] == tabela[i + pomak][j]) {
						tabela[i][j] = 2 * tabela[i][j];
						
						if(!igrajPoslijePobjede && tabela[i][j] == 8) // ako je igrac pobijedio (po prvi put, tj. nije bio nastavak igre)
							pobjeda = true;
						tabela[i + pomak][j] = 0;
						spojeno = true;
						bilo_promjene = true;
						broj_slobodnih++;
					}
			}
		}
		return bilo_promjene;
	}
	
	/**
	 * Funkcija provjerava da li se ikoja dva polja mogu spojiti. Ova funkcija sluzi za provjeru da li je igrac izgubio igru.
	 * @return Vraca true ukoliko se neka dva polja mogu spojiti (u bilo kojoj direkciji). U suprotnom vraca false.
	 */
	private boolean mozeLiSeSpojiti() {
		// provjera po redovima
		for(int i = 0 ; i < 4 ; i++)
			for(int j = 0 ; j < 3 ; j++)
				if(tabela[i][j] == tabela[i][j + 1])
					return true;
		// provjera po kolonama
		for(int j = 0 ; j < 4 ; j++)
			for(int i = 0 ; i < 3 ; i++)
				if(tabela[i][j] == tabela[i + 1][j])
					return true;
		return false;
	}
	
	/**
	 * Ukoliko se nikoja dva polja ne mogu spojiti te je broj slobodnih polja 0, igrac je izgubio.
	 * @return Vraca true ako je igra zavrsila, tj. ako je igrac izgubio. U suprotnom vraca false.
	 */
	public boolean kraj() {
		return (broj_slobodnih == 0 && !mozeLiSeSpojiti());
	}
	
	/**
	 * Ukoliko je atribut pobjeda jednak true, te igrac nije odlucio nastaviti igrati, funkcija vraca true.
	 * @return Vraca da li je igrac pobijedio (samo prvi put kada dobije broj 2048)
	 */
	public boolean pobjeda() {
		return !igrajPoslijePobjede && pobjeda;	
	}
	
	/**
	 * Ukoliko igrac odluci nastaviti igru nakon pobjede, postavlja atribut igrajPoslijePobjede na true.
	 */
	public void nastaviIgru() {
		// nastavi igru poslije pobjede
		igrajPoslijePobjede = true;
	}
	
	/**
	 * Resetuje atribute klase ukoliko igrac odluci igrati ispocetka. Generise dva polja kao i na pocetku igre.
	 */
	public void novaIgra() {
		broj_slobodnih = 16;
		for(int i = 0 ; i < 4 ; i++)
			for(int j = 0 ; j < 4 ; j++)
				tabela[i][j] = 0;
		pobjeda = false;
		igrajPoslijePobjede = false;
		generisiPolje();
		generisiPolje();
	}
	
	/**
	 * 
	 * @return Funkcija vraca kopiju tabele koja cuva numericke vrijednosti polja.
	 */
	public int[][] stanjeTabele() {
		int[][] temp_tabela = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				temp_tabela[i][j] = tabela[i][j];
			}
		}

		return temp_tabela;
	}
	
	/**
	 * Pomocna funkcija koja racuna logaritam po bazi dva broja x (koristi se u GUI radi bojanja polja)
	 * @param x Podrazumijeva se da je x stepen dvice
	 * @return Vraca log(2, x)
	 */
	static public int logaritam2(int x) {
		int lg = 0;
		while(x != 1) {
			x /= 2;
			lg++;
		}
		return lg;
	}
}
