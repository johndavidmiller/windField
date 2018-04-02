
import processing.core.*; 


public class TextDrawable extends Drawable {
	public String string;
	public float pointSize = 18f;
	
	public TextDrawable(PApplet parent, String objectName) {
		super(parent, objectName);
	}
	
	public void doDraw() {
		if (this.string == null)
			return;
		
        //p.translate(-(deltaLong_2), -deltaLat_2, alt);
        p.rotate(PI, 1, 0, 0);
        p.scale(.02f, .02f, 1.0f); // why does this need to be scaled so aggressively?
        p.fill(0xFF, 0xFF, 0xFF, 0xF8);
        p.textSize(this.pointSize);
        p.text(this.string, 0, 0, 0);
	}

	public void textUp(String s, float x, float y, float z) {
		this.p.pushMatrix();
		this.p.translate(x, y, z);
		this.p.scale(1.0f, -1.0f, 1.0f);
		this.p.text(s, 0, 0);
		this.p.popMatrix();
	}
}