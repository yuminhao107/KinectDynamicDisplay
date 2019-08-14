package myAnimation;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import myProgram.Option;

import com.jme3.math.FastMath;

public class Mosaic extends MyBoxNode{
	private final float unit=50f;//50
	private final float thickness=0.01f;
//	private AssetManager assetManager;
	ArrayList<Geometry[][]> geos;
	Material material;
	int xNum,yNum,zNum;
	float xLen,yLen,zLen;
	int duration;
	int upDuration;
	float totalTime=30;
	int time=0;
	
	public Mosaic(SimpleApplication father ,Vector3f pos ,float x,float y,float z,float ang){
		super(father,pos , x, y, z, ang);
		
//		this.assetManager=assetManager;
	}
	
	public Mosaic(SimpleApplication father,MyBoxNode node){
		super(father,new Vector3f(0,0,0),node.length,node.width,node.height,0f);
		this.node=node;
	}
	
	public Mosaic(SimpleApplication father,MyBox box){
		this(father,box.pos,box.length,box.width,box.height,box.ang);
	}
	
	public void setMat(Material mat){
		this.material=mat;
	}

	@Override
	public void iniAnimation() {
		// TODO Auto-generated method stub
		material =new Material(father.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
		material.setColor("Diffuse",ColorRGBA.White );
		material.setColor("Specular", ColorRGBA.White);
		material.setFloat("Shininess", 32f); // [0,128]
		material.getAdditionalRenderState().setColorWrite(true);
		material.setBoolean("UseMaterialColors", true);
		if (node==null){
		father.getRootNode().attachChild(this);
		this.rotate(0, 0, ang);
		this.setLocalTranslation(pos);
		}

		
		xNum=(int)(length/unit)+1;
		yNum=(int)(width/unit)+1;
		zNum=(int)(height/unit)+1;
		duration=(int) (totalTime*Option.fps/(yNum*zNum+xNum*zNum)/2);
		upDuration=(int) (totalTime*Option.fps/(xNum*yNum));
		xLen=length/xNum;
		yLen=width/yNum;
		zLen=height/zNum;
		geos=new ArrayList<Geometry[][]>();
	    Vector3f xDir=new Vector3f(xLen,0,0);
	    Vector3f yDir=new Vector3f(0,yLen,0);
	    Vector3f zDir=new Vector3f(0,0,zLen);
	    Box temBox=new Box(thickness/2,yLen/2,zLen/2);
	    Geometry yzBox=new Geometry("",temBox);
	    yzBox.move(length/2, (yLen-width)/2,zLen/2);
	    geos.add(geoArray(yzBox,yDir,zDir,yNum,zNum));
	    yzBox.move(-length,width-yLen,0);
	    geos.add(geoArray(yzBox,yDir.mult(-1),zDir,yNum,zNum));
	    temBox=new Box(xLen/2,thickness/2,zLen/2);
	    Geometry xzBox=new Geometry("",temBox);
	    xzBox.move((length-xLen)/2,width/2,zLen/2);
	    geos.add(geoArray(xzBox,xDir.mult(-1),zDir,xNum,zNum));
	    xzBox.move(xLen-length,-width,0);
	    geos.add(geoArray(xzBox,xDir,zDir,xNum,zNum));
	    temBox=new Box(xLen/2,yLen/2,thickness/2);
	    Geometry xyBox=new Geometry("",temBox);
	    xyBox.move((xLen-length)/2,(yLen-width)/2,height);
	    geos.add(geoArray(xyBox,xDir,yDir,xNum,yNum));
	    xyBox.move(0,0,-height);
	    geos.add(geoArray(xyBox,xDir,yDir,xNum,yNum));
	}

	@Override
	public void updateAnimation() {
		// TODO Auto-generated method stub
		time++;
		if (time%duration==0)
		surroundAnime();
		if (time%upDuration==0)
		upAnime();
		
	}
	
	private void surroundAnime(){
		Geometry[][] tem=geos.get((int)(Math.random()*4));
		int i=tem.length;
		int j=tem[0].length;
		i=(int)(Math.random()*i);
		j=(int)(Math.random()*j);
		Geometry temGeo=tem[i][j];
		if(temGeo.getNumControls()>0) return;
		temGeo.setMaterial(this.randomMat());
		new RotateControl(tem[i][j],0,0,FastMath.PI,20);
	}
	public void upAnime(){
		Geometry[][] tem=geos.get(4);
		int i=tem.length;
		int j=tem[0].length;
		i=(int)(Math.random()*i);
		j=(int)(Math.random()*j);
		Geometry temGeo=tem[i][j];
		if(temGeo.getNumControls()>0) return;
		temGeo.setMaterial(this.randomMat());
		new RotateControl(tem[i][j],FastMath.PI,0,0,20);		
	}
	

	
	private Material randomMat(){
		Material mat= material.clone();
		ColorRGBA color=ColorRGBA.randomColor();
		mat.setColor("Diffuse", color);
//		mat.setColor("Specular", color);

		return mat;
	}
	
	private Geometry[][] geoArray(Geometry geometry,Vector3f xDir,Vector3f yDir,int xNum,int yNum){
		Geometry[][] geos=new Geometry[yNum][xNum];
		for (int i=0;i<yNum;i++){
			for (int j=0;j<xNum;j++){
				Geometry geo=geometry.clone();
				geo.setMaterial(this.material);
				geo.move(xDir.mult(j).add(yDir.mult(i)));
				this.attachChild(geo);
				geos[i][j]=geo;
			}
		}
		return geos;
	}
}
