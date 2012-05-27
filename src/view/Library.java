package view;

import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Library extends MTList
{
	protected HashMap<MTListCell, view.Element> clones;
	protected Boolean create_new_model = true;
	
	public Library(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height, 10);
		
		this.setFillColor(MTColor.TEAL);
		this.setPickable(true);
	} 
	
	protected void addDragProcessor(MTListCell cell)
	{
		cell.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				MTListCell target = (MTListCell) ge.getTarget();
				
				if ( de.getId() == MTGestureEvent.GESTURE_STARTED )
				{
					view.Element w = (view.Element) target.getChildByIndex(0);
					view.Element nw = null;
					
					try {
						nw = w.getClass().getConstructor(w.getClass(),Boolean.class).newInstance(w,create_new_model);

						// adds the listener to the cloned view. This is ugly but it works
						if(nw instanceof view.page.Page) {
							((view.page.Page) nw).addListener((PageLibrary) Library.this);
						}
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					w.getRoot().addChild(nw);
					nw.setFullSize();
					nw.processGestureEvent(ge);
					
					Library.this.clones.put(target, nw);
				}
				else if ( Library.this.clones.get(target) != null )
				{
					Library.this.clones.get(target).processGestureEvent(ge);
				}
				
				return false;
			}
		});
	}
}
