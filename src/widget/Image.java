package widget;

import org.mt4j.components.visibleComponents.shapes.MTLine;

import processing.core.PApplet;

public class Image extends Widget 
{
	public Image(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height);
	}
	
	public Image(widget.Image widget)
	{
		super(widget);
	}
	
	protected void initGraphics()
	{		
		super.initGraphics();
		MTLine l1 = new MTLine(applet, 0, 0, this.w, this.h);
		MTLine l2 = new MTLine(applet, this.w, 0, 0, this.h);
		l1.setPickable(false);
		l2.setPickable(false);
		
		this.addChild(l1);
		this.addChild(l2);
	}
}
