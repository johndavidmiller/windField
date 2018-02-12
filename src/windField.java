import processing.core.*; 
import processing.opengl.*;
import processing.data.*; 
import processing.event.*; 

import java.awt.datatransfer.*; 
import java.awt.Toolkit; 
import java.awt.Color; 
import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class windField extends PApplet {

static int WINDOW_X = 800;
static int WINDOW_Y = 1000;
static int WINDOW_X_2 = WINDOW_X / 2;
static int WINDOW_Y_2 = WINDOW_Y / 2;

static String fileName = "/data/Perlan/wind.csv";
BufferedReader reader;
String line = "";

float  minLat=999999,    maxLat=-999999,    deltaLat=0,    deltaLat_2 = 0;
float  minLong=999999,   maxLong=-999999,   deltaLong=0,   deltaLong_2 = 0;
float  minAlt=999999,    maxAlt=-999999,    deltaAlt=0,    deltaAlt_2 = 0;

int startTime = millis();

float rot = 0.0f;
float rotX = 0.0f;
float rotY = 0.0f;

static float PI_2 = PI / 2.0f;
static float PI_4 = PI / 4.0f;
static float PI_8 = PI / 8.0f;

PVector[]  points = new PVector[6000000];
PVector[]  vec = new PVector[6000000];
int      nrPoints = 0;
float  theAltitude = 1000; 
int  colorMode = 0;
float  xZoom = 0;
float  zZoom = 0;

public void settings()
{
	size(800, 1024, P3D); 
    smooth();
}

public void setup() 
{
	surface.setResizable(true);
	loadFile(fileName);
//  stubFile();
}

public void draw()
{
  PVector  scale  = new PVector();

  background(0, 0, 0);
  lights();

  
  /*
    Processing 3D coords are x (left-to-right), y (up-to-down), and z (back-to-front)
        which makes Processing use a right-hand coord system with the Y axis flipped.
    
    Here, we fix the flipped Y axis, so that we have a proper right-hand coord space.
    HOWEVER, since other things (like text) expect the flipped Y, we have to reverse that
    when we use them, or text will be upside-down, say.
   */
  pushMatrix();
  {
    translate(0, WINDOW_Y, 0);    // move to lower-left corner of screen
    scale(1.0f, -1.0f, 1.0f);        // make Y axis point upwards, as Dog intended
 
    /*
      More fun with coordinate spaces: geo coords, x = LAT, y = LONG, z = ALT,
      so, rotate pi/2 around the X axis, which will make:
      
        Longitude=X=(left-to-right), Latitude=Y=(front-to-back), Altitude=Z(bottom-to-top)
     */
    pushMatrix();
    {
      translate(WINDOW_X_2, 0, -500); // origin is now in the middle bottom of screen
      rotateX(-PI_2);  // XY is now the horizontal plane; +Z points up
      
      // add user rotation from mouse
      rotateZ(rotX);
      rotateX(rotY);

      
      // scale to fill window      
      scale.x = WINDOW_X / deltaLong * 0.8f;    // fill the screen 80%
      scale.y = scale.x;  // lat and long should use the same scale
      scale.z = WINDOW_Y / deltaAlt * 0.8f;    // fill the screen 80%
      scale(scale.x, scale.y, scale.z);
        
      // add user zoom
 //     translate(xZoom, 0, zZoom);

      noFill();
      stroke(0x60, 0x60, 0x60, 0x50);
      for (int alt=0; alt <= 20000; alt += 1000)
      {
        pushMatrix();
        {
          translate(-(deltaLong_2), -deltaLat_2, alt);
          rotate(PI, 1, 0, 0);
//          scale(1.0 / scale.x, 1.0 / scale.y, 1.0 / scale.z);
          scale(.02f, .02f, 1.0f);
          fill(0xFF, 0xFF, 0xFF, 0xF8);
          textSize(32);
          text(str(alt), 0, 0, 0);
        }
        popMatrix();
 
 /*    
        pushMatrix();
        {
//          fill((alt % 10000) * 0xFF / 10000);
          noFill();
          strokeWeight(0.5);
          beginShape();
            vertex(-deltaLong_2, -deltaLat_2, alt);
            vertex(deltaLong_2, -deltaLat_2, alt);
            vertex(deltaLong_2, deltaLat_2, alt);
            vertex(-deltaLong_2, deltaLat_2, alt);
            vertex(-deltaLong_2, -deltaLat_2, alt);
          endShape(CLOSE);
        }
        popMatrix();
  */  
      }
      
      pushMatrix();
      {
        PVector  p0 = new PVector();
        PVector  p1 = new PVector();
        
 //       stroke(0x0, 0x80, 0x00);
        strokeWeight(0.25f);
        for (int index=0; index < nrPoints; index += 5)
        {
            // HACK - swap x/y lat/long
            p0.x = points[index].y - minLong - deltaLong_2;
            p0.y = points[index].x - minLat - deltaLat_2; 
            p0.z = points[index].z;
            
            if (p0.z != theAltitude)
              continue;
            
            // HACK - swap x/y lat/long
            p1.x = p0.x + (vec[index].y * 0.010f);
            p1.y = p0.y + (vec[index].x * 0.010f);
//            p1.z = p0.z + (vec[index].z * 100); //* 0.10);
            p1.z = p0.z;

/* FALSE COLOR

            switch (colorMode)
            {
              case 0:
                stroke(Color.getHSBColor(vec[index].x, vec[index].y, vec[index].z).getRGB(), 0xF7);
                break;
              case 1:
                stroke(Color.getHSBColor(vec[index].x, vec[index].z, vec[index].y).getRGB(), 0xF7);
                break;
              case 2:
                stroke(Color.getHSBColor(vec[index].y, vec[index].x, vec[index].z).getRGB(), 0xF7);
                break;
              case 3:
                stroke(Color.getHSBColor(vec[index].y, vec[index].z, vec[index].x).getRGB(), 0xF7);
                break;
              case 4:
                stroke(Color.getHSBColor(vec[index].z, vec[index].x, vec[index].y).getRGB(), 0xF7);
                break;
              case 5:
                stroke(Color.getHSBColor(vec[index].z, vec[index].y, vec[index].x).getRGB(), 0xF7);
                break;
            }
 */
// /* z Color
            	
            double zColor = vec[index].z;
            int theColor = 0;
            if (zColor > 0)
            {
            	zColor =  (zColor * 0xFF) / 1.33;
            	theColor = color(0x00, (int) zColor, 0x00, 0xF8);
            }
            else 
            {
            	zColor = (-zColor * 0xFF) / 1.33;
            	theColor = color((int)zColor, 0x00, 0x00, 0xF8);
            }
            stroke(theColor);
 // */
            
            // actually paint the vector
            line( p0.x, p0.y, p0.z, p1.x, p1.y, p1.z);
 
 /*           
            if (0 == (index % 100000))
            {
              println("line (" + str(p0.x) + ", " + str(p0.y) + ", " + str(p0.z) + ") to (" +
                                  str(p1.x) + ", " + str(p1.y) + ", " + str(p1.z) + ")"
                                  );
            }
  */
        }
      }
      popMatrix();
    }
    popMatrix();  // end geo coords
  }  
  popMatrix(); // end right-hand coords

}

public void mouseDragged()
{
  rotX += ((mouseX - pmouseX) * PI / WINDOW_X);
  rotY += ((mouseY - pmouseY) * PI / WINDOW_Y);

/*  
  println("mouse=(" + str(mouseX) + ", " + str(mouseY) + ") " +
        "pmouse=(" + str(pmouseX) + ", " + str(pmouseY) + ") " +
        "rotX=" + str(rotX) + " rotY=" + str(rotY));
 */
}

static final int ZOOM = 100;

public void keyPressed()
{
  println("pressed " + PApplet.parseInt(key) + " " + keyCode);
  switch (key)
  {
    case '+':
    case '=':
      theAltitude += 1000.0f;
      break;
    case '-':
    case '_':
      theAltitude -= 1000.0f;
      break;
    case 'c':
      colorMode = (++colorMode > 5) ? 0 : colorMode;
      break;
/*
    case 37:  // left arrow
      cameraX -= ZOOM;
      break;
    case 39:  // right arrow
      cameraX += ZOOM;
      break;
    case 38:  // up arrow
      cameraZ += ZOOM;
      break;
    case 40:  // down arrow
      cameraZ -= ZOOM;
      break;
 */
  }
  theAltitude = (theAltitude > maxAlt) ? maxAlt : theAltitude;
  theAltitude = (theAltitude < minAlt) ? minAlt : theAltitude;
}

public void textUp(String s, float x, float y, float z)
{
  pushMatrix();
  translate(x, y, z);
  scale(1.0f, -1.0f, 1.0f);
  text(s, 0, 0);
  popMatrix();
}

public void stubFile()
{
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

public void loadFile(String fileName)
{
  reader = createReader(fileName);   

  while (line != null)
  {
    try 
    {
      line = reader.readLine();
    } 
    catch (IOException e) 
    {
      e.printStackTrace();
      line = null;
    }
    if (line == null) 
    {
      // Stop reading because of an error or file is empty
      println("latitude=[ " + str(minLat) + ", " + str(maxLat));
      println("longitude=[ " + str(minLong) + ", " + str(maxLong));
      println("altitude=[ " + str(minAlt) + ", " + str(maxAlt));
      println("time to load: " + str(millis() - startTime));
      return;  
    } 
    else 
    {
      if (0 == (nrPoints % 1000000))
      {
        println(str(nrPoints));
      }
      
      String[] pieces = split(line, ',');
      
      float  latitude = PApplet.parseFloat(pieces[1]);
      float  longitude = PApplet.parseFloat(pieces[2]);
      float  altitude   = PApplet.parseFloat(pieces[3]);
      float  _x = PApplet.parseFloat(pieces[4]);
      float  _y = PApplet.parseFloat(pieces[5]);
      float  _z = PApplet.parseFloat(pieces[6]);
      
      if (
        Float.isNaN(latitude) || Float.isNaN(longitude) || Float.isNaN(altitude) ||
        Float.isNaN(_x) || Float.isNaN(_y) || Float.isNaN(_z)
        )
      {
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "windField" };
    PApplet.main(concat(appletArgs, passedArgs));
  }
}
