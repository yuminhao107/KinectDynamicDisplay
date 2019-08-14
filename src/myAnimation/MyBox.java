package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;


public class MyBox {
	public  Vector3f pos;
	public  float length,width,height,ang;
	
	public MyBox(Vector3f pos ,float x,float y,float z,float ang){
		this.pos=pos;
		this.length=x;
		this.width=y;
		this.height=z;
		this.ang=ang;
	}
	
	public MyBox(Vector3f upFaceCenter,float length,float width,float ang){
		this.length=length;
		this.width=width;
		this.ang=ang;
		pos=new Vector3f(upFaceCenter.getX(),upFaceCenter.getY(),0);
		height=upFaceCenter.getZ();
	}
	
	public void showBoxInNode(SimpleApplication father){
		Mosaic mosaic=new Mosaic(father,pos,length,width,height,ang);
		mosaic.iniAnimation();
	}
	
	public void printNowSize(){
		System.out.println(length + " " + width + " " + height);
	}

}
