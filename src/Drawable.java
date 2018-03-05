
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
	LogLevel logLevel = LogLevel.INFO;			// debug level

	public Drawable(PApplet parent) {
		this.p = parent;

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
	}
	
	public void log(String msg) {
		if (this.logLevel != LogLevel.NONE) {
			this.log(className + msg);
		}
	}

	public void setup() {
		this.log("::setup");
	}
	public void dispose() {
		this.p = null;
		this.log("::dispose");
	}
	
	public void pre() {
		this.log("::pre");		
	}
	public void draw() {
		this.log("::draw");				
	}
	public void post() {
		this.log("::post");				
	}
	
	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		this.log("::mouseEvent");		
	
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
		this.log("::keyEvent");				
	}
	public void touchEvent(TouchEvent event) {
		this.log("::touchEvent");				
	}
	
	public void stop() {
		this.log("::stop");			
	}
	
	// Android only
	public void pause() {
		this.log("::pause");				
	}

	// Android only
	public void resume() {
		this.log("::resume");				
	}
	
	public void textUp(String s, float x, float y, float z) {
	  this.p.pushMatrix();
	  this.p.translate(x, y, z);
	  this.p.scale(1.0f, -1.0f, 1.0f);
	  this.p.text(s, 0, 0);
	  this.p.popMatrix();
	}
}