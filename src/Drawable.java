
import processing.core.*; 
import processing.event.*; 

public abstract class Drawable implements PConstants {
	public PApplet p;				// only member variable we don't prefix with m_
	public String m_className;
	public String m_objectName = "";
	
	public Drawable m_parent;		// our parent in the scene graph
	public Drawable	m_children[];
	
	public boolean m_isVisible = true;
	public PVector m_position;
	public PVector m_scale;
	public PVector m_rotation;
	public PMatrix3D m_transform;
	public MBoundingBox m_bbox;		// bounding box

	protected AxisMarkerDrawable m_axisMarker;
	public boolean m_showAxisMarker = false;

	public LogLevel m_logLevel = LogLevel.WARNING;			// logging level
	public enum LogLevel {
		NONE,
		ERROR,
		WARNING,
		INFO,
	};
		
	public Drawable(PApplet papp, String name) {
		p = papp;
		m_objectName	= name;
		m_className = getClass().getName();
		
		/*
		 * jdm sez: it's nice that Processing provides these callbacks,
		 * but I'd much rather have them called in scenegraph order, in
		 * the context of the parent transforms and such.
		p.registerMethod("setup", this);
		p.registerMethod("dispose", this);

		p.registerMethod("pre", this);
		p.registerMethod("draw", this);
		p.registerMethod("post", this);

		p.registerMethod("mouseEvent", this);
		p.registerMethod("keyEvent", this);
		
		p.registerMethod("stop", this);
		p.registerMethod("pause", this);
		p.registerMethod("resume", this);
		*/
		
		m_position = new PVector(0f,0f,0f);
		m_scale = new PVector(1f,1f,1f);
		m_rotation = new PVector(0f,0f,0f);
		m_transform = new PMatrix3D();
		recomputeMatrix();
		
		m_axisMarker = new AxisMarkerDrawable(p, name);
	}
	
	// Transform ------------------------------------
	void recomputeMatrix() {
		m_transform.reset();
		m_transform.translate(m_position.x, m_position.y, m_position.z);
		m_transform.rotateX(m_rotation.x);
		m_transform.rotateY(m_rotation.y);
		m_transform.rotateZ(m_rotation.z);
		m_transform.scale(m_scale.x, m_scale.y, m_scale.z);
	}
	public PVector	getPosition() {
		return m_position;
	}
	public void		setPosition(PVector pos) {
		m_position = pos;
		recomputeMatrix();
	}
	public PVector	getScale() {
		return m_scale;
	}
	public void 	setScale(PVector scale) {
		m_scale = scale;
		recomputeMatrix();
	}
	public PVector	getRotation() {
		return m_rotation;
	}
	public void		setRotation(PVector rotation) {
		m_rotation = rotation;
		recomputeMatrix();
	}
	
	/* flipY - make Y axis point upwards, as Dog intended
		otherwise images, etc will be upside down
		NOTE that this works as a toggle - you can flipY to
		draw text, for example, then flipY again to draw something else.
		Just remember that when we ask Processing to draw, it assumes
		that Y is the opposite of what we want (a proper RH coord system
		with the Cartesian origin in the lower-left corner.
	*/
	public void flipY(int height) {
		p.translate(0, height, 0);
		p.scale(1.0f, -1.0f, 1.0f);        					
	}
	//------------------------------------------------
	
	public void log(String msg, LogLevel level) {
		System.out.println(m_className + ": " + msg + " " + m_objectName);
	}

	public void setup() {
		//log("::setup");
	}
	public void dispose() {
		p = null;
		//log("::dispose");
	}
	
	public void pre() {
		//log("::pre");		
	}
	public abstract void doDraw();			// must be implemented by subclasses
	public void draw() {
		if (m_isVisible != true)
			return;
			
		//log("::draw");
		
		p.pushMatrix();
			p.applyMatrix(m_transform);
			if (m_showAxisMarker == true)
				m_axisMarker.draw();
			doDraw();				// subclass, draw thyself
		p.popMatrix();
	}
	public void post() {
		//log("::post");				
	}
	
	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		//log(String.format("::mouseEvent(x=%d, y=%d)", x, y));		
	
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
		//log("::keyEvent");				
	}
	public void touchEvent(TouchEvent event) {
		//log("::touchEvent");				
	}
	
	public void stop() {
		//log("::stop");			
	}
	
	// Android only
	public void pause() {
		//log("::pause");				
	}

	// Android only
	public void resume() {
		//log("::resume");				
	}
}