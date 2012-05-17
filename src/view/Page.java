package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class Page extends MTClipRectangle
{
	protected model.Page model;
	
	protected PApplet applet;
	private float mh, mw;
	
	public Page(PApplet a, float x, float y, model.Page p) 
	{
		super(a, x, y, 0, p.getWidth(), p.getHeight());
		applet = a;
		model = p;
		this.setFillColor(MTColor.WHITE);
	}
	
	public Page(Page p)
	{
		super(p.applet, 0, 0, 0, p.model.getWidth(), p.model.getHeight());
		applet = p.applet;
		model = p.model;
		mh = p.mh; 
		mw = p.mw;
		this.setPositionGlobal(new Vector3D(p.getCenterPointGlobal().x, p.getCenterPointGlobal().y, 0));
		this.setMinSize(p.mw, p.mh);
		this.setFillColor(p.getFillColor());
		
		initGesture();
	}
	
	public void setMinSize(float _w, float _h)
	{
		this.mh = _h;
		this.mw = _w;
		
		Vector3D pos = this.getCenterPointGlobal();
		this.setSizeXYGlobal(mw, mh);
		pos.x = pos.x - (model.getWidth() - mw)/2;
		pos.y = pos.y - (model.getHeight() - mh)/2;
		this.setPositionGlobal(pos);
		
		initGesture();
	}
	
	public void setFullSize()
	{
		this.setSizeXYGlobal(model.getWidth(), model.getHeight());
	}
	
	public model.Page getModel() 
	{
		return model;
	}
	
	private void initGesture()
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

}
