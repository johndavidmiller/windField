import processing.core.*;
import java.awt.Color;

public class SliceDrawable extends VectorFieldDrawable {
	public int m_colorMode = 0;
	public TextDrawable m_label;
	public boolean m_drawGrid = true;

	public SliceDrawable(PApplet papp, String objectName) {
		super(papp, objectName);
		m_label = new TextDrawable(papp, objectName);
	}
	
	public int sliceFromVectorField(VectorFieldDrawable vf, int minAlt, int maxAlt) {
		log("sliceFromVectorField", LogLevel.INFO);
		
		// find out how many points we need to allocate
		int count = 0;
		for (int index = 0; index < vf.m_nrPoints; index++) {
			if ((vf.m_points[index].z >= minAlt) &&
				(vf.m_points[index].z <= maxAlt))
				count++;
		}
		log("nrPoints=" + PApplet.str(count), LogLevel.INFO);
				
		// copy the dater
		m_bbox.reset();
		m_points = new PVector[count];
		m_vecs = new PVector[count];
		m_nrPoints = 0;
		for (int index = 0; index < vf.m_nrPoints; index++) {
			PVector pt = vf.m_points[index];
			float latitude = pt.x;
			float longitude = pt.y;
			float altitude = pt.z;
			
			if ((altitude >= minAlt) && (altitude <= maxAlt)) {
				// remember the location and vector
				m_points[m_nrPoints] = new PVector(latitude, longitude, altitude);
				m_vecs[m_nrPoints] = new PVector(vf.m_vecs[index].x, vf.m_vecs[index].y, vf.m_vecs[index].z);

				// check extents
				m_bbox.m_ll.x = (latitude < m_bbox.m_ll.x) ? latitude : m_bbox.m_ll.x;
				m_bbox.m_ur.x = (latitude > m_bbox.m_ur.x) ? latitude : m_bbox.m_ur.x;

				m_bbox.m_ll.y = (longitude < m_bbox.m_ll.y) ? longitude : m_bbox.m_ll.y;
				m_bbox.m_ur.y = (longitude > m_bbox.m_ur.y) ? longitude : m_bbox.m_ur.y;

				m_bbox.m_ll.z = (altitude < m_bbox.m_ll.z) ? altitude : m_bbox.m_ll.z;
				m_bbox.m_ur.z = (altitude > m_bbox.m_ur.z) ? altitude : m_bbox.m_ur.z;

				m_nrPoints += 1;
				assert(m_nrPoints <= count);
			}
		}	
 		m_bbox.calcSize();
		log("bbox=" + m_bbox.toString(), LogLevel.INFO);
		return count;	// nr points sliced
	}
	
	public void doDraw() {
		p.scale(10f, 10f, 1f);
		// stroke(0x0, 0x80, 0x00);
		p.strokeWeight(0.25f);
		for (int index = 0; index < m_nrPoints; index += 1) {

			// swap x/y lat/long
			// TODO: if these coords aren't true and need to be swapped, we should do it once
			// e.g., during load, rather than expect it to be done every draw!!
			PVector vec = new PVector(this.m_vecs[index].x, this.m_vecs[index].y, this.m_vecs[index].z);
			PVector p0 = new PVector(this.m_points[index].x, this.m_points[index].y, this.m_points[index].z);
			PVector p1 = new PVector(
					p0.x + (vec.y * 0.010f),
					p0.y + (vec.x * 0.010f),
					p0.z + (vec.z * 100)
					);

			switch (m_colorMode) {
			case 0:
				p.stroke(Color.getHSBColor(vec.x, vec.y, vec.z).getRGB(), 0xF7);
				break;
			case 1:
				p.stroke(Color.getHSBColor(vec.x, vec.z, vec.y).getRGB(), 0xF7);
				break;
			case 2:
				p.stroke(Color.getHSBColor(vec.y, vec.x, vec.z).getRGB(), 0xF7);
				break;
			case 3:
				p.stroke(Color.getHSBColor(vec.y, vec.z, vec.x).getRGB(), 0xF7);
				break;
			case 4:
				p.stroke(Color.getHSBColor(vec.z, vec.x, vec.y).getRGB(), 0xF7);
				break;
			case 5:
				p.stroke(Color.getHSBColor(vec.z, vec.y, vec.x).getRGB(), 0xF7);
				break;
			case 6:
				double zColor = vec.z;
				int theColor = 0;
				if (zColor > 0) {
					zColor = (zColor * 0xFF) / 1.33;
					theColor = p.color(0x00, (int) zColor, 0x00, 0xF8);
				} else {
					zColor = (-zColor * 0xFF) / 1.33;
					theColor = p.color((int) zColor, 0x00, 0x00, 0xF8);
				}
				p.stroke(theColor);
				break;
			}

			// actually paint the vector
			p.line(p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);

			/*
			 * if (0 == (index % 100000)) { println("line (" + str(p0.x) + ", " + str(p0.y)
			 * + ", " + str(p0.z) + ") to (" + str(p1.x) + ", " + str(p1.y) + ", " +
			 * str(p1.z) + ")" ); }
			 */		
		}
	}
}