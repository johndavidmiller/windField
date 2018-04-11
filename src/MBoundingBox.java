import processing.core.*;

public class MBoundingBox extends Drawable {
	public PVector m_ll;		// lower-left corner
	public PVector m_ur;		// upper-right corner
	public PVector m_size;	// width, height, depth
	
	public MBoundingBox(PApplet papp, String objectName) {
		super(papp, objectName);
		reset();
	}

	public MBoundingBox(PApplet papp, String objectName, PVector ll, PVector ur) {
		super(papp, objectName);
		setLLUR(ll, ur);
	}

	public void doDraw() {		
		// color?
		// width?
		// alignment?
		p.box(m_size.x, m_size.y, m_size.z);
	}

	// setters and getters
	public void reset() {
		// makes box invalid - is there a better way, e.g., NaN?
		setLLUR(
				new PVector(99999999, 9999999, 9999999), 
				new PVector(-9999999, -9999999, -999999)
			);
	}
	public PVector	getLL() {
		return m_ll;
	}
	public void		setLL(PVector ll) {
		m_ll = ll;
		calcSize();
	}
	public PVector	getUR() {
		return m_ur;
	}
	public void		setUR(PVector ur) {
		m_ur = ur;
		calcSize();
	}
	public void		setLLUR(PVector ll, PVector ur) {
		m_ll = ll;
		m_ur = ur;
		calcSize();
	}
	public PVector	getSize() {
		return m_size;
	}
	protected void	calcSize() {
		m_size = new PVector(
				m_ur.x - m_ll.x, 
				m_ur.y - m_ll.y, 
				m_ur.z - m_ll.z
			);
		m_size.x = m_size.x < 0 ? 0 : m_size.x;
		m_size.y = m_size.y < 0 ? 0 : m_size.y;
		m_size.z = m_size.z < 0 ? 0 : m_size.z;
		
		log(toString(), LogLevel.INFO);
	}

	public String toString() {
		String str = new String();
		
		str = "[ll=(" + PApplet.str(m_ll.x) + ", " + PApplet.str(m_ll.y) + ", " + PApplet.str(m_ll.z) + ")";
		str += ", ur=(" + PApplet.str(m_ur.x) + ", " + PApplet.str(m_ur.y) + ", " + PApplet.str(m_ur.z) + ")]";
		str += " size=(" + PApplet.str(m_size.x) + ", " + PApplet.str(m_size.y) + ", " + PApplet.str(m_size.z) + ")";

		return str;
	}
}
