package View;

import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Library extends MTList
{
	public Library(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height, 10);
		
		this.setFillColor(MTColor.TEAL);
		this.setPickable(true);
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			cell.addChild(new Widget(applet, 0,0,50,50));
			this.addListElement(cell);
		}
	}
}
