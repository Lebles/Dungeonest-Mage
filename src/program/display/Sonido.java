package program.display;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sonido {
	private final Clip son;
	private final FloatControl floCon;

	public Sonido(final String nombre) {
		son = Loader.LAF("/sounds/" + nombre + ".wav");

		floCon = (FloatControl) son.getControl(FloatControl.Type.MASTER_GAIN);
		floCon.setValue(-30.0F);
	}

	public void play() {
		son.stop();
		son.flush();
		son.setMicrosecondPosition(0);
		son.start();
	}

	public void playInLoop() {
		son.stop();
		son.flush();
		son.setMicrosecondPosition(0);
		son.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public long span() {
		return son.getMicrosecondLength();
	}
}
