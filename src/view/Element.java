package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public abstract class Element extends MTClipRectangle
{
	protected model.Element model;
	protected PApplet applet;
	protected float mh, mw;
	
	public Element(PApplet a, float x, float y, model.Element p) 
	{
		super(a, x, y, 0, p.getWidth(), p.getHeight());
		applet = a;
		model = p;
		mw = p.getWidth();
		mh =  p.getHeight();
		initGraphics();
		initGesture();
	}
	
	public Element(Element e)
	{
		super(e.applet, 0, 0, 0, e.model.getWidth(), e.model.getHeight());
		applet = e.applet;
		model = e.model;
		mh = e.mh; 
		mw = e.mw;
		initGraphics();
		initGesture();
		this.setPositionGlobal(new Vector3D(e.getCenterPointGlobal().x, e.getCenterPointGlobal().y, 0));
		this.setMinSize(e.mw, e.mh);
		this.setFillColor(e.getFillColor());
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
	}
	
	public void setFullSize()
	{
		this.setSizeXYGlobal(model.getWidth(), model.getHeight());
	}
	
	public model.Element getModel() 
	{
		return model;
	}

	protected abstract void initGesture();
	protected abstract void initGraphics();

}
