package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import myProgram.Option;

public class CengCeng2 extends MyBoxNode{
	float levelHeight=20;
	float fallSpeed=60;
	float totalTime=3;
	
	int animeTime=60;
	int duration=30;
	
	int scaleTime=30;
	int pushTime=(int)(levelHeight/fallSpeed*Option.fps);
	
	int time=duration;
	int frameCount=0;
	
	
	int level;
	int id=0;
	Geometry boxGeo;
	Geometry last;
	boolean upFaceDone=false;
	boolean isDone=false;
	
	
	
	

	public CengCeng2(SimpleApplication father, Vector3f pos, float x, float y, float z, float ang) {
		super(father, pos, x, y, z, ang);
	}
	
	public CengCeng2(SimpleApplication father,MyBoxNode box){
		this(father,new Vector3f(0,0,0),box.length,box.width,box.height,0);
	}

	@Override
	public void iniAnimation() {
		// TODO Auto-generated method stub
		level=(int)(height/levelHeight)+1;
		levelHeight=height/level;
		Box box=new Box(length/2,width/2,levelHeight/2);
		boxGeo=new Geometry("",box);
		boxGeo.move(0,0,height);
		boxGeo.setLocalScale(1f,1f,0.001f);
		if (material==null){
			iniMat();
		}
		boxGeo.setMaterial(material);
		if (node==null){
		father.getRootNode().attachChild(this);
		this.rotate(0, 0, ang);
		this.setLocalTranslation(pos);
		}
		
		duration=(int) (totalTime*Option.fps/level);
		fallSpeed=(float) (levelHeight/duration*(3+Math.random()*2));
		
	}
	
	public boolean isDone(){
		return isDone;
	}

	@Override
	public void updateAnimation() {
		// TODO Auto-generated method stub
		if (last!=null){
			if (last.getNumControls()==0) isDone=true;
		}
		if (frameCount==0){
			Geometry geo=boxGeo.clone();
			geo.setLocalScale(0.001f, 0.001f, 0.001f);
			new ScaleControl(geo,1f,1f,0.001f,scaleTime);
			this.attachChild(geo);
		}
		if (frameCount<scaleTime){
			++frameCount;
			return;
		}
		time++;
		if (time%duration==0){
		if (id<level){
			Geometry geo=boxGeo.clone();
			float fallDist=(level-1-id)*levelHeight;
			int fallTime=(int) (fallDist/fallSpeed);
			new ScaleControl(geo,1f,1f,1f,pushTime);
			new MoveControl(geo,new Vector3f(0,0,-levelHeight/2),pushTime);
			new MoveControl(geo,new Vector3f(0,0,-fallDist),fallTime,pushTime);
			this.attachChild(geo);
			if (id==level-1) last=geo;
			++id;
		}
	}
	}

}
