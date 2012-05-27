package view.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.Element;

public class Page extends Element
{
	final static public String EVENT_DELETE_PAGE = "event_delete_page";
	
	protected MTClipRoundRect close;
	protected PageMenu menu;
	
	protected PropertyChangeSupport _viewNotifier;
	
	public Page(PApplet a, float x, float y, model.Page p) 
	{
		super(a, x, y, p);
		applet = a;
		_model = p;
		
		_viewNotifier = new PropertyChangeSupport(this);
	}
	
	public Page(Page p, Boolean create_new_model)
	{
		super(p,create_new_model); 
		
		_viewNotifier = new PropertyChangeSupport(this);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
		if(e.getPropertyName() == model.Page.EVENT_PAGE_DELETED) {
			this.destroy();
		}
		else if(e.getPropertyName() == model.Page.EVENT_PAGE_RENAMED) {
			this.menu.changePageName(e.getNewValue().toString());
		}
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
		
		close.removeAllGestureEventListeners(); // supprime les comportements par defaut (drag, zoom, ...)
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
			            view.page.Page.this.destroy();
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
		
		menu = new PageMenu(applet);
		this.addChild(menu);
		menu.setWidthXYGlobal(this.getWidthXYGlobal());
		menu.setAnchor(PositionAnchor.UPPER_LEFT);
		menu.setPositionRelativeToParent(new Vector3D(0, -menu.getHeight(), 0));
		menu.getTextArea().setText(((model.Page) _model).getLabel());
		menu.getTextArea().setPositionRelativeToParent(menu.getCenterPointLocal());

		close = new MTClipRoundRect(applet, this._model.getWidth() - 10, -(menu.getHeight() + 10), 0, 20, 20, 10, 10);
		close.setFillColor(MTColor.RED);
		
		this.addChild(close);
		
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
	
	public void setMinSize(float _w, float _h) {
		super.setMinSize(_w, _h);
		
		menu.setVisible(false);
		close.setVisible(false);
	}
	
	public void setFullSize() {
		super.setFullSize();
		
		menu.setVisible(true);
		menu.sendToFront();
		close.setVisible(true);
		close.sendToFront();
	}
	
	public void addListener(PropertyChangeListener listener)
	{
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
	
	public PropertyChangeSupport getViewNotifier() { return _viewNotifier; }
}
