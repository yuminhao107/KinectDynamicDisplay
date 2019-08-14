package processingPainter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import jme3tools.converters.ImageToAwt;
import processing.core.PApplet;
import processing.core.PGraphics;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

public class PAppletAdapter extends AbstractControl {

	JmePApplet papplet;
	BufferedImage bimage;
	Graphics graphics;
	float fps = 24;
	float currentFrametime = 0;
	float frameTime;
	PGraphics pg;
	java.awt.Image image;
	com.jme3.texture.Image.Format format;
	com.jme3.texture.Image jmeImage;
	Texture texture;

	ByteBuffer bb;

	public PAppletAdapter(JmePApplet papplet, BufferedImage bimage, Texture texture) {
		this.papplet = papplet;
		this.bimage = bimage;
		this.texture = texture;
		frameTime = 1f / fps;
		int colorComponents = 4;
		jmeImage = texture.getImage();
		int bufferSize = colorComponents * papplet.w * papplet.h;
		bb = BufferUtils.createByteBuffer(bufferSize);
		jmeImage = texture.getImage();
		format = jmeImage.getFormat();
	}

	@Override
	protected void controlUpdate(float tpf) {
		currentFrametime += tpf;
		if (currentFrametime > frameTime) {
			long time=System.currentTimeMillis();
			currentFrametime = currentFrametime % frameTime;
			if (pg==null) pg = papplet.getActivePGraphics();
			image = (java.awt.Image) pg.getNative();
			graphics = bimage.getGraphics();
			graphics.drawImage(image, 0, 0, null);
			bb.rewind();
			int bimageSize = bimage.getWidth() * bimage.getHeight() * 4;
			ImageToAwt.convert(bimage, format, bb);
			jmeImage.setData(bb);
			texture.setImage(jmeImage);
			time=System.currentTimeMillis()-time;
			System.out.println("cost "+time/1000f+" s");
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub

	}

}

