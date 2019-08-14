package myAnimation;

import org.ejml.simple.SimpleMatrix;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import kalmanFilter.KalmanFilterSimple;
import myProgram.Mymath;
import myProgram.Option;

public abstract class MyBoxNode extends Node {
	protected SimpleApplication father;
	protected Vector3f pos;
	protected float length,width,height,ang;
	protected float newLength,newWidth,newHeight;
//	public Geometry geo;
//	private Node geos;
//	public Material material4geos;
	private float currentFrametime = 0;
	private float frameTime=1f/Option.fps;
//	private float scale=1f;
//	private float scaleRate=0.995f;
//	private final float maxSc=1f;
//	private final float minSc=0.5f;
	private KalmanFilterSimple kal;
	public int appearCount=0;
	public int disappearCount=0;
	public boolean isfound=false;
	public boolean isShow=false;
	Material material;
	MyBoxNode node;
	
	public MyBoxNode(SimpleApplication father,Vector3f pos ,float x,float y,float z,float ang){
		super();
		this.father=father;
		this.pos=pos;
		this.length=x;
		this.width=y;
		this.height=z;
		this.ang=ang;
		this.newLength=x;
		this.newWidth=y;
		this.newHeight=z;
	}
	
	public MyBoxNode(SimpleApplication father,MyBox box){
		this(father,box.pos,box.length,box.width,box.height,box.ang);
	}
	
	public void setMat(Material mat){
		this.material=mat;
	}
	
	
	public void iniKalmanFilter(){
		kal=new KalmanFilterSimple();
		kal.configure(Option.fMat, Option.qMat, Option.hMat,Option.rMat);
		double[][] xNum={
				{pos.getX()},
				{pos.getY()},
				{ang},
				{length},
				{width},
				{height},
				{0},
				{0},
				};
		SimpleMatrix xMat=new SimpleMatrix(xNum);
		kal.setState(xMat, Option.pMat.copy());
	}
	
	public void iniFilter(){
		iniKalmanFilter();
	}
	
	public double point4AllMatch(MyBox box){
		float length=newLength;
		float width=newWidth;
		float height=newHeight;
		double angDelta=Math.abs(ang-box.ang);
		angDelta=Math.min(Math.PI-angDelta, angDelta);
		double dist=pos.distance(box.pos);
		double shapeDist=Math.sqrt(Math.pow(box.length-length, 2)+Math.pow(box.width-width, 2));
		double zDist=Math.abs(height-box.height);
		double res=angDelta*Option.matchBoxAngelFact+dist*Option.matchBoxDistFact+shapeDist*Option.matchBoxShapeDistFact+zDist*Option.matchBoxZDistFact;
		return res;
	}
	
	public double point4ShapeMath(MyBoxNode box){
		float length=newLength;
		float width=newWidth;
		float height=newHeight;
		double shapeDist=Math.sqrt(Math.pow(box.length-length, 2)+Math.pow(box.width-width, 2));
		double zDist=Math.abs(height-box.height);
		double res=shapeDist*Option.matchBoxShapeDistFact+zDist*Option.matchBoxZDistFact;
		return res;
	}

	
	    
	
	public void updateLocation(Vector3f newPos,float newAng){
		this.setLocalTranslation(pos);
		this.rotate(0, newAng-ang, 0);
		this.pos=newPos;
		this.ang=newAng;
		
	}
	
	public void replaceLocation(MyBox box){
		this.pos=box.pos;
		this.ang=box.ang;
		this.length=box.length;
		this.width=box.width;
		this.height=box.height;
	}
	
	public void updataBadLocation(MyBox box){
		this.setLocalTranslation(box.pos);
		this.rotate(0, 0, box.ang-this.ang);
		this.setLocalScale(box.length/length, box.width/width, box.length/length);
		this.ang=box.ang;
	
	}
	
	public void updateLocationWithKalman(MyBox box){
		if (!Option.enblekalmanFilter){
			this.replaceLocation(box);
			return;
		}
		double[][] xNum={
				{box.pos.getX()},
				{box.pos.getY()},
				{box.ang},
				{box.length},
				{box.width},
				{box.height}
				};
		SimpleMatrix xMat=new SimpleMatrix(xNum);
		kal.update(xMat);
		kal.predict();
		SimpleMatrix res=kal.getState();
		Vector3f newPos=new Vector3f((float)res.get(0, 0),(float)res.get(1,0),0);
		float newAng=(float)res.get(2,0);
		newLength=(float)res.get(3,0);
		newWidth=(float)res.get(4,0);
		newHeight=(float)res.get(5,0);
		this.setLocalTranslation(newPos);
		this.rotate(0,0,newAng-ang);
		this.setLocalScale(newLength/length, newWidth/width, newHeight/height);
		this.pos=newPos;
		this.ang=newAng;
	}
	
	public void del(){
		if (isShow)
		father.getRootNode().detachChild(this);
	}
	
	public abstract void iniAnimation();
	public abstract void updateAnimation();
	
	public void updateAnimation(float tpf){
		currentFrametime += tpf;
		while (currentFrametime > frameTime) {
			currentFrametime = currentFrametime - frameTime;
			this.updateAnimation();
		}
	}
	public void printNowSize(){
		System.out.println(newLength + " " + newWidth + " " + newHeight);
	}
	public String stringNowSize(){
		return new String(newLength + " " + newWidth + " " + newHeight);
	}
	public void iniMat(){
		material =new Material(father.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setColor("Diffuse",ColorRGBA.White );
		material.setColor("Specular", ColorRGBA.Gray);
		material.setFloat("Shininess", 10f); // [0,128]
		material.getAdditionalRenderState().setColorWrite(true);
		material.setBoolean("UseMaterialColors", true);
	}
	

}
