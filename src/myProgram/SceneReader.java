package myProgram;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;

public class SceneReader extends Thread{
    Kinect kinect;
    PointCloudGeo geo;
    SimpleApplication father;
    public boolean faceChanged=false;
    ArrayList<IFace> faces;
    
    public SceneReader(SimpleApplication father){
    	kinect=new Kinect();
    	kinect.start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
    	this.father=father;
    }
    
    public void run(){
    	while(true){
    		try {
				this.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		geo=kinect.createGeo();
    		if (geo==null) continue;
    		System.out.println("Depthmap recived. Cal...");
    		Long time=System.currentTimeMillis();
    		geo.findFaces();
    		geo.combineFaces();
    		faces=geo.faces;
    		faceChanged=true;
    		time=System.currentTimeMillis()-time;
    		System.out.println("Calculate done cost "+time/1000f+" s");
    		
     	}
    }
    
    public synchronized ArrayList<IFace> getFaces(){
    	faceChanged=false;
    	return faces;
    }
    
    public void stopKinect(){
    	kinect.stop();
    }
    
    public IVec nearDepth(IVec pos){
        if (geo==null) return null;
        double min=Double.MAX_VALUE;
        IVec res=null;
        for (IPoint pt:geo.points()){
        	if (pt.pos()==null) continue;
        	double dist=pt.pos().dist(pos);
        	if (dist<min){
        		min=dist;
        		res=pt.pos();
        	}
        }
        return res;
    }
    
    public PointCloudGeo getGeo(){
    return geo;
    }
}
