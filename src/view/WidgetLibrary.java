package view;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;


import processing.core.PApplet;

public class WidgetLibrary extends Library 
{

	public WidgetLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			widget.Widget widget = null;
			
			if ( i < 3 )
				widget = new widget.Widget(applet, 0, 0, (i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
			else if ( i == 3 )
				widget = new widget.Image(applet, 0, 0, (i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
			else
				widget = new widget.Button(applet, 0, 0);

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
				widget.Widget w = (widget.Widget) target.getChildByIndex(0);
				widget.Widget nw = (widget.Widget) w.clone();
				nw.setFullSize();
				nw.processGestureEvent(ge);
				return false;
			}
		});
	}

}
