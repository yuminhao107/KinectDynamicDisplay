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
import com.jme3.math.Vector2f;
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
import com.jme3.texture.Texture.WrapMode;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import myAnimation.Cengceng;
import myAnimation.Mosaic;
import myAnimation.MyBoxNode;
import myAnimation.RotateControl;
import myAnimation.ScaleControl;
import myProgram.EBox;
import myProgram.Option;
import myProgram.TestScene;

public class TestStatic extends SimpleApplication {
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
		TestStatic app = new TestStatic();
		app.start();
	}

	public void simpleInitApp() {
		assetManager.registerLocator("assets", FileLocator.class);
		redMat = assetManager.loadMaterial("Materials/blue.j3m");
		
		iniScene05();
		
		defaultLightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		ColorRGBA color = ColorRGBA.randomColor();
		defaultLightMat.setColor("Diffuse", color);
		defaultLightMat.setColor("Specular", color);
		defaultLightMat.setFloat("Shininess", 32f); // [0,128]
		defaultLightMat.getAdditionalRenderState().setColorWrite(true);
		defaultLightMat.setBoolean("UseMaterialColors", true);

		// setup material


//		Spatial testCube = assetManager.loadModel("Models/test1/blender2ogre-export.scene");
//		rootNode.attachChild(testCube);

		Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		redMat.setColor("Color", ColorRGBA.Red);

//		mosaic = new Mosaic(this, modelPos, 302, 180, 302, 0);
//		mosaic.setMat(this.defaultLightMat);
//		mosaic.iniAnimation();

		// testBoxNode=new Cengceng(this,new Vector3f(600,600,0),100,100,300,0);
		// testBoxNode.setMat(defaultLightMat);
		// testBoxNode.iniAnimation();

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
//		helloText = new BitmapText(guiFont, false);
//		helloText.setSize(guiFont.getCharSet().getRenderedSize());
//		helloText.setText("Hello World");
//		helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
//		guiNode.attachChild(helloText);

//		drawFramework();

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
//		helloText.setText(" " + camPos.x + " " + camPos.y + " " + camPos.z + " " + viewAng);
	}
	
	public void iniScene01() {
		Geometry geo;
		geo=cube(500,600,302,180,302,0);
		geo.setMaterial(loadMat("1"));
		rootNode.attachChild(geo);
		geo=cube(900,900,242,182,362,0);
		geo.setMaterial(loadMat("2"));
		rootNode.attachChild(geo);
		geo=cube(500,300,300,180,81,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(600,600,182,182,242,0);
		geo.setMaterial(loadMat("1"));
//		rootNode.attachChild(geo);
		geo=cube(150,350,246,121,182,90);
		geo.setMaterial(loadMat("16"));
		rootNode.attachChild(geo);
	}
	
	public void iniScene02() {
		Geometry geo;
		geo=cube(600,600,302,180,302,-22.5f);
		geo.setMaterial(loadMat("17"));
		rootNode.attachChild(geo);
		geo=cube(900,900,242,182,362,0);
		geo.setMaterial(loadMat("2"));
//		rootNode.attachChild(geo);
		geo=cube(500,300,300,180,81,0);
		geo.setMaterial(loadMat("8"));
//		rootNode.attachChild(geo);
		geo=cube(600,600,182,182,242,0);
		geo.setMaterial(loadMat("1"));
//		rootNode.attachChild(geo);
		geo=cube(150,350,246,121,182,90);
		geo.setMaterial(loadMat("16"));
//		rootNode.attachChild(geo);
		Box box=new Box(600,600,0);
		box.scaleTextureCoordinates(new Vector2f(3,3));
		geo=new Geometry("", box);
		geo.move(600,600,0);
		geo.setMaterial(loadMat("22",true));
		rootNode.attachChild(geo);
	}
	public void iniScene03() {
		Geometry geo;
		geo=cube(700,500,302,302,180,0f);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(250,900,242,182,362,60);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(650,900,180,81,300,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(300,300,182,182,242,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(1000,750,246,121,182,140);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		Box box=new Box(600,600,0);
		box.scaleTextureCoordinates(new Vector2f(3f,3f));
		geo=new Geometry("", box);
		geo.move(600,600,0);
		geo.setMaterial(loadMat("22",true));
		rootNode.attachChild(geo);
	}
	
	public void iniScene04() {
		Geometry geo;
		geo=cube(530,810,302,180,320,0f);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(290,600,242,182,362,90);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(530,448,300,180,81,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(470,630,182,182,242,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		geo=cube(683,630,246,182,121,0);
		geo.setMaterial(loadMat("8"));
		rootNode.attachChild(geo);
		Box box=new Box(600,600,0);
		box.scaleTextureCoordinates(new Vector2f(3f,3f));
		geo=new Geometry("", box);
		geo.move(600,600,0);
		geo.setMaterial(loadMat("22",true));
		rootNode.attachChild(geo);
	}
	
	public void iniScene05() {
		Geometry geo;
		geo=cube(850,850,302,302,180,0f);
		geo.setMaterial(loadMat("1"));
		rootNode.attachChild(geo);
		geo=cube(400,850,362,242,182,0);
		geo.setMaterial(loadMat("4"));
		rootNode.attachChild(geo);
		geo=cube(700,300,300,180,81,0);
		geo.setMaterial(loadMat("7"));
		rootNode.attachChild(geo);
		geo=cube(300,500,242,182,182,90);
		geo.setMaterial(loadMat("16"));
		rootNode.attachChild(geo);
		geo=cube(880,550,246,182,121,0);
		geo.setMaterial(loadMat("18"));
		rootNode.attachChild(geo);
		Box box=new Box(600,600,0);
		box.scaleTextureCoordinates(new Vector2f(3f,3f));
		geo=new Geometry("", box);
		geo.move(600,600,0);
		geo.setMaterial(loadMat("22",true));
		rootNode.attachChild(geo);
	}
	
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
            
		    cube1Mat.setTexture("ColorMap", cube1Tex);
		    return cube1Mat;
	}
	
	public Material loadMat(String num,boolean f){
		Material cube1Mat = new Material(assetManager,
		        "Common/MatDefs/Misc/Unshaded.j3md");
		    Texture cube1Tex = assetManager.loadTexture(
		        "Textures/texture ("+num+").jpg");
		    cube1Tex.setWrap(WrapMode.Repeat);
		    cube1Mat.setTexture("ColorMap", cube1Tex);
		    return cube1Mat;
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
