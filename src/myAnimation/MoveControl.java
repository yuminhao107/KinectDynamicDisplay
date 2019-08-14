package myAnimation;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import myProgram.Option;

public class MoveControl extends AbstractControl{
	float currentFrametime = 0;
	float frameTime=1f/Option.fps;
	Spatial geo;
	Vector3f v;
	int time;
	int framecount=0;
	int delay=0;
	
	public MoveControl(Spatial geo,Vector3f s,int time){
		this.geo=geo;
		this.v=s.mult(1.0f/time);
		this.time=time;
		this.geo.addControl(this);
	};
	public MoveControl(Spatial geo,Vector3f s,int time,int delay){
		this(geo,s,time);
		this.delay=delay;
	}

	@Override
	protected void controlRender(RenderManager arg0, ViewPort arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void controlUpdate(float tpf) {
		currentFrametime += tpf;
		while (currentFrametime > frameTime) {
			currentFrametime = currentFrametime - frameTime;
			if (framecount<delay){
				++framecount;
				return;
			}
			if (time>0){
				--time;
				geo.move(v);
			}else{
				geo.removeControl(this);
				return;
			}
		}
	}

}
