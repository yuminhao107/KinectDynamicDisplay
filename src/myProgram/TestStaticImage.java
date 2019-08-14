package myProgram;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;

public class TestStaticImage extends SimpleApplication{
	
	ArrayList<IFace> faces;
	boolean faceChanged=false;
    double showPercent=0.1;
    String location="data/depthmap/test03depth.png";
//	Kinect kinect=new Kinect();
	SceneReader sceneReader;
	final int depthmapXNum=512;
	final int depthmapYNum=424;
	Material orange;
	long num=0;
	PointGeo geo;
	boolean drawed=false;
	
	public static void main(String[] args) {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		TestStaticImage app = new TestStaticImage();
		app.start();
	}
		
	public void simpleInitApp() {
		
		Line line = new Line(new Vector3f(0, 0, 0), new Vector3f(0, 10, 0));

//		line.setLineWidth(2);;
//		Geometry geometry = new Geometry("Bullet", line);
//		orange = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		orange.setColor("Color", ColorRGBA.White);
//		geometry.setMaterial(orange);                  
//		rootNode.attachChild(geometry);
		
//		drawLine(new IPoint(new IVec(0,0,0),0,0),new IPoint(new IVec(0,0,10),0,0),orange,rootNode);

		// sceneReader=new SceneReader(this);
		// sceneReader.start();

		int[][] depthmap = getImageGRB(location);
		IVec[][] vecs = short2IVec(depthmap);
		PointCloudGeo geo = new PointCloudGeo(vecs);
		geo.findFaces();
		geo.combineFaces();
		System.out.println(Option.errorCount + " eigens is 0");
		for (IFace face : geo.faces) {
			this.showPoint(face);
		}
		setFocalLength(8f);
		cam.setLocation(new Vector3f(0, 0, 0));
		cam.lookAtDirection(new Vector3f(0f, 0f, 1f), new Vector3f(0f, 1f, 0f));
	}
	
	 public void simpleUpdate(float tpf){
		// if (drawed) return;
		// if (this.faceChanged){
		// faceChanged=false;
		// rootNode.detachAllChildren();
		// showAllPoints();
		// this.drawed=true;
		// for (IFace face:faces){
		// face.anaBox();
		// Geometry geo=face.generateBox();
		// geo.setMaterial(orange);
		//// rootNode.attachChild(geo);
		// showPoint(face);
		// }
		//
		// }
//		 geo=kinect.createGeo();
//		 if (geo==null) return;
//		 geo.findFaces(6, 100);
	 }
	 
	public void setFocalLength(float blenderFocalLength) {

		// film back for a 35mm camera is 36x24mm

		float filmBackRatio = (float) Math.sqrt(Math.pow(36f, 2) + Math.pow(24f, 2f));

		float frustumSize = 0.02075f * filmBackRatio / blenderFocalLength;

		float aspect = (float) cam.getWidth() / cam.getHeight();

		cam.setFrustum(0.1f, 1000f, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);
	}

	public static IVec[][] short2IVec(int[][] pts) {
		final int depthmapXNum = 512;
		final int depthmapYNum = 424;
		final double camera_factor = 1;
		final double camera_cx = 255.5;
		final double camera_cy = 210.5;
		final double camera_fx = 518.0;
		final double camera_fy = 519.0;
		IVec[][] results = new IVec[depthmapYNum][depthmapXNum];
		for (int i = 0; i < depthmapYNum; i++)
			for (int j = 0; j < depthmapXNum; j++) {
				int value = pts[i][j];
				if (value > 0) {
					double z = value / camera_factor;
					double x = (i - camera_cx) * z / camera_fx;
					double y = (j - camera_cy) * z / camera_fy;
					results[i][j] = new IVec(x, y, z);
				}
			}
		return results;
	}

	public static int[][] getImageGRB(String filePath) {
		File file = new File(filePath);
		int[][] result = null;
		if (!file.exists()) {
			return result;
		}
		try {
			BufferedImage bufImg = ImageIO.read(file);
			int height = bufImg.getHeight();
			int width = bufImg.getWidth();
			result = new int[height][width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					result[i][j] = bufImg.getRGB(j, i) & 0xFF;
					result[i][j] *= 4;
					

					// System.out.println(bufImg.getRGB(i, j) & 0xFFFFFF);

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	 public void showPoint(IFace face){
			Box box=new Box(1f,1f,1f);
			Geometry boxgeo=new Geometry("box",box);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.randomColor());
			boxgeo.setMaterial(mat);
			for (IPoint pt:face.points()){
			if (Math.random() < 1-showPercent)
				continue;
				Geometry thisBox=boxgeo.clone();
				thisBox.setLocalTranslation(Mymath.IVecToVector3f(pt.pos()));
				rootNode.attachChild(thisBox);
			}
		}
	 
	public void showAllPoints(ArrayList<IPoint> pts) {
		 Box box=new Box(1f,1f,1f);
			Geometry boxgeo=new Geometry("box",box);
			Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			mat.setColor("Color", ColorRGBA.Gray);
			boxgeo.setMaterial(mat);
		for (IPoint pt : pts) {
				if (!pt.isUseful()) continue;
				Geometry thisBox=boxgeo.clone();
				thisBox.setLocalTranslation(Mymath.IVecToVector3f(pt.pos()));
				rootNode.attachChild(thisBox);
			}
	 }
	 
	
	
	private void drawLine(IPoint p1,IPoint p2,Material mat,Node node){
		Line line = new Line(IVecTo3f(p1.pos()), IVecTo3f(p2.pos()));
		System.out.println("line"+num);
		Geometry geometry = new Geometry("Line"+(++num), line);
		geometry.setMaterial(mat);
		rootNode.attachChild(geometry);
	}
	
	public Vector3f IVecTo3f(IVec pt){
		System.out.println(""+pt.x()+" "+pt.y()+" "+pt.z());
		return new Vector3f((float)pt.x(),(float)pt.y(),(float)pt.z());
		
	}
}
