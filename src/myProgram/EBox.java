package myProgram;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class EBox extends Node {
	public Vector3f pos;
	public float x,y,z;
	public Geometry geo;
	public Node geos;
	public Material material4geos;
	
	private float scale=1f;
	private float scaleRate=0.995f;
	private final float maxSc=1f;
	private final float minSc=0.5f;
	
	public EBox(Node father,String name,Vector3f pos ,float x,float y,float z){
		super(name);
		father.attachChild(this);
		this.pos=pos;
		this.x=x;
		this.y=y;
		this.z=z;
	}
	
	public void setMat(Material mat){
		geo.setMaterial(mat);
	}
	
	public void iniBox(Material m){
		Box cube1Mesh = new Box( x/2,y/2,z/2);
	    geo = new Geometry(this.getName()+"Box", cube1Mesh);
	    geo.move(pos.add(x/2, y/2, z/2));
	    geo.setMaterial(m);
	    this.attachChild(geo);
	}
	
	public void split(float lenght,Material material){
		this.material4geos=material;
//		this.detachChild(geo);
		
		int xNum=(int)Math.floor(x/lenght);
		if (xNum<1) xNum=1;
		int yNum=(int)Math.floor(y/lenght);
		if (yNum<1) yNum=1;
		int zNum=(int)Math.floor(z/lenght);
		if (zNum<1) zNum=1;

		geos=new Node("");
		this.attachChild(geos);
		
		float xx=x/xNum;
		float yy=y/yNum;
		float zz=z/zNum;
		Box box1mesh = new Box(xx/2, yy/2, zz/2);
		for (int i = 0; i < xNum; i++)
			for (int j = 0; j < yNum; j++)
				for (int k = 0; k < zNum; k++) {
					Vector3f target = pos.clone().add(new Vector3f(xx*i+xx/2,yy*j+yy/2,zz*k+zz/2));
//					System.out.println(" " +target.x+" " +target.y+" " +target.z);

					Geometry box1geo = new Geometry(this.getName()+"BoxNo" + i * yNum * zNum + j * zNum + k, box1mesh);
					
					box1geo.move(target);
					box1geo.setMaterial(material4geos);
					
					geos.attachChild(box1geo);
				}
		
		
	}
	
	public void updateAnimation(float tpf){
		if (geos==null) return;
		if (scale>maxSc && scaleRate>1) scaleRate=2-scaleRate;
		if (scale<minSc && scaleRate<1) scaleRate=2-scaleRate;
		scale*=scaleRate;
		for (Spatial geo:geos.getChildren()){
			geo=(Geometry) geo;
			geo.setLocalScale(scale);
//			System.out.println(scale);
		}
	}

}
