package view;

import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Library extends MTList
{
	public Library(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height, 10);
		
		this.setFillColor(MTColor.TEAL);
		this.setPickable(true);
	}
}
