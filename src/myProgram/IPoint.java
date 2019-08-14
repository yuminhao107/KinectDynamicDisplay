package myProgram;

import igeo.IVec;

public class IPoint {
	PointCloudGeo father;
	private IVec pos;
	private IVec normal;

	private IPoint up, down, left, right;

	private IFace face,preFace;
	
	public double mindist=Option.findFacePoint2FaceDistMax;
	
	int x,y;
	
	boolean visited=false;
	
	boolean isUseful = true;

	public IPoint() {

	}

	public IPoint(IVec pos,int x,int y,PointCloudGeo father) {
		this.x=x;
		this.y=y;
		this.father=father;
		if (pos == null){
			isUseful=false;
			return;
		}
		this.pos = pos.cp();
	}

	public IPoint(IVec pos, IPoint up, IPoint down, IPoint left, IPoint right) {
		this.pos = pos.cp();
		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}
	
	public void link(){
		if (y>0) up=father.pts[y-1][x];
		if (x>0) left=father.pts[y][x-1];
		if (y<Option.depthNumY-1) down=father.pts[y+1][x];
		if (x<Option.depthNumX-1) right=father.pts[y][x+1];
	}

	public void setUp(IPoint vec) {
		this.up = vec;
	}

	public void setDown(IPoint vec) {
		this.down = vec;
	}

	public void setLeft(IPoint vec) {
		this.left = vec;
	}

	public void setRight(IPoint vec) {
		this.right = vec;
	}

	public IPoint up() {
		return up;
	}

	public IPoint down() {
		return down;
	}

	public IPoint left() {
		return left;
	}

	public IPoint right() {
		return right;
	}

	public void setFace(IFace face) {
		this.face = face;
	}
	
	public void changeFace(IFace face){
		if (this.face!=null){
			this.face.points.remove(this);
		}
		this.face=face;
	}

	public IFace getFace() {
		return this.face;
	}

	public IVec pos() {
		return this.pos;
	}
	
	public boolean isUseful(){
		if (pos!=null) return true;
		return false;
	}
  
    public void printlnXY(){
	System.out.println(""+x+"  "+y);
	}
    
    public void printlnPos(){
    	if (isUseful){
    		System.out.println(pos.x()+" "+pos.y()+" "+pos.z());
    	}else{
    		System.out.println("position is null");
    	}
    }
    
    public boolean needVisit(){
    	return (this.pos()!=null && (!this.visited) && this.face==null);
    }
}
