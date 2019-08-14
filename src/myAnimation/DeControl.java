package myAnimation;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import myProgram.Option;

public class DeControl extends AbstractControl{
	
	float currentFrametime = 0;
	float frameTime=1f/Option.fps;
	Spatial geo;
	Node node;
	int time;
	
	public DeControl(Spatial geo,Node node,int time){
		this.time=time;
		this.geo=geo;
		this.node=node;
		geo.addControl(this);
	}
	
	protected void controlUpdate(float tpf) {
		currentFrametime += tpf;
		while (currentFrametime > frameTime) {
			currentFrametime = currentFrametime - frameTime;

			if (time>0){
				--time;
			}else{
				geo.removeControl(this);
				node.detachChild(geo);
				
			}
		}
	}

	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

}
