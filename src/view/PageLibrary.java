package view;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;

import processing.core.PApplet;

public class PageLibrary extends Library 
{
	public PageLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			Page page = new Page(applet, 0, 0, 500, 500);
			page.setMinSize(50, 50);
			cell.addChild(page);
			this.addListElement(cell);
			this.addDragProcessor(cell);
		}
	}	
	
	private void addDragProcessor(MTListCell cell)
	{
		cell.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				MTListCell target = (MTListCell) ge.getTarget();
				Page w = (Page) target.getChildByIndex(0);
				Page nw = (Page) w.clone();
				nw.setFullSize();
				nw.processGestureEvent(ge);
				return false;
			}
		});
	}

}
