package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public abstract class Element extends MTClipRectangle implements PropertyChangeListener
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
		
		model.addListener(this);
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
		
		model.addListener(this);
	}
	
	public Element(Element e, Boolean create_new_model)
	{
		super(e.applet, 0, 0, 0, e.model.getWidth(), e.model.getHeight());
		
		if ( create_new_model )
		{
			model = (model.Element) e.model.clone();
		}
		else
		{
			model = e.model;
		}
		
		applet = e.applet;
		mh = e.mh; 
		mw = e.mw;
		initGraphics();
		initGesture();
		this.setPositionGlobal(new Vector3D(e.getCenterPointGlobal().x, e.getCenterPointGlobal().y, 0));
		this.setMinSize(e.mw, e.mh);
		this.setFillColor(e.getFillColor());
		
		model.addListener(this);
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

	public void propertyChange(PropertyChangeEvent e) 
	{
        String propertyName = e.getPropertyName();
        
        if ( propertyName == "addElement" ) 
        {
			view.Element el = view.Element.newInstance(applet, (model.Element)e.getNewValue());
			this.addChild(el);
        }
        else if ( propertyName == "removeElement" ) 
        {
        	for ( int i = 0 ; i < this.getChildCount() ; i++ )
        	{
        		if ( this.getChildByIndex(i) instanceof view.Element )
        		{
        			view.Element child = (Element) this.getChildByIndex(i);
        			if ( child.getModel() ==  (model.Element)e.getNewValue())
        			{
        				this.removeChild(i);
        				break;
        			}
        		}
        	}
        }
    }
	
	protected static Element newInstance(PApplet applet, model.Element e) 
	{		
		Element el = null;
	
		if ( e instanceof model.Page )
			el = new view.Page(applet, 0, 0, (model.Page) e);
		else if ( e instanceof model.widget.ButtonWidget )
			el = new view.widget.Button(applet, (model.widget.ButtonWidget) e);
		else if ( e instanceof model.widget.ImgWidget )
			el = new view.widget.Image(applet, (model.widget.ImgWidget) e);
		else if ( e instanceof model.widget.Widget )
			el = new view.widget.Widget(applet, (model.widget.Widget) e);
	
		return el;
	}

	protected abstract void initGesture();
	protected abstract void initGraphics();

}
