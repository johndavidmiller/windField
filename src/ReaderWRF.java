import java.io.IOException;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * 
 */

/**
 * @author jdmille2
 *
 */
public class ReaderWRF implements PConstants {
	public void stubFile() {
		minLat = -54.0f;
		maxLat = -44.0f;
		deltaLat = maxLat - minLat;
		deltaLat_2 = deltaLat / 2;

		minLong = 280;
		maxLong = 295;
		deltaLong = maxLong - minLong;
		deltaLong_2 = deltaLong / 2;

		minAlt = 0;
		maxAlt = 20000;
		deltaAlt = maxAlt - minAlt;
		deltaAlt_2 = deltaAlt / 2;
	}

	public void loadFile(String fileName) {
		reader = createReader(fileName);

		while (line != null) {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				line = null;
			}
			if (line == null) {
				println("latitude=[ " + str(minLat) + ", " + str(maxLat));
				println("longitude=[ " + str(minLong) + ", " + str(maxLong));
				println("altitude=[ " + str(minAlt) + ", " + str(maxAlt));
				println("time to load: " + str(millis() - startTime));
				return;
			} else {
				if (0 == (nrPoints % 1000000)) {
					println(str(nrPoints));
				}

				String[] pieces = split(line, ',');

				float latitude = PApplet.parseFloat(pieces[1]);
				float longitude = PApplet.parseFloat(pieces[2]);
				float altitude = PApplet.parseFloat(pieces[3]);
				float _x = PApplet.parseFloat(pieces[4]);
				float _y = PApplet.parseFloat(pieces[5]);
				float _z = PApplet.parseFloat(pieces[6]);

				if (Float.isNaN(latitude) || Float.isNaN(longitude) || Float.isNaN(altitude) || Float.isNaN(_x)
						|| Float.isNaN(_y) || Float.isNaN(_z)) {
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

				minLat = (latitude < minLat) ? latitude : minLat;
				maxLat = (latitude > maxLat) ? latitude : maxLat;
				deltaLat = maxLat - minLat;
				deltaLat_2 = deltaLat / 2;

				minLong = (longitude < minLong) ? longitude : minLong;
				maxLong = (longitude > maxLong) ? longitude : maxLong;
				deltaLong = maxLong - minLong;
				deltaLong_2 = deltaLong / 2;

				minAlt = (altitude < minAlt) ? altitude : minAlt;
				maxAlt = (altitude > maxAlt) ? altitude : maxAlt;
				deltaAlt = maxAlt - minAlt;
				deltaAlt_2 = deltaAlt / 2;

				nrPoints += 1;
			}
		}
}
