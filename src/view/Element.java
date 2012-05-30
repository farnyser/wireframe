package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import model.widget.Widget;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public abstract class Element extends MTClipRectangle implements PropertyChangeListener
{
	protected enum DragType { MOVE, RESIZE, LINK };
	
	protected model.Element _model;
	protected PApplet applet;
	protected float mh, mw;
	protected DragType dragType = DragType.MOVE;
	protected model.Element.Corner resizeStart = model.Element.Corner.UPPER_LEFT;
	protected boolean isMiniature = false;
	
	public Element(PApplet a, float x, float y, model.Element p) 
	{ 
		super(a, x, y, 0, p.getWidth(), p.getHeight());
		applet = a;
		_model = p;
		mw = p.getWidth();
		mh =  p.getHeight();
		initGraphics();
		initGesture();
		
		_model.addListener(this);
	}
	
	public Element(Element e)
	{
		super(e.applet, 0, 0, 0, e._model.getWidth(), e._model.getHeight());
		applet = e.applet;
		_model = e._model;
		mh = e.mh; 
		mw = e.mw;
		initGraphics();
		initGesture();
		this.setPositionGlobal(new Vector3D(e.getCenterPointGlobal().x, e.getCenterPointGlobal().y, 0));
		this.setMinSize(e.mw, e.mh);
		this.setFillColor(e.getFillColor());
		
		_model.addListener(this);
	}
	
	public Element(Element e, Boolean create_new_model)
	{
		super(e.applet, 0, 0, 0, e._model.getWidth(), e._model.getHeight());
		
		if ( create_new_model )
		{
			_model = (model.Element) e._model.clone();
		}
		else
		{
			_model = e._model;
		}
		
		applet = e.applet;
		mh = e.mh; 
		mw = e.mw;
		initGraphics();
		initGesture();
		this.setPositionGlobal(new Vector3D(e.getCenterPointGlobal().x, e.getCenterPointGlobal().y, 0));
		this.setMinSize(e.mw, e.mh);
		this.setFillColor(e.getFillColor());
		
		_model.addListener(this);
	}

	public void setMinSize(float _w, float _h)
	{
		this.mh = _h;
		this.mw = _w;
		
		this.isMiniature = true;
		
		Vector3D pos = this.getCenterPointGlobal();
		this.setSizeXYGlobal(mw, mh);
		pos.x = pos.x - (_model.getWidth() - mw)/2;
		pos.y = pos.y - (_model.getHeight() - mh)/2;
		this.setPositionGlobal(pos);
	}
	
	public void setFullSize()
	{
		this.isMiniature = false;
		this.mw = _model.getWidth();
		this.mh = _model.getHeight();
		this.setSizeXYGlobal(_model.getWidth(), _model.getHeight());
	}
	
	public model.Element getModel() 
	{
		return _model;
	}

	public void propertyChange(PropertyChangeEvent e) 
	{
        String propertyName = e.getPropertyName();
        
        if ( propertyName == "addElement" ) 
        {
			view.Element el = view.Element.newInstance(applet, (model.Element) e.getNewValue());
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
        else if ( propertyName == "setSize" ) 
        {
        	if(!this.isMiniature)
        	{
        		Vector3D oldsize = new Vector3D(this.getWidthXYRelativeToParent(), this.getHeightXYRelativeToParent(), 0);
        		Vector3D newsize = (Vector3D) e.getNewValue();
        		this.setSizeXYRelativeToParent(newsize.x, newsize.y);
        		Vector3D move = new Vector3D(0,0,0);

        		if ( e.getOldValue() instanceof model.Element.Corner )
        		{
	        		
	        		switch ( (model.Element.Corner)e.getOldValue() )
	        		{
		        		case LOWER_LEFT: move = new Vector3D(-1, 1, 0); break;
	        			case LOWER_RIGHT: move = new Vector3D(1, 1, 0); break;
	        			case UPPER_LEFT: move = new Vector3D(-1, -1, 0); break;
	        			case UPPER_RIGHT: move = new Vector3D(1, -1, 0); break;
	        		}
	        		
	        		this.translate(new Vector3D(move.x*(newsize.x - oldsize.x)/2, move.y*(newsize.y - oldsize.y)/2));
        		}
        		
        		for ( MTComponent cp : this.getChildren() )
        		{
            		if ( cp instanceof view.widget.Widget )
            		{
            			model.widget.Widget em = (Widget) ((view.widget.Widget) cp).getModel();
//            			Vector3D movebis = ((view.widget.Widget) cp).getCenterPointGlobal();
            			((view.Element) cp).setSizeXYGlobal(em.getWidth(), em.getHeight());
//            			((view.Element) cp).setPositionGlobal(movebis);

            			
    	        		cp.translate(new Vector3D(-move.x*(newsize.x - oldsize.x)/2, -move.y*(newsize.y - oldsize.y)/2));
            		}
        		}
        	}
        }
    }
	
	protected static Element newInstance(PApplet applet, model.Element e) 
	{		
		Element el = null;
	
		if ( e instanceof model.Page )
			el = new view.page.Page(applet, 0, 0, (model.Page) e);
		else if ( e instanceof model.widget.ButtonWidget )
			el = new view.widget.Button(applet, (model.widget.ButtonWidget) e);
		else if ( e instanceof model.widget.ListWidget )
			el = new view.widget.List(applet, (model.widget.ListWidget) e);
		else if ( e instanceof model.widget.ImgWidget )
			el = new view.widget.Image(applet, (model.widget.ImgWidget) e);
		else if ( e instanceof model.widget.Widget )
			el = new view.widget.Widget(applet, (model.widget.Widget) e);
	
		return el;
	}

	protected abstract void initGesture();
	protected abstract void initGraphics();
}
