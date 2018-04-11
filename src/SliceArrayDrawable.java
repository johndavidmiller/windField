import processing.core.*;

public class SliceArrayDrawable extends Drawable {
	public int m_nrSlices = 0;
	protected VectorFieldDrawable m_vectorField;
	public int m_altitude = 0;
	protected SliceDrawable m_slices[];
	
	public SliceArrayDrawable(PApplet papp, String objectName, VectorFieldDrawable vf) {
		super(papp, objectName);
		m_vectorField = vf;
		
		// figure out how many slices we need

		m_nrSlices = ((int)m_vectorField.m_bbox.m_size.z) / 1000;
		m_nrSlices += 1;
		m_slices = new SliceDrawable[m_nrSlices];
		for (int index = 0; index < m_nrSlices; index++) {
			int baseAlt = index * 1000;
			// typically our altitude from WRF it's a solid 1000m grid, so nothing in-between,
			// but defensively we'll pick up anything within a 999m rangne.
			m_slices[index] = new SliceDrawable(p, "slice " + PApplet.str(baseAlt));
			m_slices[index].sliceFromVectorField(m_vectorField, baseAlt - 500, baseAlt + 499);
			m_slices[index].m_label.m_string = PApplet.str(baseAlt);
		}
		m_bbox = m_vectorField.m_bbox;	// we are the same extents as the vf
	}
	
	public void doDraw() {
		for (int i=0; i < m_nrSlices; i++) {
			if (i != 5) continue;
			m_slices[i].draw();
		}
	}

	
}
