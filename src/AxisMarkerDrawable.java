
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


public class AxisMarkerDrawable extends Drawable {
	public PImage m_image;
	public boolean billboard = false;
	
	public AxisMarkerDrawable(PApplet parent, String objectName) {
		super(parent, objectName);
	}
	
	public void doDraw() {
		/*
		 * the point of this Drawable is to figure out where we're at
		 * in Processing's  3D space.  It has a weird coordinate system,
		 * essentially a left-hand coord system with origin in the upper left corner.
		 * i.e.:
		 * 		X increases to the right
		 * 		Y increases down
		 * 		Z increases towards the user
		 * 
		 * So here in doDraw(), we wanna put a red marker pointing at +X,
		 * a green marker pointing +Y and a blue marker pointing +Z.
		 * 
		 * Rotations are specified in RADIANS and are CLOCKWISE 
		 * looking down the axis FROM the origin.
		 */
		
		this.p.noStroke();
		
		this.p.pushMatrix();
			this.p.fill(255, 255, 255);
			this.p.translate(110, 0, 0);			// position beyond cylinder, drawn below
			this.p.scale(1.0f, -1.0f, 1.0f);   // flip because of stupid Y axis pointing down

			if (billboard) {
				// billboard, to face the camera

				float rotX = ((windField) this.p).rotX;
				float rotY = ((windField) this.p).rotY;
				//PApplet.println("xrot= " + PApplet.str(rotX) + "yrot= " + PApplet.str(rotY) );
				this.p.rotateX(rotY);
				this.p.rotateZ(rotX);
				this.p.rotateX(-PI/2f);
			}
			this.p.text(this.objectName, 0, 0);
		this.p.popMatrix();
		
		// x
		this.p.pushMatrix();
			this.p.rotateZ(-PI / 2.0f);	// rotate along +Z by -90 degrees for X axis
			this.p.fill(255, 0, 0);
			drawCylinder(10, 10, 100, 50); // Draw a cylinder
		this.p.popMatrix();
			
		// y
		this.p.pushMatrix();
			this.p.fill(0, 255, 0);			// no rotation needed for Y, given how we draw the cyl
			drawCylinder(10, 10, 100, 50); // Draw a cylinder
		this.p.popMatrix();

		// z
		this.p.pushMatrix();
			this.p.rotateX(PI / 2.0f);	// rotate along +X by 90 degrees for Z axis
			this.p.fill(0, 0, 255);
			drawCylinder(10, 10, 100, 50); // Draw a cylinder
		this.p.popMatrix();

	}
	
	public void drawCylinder(float topRadius, float bottomRadius, float tall, int sides) {
		float angle = 0;
		float angleIncrement = TWO_PI / sides;
		this.p.beginShape(QUAD_STRIP);
		for (int i = 0; i < sides + 1; ++i) {
			this.p.vertex(topRadius * PApplet.cos(angle), 0, topRadius * PApplet.sin(angle));
			this.p.vertex(bottomRadius * PApplet.cos(angle), tall, bottomRadius * PApplet.sin(angle));
			angle += angleIncrement;
		}
		this.p.endShape();

		// If it is not a cone, draw the circular top cap
		if (topRadius != 0) {
			angle = 0;
			this.p.beginShape(TRIANGLE_FAN);

			// Center point
			this.p.vertex(0, 0, 0);
			for (int i = 0; i < sides + 1; i++) {
				this.p.vertex(topRadius * PApplet.cos(angle), 0, topRadius * PApplet.sin(angle));
				angle += angleIncrement;
			}
			this.p.endShape();
		}

		// If it is not a cone, draw the circular bottom cap
		if (bottomRadius != 0) {
			angle = 0;
			this.p.beginShape(TRIANGLE_FAN);

			// Center point
			this.p.vertex(0, tall, 0);
			for (int i = 0; i < sides + 1; i++) {
				this.p.vertex(bottomRadius * PApplet.cos(angle), tall, bottomRadius * PApplet.sin(angle));
				angle += angleIncrement;
			}
			this.p.endShape();
		}
	}
}