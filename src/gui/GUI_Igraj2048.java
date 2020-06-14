/**
 * Sadrzi elemente potrebne za pokretanje igrice u grafickom okruzenju.
 */
package gui;

/**
 * Klasa koja sluzi za pokretanje igrice u grafickom okruzenju.
 * @author Adisa Bolic
 *
 */
public class GUI_Igraj2048 {
	
	/**
	 * Pravi se instanca grafickog okruzenja igrice te se poziva funkcija prikazi() klase Panel2048 koja prikazuje sav sadrzaj igrice.
	 * @param args
	 */
	public static void main(String[] args) {
		Panel2048 igra_pane = new Panel2048();
		igra_pane.prikazi();
	}
}
