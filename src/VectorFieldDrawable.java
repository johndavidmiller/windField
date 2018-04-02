import java.io.BufferedReader;
import java.io.IOException;
import processing.core.*;

public class VectorFieldDrawable extends Drawable {
	public float minLat  = 999999, maxLat  = -999999, deltaLat  = 0;
	public float minLong = 999999, maxLong = -999999, deltaLong = 0;
	public float minAlt  = 999999, maxAlt  = -999999, deltaAlt  = 0;
	protected PVector dimensions_min;
	protected PVector dimensions_max;
	protected PVector dimensions_delta;
	
	public PVector[] points = new PVector[6000000];
	public PVector[] vec = new PVector[6000000];
	public int nrPoints = 0;

	public VectorFieldDrawable(PApplet parent, String objectName) {
		super(parent, objectName);
	}

	public void doDraw() {
	}
	
	public void load(String fileName) {
		if (fileName == null) {
			this.minLat = -54.0f;
			this.maxLat = -44.0f;
			this.deltaLat = maxLat - minLat;

			this.minLong = 280;
			this.maxLong = 295;
			this.deltaLong = this.maxLong - this.minLong;

			this.minAlt = 0;
			this.maxAlt = 20000;
			this.deltaAlt = this.maxAlt - this.minAlt;
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
			if (line == null) {
				this.log("latitude=[ " + PApplet.str(this.minLat) + ", " + PApplet.str(this.maxLat), LogLevel.INFO);
				this.log("longitude=[ " + PApplet.str(this.minLong) + ", " + PApplet.str(this.maxLong), LogLevel.INFO);
				this.log("altitude=[ " + PApplet.str(this.minAlt) + ", " + PApplet.str(this.maxAlt), LogLevel.INFO);
				this.log("time to load: " + PApplet.str(p.millis() - startTime), LogLevel.INFO);
				return;
			} else {
				if (0 == (nrPoints % 1000000)) {
					this.log(PApplet.str(nrPoints), LogLevel.INFO);
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

				points[nrPoints] = new PVector();
				points[nrPoints].x = latitude;
				points[nrPoints].y = longitude;
				points[nrPoints].z = altitude;

				vec[nrPoints] = new PVector();
				vec[nrPoints].x = _x;
				vec[nrPoints].y = _y;
				vec[nrPoints].z = _z;

				this.minLat = (latitude < this.minLat) ? latitude : this.minLat;
				this.maxLat = (latitude > this.maxLat) ? latitude : this.maxLat;

				this.minLong = (longitude < this.minLong) ? longitude : this.minLong;
				this.maxLong = (longitude > this.maxLong) ? longitude : this.maxLong;

				this.minAlt = (altitude < this.minAlt) ? altitude : this.minAlt;
				this.maxAlt = (altitude > this.maxAlt) ? altitude : this.maxAlt;

				nrPoints += 1;
			}
			this.deltaLat = this.maxLat - this.minLat;
			this.deltaLong = this.maxLong - this.minLong;
			this.deltaAlt = this.maxAlt - this.minAlt;
		}
	}
}