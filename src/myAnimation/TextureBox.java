package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

import myProgram.Option;

public class TextureBox extends MyBoxNode{
	CengCeng2 cengceng;
	Geometry geo;
	float delta=0.1f;
	int scTime;
	int moveTime;
	float speed=100;
	int frameCount=0;
	int minDuration=20*Option.fps;
	int range=30*Option.fps;
	int duration=0;
	
	private int maxNum=16;

	public TextureBox(SimpleApplication father, Vector3f pos, float x, float y, float z, float ang) {
		super(father, pos, x, y, z, ang);
		// TODO Auto-generated constructor stub
	}
	
	public TextureBox(SimpleApplication father,MyBox box){
		this(father,box.pos,box.length,box.width,box.height,box.ang);
	}

	@Override
	public void iniAnimation() {
		// TODO Auto-generated method stub
		father.getRootNode().attachChild(this);
		this.rotate(0, 0, ang);
		this.setLocalTranslation(pos);

		cengceng=new CengCeng2(father,this);
		iniMat();
//		if (material!=null) cengceng.setMat(material);
		cengceng.iniAnimation();
		this.attachChild(cengceng);
		scTime=(int) (length/speed*Option.fps/2);
		moveTime=(int) (height/speed*Option.fps);
		
	}

	@Override
	public void updateAnimation() {
		
		if (cengceng.isDone){
			new DeControl(cengceng,this,scTime+moveTime);
			frameCount++;
			if (frameCount>=duration){
				frameCount=0;
				duration=(int) (minDuration+Math.random()*range);
				if (geo!=null)new DeControl(geo,this,scTime+moveTime-1);
				Box box=new Box(length/2+delta,width/2+delta,height/2+delta);
				geo=new Geometry("box",box);
				geo.setMaterial(randomMat());
				geo.scale(0.001f);
				geo.move(0,0,height);
				new ScaleControl(geo,1,1,0.001f,scTime);
				new ScaleControl(geo,1,1,1,moveTime,scTime);
				new MoveControl(geo,new Vector3f(0,0,-height/2),moveTime,scTime);
				new ScaleControl(geo,length/(length+delta*2),width/(width+delta*2),height/(height+delta*2),1,scTime+moveTime+1);
				this.attachChild(geo);
			}
		}else{
			cengceng.updateAnimation();
		}
	}
	public void setMat(Material mat){
		this.material=mat;
	}
	
	public Material loadMat(int num){
		Material cube1Mat = new Material(father.getAssetManager(),
		        "Common/MatDefs/Misc/Unshaded.j3md");
		    Texture cube1Tex = father.getAssetManager().loadTexture(
		        "Textures/TextureBox/texture ("+num+").jpg");
            
		    cube1Mat.setTexture("ColorMap", cube1Tex);
		    return cube1Mat;
	}
	
	public Material randomMat(){
		int id=(int) (Math.random()*maxNum);
		return loadMat(id);
	}
	



}
