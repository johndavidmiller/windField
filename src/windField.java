
import processing.core.PApplet;
import processing.core.*;

public class windField extends PApplet {

	int WINDOW_X = 1024;
	int WINDOW_Y = 1280;
	int WINDOW_X_2 = WINDOW_X / 2;
	int WINDOW_Y_2 = WINDOW_Y / 2;

	static String fileName = "/data/Perlan/wind.csv";

	public float m_rotx = 0.0f;
	public float m_roty = 0.0f;

	static float PI_2 = PI / 2.0f;
	static float PI_4 = PI / 4.0f;
	static float PI_8 = PI / 8.0f;
	
	public float m_altitude = 1000;
	public int m_colorMode = 0;
	public float m_zoomx = 0;
	public float m_zoomz = 0;

	boolean m_ortho = false;

	ImageDrawable m_floor;
	VectorFieldDrawable m_vectorField;
	SliceArrayDrawable m_sad;

	public void settings() {
		// CAN NOT use size() with variables in setup() but seems okay in settings()
		size(WINDOW_X, WINDOW_Y, P3D);
	}

	public void setup() {
		m_floor = new ImageDrawable(this, "Floor", "/data/Perlan/staticmap_cropped.png");
		print("Floor image size=(" + str(m_floor.m_image.width) + ", " + str(m_floor.m_image.height) + ")");
		m_floor.m_imageMode = CENTER;
		//floor.setPosition(new PVector(-floor.m_image.width, -floor.m_image.height, 0.0f));
		m_floor.setScale(new PVector(2f, 2f, 2f));

		m_vectorField = new VectorFieldDrawable(this, "vectorField");
		m_vectorField.load(fileName);
		m_sad = new SliceArrayDrawable(this, "slice array", m_vectorField);
		
		surface.setResizable(true);
		smooth(); 
		noLoop();
	}

	public void draw()
	{
		//PVector  scale_factor  = new PVector();

		// debug hack
		m_floor.m_isVisible = true;

		background(0, 0, 0);
		//lights();				// TODO: set lights up to mimic the Sun

		if (m_ortho == true) {
			ortho(-WINDOW_X_2, WINDOW_X_2, -WINDOW_Y_2, WINDOW_Y_2);
			translate(0, -200, 0);
		} else {
			//float fov = PI/3.0; 
			//float cameraZ = (height/2.0) / tan(fov/2.0); 
			perspective(); 
		}
		/*
	    Processing 3D coords are x (left-to-right), y (up-to-down), and z (front-to-back)
	        which makes Processing use a left-hand coord system with the origin in the upper right corner:
	        
	        	x: left to right (yay!)
	        	y: top to bottom (wtf?) - ostensibly to make it sane for 2D apps, 
	        								but geez, only if you're doing English text.
	        	z: back to front (yay! - same for a proper OpenGL environment)

	    Here, we fix the flipped Y axis, so that we have a proper right-hand coord space.
	    HOWEVER, since other things (like text) expect the flipped Y, we have to reverse that
	    when we use them, or text will be upside-down. Sigh.
		 */
		pushMatrix(); // begin RIGHT-HAND coords
		{
			translate(0, WINDOW_Y, 0);    	// move origin to lower-left corner of screen
			scale(1.0f, -1.0f, 1.0f);        // make Y axis point upwards, as Dog intended

			AxisMarkerDrawable axisRH = new AxisMarkerDrawable(this, "right hand coords");
			axisRH.draw();

			
			/*
			 * Begin "map" coords - this should be moved to SliceArray
		     * More fun with coordinate spaces: geo coords, x = LAT, y = LONG, z = ALT,
		     * so, rotate pi/2 around the X axis, which will make a RIGHT HAND coord
		     * system laid on its back.
			 *
	         * Longitude=X=(left-to-right), Latitude=Y=(front-to-back), Altitude=Z(bottom-to-top)
			 */
			pushMatrix();
			{
				translate(WINDOW_X_2, 0, -750); // origin is now in the middle bottom of screen
				rotateX(-PI_2);  // XY is now the horizontal plane; +Z points up


				// add user rotation from mouse
				rotateZ(m_rotx);
				rotateX(m_roty);

				// draw Floor
				m_floor.draw();

				/*
				// scale to fill window
				PVector scale_factor = new PVector();
				float deltaLong = m_sad.m_bbox.size.x;
				float deltaAlt = m_sad.m_bbox.size.y;
				scale_factor.x = WINDOW_X / deltaLong * 0.8f; // fill the screen 80%
				scale_factor.y = scale_factor.x; // lat and long should use the same scale
				scale_factor.z = WINDOW_Y / deltaAlt * 0.8f; // fill the screen 80%
				scale(scale_factor.x, scale_factor.y, scale_factor.z);
				*/
				// add user zoom
				// translate(xZoom, 0, zZoom);
	
				noFill();
				stroke(0x60, 0x60, 0x60, 0x50);

				m_sad.draw();

			} popMatrix(); // end geo coords

		} popMatrix(); // end right-hand coords

	}

	public void mouseDragged() {
		m_rotx += ((mouseX - pmouseX) * PI / WINDOW_X);
		m_roty += ((mouseY - pmouseY) * PI / WINDOW_Y);

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
			m_altitude += 1000.0f;
			break;
		case '-':
		case '_':
			m_altitude -= 1000.0f;
			break;
		case 'c':
			m_colorMode = (++m_colorMode > 6) ? 0 : m_colorMode;
			break;
		case 'o':
			m_ortho = m_ortho == false;
			break;
			/*
			 * case 37: // left arrow cameraX -= ZOOM; break; case 39: // right arrow
			 * cameraX += ZOOM; break; case 38: // up arrow cameraZ += ZOOM; break; case 40:
			 * // down arrow cameraZ -= ZOOM; break;
			 */

		}
		//theAltitude = (theAltitude > maxAlt) ? maxAlt : theAltitude;
		//theAltitude = (theAltitude < minAlt) ? minAlt : theAltitude;
		redraw();
	}


	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "windField" };
		PApplet.main(concat(appletArgs, passedArgs));
	}
}
