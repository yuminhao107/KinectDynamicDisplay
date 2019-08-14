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
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowRenderer;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import myAnimation.Mosaic;
import myAnimation.RotateControl;
import myAnimation.ScaleControl;
import myProgram.EBox;
import myProgram.TestScene;

public class Test1 extends SimpleApplication{
Material redMat,blueMat,defaultLightMat;
Mosaic mosaic;
Vector3f camPos=new Vector3f(-889.18f, 1334.8f, 1137.55f);
Vector3f screenCenter=new Vector3f(304,312,729);
Vector3f modelPos=new Vector3f(screenCenter.x+90.5f,0,screenCenter.z-151f);
float movement=0.5f;
float viewAng=24.1f;
float viewAngDelta=0.02f;
BitmapText helloText;

public static void main(String[] args) {
	Test1 app = new Test1();
	app.start();
}
	
	public void simpleInitApp() {
		
		drawFramework();
		defaultLightMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");

		// setup material
		assetManager.registerLocator("assets", FileLocator.class);
		 redMat = assetManager.loadMaterial("Materials/blue.j3m");
		
//		Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		redMat.setColor("Color", ColorRGBA.Red);
		 
		 Box box=new Box(0.5f,0.5f,0.5f);
		 Geometry boxGeo=new Geometry("box",box);
		 boxGeo.setMaterial(redMat);
		 Node node=new Node();
		 Geometry boxGeo2=boxGeo.clone();
		 Geometry boxGeo3=boxGeo.clone();
		 boxGeo3.setLocalTranslation(3, 0, 0);
		 boxGeo3.rotate(0, 1, 0);
		 node.attachChild(boxGeo2);
		 
		 
		 
//		 node.rotate(0, 1.57f, 0);
		 node.setLocalTranslation(3, 3, 0);
		 node.setLocalTranslation(0, 3, 0);
//		 node.rotate(0, 1.57f/2, 0);
		 node.attachChild(boxGeo3);
		 rootNode.attachChild(node); 
		 rootNode.attachChild(boxGeo);
		 
		 box=new Box(new Vector3f(3,3,3),new Vector3f(4,4,4));
		 boxGeo=new Geometry("box",box);
		 boxGeo.setMaterial(redMat);
//		 boxGeo.rotate(0, 1, 0);
		 rootNode.attachChild(boxGeo);
		 
		 Quad quad=new Quad(10,10);
		 Geometry quadGeo=new Geometry("123",quad);
		 quadGeo.setMaterial(redMat);
		 quadGeo.rotate(0,1.57f,0);
		 rootNode.attachChild(quadGeo);
		 
		 mosaic=new Mosaic(this,modelPos,181,302,302,0);
		 mosaic.setMat(this.defaultLightMat);
		 mosaic.iniAnimation();
		 
		 Mosaic mosaic2=new Mosaic(this,new Vector3f(800,0,800),181,120,242,0);
		 mosaic2.setMat(this.defaultLightMat);
		 mosaic2.iniAnimation();
		 
//		 new ScaleControl(mosaic,0.5f,120);
		 
		 flyCam.setMoveSpeed(100);
		 flyCam.setDragToRotate(true);
		 
		 DirectionalLight sun = new DirectionalLight();
			sun.setDirection(new Vector3f(1f, -1f, -1f).normalizeLocal());
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

//        cam.setLocation(new Vector3f(0, 0, 0));
//		cam.lookAtDirection(new Vector3f(0f, 0f, 1f), new Vector3f(0f, 1f, 0f));
		
	}
	
	 public void simpleUpdate(float tpf){
		 mosaic.updateAnimation();
		 cam.setLocation(camPos);
		 cam.lookAt(screenCenter, new Vector3f(0f, 1f, 0f));
		 cam.setFrustumPerspective(viewAng, 1.6f, 100, 6000);
		 helloText.setText(" "+camPos.x+" "+camPos.y+" "+camPos.z+" "+viewAng);
	 }
	 
	 private void initKeys() {
		    // You can map one or several inputs to one named action
		    inputManager.addMapping("x+",  new KeyTrigger(KeyInput.KEY_I));
		    inputManager.addMapping("x-",   new KeyTrigger(KeyInput.KEY_K));
		    inputManager.addMapping("y+",  new KeyTrigger(KeyInput.KEY_U));
		    inputManager.addMapping("y-",  new KeyTrigger(KeyInput.KEY_M));
		    inputManager.addMapping("z+",  new KeyTrigger(KeyInput.KEY_L));
		    inputManager.addMapping("z-",  new KeyTrigger(KeyInput.KEY_J));
		    inputManager.addMapping("view+",  new KeyTrigger(KeyInput.KEY_UP));
		    inputManager.addMapping("view-",  new KeyTrigger(KeyInput.KEY_DOWN));

		    // Add the names to the action listener.
		    inputManager.addListener(analogListener,"x+","x-","y+","y-","z+","z-","view+","view-");

		  }
	 
	 private AnalogListener analogListener = new AnalogListener() {
		    public void onAnalog(String name, float value, float tpf) {
		    	Vector3f forward=screenCenter.subtract(camPos).normalize();
		    	Vector3f right=forward.cross(new Vector3f(0,1,0)).normalize();
		    	Vector3f up=right.cross(forward).normalize();
		        if (name.equals("x+")) {
		        	System.out.println("move x+");
		          camPos.addLocal(forward.mult(movement));
		        }
		        if (name.equals("x-")) {
		        	 camPos.addLocal(forward.mult(-movement));
			        }
		        if (name.equals("y+")) {
		        	camPos.addLocal(up.mult(movement));
			        }
		        if (name.equals("y-")) {
		        	camPos.addLocal(up.mult(-movement));
			        }
		        if (name.equals("z+")) {
		        	 camPos.addLocal(right.mult(movement));
			        }
		        if (name.equals("z-")) {
		        	camPos.addLocal(right.mult(-movement));
			        }
		        if (name.equals("view+")) {
			          viewAng+=viewAngDelta;
			        }
		        if (name.equals("view-")) {
			          viewAng-=viewAngDelta;
			        }
		      
		    }
		  };
	 
	 public void drawFramework(){
		 Box box=new Box(600,0,600);
		 Geometry boxGeo=new Geometry("",box);
		 Material gray = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		 gray.setColor("Color", ColorRGBA.Gray);
		 boxGeo.setMaterial(gray);
		 boxGeo.move(600,0,600);
		 rootNode.attachChild(boxGeo);
	 }
}
