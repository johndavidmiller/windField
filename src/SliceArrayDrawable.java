import processing.core.*;

public class SliceArrayDrawable extends Drawable {
	public int nrSlices = 0;
	public float minLat  = 999999, maxLat  = -999999, deltaLat  = 0;
	public float minLong = 999999, maxLong = -999999, deltaLong = 0;
	public float minAlt  = 999999, maxAlt  = -999999, deltaAlt  = 0;
	protected VectorFieldDrawable vectorField;
	public int theAltitude = 0;
	protected SliceDrawable[] slices;
	
	public SliceArrayDrawable(PApplet parent, String objectName, VectorFieldDrawable vf) {
		super(parent, objectName);
		this.vectorField = vf;
		
		// figure out how many slices we need

		this.nrSlices = (((int)this.vectorField.deltaAlt) / 1000);

		this.slices = new SliceDrawable[this.nrSlices];
	}
	
	public void doDraw() {

	}

	public PVector getMinimums() {
		return new PVector(this.vectorField.minLat, this.vectorField.minLong, this.vectorField.minAlt);
	}
	public PVector getMaximums() {
		return new PVector(this.vectorField.maxLat, this.vectorField.maxLong, this.vectorField.maxAlt);
	}
	public PVector getDeltas() {
		return new PVector(this.vectorField.deltaLat, this.vectorField.deltaLong, this.vectorField.deltaAlt);
	}

	
}
