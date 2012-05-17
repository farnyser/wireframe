package view;

import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;

import processing.core.PApplet;

public class PageLibrary extends Library 
{
	public PageLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, Element>();
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			Page page = new Page(applet, 0, 0, new model.Page("untitled"));
			page.setMinSize(50, 50);
			cell.addChild(page);
			this.addListElement(cell);
			this.addDragProcessor(cell);
		}
	}
}
