
import processing.core.*;

public class windField extends PApplet {

	int WINDOW_X = 1024;
	int WINDOW_Y = 1280;
	int WINDOW_X_2 = WINDOW_X / 2;
	int WINDOW_Y_2 = WINDOW_Y / 2;

	static String fileName = "/data/Perlan/wind.csv";

	float rot = 0.0f;
	float rotX = 0.0f;
	float rotY = 0.0f;

	static float PI_2 = PI / 2.0f;
	static float PI_4 = PI / 4.0f;
	static float PI_8 = PI / 8.0f;
	
	float theAltitude = 1000;
	int colorMode = 0;
	float xZoom = 0;
	float zZoom = 0;

	boolean ortho = false;

	TextDrawable[] altLabel;
	ImageDrawable floor;
	VectorFieldDrawable vf;

	public void settings() {
		// CAN NOT use size() with variables in setup() but seems okay in settings()
		size(WINDOW_X, WINDOW_Y, P3D);
	}

	public void setup() {
		floor = new ImageDrawable(this, "Floor");
		floor.m_image = loadImage("/data/Perlan/staticmap_cropped.png");
		print("Floor image size=(" + str(floor.m_image.width) + ", " + str(floor.m_image.height) + ")");
		floor.setPosition(new PVector(-floor.m_image.width, -floor.m_image.height, 0.0f));
		floor.setScale(new PVector(2f, 2f, 2f));

		this.vf = new VectorFieldDrawable(this, "vectorField");
		this.vf.load(fileName);
		
		this.altLabel = new TextDrawable[21];
		for (int i=0; i <= 20; i++) {
			altLabel[i] = new TextDrawable(this, str(i));
			altLabel[i].string = str(i * 1000);
			altLabel[i].pointSize = 32f;
		}

		surface.setResizable(true);
		smooth(); 
		noLoop();
	}

	public void draw()
	{
		PVector  scale_factor  = new PVector();

		// debug hack
		floor.visible = true;

		background(0, 0, 0);
		//lights();

		if (ortho == true) {
			ortho(-WINDOW_X_2, WINDOW_X_2, -WINDOW_Y_2, WINDOW_Y_2);
			translate(0, -200, 0);
		} else {
			//float fov = PI/3.0; 
			//float cameraZ = (height/2.0) / tan(fov/2.0); 
			perspective(); 
		}
		/*
	    Processing 3D coords are x (left-to-right), y (up-to-down), and z (back-to-front)
	        which makes Processing use a right-hand coord system with the Y axis flipped.

	    Here, we fix the flipped Y axis, so that we have a proper right-hand coord space.
	    HOWEVER, since other things (like text) expect the flipped Y, we have to reverse that
	    when we use them, or text will be upside-down.
		 */
		pushMatrix(); // begin RIGHT-HAND coords
		{
			translate(0, WINDOW_Y, 0);    // move to lower-left corner of screen
			scale(1.0f, -1.0f, 1.0f);        // make Y axis point upwards, as Dog intended

			/*
		     * More fun with coordinate spaces: geo coords, x = LAT, y = LONG, z = ALT,
		     * so, rotate pi/2 around the X axis, which will make:
			 *
	         * Longitude=X=(left-to-right), Latitude=Y=(front-to-back), Altitude=Z(bottom-to-top)
			 */
			pushMatrix();
			{
				translate(WINDOW_X_2, 0, -750); // origin is now in the middle bottom of screen
				rotateX(-PI_2);  // XY is now the horizontal plane; +Z points up


				// add user rotation from mouse
				rotateZ(rotX);
				rotateX(rotY);

				// draw Floor
				floor.draw();
				AxisMarkerDrawable axis3 = new AxisMarkerDrawable(this, "floor axis");
				axis3.draw();

				// scale to fill window
				scale_factor.x = WINDOW_X / deltaLong * 0.8f; // fill the screen 80%
				scale_factor.y = scale_factor.x; // lat and long should use the same scale
				scale_factor.z = WINDOW_Y / deltaAlt * 0.8f; // fill the screen 80%
				scale(scale_factor.x, scale_factor.y, scale_factor.z);
	
				// add user zoom
				// translate(xZoom, 0, zZoom);
	
				noFill();
				stroke(0x60, 0x60, 0x60, 0x50);
				for (int alt = 0; alt <= 20000; alt += 1000) {
			        //p.translate(-(deltaLong_2), -deltaLat_2, alt);
					altLabel[alt/1000].setPosition(new PVector(-(deltaLong_2), -deltaLat_2, alt));
					altLabel[alt/1000].draw();
	
					/*
					 * pushMatrix(); { // fill((alt % 10000) * 0xFF / 10000); noFill();
					 * strokeWeight(0.5); beginShape(); vertex(-deltaLong_2, -deltaLat_2, alt);
					 * vertex(deltaLong_2, -deltaLat_2, alt); vertex(deltaLong_2, deltaLat_2, alt);
					 * vertex(-deltaLong_2, deltaLat_2, alt); vertex(-deltaLong_2, -deltaLat_2,
					 * alt); endShape(CLOSE); } popMatrix();
					 */
				}


				}
			} popMatrix(); // end geo coords

		} popMatrix(); // end right-hand coords

	}

	public void mouseDragged() {
		rotX += ((mouseX - pmouseX) * PI / WINDOW_X);
		rotY += ((mouseY - pmouseY) * PI / WINDOW_Y);

		/*
		 * println("mouse=(" + str(mouseX) + ", " + str(mouseY) + ") " + "pmouse=(" +
		 * str(pmouseX) + ", " + str(pmouseY) + ") " + "rotX=" + str(rotX) + " rotY=" +
		 * str(rotY));
		 */
		redraw();
	}

	static final int ZOOM = 100;

	public void keyPressed() {
		println("pressed " + PApplet.parseInt(key) + " " + keyCode);
		switch (key) {
		case '+':
		case '=':
			theAltitude += 1000.0f;
			break;
		case '-':
		case '_':
			theAltitude -= 1000.0f;
			break;
		case 'c':
			colorMode = (++colorMode > 6) ? 0 : colorMode;
			break;
		case 'o':
			ortho = ortho == false;
			break;
			/*
			 * case 37: // left arrow cameraX -= ZOOM; break; case 39: // right arrow
			 * cameraX += ZOOM; break; case 38: // up arrow cameraZ += ZOOM; break; case 40:
			 * // down arrow cameraZ -= ZOOM; break;
			 */

		}
		theAltitude = (theAltitude > maxAlt) ? maxAlt : theAltitude;
		theAltitude = (theAltitude < minAlt) ? minAlt : theAltitude;
		redraw();
	}


	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "windField" };
		PApplet.main(concat(appletArgs, passedArgs));
	}
}
