import processing.core.*;
import java.awt.Color;

public class SliceDrawable extends Drawable {
	public int colorMode = 0;
	public float minLat  = 999999, maxLat  = -999999, deltaLat  = 0;
	public float minLong = 999999, maxLong = -999999, deltaLong = 0;
	public float minAlt  = 999999, maxAlt  = -999999, deltaAlt  = 0;
	public PVector[] points;
	public PVector[] vec;
	public int nrPoints = 0;
	protected VectorFieldDrawable vectorField;

	public SliceDrawable(PApplet parent, String objectName, VectorFieldDrawable vf) {
		super(parent, objectName);
		this.vectorField = vf;
	}
	
	public int slice(int minAlt, int maxAlt) {
		// find out how many points we need to allocate
		int count = 0;
		for (int index = 0; index < this.vectorField.nrPoints; index++) {
			if ((this.vectorField.points[index].z >= minAlt) &&
				(this.vectorField.points[index].z <= maxAlt))
				count++;
		}
		
		this.points = new PVector[count];
		this.vec = new PVector[count];
		this.nrPoints = count;
		
		// copy the dater
		count = 0;
		for (int index = 0; index < this.vectorField.nrPoints; index++) {
			PVector pt = this.points[count];
			float latitude = pt.x;
			float longitude = pt.y;
			float altitude = pt.z;
			
			if ((altitude >= minAlt) && (altitude <= maxAlt)) {

				this.points[count] = this.vectorField.points[index];
				this.vec[count] = this.vectorField.vec[index];

				this.minLat = (latitude < this.minLat) ? latitude : this.minLat;
				this.maxLat = (latitude > this.maxLat) ? latitude : this.maxLat;

				this.minLong = (longitude < this.minLong) ? longitude : this.minLong;
				this.maxLong = (longitude > this.maxLong) ? longitude : this.maxLong;

				this.minAlt = (altitude < this.minAlt) ? altitude : this.minAlt;
				this.maxAlt = (altitude > this.maxAlt) ? altitude : this.maxAlt;

				count++;
			}
			this.deltaLat = this.maxLat - this.minLat;
			this.deltaLong = this.maxLong - this.minLong;
			this.deltaAlt = this.maxAlt - this.minAlt;
		}
		
		return count;	// nr points sliced
	}
	
	public void doDraw() {
		PVector p0 = new PVector();
		PVector p1 = new PVector();

		// stroke(0x0, 0x80, 0x00);
		p.strokeWeight(0.25f);
		for (int index = 0; index < this.nrPoints; index += 5) {

			// swap x/y lat/long
			// TODO: if these coords aren't true and need to be swapped, we should do it once
			// e.g., during load, rather than expect it to be done every draw!!
			p1.x = p0.x + (vec[index].y * 0.010f);
			p1.y = p0.y + (vec[index].x * 0.010f);
			p1.z = p0.z + (vec[index].z * 100); // * 0.10);
			// p1.z = p0.z;

			switch (this.colorMode) {
			case 0:
				p.stroke(Color.getHSBColor(vec[index].x, vec[index].y, vec[index].z).getRGB(), 0xF7);
				break;
			case 1:
				p.stroke(Color.getHSBColor(vec[index].x, vec[index].z, vec[index].y).getRGB(), 0xF7);
				break;
			case 2:
				p.stroke(Color.getHSBColor(vec[index].y, vec[index].x, vec[index].z).getRGB(), 0xF7);
				break;
			case 3:
				p.stroke(Color.getHSBColor(vec[index].y, vec[index].z, vec[index].x).getRGB(), 0xF7);
				break;
			case 4:
				p.stroke(Color.getHSBColor(vec[index].z, vec[index].x, vec[index].y).getRGB(), 0xF7);
				break;
			case 5:
				p.stroke(Color.getHSBColor(vec[index].z, vec[index].y, vec[index].x).getRGB(), 0xF7);
				break;
			case 6:
				double zColor = vec[index].z;
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