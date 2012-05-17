package view;

import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Page extends Element
{
	protected model.Page model;
	
	public Page(PApplet a, float x, float y, model.Page p) 
	{
		super(a, x, y, p);
		applet = a;
		model = p;
	}
	
	public Page(Page p)
	{
		super(p);
	}
	
	protected void initGesture()
	{
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				Page.this.translateGlobal(de.getTranslationVect());
				Page.this.sendToFront();
				
				return false;
			}
		});
	}

	@Override
	protected void initGraphics() 
	{
		this.setFillColor(MTColor.WHITE);
	}

}
