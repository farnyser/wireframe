package view;

import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;


import processing.core.PApplet;

public class WidgetLibrary extends Library 
{
	protected HashMap<MTListCell, view.widget.Widget> clones;
	
	public WidgetLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, view.widget.Widget>();
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			view.widget.Widget widget = null;
			model.widget.Widget model = new model.widget.Widget();
			
			if ( i < 3 )
			{
				model.setSize((i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
				widget = new view.widget.Widget(applet, model);
			}
			else if ( i == 3 )
			{
				model.setSize((i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
				widget = new view.widget.Image(applet, model);
			}
			else
			{
				model.setSize(100, 30);
				widget = new view.widget.Button(applet, model);
			}
			
			widget.setMinSize(50, 50);
			cell.addChild(widget);
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
					view.widget.Widget w = (view.widget.Widget) target.getChildByIndex(0);
					view.widget.Widget nw = null;
					
					try {
						nw = w.getClass().getConstructor(w.getClass()).newInstance(w);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					w.getRoot().addChild(nw);
					nw.setFullSize();
					nw.processGestureEvent(ge);
					
					WidgetLibrary.this.clones.put(target, nw);
				}
				else if ( WidgetLibrary.this.clones.get(target) != null )
				{
					WidgetLibrary.this.clones.get(target).processGestureEvent(ge);
				}
				
				return false;
			}
		});
	}

}
