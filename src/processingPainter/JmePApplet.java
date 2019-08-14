package processingPainter;

import processing.core.PApplet;
import processing.core.PGraphics;

public abstract class JmePApplet extends PApplet {
	public int w, h;

	abstract public PGraphics getActivePGraphics();
	abstract public PGraphics createFullSizeGraphics();

}
