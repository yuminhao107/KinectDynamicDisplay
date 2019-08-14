package myAnimation;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

import myProgram.Option;

public class ScaleControl extends AbstractControl{
	float currentFrametime = 0;
	float frameTime=1f/Option.fps;
	Spatial geo;
	int time;
	float x,y,z,vx,vy,vz,xt,yt,zt;
	Node node;
	int framecount=0;
	int delay=0;
	boolean ini=false;
	
	public ScaleControl(Spatial geo,float xt,float yt,float zt,int time){
		this.geo=geo;
		this.xt=xt;
		this.yt=yt;
		this.zt=zt;
		this.time=time;
		geo.addControl(this);
	};
	public ScaleControl(Spatial geo,float xt,float yt,float zt,int time,int delay){
		this(geo, xt, yt, zt, time);
		this.delay=delay;
	}
	
		
	
	
	public ScaleControl(Spatial geo,float x,int time){
		this(geo,x,x,x,time);
	}
	public ScaleControl(Spatial geo,float x,int time,int delay){
		this(geo, x, time);
		this.delay=delay;
	}
	public ScaleControl(Spatial geo,Node node,int time){
		this(geo,0.001f,0.001f,0.001f,time);
		this.node=node;
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
			if (!ini){
				Vector3f tem=geo.getLocalScale();
				this.x=tem.getX();
				this.y=tem.getY();
				this.z=tem.getZ();
				vx=(xt-x)/time;
				vy=(yt-y)/time;
				vz=(zt-z)/time;
				ini=true;
			}
				
			if (time>0){
				--time;
				x+=vx;
				y+=vy;
				z+=vz;
				geo.setLocalScale(x, y, z);
			}else{
				geo.removeControl(this);
				if (node!=null) node.detachChild(geo);
				return;
			}
		}
	}

}
