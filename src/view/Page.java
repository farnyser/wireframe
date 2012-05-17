package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRoundRect;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Page extends Element
{
	MTClipRoundRect close;
	
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
			view.widget.Widget w = view.widget.Widget.newInstance(applet, e);
			this.addChild(w);
		}
	}

}
