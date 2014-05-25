This is a quick-and-dirty 3D vector field visualization, for viewing output of a NWP model such as NAM or WRF 
(file format may vary).

Requirements:
* Processing 2.x (www.processing.com)
* Java 7 JDK (www.java.com)
* Hardware-accelerated OpenGL (most modern computers)

UI:
* mouse drag: rotate X (tilt) and Z (lazy susan)
* +/- keys (actually + and = / - and _) select altitude

Expects file ./wind.csv to be of the form:

"ID","Lat","Long","Alt","Field4","Field5","Field6"
1,-44.65,280.94,0.00,20.35,-6.23,0.00
2,-44.65,280.94,1000.00,11.29,-14.33,0.08
3,-44.65,280.94,2000.00,18.27,-7.21,-0.16
4,-44.65,280.94,3000.00,13.09,-13.21,-0.05
5,-44.65,280.94,4000.00,24.20,-11.38,-0.04
6,-44.65,280.94,5000.00,31.42,-11.57,-0.16
7,-44.65,280.94,6000.00,30.16,-11.88,-0.02
8,-44.65,280.94,7000.00,31.48,-21.01,0.22
9,-44.65,280.94,8000.00,31.56,-21.32,0.53
