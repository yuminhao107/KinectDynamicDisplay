package myProgram;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Surface;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.util.TangentBinormalGenerator;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;

public class TestScene extends SimpleApplication {
	
	final int depthmapXNum=512;
	final int depthmapYNum=424;
	
	final double camera_factor = 1000;
	final double camera_cx = 255.5;
	final double camera_cy = 210.5;
	final double camera_fx = 518.0;
	final double camera_fy = 519.0;

	EBox big;
	
	Geometry sphereGeo;
	
	private BulletAppState bulletAppState=new BulletAppState();
	
	int frameCount=0;
	
	float ang=0;
	
	float dist=15f;
	PointLight lamp_light;
	
	Material sphereMat,frame;
	
	Kinect kinect=new Kinect();
	
	public static void main(String[] args) {
		TestScene app = new TestScene();
		app.start();
	}

	@Override
	  public void simpleInitApp() {

		// setup material
		 sphereMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		// sphereMat.setTexture("DiffuseMap",
		// assetManager.loadTexture("Textures/Terrain/Pond/Pond.jpg"));
		// sphereMat.setTexture("NormalMap",
		// assetManager.loadTexture("Textures/Terrain/Pond/Pond_normal.png"));
		// sphereMat.setBoolean("UseMaterialColors", true);
		sphereMat.setColor("Diffuse", ColorRGBA.White);
		sphereMat.setColor("Specular", ColorRGBA.White);
		sphereMat.setFloat("Shininess", 32f); // [0,128]
		
		 frame=sphereMat.clone();
		frame.getAdditionalRenderState().setWireframe(true);
		
		big=new EBox(rootNode,"testBox233",new Vector3f(0,0.5f,0),2f,4f,2f);
		big.iniBox(sphereMat);
		
		rootNode.setShadowMode(ShadowMode.CastAndReceive);
		
		Box plain=new Box(new Vector3f(-100,-1,-100),new Vector3f(100,0,100));
		Geometry plainGeo=new Geometry("plain cube", plain);
		plainGeo.setMaterial(sphereMat);
		rootNode.attachChild(plainGeo);





	    /** A bumpy rock with a shiny light effect.*/


	    /** Must add a light to make the lit object visible! */
//	    DirectionalLight sun = new DirectionalLight();
//		sun.setDirection(new Vector3f(-1.5f, -2f, -3f).normalizeLocal());
//	    sun.setColor(ColorRGBA.White.mult(0.5f));
//	    rootNode.addLight(sun);
	    
	    lamp_light = new PointLight();
	    lamp_light.setColor(ColorRGBA.White.mult(1.5f));
	    lamp_light.setRadius(40f);
	    lamp_light.setPosition(new Vector3f(dist, 0, 0));
	    rootNode.addLight(lamp_light);
	    
	    
	    PointLightShadowRenderer plsr = new PointLightShadowRenderer(assetManager, 1024);
	    plsr.setLight(lamp_light);// 设置点光源
	    plsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
	    viewPort.addProcessor(plsr);
	    
//	    final int SHADOWMAP_SIZE=1024;
//	    PointLightShadowFilter dlsf = new PointLightShadowFilter(assetManager, SHADOWMAP_SIZE);
//	    dlsf.setLight(lamp_light);
//	    dlsf.setEnabled(true);
//	    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
//	    fpp.addFilter(dlsf);
//	    viewPort.addProcessor(fpp);
	    
	    AmbientLight al = new AmbientLight();
	    al.setColor(ColorRGBA.White.mult(0.3f));
	    rootNode.addLight(al);
	    
		cam.setLocation(new Vector3f(10, 10, 10));
		cam.lookAtDirection(new Vector3f(-1f, -1f, -1f), new Vector3f(0f, 1f, 0f));
		
//		bulletAppState.setDebugEnabled(true);
		
//		kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
}
	
	 public void simpleUpdate(float tpf){
		 ++frameCount;
		 
		 
		 ang+=0.02;
		 lamp_light.setPosition(new Vector3f(dist*(float)Math.cos(ang),15f,dist*(float)Math.sin(ang)));
		 
		 if (frameCount<300) return;
		 if (frameCount==300){
		 big.split(1, sphereMat);
		 big.geo.setMaterial(frame);
		 }
		 big.updateAnimation(tpf);
		 System.out.println("getting map");
	 }
	 
	 public void getDepthmap(){
		 System.out.println("getting map");
		 short[] depth=kinect.getDepthFrame();
		 long sum=0;
		 for (int i=0;i<depth.length;i++){
			 sum+=depth[i];
		 }
		 System.out.println(sum/depth.length);
		 
		 for (int i=0;i<this.depthmapXNum;i++)
			 for (int j=0;j<this.depthmapYNum;j++){
				 short value=depth[i+j*depthmapXNum];
				 if (value>0){
					 double z=value/camera_factor;
					 double x=(i-camera_cx)*z/camera_fx;
					 double y=(j-camera_cy)*z/camera_fy;
				 }
			 }
	 }

	public short[][] getDepthmapArray() {
		short[] depth = kinect.getDepthFrame();
		short[][] results = new short[depthmapYNum][depthmapXNum];
		for (int i = 0; i < this.depthmapYNum; i++)
			for (int j = 0; j < this.depthmapXNum; j++) {
				results[i][j] = depth[j + i * depthmapXNum];
			}
		return results;
	}

	public IVec[][] short2IVec(short[][] pts) {
		IVec[][] results = new IVec[depthmapYNum][depthmapXNum];
		for (int i = 0; i < this.depthmapYNum; i++)
			for (int j = 0; j < this.depthmapXNum; j++) {
				short value = pts[i][j];
				if (value > 0) {
					double z = value / camera_factor;
					double x = (i - camera_cx) * z / camera_fx;
					double y = (j - camera_cy) * z / camera_fy;
					results[i][j] = new IVec(x, y, z);
				}
			}
		return results;
	}

	// range=1 3*3 ;range=2 5*5 etc..
	public short[][] mediaFilter(short[][] pts, int range) {
		short[][] results = pts.clone();
		int filterSize = range * 2 + 1;
		int num = filterSize * filterSize;
		int target = num / 2;
		short[] tem = new short[num];
		for (int i = range; i < depthmapYNum - range; i++)
			for (int j = range; j < depthmapXNum - range; j++) {

				for (int ii = 0; ii < filterSize; ii++)
					for (int jj = 0; jj < filterSize; jj++) {
						tem[ii * filterSize + jj] = pts[i - range + ii][j - range + jj];
					}

				for (int m = 0; m < target + 1; m++) {
					int min = m;
					for (int n = m + 1; n < num; n++) {
						if (tem[min] > tem[n])
							min = n;
					}
					short swapTem = tem[m];
					tem[m] = tem[min];
					tem[min] = swapTem;
				}

				results[i][j] = tem[target];
			}

		return results;
	}
}
