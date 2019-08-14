package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class PureBox extends MyBoxNode{
	Material material;
	public PureBox(SimpleApplication father, Vector3f pos, float x, float y, float z, float ang) {
		super(father, pos, x, y, z, ang);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void iniAnimation() {
		// TODO Auto-generated method stub
		Box box=new Box(length/2,width/2,height/2);
		Geometry boxGeo=new Geometry("",box);
		boxGeo.move(0,0,height/2);
		
		if (material==null){
			material = new Material(father.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			material.setColor("Color", ColorRGBA.White);
		}
		boxGeo.setMaterial(material);
		this.attachChild(boxGeo);
	}
	
	public void setMat(Material mat){
		this.material=mat;
	}

	@Override
	public void updateAnimation() {
		// TODO Auto-generated method stub
		
	}

}
