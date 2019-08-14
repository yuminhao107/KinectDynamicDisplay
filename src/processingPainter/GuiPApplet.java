package processingPainter;

import java.util.ArrayList;

import igeo.IVec;
import myProgram.IPoint;
import myProgram.SceneReader;
import processing.core.PApplet;
import processing.core.PGraphics;

public class GuiPApplet extends JmePApplet {

	PGraphics activeGraphics;
	SceneReader sceneReader;
	ArrayList<IVec> target = new ArrayList<IVec>();
	float r = 100;
	float max = 4;

	public GuiPApplet() {
		this(300, 200);
	}

	public GuiPApplet(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public GuiPApplet(int w, int h, SceneReader sceneReader) {

		this.w = w;
		this.h = h;
		this.sceneReader = sceneReader;
	}

	public void setup() {
		size(w, h);
		frameRate(-1);
		activeGraphics = createGraphics(w, h);
		System.out.println("GUI PAPPLET, w = " + getWidth() + " h = " + getHeight());
		 noLoop();
		target.add(new IVec(200, 200, 0));
		target.add(new IVec(200, 1000, 0));
		target.add(new IVec(1000, 1000, 0));
		target.add(new IVec(1000, 200, 0));
		target.add(new IVec(600, 600, 0));
		target.add(new IVec(1000, 600, 0));
		target.add(new IVec(200, 600, 0));
		target.add(new IVec(600, 200, 0));
		target.add(new IVec(600, 1000, 0));
		target.add(new IVec(1000, 300, 0));
		activeGraphics.beginDraw();
		activeGraphics.background(100, 70, 45, 10);
		activeGraphics.endDraw();


	}

	public void draw() {
		
		
		// image(activeGraphics, 0, 0);
	}

	@Override
	public PGraphics getActivePGraphics() {
		if (sceneReader.getGeo()==null) return activeGraphics;
		IVec[] res = new IVec[target.size()];
		double sum = 0;
		for (int i = 0; i < target.size(); i++) {
			res[i] = sceneReader.nearDepth(target.get(i));
			sum += res[i].z();
		}
		sum /= res.length;
		activeGraphics.beginDraw();
		activeGraphics.clear();
		activeGraphics.fill(255);
		activeGraphics.rect(0,0,1200,1200);
		activeGraphics.background(100, 70, 45, 10);
		for (int i = 0; i < target.size(); i++) {
			float x = (float) target.get(i).x();
			float y = (float) target.get(i).y();
			float z = (float) (res[i].z() - sum);
			float ang = z / max;
			if (ang > 1)
				ang = 1;
			if (ang < -1)
				ang = -1;
			if (ang > 0) {
				activeGraphics.fill(255, 255*(1-ang), 0);
			} else {
				activeGraphics.fill(255+255*ang, 255, 0);
			}
			activeGraphics.ellipse(x, y, r, r);

		}
		activeGraphics.textSize(24);
		activeGraphics.fill(255);
		for (int i=0;i<424;i+=20)
			for (int j=0;j<512;j+=20){
				IPoint pt=sceneReader.getGeo().pts[i][j];
				if (pt.pos()==null) continue;
				int dist=(int)(pt.pos().z());
				int x=(int)(pt.pos().x());
				int y=(int)(pt.pos().y());
				
				String st=""+dist;
				activeGraphics.text(st,x,y);
			}
		activeGraphics.endDraw();
		return activeGraphics;
	}

	public PGraphics createFullSizeGraphics() {
		return createGraphics(getWidth(), getHeight());
	}

}
