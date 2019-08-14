package myAnimation;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import myProgram.Option;

public class RotateControl extends AbstractControl{
	float currentFrametime = 0;
	float frameTime=1f/Option.fps;
	Spatial geo;
	float vx,vy,vz;
	int time;
	int framecount=0;
	int delay=0;
	
	public RotateControl(Spatial geo,float x,float y,float z,int time){
		this.geo=geo;
		this.vx=x/time;
		this.vy=y/time;
		this.vz=z/time;
		this.time=time;
		geo.addControl(this);
	};
	public RotateControl(Spatial geo,float x,float y,float z,int time,int delay){
		this(geo, x, y, z, time);
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
				geo.rotate(vx, vy, vz);
			}else{
				geo.removeControl(this);
				return;
			}
		}
	}

}
