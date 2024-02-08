package program;

import java.util.Random;

import program.display.Pantalla;
import program.display.Ventana;

public class Punto {
	private final static int ecp = 64;
	public final static int pac = 7 * ecp;
	public final static int pal = 5 * ecp;
	public final static byte aco = 50;
	
	public static boolean debMode = false; // Debugging Mode
	public static Random random = new Random(); // Erramienta del sistema

	private final Pantalla pp;
	private final Ventana ven;

	private final GestorCentral gc;

	private Punto() {
		pp = new Pantalla(pac * 2, pal * 2);
		ven = new Ventana("Dungeonest Mage", pp);
		gc = new GestorCentral(pp.teclado, pp.rat);
	}

	public static void main(String[] args) {
		Punto cen = new Punto();

		cen.iniciar();
	}

	private void mostrar() {
		pp.dibujar(gc);
	}

	private void iniciar() {
		final int nps = 1_000_000_000;
		final int npa = nps / aco;

		int delta = 0;
		long fibu = 0;

		long reco = System.nanoTime();
		long reac = reco;

		while (true) {
			long naac = System.nanoTime();

			while (naac - reco >= npa) {
				long inbu = System.nanoTime();

				mostrar();

				reco += npa;

				fibu += System.nanoTime() - inbu;
				delta++;
			}

			// if (delta >= 2) {
			// System.out.println("mal");
			// }

			if (naac >= reac + nps) {
				float most = (float) (100 - (((float) (((fibu / delta) * 10000) / npa))) / 100);

				ven.setNombre("Nanos disponibles: " + most + "%");

				fibu = 0;
				delta = 0;

				reac = naac;
			}
		}
	}
	
	private static long asignatorUID = 0;
	public static long asignateUID() {
		asignatorUID++;
		return asignatorUID;
	}
}
