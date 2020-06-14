/**
 * Sadrzi elemente potrebne za igranje igrice u grafickom okruzenju.
 */
package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import logika.Igra2048;

/**
 * Klasa koja sluzi za graficki prikaz igrice 2048.
 * @author Adisa Bolic
 *
 */
public class Panel2048 {
	/**
	 * Atribut koji cuva instancu objekta Igra2048 (sadrzi logiku igre).
	 */
	Igra2048 igra;
	/**
	 * JFrame koji sadrze sve graficke elemente. Posjeduje GridLayout dimenzije (1, 2).
	 */
	JFrame omotac;
	/**
	 * JPanel koji sadrzi tabelu (predstavljenu kao matricu JButtona) igrice. U njemu se izvrsavaju svi potezi.
	 */
	JPanel prozor_igre;
	
	/**
	 * JFrame koji se pojavi ukoliko igrac pobijedi igru. Nudi se opcija nastavka igre ili igranja ispocetka.
	 */
	JFrame okvir_kraj_igre;
	
	/**
	 * JPanel koji se nalazi pored prozora za igranje koji sadrzi dugmic za igranje ispocetka te kratke instrukcije za igranje.
	 * Takodjer sadrzi odgovarajucu poruku koja se pojavi ukoliko igrac pobijedi ili izgubi igru.
	 */
	JPanel meni;
	/**
	 * Poruka koja se pojavi ako igrac pobijedi ili izgubi.
	 */
	JLabel poruka;
	/**
	 * Matrica polja, koja sa predstavljena kao JButtoni. Tekst dugmica je upravo broj koji se trenutno nalazi u odgovarajucem polju tabele.
	 */
	JButton [][]polje;
	/**
	 * Niz koji sadrzi hes kodove boja polja u zavisnosti od njihove numericke vrijednost. Iznad 2048 sva polja imaju
	 * istu boju (zadnju u nizu).
	 */
	String[] boja_polja; 
	/**
	 * Hes kod boje pozadine prozora za igranje.
	 */
	String boja_pozadine;
	
