
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


public class ImageDrawable extends Drawable {
	public PImage m_image;
	
	public ImageDrawable(PApplet parent, String objectName) {
		super(parent, objectName);
	}
	
	public void doDraw() {
    	  	//floor.resize(0,15);
  	    //translate(-floor.width / 2.0f, -floor.height / 2.0f, -100f); // origin is now in the middle bottom of screen
    	  	p.image(m_image, 0, 0);
	}
}