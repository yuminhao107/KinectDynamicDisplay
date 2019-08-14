package jmeTest;

import java.util.ArrayList;

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
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import myAnimation.MixBox;
import myAnimation.Mosaic;
import myAnimation.MyBox;
import myAnimation.MyBoxNode;
import myAnimation.RotateControl;
import myAnimation.ScaleControl;
import myAnimation.TextureBox;
import myProgram.EBox;
import myProgram.IFace;
import myProgram.IPoint;
import myProgram.Mymath;
import myProgram.Option;
import myProgram.SceneReader;
import myProgram.TestScene;

public class StartMain extends SimpleApplication {
	Node frameWork = new Node("frameWork");
	SceneReader sceneReader;

	Material defaultLightMat, redMat;
	Mosaic mosaic;
	Vector3f camPos = Option.camPos.clone();
	Vector3f screenCenter = Option.screenCenter.clone();
	Vector3f modelPos = new Vector3f(screenCenter.x - 151f, screenCenter.z + 90.5f, 0);
	float movement = 10f;
	float viewAng = Option.viewAng;
	float viewAngDelta = 1f;
	BitmapText helloText;

	ArrayList<MyBoxNode> currentGeo = new ArrayList<MyBoxNode>();

	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		StartMain app = new StartMain();
		app.start();
	}

	public void simpleInitApp() {

		
		defaultLightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		this.setDisplayFps(false);
		this.setDisplayStatView(false);

		// ini assetManager
		assetManager.registerLocator("assets", FileLocator.class);
		redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		redMat.setColor("Color", ColorRGBA.Red);
		
//		drawFramework();

		// ini Light 
		SpotLight sun2=new SpotLight(camPos,screenCenter.subtract(camPos).normalize(),4000f);
		sun2.setSpotInnerAngle(1.3f);
		sun2.setSpotOuterAngle(1.5f);
		sun2.setColor(ColorRGBA.White.mult(2f));
		rootNode.addLight(sun2);
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(Vector3f.UNIT_X.mult(-1));
		sun.setColor(ColorRGBA.White);
		rootNode.addLight(sun);

		// ini keyboard
		initKeys();

		// ini Gui
//		guiNode.detachAllChildren();
//		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
//		helloText = new BitmapText(guiFont, false);
//		helloText.setSize(guiFont.getCharSet().getRenderedSize());
//		helloText.setText("Hello World");
//		helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
//		guiNode.attachChild(helloText);

		// ini Kinect
		sceneReader = new SceneReader(this);
		sceneReader.start();

	}

	public void simpleUpdate(float tpf) {
		cam.setLocation(camPos);
		cam.lookAt(screenCenter, new Vector3f(0f, 0f, 1f));
		cam.setFrustumPerspective(viewAng, 1.6f, 100, 6000);
//		helloText.setText(" " + camPos.x + " " + camPos.y + " " + camPos.z + " " + viewAng);
		
		updataBoxs(tpf);
	
	}
	
	public void updataBoxs(float tpf){
		if (sceneReader.faceChanged) {
			sceneReader.faceChanged=false;
			for (MyBoxNode node : this.currentGeo) {
				node.isfound = false;
			}

			ArrayList<MyBoxNode> newGeos = new ArrayList<MyBoxNode>();
			for (IFace face : sceneReader.getFaces()) {
				MyBox box = face.anaUpFace2Box();
				MyBoxNode boxNode = this.matchCurrentGeo(box);
				if (boxNode == null) {
					TextureBox node = new TextureBox(this, box);
					newGeos.add(node);
					node.appearCount++;
					node.iniKalmanFilter();
				} else {
					if (boxNode.isShow){
					boxNode.updateLocationWithKalman(box);
//					boxNode.updateAnimation(tpf);
					boxNode.disappearCount = 0;
					boxNode.isfound = true;
					}else{
						boxNode.replaceLocation(box);
						boxNode.appearCount++;
						boxNode.isfound=true;
					}
				}
			}
			ArrayList<MyBoxNode> trash = new ArrayList<MyBoxNode>();
			for (MyBoxNode node : this.currentGeo) {
				if (!node.isfound) {
					if (node.isShow) {
						++node.disappearCount;
						if (node.disappearCount >= Option.animationThrowStep) {
							trash.add(node);
						}
					}else{
						trash.add(node);
					}

				}else{
					if (!node.isShow){
						if (node.appearCount>Option.animationCreatStep){
							node.iniAnimation();
							node.iniKalmanFilter();
							node.isShow=true;
						}
					}
				}

			}
			for (MyBoxNode node : trash) {
				currentGeo.remove(node);
				node.del();
			}
			currentGeo.addAll(newGeos);

		}
		for (MyBoxNode node : this.currentGeo){
			if (node.isShow){
				node.updateAnimation(tpf);
			}
		}
	}

	public MyBoxNode matchCurrentGeo(MyBox box) {
		MyBoxNode res = null;
		double minPoints = Option.matchBoxAllMaxPoint;
		for (MyBoxNode boxNode : this.currentGeo) {
			double points = boxNode.point4AllMatch(box);
			if (points < minPoints) {
				minPoints = points;
				res = boxNode;
			}
		}
		return res;
	}

	public void destroy() {
		sceneReader.stopKinect();
		sceneReader.stop();
		this.stop();
		super.destroy();
	}

	public void showPoint(IFace face) {
		Box box = new Box(1f, 1f, 1f);
		Geometry boxgeo = new Geometry("box", box);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.randomColor());
		boxgeo.setMaterial(mat);
		for (IPoint pt : face.points()) {
			if (Math.random() < 0.9)
				continue;
			Geometry thisBox = boxgeo.clone();
			thisBox.setLocalTranslation(Mymath.IVecToVector3f(pt.pos()));
			rootNode.attachChild(thisBox);
		}
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
	public Geometry cube(float x,float y,float length,float width,float height,float ang){
		Box box=new Box(length/2,width/2,height/2);
		Geometry geo=new Geometry("",box);
		geo.move(x,y,height/2);
		geo.rotate(0, 0, ang/180*FastMath.PI);
		return geo;
	}
	
	public Material loadMat(String num){
		Material cube1Mat = new Material(assetManager,
		        "Common/MatDefs/Misc/Unshaded.j3md");
		    Texture cube1Tex = assetManager.loadTexture(
		        "Textures/texture ("+num+").jpg");
		    cube1Tex.setWrap(WrapMode.Repeat);
		    cube1Mat.setTexture("ColorMap", cube1Tex);
		    return cube1Mat;
	}
	

	public void drawFramework() {
		Box box=new Box(600,600,0);
		box.scaleTextureCoordinates(new Vector2f(3f,3f));
		Geometry geo=new Geometry("", box);
		geo.move(600,600,0);
		geo.setMaterial(loadMat("22"));
		rootNode.attachChild(geo);
	}
	

}
