package View;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Page extends MTClipRectangle
{
	public Page(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, 0, width, height);
		this.setFillColor(MTColor.WHITE);
	}
}
