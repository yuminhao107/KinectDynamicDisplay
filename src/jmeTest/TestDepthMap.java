package jmeTest;

import java.awt.image.BufferedImage;

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
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;
import myAnimation.Mosaic;
import myAnimation.MyBox;
import myAnimation.RotateControl;
import myAnimation.ScaleControl;
import myProgram.EBox;
import myProgram.IFace;
import myProgram.IPoint;
import myProgram.Mymath;
import myProgram.Option;
import myProgram.SceneReader;
import myProgram.TestScene;
import processingPainter.GuiPApplet;
import processingPainter.PAppletAdapter;

public class TestDepthMap extends SimpleApplication {
	Node frameWork=new Node("frameWork");
	SceneReader sceneReader;
	
	Geometry screen;
	GuiPApplet papplet;
	PAppletAdapter adapter;
	Node points=new Node();


	Material redMat, blueMat, defaultLightMat;
	Mosaic mosaic;
	Vector3f camPos = Option.camPos.clone();
	Vector3f screenCenter = Option.screenCenter.clone();
	Vector3f modelPos = new Vector3f(screenCenter.x - 151f, screenCenter.y + 90.5f, 0);
	float movement = 1f;
	boolean tab=false;
	
	float viewAng = Option.viewAng;
	float viewAngDelta = 0.005f;
	BitmapText helloText;
	IVec depthTarget=Mymath.depthTarget.cp();
	IVec depthCamPos=Mymath.depthCamPos.cp();
	double depthAng=Mymath.depthAng;

	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		TestDepthMap app = new TestDepthMap();
		app.start();
	}

	public void simpleInitApp() {

		
		defaultLightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

		// setup material
		assetManager.registerLocator("assets", FileLocator.class);
		redMat = assetManager.loadMaterial("Materials/blue.j3m");

		Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		redMat.setColor("Color", ColorRGBA.Red);

//		 mosaic=new Mosaic(this,modelPos,302,181,302,0);
//		 mosaic.setMat(this.defaultLightMat);
//		 mosaic.iniAnimation();


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
		helloText.setLocalTranslation(300, helloText.getLineHeight()*6, 0);
		guiNode.attachChild(helloText);
		
		sceneReader=new SceneReader(this);
	    sceneReader.start();
	    rootNode.attachChild(points);

		// cam.lookAtDirection(new Vector3f(0f, 0f, 1f), new Vector3f(0f, 1f,
		// 0f));
//	    drawFramework();
//	    iniScreen();
	}

	public void simpleUpdate(float tpf) {
		this.updataDepthCam();
//		if (mosaic!=null)this.mosaic.updateAnimation();
		cam.setLocation(camPos);
		cam.lookAt(screenCenter, new Vector3f(0f, 0f, 1f));
		cam.setFrustumPerspective(viewAng, 1.6f, 100, 6000);
		String st=new String(
				"ang="+this.depthAng+"\n"+
				"depthCamPos=(" + depthCamPos.x + "," + depthCamPos.y + "," + depthCamPos.z +")\n"+
				"depthTarget=("+ depthTarget.x + "," + depthTarget.y + "," + depthTarget.z +")\n"
				);
		st+="facotr:"+Mymath.camera_fx+" "+Mymath.camera_fy+"\n";
		if (mosaic!=null)st+="\n"+mosaic.stringNowSize();
		helloText.setText(st);
		if (sceneReader.faceChanged){
			points.detachAllChildren();
			sceneReader.faceChanged=false;
		
//			if (mosaic !=null )rootNode.attachChild(mosaic);
			for (IFace face:sceneReader.getFaces()){
				double ang=face.normal.angle(IVec.zaxis);
				if (ang>5.0/180*Math.PI) continue;
				if (face.center.z()<50) continue;
//				showPoint(face,points);
				
				MyBox box=face.anaUpFace2Box();
	
				if (mosaic==null) {
					mosaic=new Mosaic(this,box.pos,box.length,box.width,box.height,box.ang);
					mosaic.iniAnimation();
					mosaic.iniKalmanFilter();
					rootNode.attachChild(mosaic);
				}else{
					System.out.println("Point is "+mosaic.point4AllMatch(box));
					mosaic.updateLocationWithKalman(box);
					mosaic.printNowSize();
					box.printNowSize();
					
				}
				
			}
		}
	}
	
	public void getDepthDir(){
		
	}
	
	public void iniScreen(){
		int width = 1200;
		int height = 1200;
		Image image = new Image(Image.Format.ABGR8, width, height, null);
		Texture2D gui = new Texture2D(image);
		Quad quad = new Quad(width, height);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setTexture("ColorMap", gui);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		screen = new Geometry("GUI Screen", quad);
		screen.setMaterial(mat);

		papplet = new GuiPApplet(width, height,sceneReader);
		papplet.init();
		papplet.start();

		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		adapter = new PAppletAdapter(papplet, bimage, gui);

		// screen.setLocalTranslation(100, 100, 10);
		screen.addControl(adapter);
		rootNode.attachChild(screen);
	}
	
	 public void destroy(){
		 sceneReader.stopKinect();
		 sceneReader.stop();
		 if (papplet!=null) {
			 papplet.destroy();
		 }
		 this.stop();
		 super.destroy();
		 }
	 public void showPoint(IFace face,Node node){
			Box box=new Box(1f,1f,1f);
			Geometry boxgeo=new Geometry("box",box);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.Yellow);
			boxgeo.setMaterial(mat);
			for (IPoint pt:face.points()){
			if (Math.random() < 0.9)
				continue;
				Geometry thisBox=boxgeo.clone();
				IVec pos=pt.pos().cp();
				if (tab) pos.z(0);
				thisBox.setLocalTranslation(Mymath.IVecToVector3f(pos));
				node.attachChild(thisBox);
			}
		}

	private void initKeys() {
		// You can map one or several inputs to one named action
		inputManager.addMapping("anticlock", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("clock", new KeyTrigger(KeyInput.KEY_M));
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("cup", new KeyTrigger(KeyInput.KEY_I));
		inputManager.addMapping("cdown", new KeyTrigger(KeyInput.KEY_K));
		inputManager.addMapping("cright", new KeyTrigger(KeyInput.KEY_L));
		inputManager.addMapping("cleft", new KeyTrigger(KeyInput.KEY_J));
		inputManager.addMapping("tab", new KeyTrigger(KeyInput.KEY_TAB));
		inputManager.addMapping("del", new KeyTrigger(KeyInput.KEY_D));




		// Add the names to the action listener.
		inputManager.addListener(analogListener,"tab", "del","anticlock", "clock", "up", "down", "right", "left","cup", "cdown", "cright", "cleft");

	}
	
	public void updataDepthCam(){
		IVec y=new IVec(Math.cos(depthAng),Math.sin(depthAng),0);
		IVec z=depthTarget.dif(depthCamPos).unit();
		IVec x=y.cross(z).unit();
		y=z.cross(x).unit();
		Mymath.depthCamPos=this.depthCamPos;
		Mymath.depthCamX=x;
		Mymath.depthCamY=y;
		Mymath.depthCamZ=z;
	}
	
	

	private AnalogListener analogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {
			Vector3f forward = screenCenter.subtract(camPos).normalize();
			Vector3f right = forward.cross(new Vector3f(0, 0, 1)).normalize();
			Vector3f up = right.cross(forward).normalize();
			if (name.equals("anticlock")) {
				depthAng+=viewAngDelta;
			}
			if (name.equals("clock")) {
				depthAng-=viewAngDelta;
			}
			if (name.equals("up")) {
				depthTarget.add(0, movement, 0);
			}
			if (name.equals("down")) {
				depthTarget.add(0, -movement, 0);
			}
			if (name.equals("right")) {
				depthTarget.add(movement, 0, 0);
			}
			if (name.equals("left")) {
				depthTarget.add(-movement, 0, 0);
			}
			
			if (name.equals("cup")) {
				depthTarget.add(0, movement, 0);
				depthCamPos.add(0, movement, 0);
			}
			if (name.equals("cdown")) {
				depthTarget.add(0, -movement, 0);
				depthCamPos.add(0, -movement, 0);
			}
			if (name.equals("cright")) {
				depthTarget.add(movement, 0, 0);
				depthCamPos.add(movement, 0, 0);
			}
			if (name.equals("cleft")) {
				depthTarget.add(-movement, 0, 0);
				depthCamPos.add(-movement, 0, 0);
			}
			if (name.equals("tab")) {
				tab=!tab;
			}
			if (name.equals("del")) {
				mosaic=null;
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
		Box box2=new Box(400,400,0);
		Geometry boxGeo2 = new Geometry("", box2);
		boxGeo2.move(600,600,0.1f);
		Material bw=gray.clone();
		Texture bwT=assetManager.loadTexture("Textures/blackwhite.jpg");
		bw.setTexture("ColorMap", bwT);
		bw.setColor("Color", ColorRGBA.White);
		
		boxGeo2.setMaterial(bw);
		frameWork.attachChild(boxGeo);
		frameWork.attachChild(boxGeo2);
		rootNode.attachChild(frameWork);
	}
}
