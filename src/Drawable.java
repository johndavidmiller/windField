
import processing.core.*; 
import processing.event.*; 


public class Drawable implements PConstants {
	public enum LogLevel {
		NONE,
		ERROR,
		WARNING,
		INFO,
	};

	PApplet p;
	String className = this.getClass().getName();
	String objectName = "";

	LogLevel logLevel = LogLevel.WARNING;			// debug level

	PVector pos;
	PVector scale;
	PVector rotation;
	PMatrix3D mat;

	
	public Drawable(PApplet parent, String objectName) {
		this.p 			= parent;
		this.objectName	= objectName;
		
		/*
		 * jdm sez: it's nice that Processing provides these callbacks,
		 * but I'd much rather have them called in scenegraph order, in
		 * the context of the parent transforms and such.
		this.p.registerMethod("setup", this);
		this.p.registerMethod("dispose", this);

		this.p.registerMethod("pre", this);
		this.p.registerMethod("draw", this);
		this.p.registerMethod("post", this);

		this.p.registerMethod("mouseEvent", this);
		this.p.registerMethod("keyEvent", this);
		
		this.p.registerMethod("stop", this);
		this.p.registerMethod("pause", this);
		this.p.registerMethod("resume", this);
		*/
		
		pos = new PVector(0f,0f,0f);
		scale = new PVector(1f,1f,1f);
		rotation = new PVector(0f,0f,0f);
		mat = new PMatrix3D();
		this.recomputeMatrix();
	}
	
	public void log(String msg, LogLevel level) {
		System.out.println(this.className + msg + " " + this.objectName);
	}

	// Transform ------------------------------------
	void recomputeMatrix() {
		this.mat.reset();
		this.mat.translate(this.pos.x, this.pos.y, this.pos.z);
		this.mat.rotateX(this.rotation.x);
		this.mat.rotateY(this.rotation.y);
		this.mat.rotateZ(this.rotation.z);
		this.mat.scale(this.scale.x, this.scale.y, this.scale.z);
	}
	public PVector getPos() {
		return pos;
	}
	public void setPos(PVector pos) {
		this.pos = pos;
		this.recomputeMatrix();
	}
	public PVector getScale() {
		return scale;
	}
	public void setScale(PVector scale) {
		this.scale = scale;
		this.recomputeMatrix();
	}
	public PVector getRotation() {
		return rotation;
	}
	public void setRotation(PVector rotation) {
		this.rotation = rotation;
		this.recomputeMatrix();
	}
	//------------------------------------------------
	

	public void setup() {
		//this.log("::setup");
	}
	public void dispose() {
		this.p = null;
		//this.log("::dispose");
	}
	
	public void pre() {
		//this.log("::pre");		
	}
	public boolean visible = true;
	public void doDraw() {};			// must be implemented by subclasses
	public void draw() {
		if (this.visible != true)
			return;
			
		//this.log("::draw");
		
		this.p.pushMatrix();
			this.p.applyMatrix(this.mat);
			doDraw();				// subclass, draw thyself
		this.p.popMatrix();
	}
	public void post() {
		//this.log("::post");				
	}
	
	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		//this.log(String.format("::mouseEvent(x=%d, y=%d)", x, y));		
	
		switch (event.getAction()) {
	    		case MouseEvent.PRESS:
	    			// do something for the mouse being pressed
	    			break;
	    		case MouseEvent.RELEASE:
	    			// do something for mouse released
	    			break;
	    		case MouseEvent.CLICK:
		    		// do something for mouse clicked
		    		break;
	    		case MouseEvent.DRAG:
	    			// do something for mouse dragged
	    			break;
	    		case MouseEvent.MOVE:
	    			// do something for mouse moved
	    			break;
		}
	}
	public void keyEvent(KeyEvent event) {
		//this.log("::keyEvent");				
	}
	public void touchEvent(TouchEvent event) {
		//this.log("::touchEvent");				
	}
	
	public void stop() {
		//this.log("::stop");			
	}
	
	// Android only
	public void pause() {
		//this.log("::pause");				
	}

	// Android only
	public void resume() {
		//this.log("::resume");				
	}
}