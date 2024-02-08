package program.display;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import program.Punto;

public class Ventana extends JFrame {
	private static final long serialVersionUID = 1L;

	private String titulo;

	public Ventana(final String titulo, final Pantalla pantalla) {
		this.titulo = titulo;

		confVent(pantalla);
	}

	private void confVent(final Pantalla pantalla) {
		setTitle(titulo);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new BorderLayout());
		add(pantalla, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void reChan(final byte tam) {
		getContentPane().setPreferredSize(new Dimension(tam * Punto.pac, tam * Punto.pal));
		getContentPane().setSize(new Dimension(tam * Punto.pac, tam * Punto.pal));
		pack();
	}

	public void setNombre(final String s) {
		final String vn = titulo + " || " + s;
		setTitle(vn);
	}
}
