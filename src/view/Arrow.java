package view;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public class Arrow extends MTComponent
{
	private final static int TRIANGLE_SIZE = 8;
	
	public Arrow(PApplet applet, Vector3D start, Vector3D stop, MTColor c)
	{
		super(applet);
		MTLine line = new MTLine(applet, start.x, start.y, stop.x, stop.y);
		line.setStrokeColor(c);
		Vertex[] v = new Vertex[]{
	       		new Vertex(0,0,0,   0,0,0,255),
	       		new Vertex((float) (3*TRIANGLE_SIZE), (float) (1.5*TRIANGLE_SIZE),0, 0,0,0,255),
	       		new Vertex(0,3*TRIANGLE_SIZE,0, 0,0,0,255),
		};
		MTPolygon triangle = new MTPolygon(applet, v);
		triangle.setFillColor(c);
		triangle.setNoStroke(true);
		
		this.addChild(line);
		this.addChild(triangle);
		
		if ( stop.y > start.y )
			triangle.rotateZGlobal(triangle.getCenterPointGlobal(), (float) (180.0/3.14957*Vector3D.angleBetween(new Vector3D(1,0), stop.getSubtracted(start))));
		else
			triangle.rotateZGlobal(triangle.getCenterPointGlobal(), - (float) (180.0/3.14957*Vector3D.angleBetween(new Vector3D(1,0), stop.getSubtracted(start))));
		triangle.setPositionGlobal(stop);
	}
}
