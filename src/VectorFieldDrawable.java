import java.io.BufferedReader;
import java.io.IOException;
import processing.core.*;

public class VectorFieldDrawable extends Drawable {
	
	public PVector[] m_points = new PVector[6000000];
	public PVector[] m_vecs = new PVector[6000000];
	public int m_nrPoints = 0;

	public VectorFieldDrawable(PApplet papp, String objectName) {
		super(papp, objectName);
		m_bbox = new MBoundingBox(
						papp, 
						"vector field bbox", 
						new PVector(99999, 99999, 99999),
						new PVector(-99999, -99999, -99999)
					);
	}

	public void doDraw() {
	}
	
	public void load(String fileName) {
		if (fileName == null) {
			m_bbox.m_ll.x = -54.0f;	// lat
			m_bbox.m_ll.y = 280;		// long
			m_bbox.m_ll.z = 0;		// altitude

			m_bbox.m_ur.x = -44.0f;
			m_bbox.m_ur.y = 295;
			m_bbox.m_ur.z = 20000;
		}
		
		BufferedReader reader;
		String line = "";
		int startTime = p.millis();

		reader = p.createReader(fileName);

		while (line != null) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				line = null;
			}
			if (line != null) {
				if (0 == (m_nrPoints % 1000000)) {
					log(PApplet.str(m_nrPoints), LogLevel.INFO);
				}

				String[] pieces = PApplet.split(line, ',');

				float latitude = PApplet.parseFloat(pieces[1]);
				float longitude = PApplet.parseFloat(pieces[2]);
				float altitude = PApplet.parseFloat(pieces[3]);
				float _x = PApplet.parseFloat(pieces[4]);
				float _y = PApplet.parseFloat(pieces[5]);
				float _z = PApplet.parseFloat(pieces[6]);

				if (Float.isNaN(latitude) || Float.isNaN(longitude) || Float.isNaN(altitude) 
						|| Float.isNaN(_x) || Float.isNaN(_y) || Float.isNaN(_z)) {
					continue;
				}

				// remember the location and vector
				m_points[m_nrPoints] = new PVector(latitude, longitude, altitude);
				m_vecs[m_nrPoints]   = new PVector(_x, _y, _z);

				// check extents
				m_bbox.m_ll.x = (latitude < m_bbox.m_ll.x) ? latitude : m_bbox.m_ll.x;
				m_bbox.m_ur.x = (latitude > m_bbox.m_ur.x) ? latitude : m_bbox.m_ur.x;

				m_bbox.m_ll.y = (longitude < m_bbox.m_ll.y) ? longitude : m_bbox.m_ll.y;
				m_bbox.m_ur.y = (longitude > m_bbox.m_ur.y) ? longitude : m_bbox.m_ur.y;

				m_bbox.m_ll.z = (altitude < m_bbox.m_ll.z) ? altitude : m_bbox.m_ll.z;
				m_bbox.m_ur.z = (altitude > m_bbox.m_ur.z) ? altitude : m_bbox.m_ur.z;

				m_nrPoints += 1;
			}
		}

		m_bbox.calcSize();
		log("latitude=[ "  + PApplet.str(m_bbox.m_ll.x) + ", " + PApplet.str(m_bbox.m_ur.x) + "]", LogLevel.INFO);
		log("longitude=[ " + PApplet.str(m_bbox.m_ll.y) + ", " + PApplet.str(m_bbox.m_ur.y) + "]", LogLevel.INFO);
		log("altitude=[ "  + PApplet.str(m_bbox.m_ll.z) + ", " + PApplet.str(m_bbox.m_ur.z) + "]", LogLevel.INFO);
		log("size=[ "    + PApplet.str(m_bbox.m_size.x) + ", " + 
								PApplet.str(m_bbox.m_size.y) + ", " + 
								PApplet.str(m_bbox.m_size.z) + "]", LogLevel.INFO);
		log("time to load: " + PApplet.str(p.millis() - startTime), LogLevel.INFO);
	}
}