	/**
	 * Konstruktor bez parametara koji postavlja sve atribute klase na odgovarajuce vrijednosti. Pored inicijalizacije
	 * atributa tipa JFrame, JPanel, JLabel, boja polja i pozadine i postavljanja stilistickih opcija kod ovih atributa,
	 * dodaje KeyListenere i ActionListenere koji sluze za izvrsavanje poteza (pomocu strelica na tastaturi)
	 *  i postavljanja akcija nad dugmicima za nastavak i novu igru.
	 * Na kraju se poziva funkcija osvjezi() koja azurira prozor za igranje na osnovu trenutnog stanja tabele.
	 *
	 */
	public Panel2048() {
		igra = new Igra2048();
		boja_pozadine = "#b8af9e";
		boja_polja = new String[] {"#cdc1b5", "#eee4da", "#ede0c8", "#f2b179", "#f59563", "#f67c5f", 
		              			   "#f65e3b", "#edce71", "#eccc67", "#edc850", "#edc541", "#ecc230", "#413e33"};
		omotac = new JFrame("2048");
		omotac.setLayout(new GridLayout(1, 2, 5, 5));
		omotac.setForeground(Color.decode(boja_pozadine));
		
		prozor_igre = new JPanel();
		prozor_igre.setLayout(new GridLayout(4, 4, 5, 5));
		prozor_igre.setBackground(Color.decode(boja_pozadine));
		
		// kreiranje dijela za igranje
		polje = new JButton[4][4];
		
		KeyListener strelice = new KeyListener() { 
			
			public void keyPressed(KeyEvent e) {
			    int key = e.getKeyCode();
			    if (key == KeyEvent.VK_UP) igra.uradiPotez(Igra2048.DIREKCIJA_GORE);
			    else if (key == KeyEvent.VK_RIGHT) igra.uradiPotez(Igra2048.DIREKCIJA_DESNO);
			    else if (key == KeyEvent.VK_LEFT) igra.uradiPotez(Igra2048.DIREKCIJA_LIJEVO);
			    else if (key == KeyEvent.VK_DOWN) igra.uradiPotez(Igra2048.DIREKCIJA_DOLE);
			    
			    osvjezi();
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent arg0) {}
		};
		
		ActionListener fokus = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				prozor_igre.requestFocusInWindow();
			}
			
		};
		
		prozor_igre.addKeyListener(strelice);
		prozor_igre.setFocusable(true);
		prozor_igre.requestFocus();
		
		for(int i = 0 ; i < 4 ; i++)
			for(int j = 0 ; j < 4 ; j++) {
				polje[i][j] = new JButton("");
				polje[i][j].setForeground(Color.white);
				polje[i][j].setFont(new Font("Arial", Font.BOLD, 30));
				polje[i][j].addActionListener(fokus);
				prozor_igre.add(polje[i][j]);
			}
		
		omotac.add(prozor_igre);
		
		// kreiranje menija sa strane
		meni = new JPanel();
		meni.setLayout(new BoxLayout(meni, BoxLayout.PAGE_AXIS));
		
		ActionListener igraj_opet = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				poruka.setText("");
				okvir_kraj_igre.setVisible(false);
				igra.novaIgra();
				osvjezi();	
			}
		};
		// dugme za novu igru
		JButton nova_igra = new JButton("Zapocni novu igru");
		nova_igra.setAlignmentX(Component.CENTER_ALIGNMENT);
		nova_igra.setFont(new Font("Arial", Font.PLAIN, 32));
		nova_igra.addActionListener(igraj_opet);
		nova_igra.setPreferredSize(new Dimension(50, 50));
		
		// instrukcije za igranje
		JLabel instrukcije = new JLabel();
		instrukcije.setText("<html><div style='text-align: center;'> "
				+ "Igraj 2048! <br /> Strelicama pomjeraj figure gore, lijevo, desno ili dole. Svaki put kada uradiš "
				+ "potez pojavi se novo polje sa brojem 2 ili 4. <br /> "
				+ "Cilj je spajanjem polja istih vrijednosti dobiti broj 2048!"
				+ "</div></html>");
		instrukcije.setAlignmentX(Component.CENTER_ALIGNMENT);
		instrukcije.setFont(new Font("Arial", Font.PLAIN, 20));
		// poruka koja se ispisuje kada igrac pobijedi ili izgubi
		poruka = new JLabel();
		poruka.setAlignmentX(Component.CENTER_ALIGNMENT);
		poruka.setFont(new Font("Arial", Font.PLAIN, 20));
		
		meni.add(Box.createRigidArea(new Dimension(0, 50)));
		meni.add(instrukcije);
		meni.add(Box.createRigidArea(new Dimension(0, 50)));
		meni.add(nova_igra);
		meni.add(Box.createRigidArea(new Dimension(0, 50)));
		meni.add(poruka);
		omotac.add(meni);
		
		// prozor koji se pojavi kada igrac pobijedi
		
		okvir_kraj_igre = new JFrame("Kraj");
		JPanel meni_kraj_igre = new JPanel();
		meni_kraj_igre.setLayout(new BoxLayout(meni_kraj_igre, BoxLayout.PAGE_AXIS));
		
		// dva dugmeta za novu igru ili nastavak igre
		
		JButton kraj_nastavi = new JButton("Nastavi igrati");
		JButton kraj_nova_igra = new JButton("Zapocni novu igru");
		kraj_nastavi.setAlignmentX(Component.CENTER_ALIGNMENT);
		kraj_nastavi.setFont(new Font("Arial", Font.PLAIN, 32));
		kraj_nova_igra.setAlignmentX(Component.CENTER_ALIGNMENT);
		kraj_nova_igra.setFont(new Font("Arial", Font.PLAIN, 32));
		
		meni_kraj_igre.add(Box.createRigidArea(new Dimension(0, 50)));
		meni_kraj_igre.add(kraj_nastavi);
		meni_kraj_igre.add(Box.createRigidArea(new Dimension(0, 50)));
		meni_kraj_igre.add(kraj_nova_igra);
		
		ActionListener nastavi = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				igra.nastaviIgru();
				okvir_kraj_igre.setVisible(false);
				osvjezi();	
			}
		};
		
		kraj_nastavi.addActionListener(nastavi);
		kraj_nova_igra.addActionListener(igraj_opet);
		
		okvir_kraj_igre.add(meni_kraj_igre);
		okvir_kraj_igre.setSize(512, 350);
		okvir_kraj_igre.setLocationRelativeTo(null);	
		
		osvjezi();
	}
	
	/**
	 * Metoda koja sluzi za prikaza glavnog JFrame objekta koji sadrzi sve ostale graficke elemente.
	 */
	public void prikazi() {
		omotac.setSize(1024, 512);
		omotac.setLocationRelativeTo(null);
		omotac.setVisible(true);
		omotac.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Metoda azurira graficke elemente na osnovu trenutnog stanja tabele. U svaki JButton iz matrice polje upisuje
	 * odgovarajucu numericku vrijednost iz tabele. Ukoliko je igrac pobijedio ili izgubio
	 * postavlja odgovarajuci tekst objekta poruka (tipa JLabel) u meniju sa strane te prikazuje JFrame za kraj igre (ukoliko je to slucaj)
	 * gdje igrac bira da li da nastavi igrati ili da pocne ispocetka.
	 */
	public void osvjezi() {
		int[][] tabela = igra.stanjeTabele();
		for(int i = 0; i < 4 ; i++)
			for(int j = 0 ; j < 4 ; j++) {
				if(tabela[i][j] != 0) {
					polje[i][j].setText("" + tabela[i][j]);
					if(tabela[i][j] <= 2048)
						 polje[i][j].setBackground(Color.decode(boja_polja[Igra2048.logaritam2(tabela[i][j])]));
					else
						polje[i][j].setBackground(Color.decode(boja_polja[12]));
				}
				else {
					polje[i][j].setText("");
					polje[i][j].setBackground(Color.decode(boja_polja[0]));
				}
			}
		if(igra.pobjeda()) {
			okvir_kraj_igre.setVisible(true);
			poruka.setText("Pobijedili ste!");
		}
		if(igra.kraj())
			poruka.setText("Izgubili ste! Za novu igru pritisnite dugme iznad.");
	}
}
