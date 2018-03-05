
import processing.core.*; 
import processing.opengl.*;
import processing.data.*; 
import processing.event.*; 

import java.awt.datatransfer.*; 
import java.awt.Toolkit; 
import java.awt.Color; 
import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 


public class TextWidget implements PConstants {
	PApplet p;
	
	public TextWidget(PApplet parent) {
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
	public void setup() {
		System.out.println("TextWidget::setup");
	}
	public void dispose() {
		this.p = null;
		System.out.println("TextWidget::dispose");
	}
	
	public void pre() {
		System.out.println("TextWidget::pre");		
	}
	public void draw() {
		System.out.println("TextWidget::draw");				
	}
	public void post() {
		System.out.println("TextWidget::post");				
	}
	
	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();
		System.out.println("TextWidget::mouseEvent");		
	
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
		System.out.println("TextWidget::keyEvent");				
	}
	public void touchEvent(TouchEvent event) {
		System.out.println("TextWidget::touchEvent");				
	}
	
	public void stop() {
		System.out.println("TextWidget::stop");			
	}
	
	// Android only
	public void pause() {
		System.out.println("TextWidget::pause");				
	}

	// Android only
	public void resume() {
		System.out.println("TextWidget::resume");				
	}
	
	public void textUp(String s, float x, float y, float z) {
	  this.p.pushMatrix();
	  this.p.translate(x, y, z);
	  this.p.scale(1.0f, -1.0f, 1.0f);
	  this.p.text(s, 0, 0);
	  this.p.popMatrix();
	}
}