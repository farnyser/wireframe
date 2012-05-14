package widget;

import org.mt4j.components.visibleComponents.shapes.MTLine;

import processing.core.PApplet;

public class Image extends Widget 
{
	public Image(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height);
		
		MTLine l1 = new MTLine(applet, 0, 0, width, height);
		MTLine l2 = new MTLine(applet, width, 0, 0, height);
		l1.setPickable(false);
		l2.setPickable(false);
		
		this.addChild(l1);
		this.addChild(l2);
	}
}
