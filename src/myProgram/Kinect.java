package myProgram;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import igeo.IVec;

public class Kinect extends J4KSDK{
	
	final int depthmapXNum=512;
	final int depthmapYNum=424;
	
	final double camera_factor = 1;
	final double camera_cx = 256;//261.96
	final double camera_cy = 207.6;
	final double camera_fx = 366.6;//366.6 378.6
	final double camera_fy = 366.6;//366.6 373.6

	@Override
	public void onColorFrameEvent(byte[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] arg0, float[] arg1, float[] arg2, byte[] arg3) {
		// TODO Auto-generated method stub
		
	}
	
	public PointCloudGeo createGeo(){
		short[][] tem=this.getDepthmapArray();
		if (tem==null) return null;
		if (Option.enbleMediaFilter)
		tem=this.mediaFilter(tem, 1);
		IVec[][] vecs=short2IVec(tem);
		return new PointCloudGeo(vecs);
	}
	
	public short[][] getDepthmapArray() {
		short[] depth = this.getDepthFrame();
		if (depth==null) return null;
		short[][] results = new short[depthmapYNum][depthmapXNum];
		for (int i = 0; i < this.depthmapYNum; i++)
			for (int j = 0; j < this.depthmapXNum; j++) {
//				System.out.println(""+i+"   "+j);
				results[i][j] = depth[depthmapXNum-1-j  + i * depthmapXNum];
			}
		return results;
	}

	public IVec[][] short2IVec(short[][] pts) {
		IVec[][] results = new IVec[depthmapYNum][depthmapXNum];
		for (int i = 0; i < this.depthmapYNum; i++)
			for (int j = 0; j < this.depthmapXNum; j++) {
				short value = pts[i][j];
				if (value > 0) {
					double z = value / camera_factor;
					double x = (j - camera_cx) * z / camera_fx;
					double y = (i - camera_cy) * z / camera_fy;
					IVec tem=new IVec(x,y,z);
					results[i][j] = Mymath.depthCamera2World(tem);
					
				}
			}
		return results;
	}

	// range=1 3*3 ;range=2 5*5 etc..
	public short[][] mediaFilter(short[][] pts, int range) {
		short[][] results = pts.clone();
		int filterSize = range * 2 + 1;
		int num = filterSize * filterSize;
		int target = num / 2;
		short[] tem = new short[num];
		for (int i = range; i < depthmapYNum - range; i++)
			for (int j = range; j < depthmapXNum - range; j++) {

				for (int ii = 0; ii < filterSize; ii++)
					for (int jj = 0; jj < filterSize; jj++) {
						tem[ii * filterSize + jj] = pts[i - range + ii][j - range + jj];
					}

				for (int m = 0; m < target + 1; m++) {
					int min = m;
					for (int n = m + 1; n < num; n++) {
						if (tem[min] > tem[n])
							min = n;
					}
					short swapTem = tem[m];
					tem[m] = tem[min];
					tem[min] = swapTem;
				}

				results[i][j] = tem[target];
			}

		return results;
	}

}
