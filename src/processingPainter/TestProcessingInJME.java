package processingPainter;

import java.awt.image.BufferedImage;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;


public class TestProcessingInJME extends SimpleApplication {

	Geometry screen;
	GuiPApplet papplet;
	PAppletAdapter adapter;

	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		TestProcessingInJME app = new TestProcessingInJME();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		// TODO Auto-generated method stub
		int width = this.getViewPort().getCamera().getWidth();
		int height = this.getViewPort().getCamera().getHeight();
		Image image = new Image(Image.Format.ABGR8, width, height, null);
		Texture2D gui = new Texture2D(image);
		Quad quad = new Quad(width, height);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", gui);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		screen = new Geometry("GUI Screen", quad);
		screen.setMaterial(mat);

		papplet = new GuiPApplet(width, height);
		papplet.init();
		papplet.start();

		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		adapter = new PAppletAdapter(papplet, bimage, gui);

		// screen.setLocalTranslation(100, 100, 10);
		screen.addControl(adapter);
		rootNode.attachChild(screen);

	}
}
