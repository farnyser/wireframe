package view.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.mt4j.MTApplication;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.Element;
import view.Library;

public class Page extends Element
{
	final static public String EVENT_DELETE_PAGE = "event_delete_page";
	
	protected PageContent content;
	protected PageMenu menu;
	protected boolean isLocked = false;
	protected PropertyChangeSupport _viewNotifier  = new PropertyChangeSupport(this);
	
	public Page(PApplet a, model.Page p) 
	{
		super(a, 0, 0, p);
	}
	
	public Page(Page p, Boolean create_new_model)
	{
		super(p,create_new_model);
		
		for ( PropertyChangeListener c : p.getViewNotifier().getPropertyChangeListeners() )
		{
			if ( c instanceof Library )
				this.addListener(c);
		}
	}
	
	public model.Page getModel() 
	{
		return (model.Page) _model;
	}

	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
		if(e.getPropertyName() == model.Page.EVENT_PAGE_DELETED) 
		{
			this.destroy();
		}
		else if(e.getPropertyName() == model.Page.EVENT_PAGE_RENAMED) 
		{
			this.menu.changePageName(e.getNewValue().toString());
		}
		else if ( e.getPropertyName() == model.Page.EVENT_PAGE_CLOSED )
		{
			this.destroy();
		}
	}

	@Override
	protected void initObject()
	{
		this.content = new PageContent(this.getRenderer(), 0, PageMenu.HEIGHT, this.getWidthXYGlobal(), this.getHeightXYGlobal()-(PageMenu.HEIGHT));
		this.addChild(content);
	}
	
	protected void initGesture()
	{
		content.removeAllGestureEventListeners();
		content.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) { return Page.this.processGestureEvent(ge); }
		});
		
		content.addGestureListener(ScaleProcessor.class, new IGestureEventListener() 
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) 
			{ 
				ge.setTarget(Page.this);
				Page.this.processGestureEvent(ge); 
				return false;
			}
		});
		
		content.addGestureListener(RotateProcessor.class, new IGestureEventListener() 
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				ge.setTarget(Page.this);
				Page.this.processGestureEvent(ge); 
				return false;
			}
		});
		
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
					if ( Page.this.resizeStart == model.Element.Corner.LOWER_RIGHT )
						Page.this._model.setSize(Page.this._model.getWidth() + de.getTranslationVect().x, Page.this._model.getHeight() + de.getTranslationVect().y, Page.this.resizeStart);
					else if ( Page.this.resizeStart == model.Element.Corner.LOWER_LEFT )
						Page.this._model.setSize(Page.this._model.getWidth() - de.getTranslationVect().x, Page.this._model.getHeight() + de.getTranslationVect().y, Page.this.resizeStart);
					else if ( Page.this.resizeStart == model.Element.Corner.UPPER_RIGHT )
						Page.this._model.setSize(Page.this._model.getWidth() + de.getTranslationVect().x, Page.this._model.getHeight() - de.getTranslationVect().y, Page.this.resizeStart);
					else
						Page.this._model.setSize(Page.this._model.getWidth() - de.getTranslationVect().x, Page.this._model.getHeight() - de.getTranslationVect().y, Page.this.resizeStart);
					
					if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
						Page.this.dragType = DragType.MOVE;
				}
				
				return false;
			}
		});
		content.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		content.addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer((MTApplication)applet, this));
		content.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() 
		{
		    @Override
		    public boolean processGestureEvent(MTGestureEvent ge) 
		    {
		        TapAndHoldEvent te = (TapAndHoldEvent)ge;
		        if(te.getId() == TapAndHoldEvent.GESTURE_ENDED && te.getElapsedTime() >= te.getHoldTime())
		        {
		        	Vector3D pos = te.getLocationOnScreen();
		        	if ( pos.x <= Page.this.getCenterPointGlobal().x && pos.y <= Page.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.UPPER_LEFT;
		        	else if ( pos.x <= Page.this.getCenterPointGlobal().x && pos.y > Page.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.LOWER_LEFT;
		        	else if ( pos.x > Page.this.getCenterPointGlobal().x && pos.y <= Page.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.UPPER_RIGHT;
		        	else
		        		resizeStart = model.Element.Corner.LOWER_RIGHT;
		            
		            Page.this.dragType = DragType.RESIZE;
		        }
		        return false;
		    }    
		});
		
	}

	@Override
	protected void initGraphics() 
	{
		this.setFillColor(MTColor.WHITE);

		menu = new PageMenu(applet);
		this.addChild(menu);
		menu.setWidthXYGlobal(this.getWidthXYGlobal());
		menu.setAnchor(PositionAnchor.UPPER_LEFT);
		menu.setPositionRelativeToParent(new Vector3D(0, 0, 0));
		menu.changePageName(((model.Page) _model).getLabel());
		menu.setColor(this.getModel().getColorFromId());
		
		for ( model.Element e : _model.getElements() )
		{
			view.Element w = view.Element.newInstance(applet, e);
			this.addChild(w);
			
			if ( e instanceof model.widget.Widget ) 
			{
				w.setPositionRelativeToParent(((model.widget.Widget)e).getPosition()); 
			}
		}
	}
	
	public void setMinSize(float _w, float _h) 
	{
		super.setMinSize(_w, _h);
		content.setFillColor(this.getModel().getColorFromId());
		menu.setVisible(false);
	}
	
	public void setFullSize() 
	{
		super.setFullSize();
		content.setFillColor(MTColor.WHITE);
		menu.setVisible(true);
		menu.sendToFront();
	}
	
	public void fireDuplicate(model.Page model) 
	{
		this.getViewNotifier().firePropertyChange("duplicatedPage", null, model);
	}

	public void addListener(PropertyChangeListener listener)
	{
		_viewNotifier.removePropertyChangeListener(listener);
		_viewNotifier.addPropertyChangeListener(listener);
	}

	public void removeListener(PropertyChangeListener listener)
	{
		_viewNotifier.removePropertyChangeListener(listener);
	}
	
	public void resetListener()
	{
		_viewNotifier = new PropertyChangeSupport(this);
	}
	
	public PropertyChangeSupport getViewNotifier() 
	{ 
		return _viewNotifier; 
	}
	
	public void setIsLocked(boolean locked) 
	{
		this.isLocked = locked;
		this.content.setComposite(locked);
	}
	
	public boolean getIsLocked() 
	{ 
		return isLocked; 
	}
}
