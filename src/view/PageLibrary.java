package view;

import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;

import processing.core.PApplet;

public class PageLibrary extends Library 
{
	protected HashMap<MTListCell, Page> clones;
	
	public PageLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, Page>();
		
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
				
				if ( de.getId() == MTGestureEvent.GESTURE_STARTED )
				{
					Page w = (Page) target.getChildByIndex(0);
					Page nw = null;
					
					try {
						nw = w.getClass().getConstructor(w.getClass()).newInstance(w);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					w.getRoot().addChild(nw);
					nw.setFullSize();
					nw.processGestureEvent(ge);
					
					PageLibrary.this.clones.put(target, nw);
				}
				else if ( PageLibrary.this.clones.get(target) != null )
				{
					PageLibrary.this.clones.get(target).processGestureEvent(ge);
				}
				
				return false;
			}
		});
	}

}
