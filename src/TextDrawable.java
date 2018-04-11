
import processing.core.*; 


public class TextDrawable extends Drawable {
	public String m_string;
	public float m_pointSize = 18f;
	public boolean m_billboard = false;
	public PColor 
	
	public TextDrawable(PApplet papp, String objectName) {
		super(papp, objectName);
	}
	public TextDrawable(PApplet papp, String objectName, String string) {
		this(papp, objectName);
		m_string = string;
	}
	
	public void doDraw() {
		if (m_string == null)
			return;
		
		if (m_billboard) {
			// billboard, to face the camera

			float rotX = ((windField) p).m_rotx;
			float rotY = ((windField) p).m_roty;
			//PApplet.println("xrot= " + PApplet.str(rotX) + "yrot= " + PApplet.str(rotY) );
			p.rotateX(rotY);
			p.rotateZ(rotX);
			p.rotateX(-PI/2f);
		}

        p.scale(.02f, .02f, 1.0f); // why does this need to be scaled so aggressively?
        p.fill(0xFF, 0xFF, 0xFF, 0xF8);
        p.textSize(m_pointSize);
        flipY((int)m_pointSize);
        p.text(m_string, 0, 0, 0);
	}
}