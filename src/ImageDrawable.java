
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
	public String m_filename;
	public int m_imageMode = CORNERS;
	
	public ImageDrawable(PApplet parent, String objectName) {
		super(parent, objectName);
	}
	public ImageDrawable(PApplet parent, String objectName, String filename) {
		this(parent, objectName);
		m_image = p.loadImage(filename);
		m_filename = filename;
	}
	public void doDraw() {
		if (m_image == null)
			return;

		p.imageMode(CORNERS);	// we'll do our own calcs, thanks
		switch (m_imageMode) {
			case CORNERS: 
				//p.image(m_image, 0, 0, m_image.width, m_image.height);
				break;
			
			case CENTER: 		
				int halfWidth = m_image.width / 2;
				int halfHeight = m_image.height / 2;
				p.translate(-halfWidth, -halfHeight, 0); 
				break;
			
			default: 
				log("Illegal imageMode: " + PApplet.str(m_imageMode), LogLevel.ERROR);
				assert(false);
				break;
		}

		flipY(m_image.height);
		p.image(m_image, 0, 0, m_image.width, m_image.height);
	}
}