package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Cengceng extends MyBoxNode {
	float levelHeight=20;
	int animeTime=60;
	int duration=10;
	int time=0;
	
	int level;
	int id=0;
	Geometry boxGeo;
	float zScale=0.9f;
	
	

	public Cengceng(SimpleApplication father, Vector3f pos, float x, float y, float z, float ang) {
		super(father, pos, x, y, z, ang);
	}
	
	public void setFinalZScale(float z){
		zScale=z;
	}

	@Override
	public void iniAnimation() {
		// TODO Auto-generated method stub
		level=(int)(height/levelHeight)+1;
		levelHeight=height/level;
		Box box=new Box(length/2,width/2,levelHeight/2);
		boxGeo=new Geometry("",box);
		boxGeo.move(0,0,levelHeight/2);
		if (material==null){
			material = new Material(father.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			material.setColor("Color", ColorRGBA.White);
		}
		boxGeo.setMaterial(material);
		father.getRootNode().attachChild(this);
		this.rotate(0, 0, ang);
		this.setLocalTranslation(pos);
	}

	@Override
	public void updateAnimation() {
		// TODO Auto-generated method stub
		time++;
		if (time<duration){
			return;
		}else{
		  time-=duration;
		}
	
		if (id<level){
			Geometry geo=boxGeo.clone();
			geo.move(0,0,id*levelHeight);
			float tem=levelHeight*10;
			if ((id+10)*levelHeight>height){
				tem=(float) (height-(id+0.5)*levelHeight);
			}
			Vector3f vec=new Vector3f(0,0,tem);
			geo.move(vec);
			geo.setLocalScale(0.01f);
			new ScaleControl(geo,1,1,zScale,animeTime);
			new MoveControl(geo,vec.mult(-1),animeTime);
			this.attachChild(geo);
			++id;
		}
	}

}
