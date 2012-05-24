package view;

import java.beans.PropertyChangeEvent;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.widgets.MTClipRoundRect;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import view.Element.DragType;
import view.widget.Widget;

public class Page extends Element
{
	MTClipRoundRect close;
	
	public Page(PApplet a, float x, float y, model.Page p) 
	{
		super(a, x, y, p);
		applet = a;
		model = p;
	}
	
	public Page(Page p, Boolean create_new_model)
	{
		super(p,create_new_model);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
	}

	protected void initGesture()
	{
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				
				if ( dragType == DragType.MOVE )
				{
					Page.this.translateGlobal(de.getTranslationVect());
					Page.this.sendToFront();
				}
				else if ( dragType == DragType.RESIZE )
				{
					Page.this.model.setSize(Page.this.model.getWidth() + de.getTranslationVect().x, Page.this.model.getHeight() + de.getTranslationVect().y);
					
					if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
						Page.this.dragType = DragType.MOVE;
				}
				
				return false;
			}
		});
		this.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		this.addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer((MTApplication)applet, this));
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() 
		{
		    @Override
		    public boolean processGestureEvent(MTGestureEvent ge) 
		    {
		        TapAndHoldEvent te = (TapAndHoldEvent)ge;
		        if(te.getId() == TapAndHoldEvent.GESTURE_ENDED && te.getElapsedTime() >= te.getHoldTime())
		        {
		            System.out.println("start resizing");
		            Page.this.dragType = DragType.RESIZE;
		        }
		        return false;
		    }    
		});
		
		close.registerInputProcessor(new TapProcessor(this.applet));
		close.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
			            view.Page.this.destroy();
					}
		        }
				
		        return false;
			}
		});
	}

	@Override
	protected void initGraphics() 
	{
		this.setFillColor(MTColor.WHITE);
		
		close = new MTClipRoundRect(applet, this.model.getWidth() - 10, -10, 0, 20, 20, 10, 10);
		close.setFillColor(MTColor.RED);
		this.addChild(close);
		
		for ( model.Element e : model.getElements() )
		{
			view.Element w = view.Element.newInstance(applet, e);
			this.addChild(w);
		}
	}

}
