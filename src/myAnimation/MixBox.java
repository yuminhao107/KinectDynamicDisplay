package myAnimation;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class MixBox extends MyBoxNode{
	Mosaic mosaic;
	CengCeng2 cengceng;

	public MixBox(SimpleApplication father, Vector3f pos, float x, float y, float z, float ang) {
		super(father, pos, x, y, z, ang);
		// TODO Auto-generated constructor stub
	}
	
	public MixBox(SimpleApplication father,MyBox box){
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
		
	}

	@Override
	public void updateAnimation() {
		if (cengceng.isDone){
			System.out.println("new mosaic");
			if (mosaic==null){
				this.detachChild(cengceng);
				mosaic=new Mosaic(father,this);
//				if (material!=null) mosaic.setMat(material);
				mosaic.iniAnimation();
				this.attachChild(mosaic);
			}
			mosaic.updateAnimation();
		}else{
			System.out.println("updata");
			cengceng.updateAnimation();
		}
		
	}
	public void setMat(Material mat){
		this.material=mat;
	}


}
