package jmeTest;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Texture;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import myAnimation.CengCeng2;
import myAnimation.Cengceng;
import myAnimation.MixBox;
import myAnimation.Mosaic;
import myAnimation.MyBoxNode;
import myAnimation.RotateControl;
import myAnimation.ScaleControl;
import myAnimation.TextureBox;
import myProgram.EBox;
import myProgram.Option;
import myProgram.TestScene;

public class TestProjiection extends SimpleApplication {
	Node frameWork = new Node("frameWork");
	Material redMat, blueMat, defaultLightMat;
	Mosaic mosaic;
	Vector3f camPos = Option.camPos.clone();
	Vector3f screenCenter = new Vector3f(560, 782, 0);
	Vector3f modelPos = new Vector3f(screenCenter.x - 151f, screenCenter.y + 90.5f, 0);
	float movement = 0.5f;
	float viewAng = Option.viewAng;
	float viewAngDelta = 0.05f;
	BitmapText helloText;
	MyBoxNode testBoxNode;

	public static void main(String[] args) {
		TestProjiection app = new TestProjiection();
		app.start();
	}

	public void simpleInitApp() {

		defaultLightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		ColorRGBA color = ColorRGBA.randomColor();
		defaultLightMat.setColor("Diffuse", color);
		defaultLightMat.setColor("Specular", color);
		defaultLightMat.setFloat("Shininess", 32f); // [0,128]
		defaultLightMat.getAdditionalRenderState().setColorWrite(true);
		defaultLightMat.setBoolean("UseMaterialColors", true);

		// setup material
		assetManager.registerLocator("assets", FileLocator.class);
		redMat = assetManager.loadMaterial("Materials/blue.j3m");

		Spatial testCube = assetManager.loadModel("Models/test1/blender2ogre-export.scene");
		rootNode.attachChild(testCube);

		Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		redMat.setColor("Color", ColorRGBA.Red);

		mosaic = new Mosaic(this, modelPos, 302, 180, 302, 0);
		mosaic.setMat(this.defaultLightMat);
		mosaic.iniAnimation();

		 testBoxNode=new TextureBox(this,new Vector3f(600,600,0),300,300,400,0);
		 testBoxNode.setMat(defaultLightMat);
		 testBoxNode.iniAnimation();

		// new ScaleControl(mosaic,0.5f,120);

		flyCam.setMoveSpeed(100);
		flyCam.setDragToRotate(true);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(screenCenter.subtract(camPos).normalizeLocal());
		sun.setColor(ColorRGBA.White.mult(1f));
		rootNode.addLight(sun);

		initKeys();

		guiNode.detachAllChildren();
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		helloText = new BitmapText(guiFont, false);
		helloText.setSize(guiFont.getCharSet().getRenderedSize());
		helloText.setText("Hello World");
		helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
		guiNode.attachChild(helloText);

		drawFramework();

		// cam.setLocation(new Vector3f(0, 0, 0));
		// cam.lookAtDirection(new Vector3f(0f, 0f, 1f), new Vector3f(0f, 1f,
		// 0f));

	}

	public void simpleUpdate(float tpf) {
		if (mosaic != null)
			this.mosaic.updateAnimation();
		if (testBoxNode != null)
			testBoxNode.updateAnimation();
		;
		cam.setLocation(camPos);
		cam.lookAt(screenCenter, new Vector3f(0f, 0f, 1f));
		cam.setFrustumPerspective(viewAng, 1.6f, 100, 6000);
		helloText.setText(" " + camPos.x + " " + camPos.y + " " + camPos.z + " " + viewAng);
	}

	private void initKeys() {
		// You can map one or several inputs to one named action
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("backward", new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("far", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("near", new KeyTrigger(KeyInput.KEY_DOWN));

		// Add the names to the action listener.
		inputManager.addListener(analogListener, "forward", "backward", "up", "down", "right", "left", "far", "near");

	}

	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			Vector3f forward = screenCenter.subtract(camPos).normalize();
			Vector3f right = forward.cross(new Vector3f(0, 0, 1)).normalize();
			Vector3f up = right.cross(forward).normalize();
			if (name.equals("forward")) {
				camPos.addLocal(forward.mult(movement));
			}
			if (name.equals("backward")) {
				camPos.addLocal(forward.mult(-movement));
			}
			if (name.equals("up")) {
				camPos.addLocal(up.mult(movement));
			}
			if (name.equals("down")) {
				camPos.addLocal(up.mult(-movement));
			}
			if (name.equals("right")) {
				camPos.addLocal(right.mult(movement));
			}
			if (name.equals("left")) {
				camPos.addLocal(right.mult(-movement));
			}
			if (name.equals("far")) {
				viewAng += viewAngDelta;
			}
			if (name.equals("near")) {
				viewAng -= viewAngDelta;
			}

		}
	};

	public void drawFramework() {
		Box box = new Box(600, 600, 0);
		Geometry boxGeo = new Geometry("", box);
		Material gray = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		gray.setColor("Color", ColorRGBA.Gray);
		boxGeo.setMaterial(gray);
		boxGeo.move(600, 600, 0);
		Box box2 = new Box(400, 400, 0);
		Geometry boxGeo2 = new Geometry("", box2);
		boxGeo2.move(600, 600, 0.1f);
		Material bw = gray.clone();
		Texture bwT = assetManager.loadTexture("Textures/blackwhite.jpg");
		bw.setTexture("ColorMap", bwT);
		bw.setColor("Color", ColorRGBA.White);

		boxGeo2.setMaterial(bw);
		frameWork.attachChild(boxGeo);
		frameWork.attachChild(boxGeo2);
		rootNode.attachChild(frameWork);
	}
}
