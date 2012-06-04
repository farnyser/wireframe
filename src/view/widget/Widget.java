package view.widget;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.PickResult;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.DeleteEffect;
import view.Library;

public class Widget extends view.Element
{
	// fake position
	protected float fx = -1, fy = -1, fw = -1, fh = -1;
	
	// linking (action)
	protected MTLine line = null;
	
	// delete
	protected DeleteEffect delEffect = null;
	
	protected boolean GRID_ENABLED = true;
	protected int GRID_SPACING = 20;
	
	public Widget(PApplet a, model.widget.Widget m) 
	{
		super(a, m.getPosition().x, m.getPosition().y, m);
		delEffect = new DeleteEffect(this.applet, this, _model.getWidth(), _model.getHeight());
	}
	
	public Widget(Widget widget, Boolean create_new_model) 
	{
		super(widget, create_new_model);
		delEffect = new DeleteEffect(this.applet, this, _model.getWidth(), _model.getHeight());
	}
	
	public model.widget.Widget getModel() 
	{
		return (model.widget.Widget) _model;
	}

	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
        String propertyName = e.getPropertyName();
		if ( propertyName == "setPosition" ) 
        {
        	this.setPositionRelativeToParent(this.getModel().getPosition());
        }
		else if ( propertyName == "setLinks" ) 
        {
			if ( getModel().getLinks() != null )
				this.setStrokeColor(getModel().getLinks().getColorFromId());
			else
				this.setStrokeColor(MTColor.BLACK);
        }
	}
	
	/**
	 * Enable or Disable Delete Effect
	 * @param visible
	 */
	protected void setDelEffect(boolean visible)
	{
		this.delEffect.setVisible(visible);
		for ( MTComponent cm : this.getChildren() ) 
			if ( cm instanceof view.widget.Widget )
				((view.widget.Widget)cm).setDelEffect(visible);
	}
	
	/**
	 * Get all MTComponent on scene
	 * @return List of components
	 */
	protected Vector<MTComponent> getMTcomponents()
	{
		Vector<MTComponent> mtcs= new Vector<MTComponent>();
		mtcs.add(Widget.this.getRoot());
		
		for ( int i = 0; i < mtcs.size() ; i++ )
		{
			for ( int j = 0 ; j < mtcs.get(i).getChildCount() ; j++ )
			{
				mtcs.add( mtcs.get(i).getChildByIndex(j) );
			}
		}
		
		mtcs.remove(Widget.this);
		return mtcs;
	}
	
	/**
	 * Get component in which this is included
	 * @return a component or null
	 */
	protected MTComponent getMTcomponent()
	{
		MTComponent c = null;
		Vector3D[] newposshape = this.getBounds().getVectorsGlobal();
		
		Vector<MTComponent> mtcs = Widget.this.getMTcomponents();
		mtcs.remove(Widget.this);
		
		for ( int i = mtcs.size()-1 ; i >= 0 ; i-- )
		{
			// on ne s'interesse qu'aux collisions avec des objets "element" et "library"
			if ( !(mtcs.get(i) instanceof view.Element) && !(mtcs.get(i) instanceof view.Library) )
				continue;
			
			boolean inside = true;
			MTComponent p = mtcs.get(i);
			if ( p.getBounds() == null ) { continue; }

			Vector3D[] v_p = p.getBounds().getVectorsGlobal();

			
			for (Vector3D v : newposshape) 
			{
				if ( v_p.length <= 1 || !ToolsGeometry.isPolygonContainsPoint(v_p, v) )
				{
					inside = false;
					break;
				}
			}
			
			if ( inside )
			{
				c = p;
				break;
			}
		}
		
		return c;
	}
	
	protected void initGraphics()
	{
		this.setFillColor(MTColor.WHITE);
		this.setStrokeColor(MTColor.BLACK);
		
		for ( model.Element e : _model.getElements() )
		{
			view.Element w = view.Element.newInstance(applet, e);
			this.addChild(w);
			
			if ( e instanceof model.widget.Widget ) 
			{ 
				Vector3D wpos = ((model.widget.Widget)e).getPosition();				
				w.setPositionRelativeToParent(wpos); 
			}
		}
	}
	
	protected void initGesture()
	{
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				boolean destroy = false;
				DragEvent de = (DragEvent)ge;
				
				if ( dragType == DragType.MOVE )
				{
					Widget.this.setDelEffect(false);
					
					if ( fx == -1 || fy == -1 ) 
					{ 
						fx = Widget.this.getCenterPointRelativeToParent().x; 
						fy = Widget.this.getCenterPointRelativeToParent().y; 
					};					
					if ( fw == -1 || fh == -1 ) 
					{ 
						fw = Widget.this.getModel().getWidth(); 
						fh = Widget.this.getModel().getHeight(); 
					};
					Widget.this.fx += de.getTranslationVect().x;
					Widget.this.fy += de.getTranslationVect().y;
					
					Widget.this.translateGlobal(de.getTranslationVect());
					Widget.this.sendToFront();
					
					// calcul du "parent" uniquement si on utilise la grille
					// ou bien lors de la fin du mouvement
					if ( GRID_ENABLED || de.getId() == MTGestureEvent.GESTURE_ENDED )
					{
						MTComponent c = Widget.this.getMTcomponent();
						Vector3D newpos = Widget.this.getCenterPointGlobal();
						
						if ( c != null )
						{
							// dragged to library
							if ( c instanceof Library ) 
							{
								Widget.this.setDelEffect(true);
								
								if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
								{
									System.out.println("Widget dragged to library"); 
		
									// remove from parent (model)
									if ( Widget.this.getParent() instanceof view.Element ){ ((view.Element)Widget.this.getParent()).getModel().removeElement(Widget.this._model); }
		
									destroy = true;
								}
							}
							// dragged to page/widget
							else if ( c instanceof view.Element ) 
							{ 
								if ( de.getId() == MTGestureEvent.GESTURE_ENDED &&  c.getChildbyID(Widget.this.getID()) == null )
								{
									System.out.println("Widget dragged to page/widget");
									
									// remove from parent (model)
									if ( Widget.this.getParent() instanceof view.Element ){ ((view.Element)Widget.this.getParent()).getModel().removeElement(Widget.this._model); }
									
									// add to new parent (model)
									{ ((view.Element) c).getModel().addElement(Widget.this._model); }
									
									c.addChild(Widget.this);
									destroy = true;
									Widget.this.setPositionGlobal(newpos);
									fx = Widget.this.getCenterPointRelativeToParent().x; 
									fy = Widget.this.getCenterPointRelativeToParent().y; 
								}
								
								// grid
								if ( GRID_ENABLED )
								{
									float x = fx - Widget.this._model.getWidth()/2;
									float y = fy - Widget.this._model.getHeight()/2;
									
									if ( x%GRID_SPACING <= (GRID_SPACING/2) ) 
										x = (x-x%GRID_SPACING); 
									else if ( x%GRID_SPACING > GRID_SPACING-(GRID_SPACING/2) ) 
										x = (x-x%GRID_SPACING+GRID_SPACING); 
									if ( y%GRID_SPACING <= (GRID_SPACING/2) ) 
										y = (y-y%GRID_SPACING); 
									else if ( y%GRID_SPACING > GRID_SPACING-(GRID_SPACING/2) ) 
										y = (y-y%GRID_SPACING+GRID_SPACING);
									
									x += Widget.this._model.getWidth()/2;
									y += Widget.this._model.getHeight()/2;
									
									Widget.this.setPositionRelativeToParent(new Vector3D(x,y,0));
								}
								
								// update model position
								if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
								{
									// update coordinate in the model
									model.widget.Widget mw = (model.widget.Widget) Widget.this._model;
									mw.setPosition(Widget.this.getCenterPointRelativeToParent());
								}
							}	
						}
						// dragged to scene
						else if ( Widget.this.getParent() != Widget.this.getRoot() )
						{
							Widget.this.setDelEffect(true);
							
							if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
							{
								System.out.println("Widget dragged to scene (workspace)");
								Object o = Widget.this.getParent();
								
								Widget.this.setPositionGlobal(newpos);
								fx = -1;
								
								// remove from parent (model)
								if ( o instanceof view.Element ){ view.Element cc = (view.Element)  o; cc.getModel().removeElement(Widget.this._model); }
							}
						}
					}
					
					
					if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
					{
						System.out.println("widget(view) position global : " + Widget.this.getCenterPointGlobal());
						System.out.println("widget(view) position local2parent : " + Widget.this.localToParent(Widget.this.getCenterPointLocal()));
					}
				}
				else if (Widget.this.dragType == DragType.RESIZE) 
				{
					Vector3D direction = null;
					
					if ( Widget.this.resizeStart == model.Element.Corner.LOWER_RIGHT )
						direction = new Vector3D(1,1);
					else if ( Widget.this.resizeStart == model.Element.Corner.LOWER_LEFT )
						direction = new Vector3D(-1,1);
					else if ( Widget.this.resizeStart == model.Element.Corner.UPPER_RIGHT )
						direction = new Vector3D(1,-1);
					else
						direction = new Vector3D(-1,-1);
					
					fw += direction.x * de.getTranslationVect().x;
					fh += direction.y * de.getTranslationVect().y;
					
					if ( GRID_ENABLED )
						Widget.this._model.setSize(fw-fw%GRID_SPACING, fh-fh%GRID_SPACING, Widget.this.resizeStart);
					else
						Widget.this._model.setSize(fw, fh, Widget.this.resizeStart);
					
					// retour en mode "deplacement"
					if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
						Widget.this.dragType = DragType.MOVE;
				}
				
				if ( destroy ) { Widget.this.removeFromParent(); }
				return false;
			}
		});
		
		this.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		this.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer((MTApplication)applet, this));
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() 
		{
		    @Override
		    public boolean processGestureEvent(MTGestureEvent ge) 
		    {
		        TapAndHoldEvent te = (TapAndHoldEvent)ge;
		        if(te.getId() == TapAndHoldEvent.GESTURE_ENDED && te.getElapsedTime() >= te.getHoldTime())
		        {
		        	Vector3D pos = te.getLocationOnScreen();
		        	if ( pos.x <= Widget.this.getCenterPointGlobal().x && pos.y <= Widget.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.UPPER_LEFT;
		        	else if ( pos.x <= Widget.this.getCenterPointGlobal().x && pos.y > Widget.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.LOWER_LEFT;
		        	else if ( pos.x > Widget.this.getCenterPointGlobal().x && pos.y <= Widget.this.getCenterPointGlobal().y )
		        		resizeStart = model.Element.Corner.UPPER_RIGHT;
		        	else
		        		resizeStart = model.Element.Corner.LOWER_RIGHT;
		            
		            Widget.this.dragType = DragType.RESIZE;
		        }
		        return false;
		    }    
		});
		
		// 2 doigts pour creer les liens
		this.removeAllGestureEventListeners(RotateProcessor.class);
		this.removeAllGestureEventListeners(ScaleProcessor.class);
		this.addGestureListener(ScaleProcessor.class, new IGestureEventListener()
		{
			@Override
			public boolean processGestureEvent(MTGestureEvent arg0) 
			{
	            ScaleEvent de = (ScaleEvent) arg0;
	            
				if ( line == null )
				{
					Vector3D start = localToGlobal( de.getFirstCursor().getPosition() );
					Vector3D stop = localToGlobal( de.getSecondCursor().getPosition() );
					line = new MTLine(Widget.this.applet, start.x, start.y, stop.x, stop.y);
					line.setStrokeColor(MTColor.RED);
					line.setUserData("start", de.getFirstCursor().getPosition());
					Widget.this.getRoot().addChild(line);
				}
				else
				{
					Vector3D start = (Vector3D) line.getUserData("start");
					line.destroy();
					line = new MTLine(Widget.this.applet, start.x, start.y, de.getSecondCursor().getCurrentEvtPosX(), de.getSecondCursor().getCurrentEvtPosY());
					line.setStrokeColor(MTColor.RED);
					line.setUserData("start", start);
					Widget.this.getRoot().addChild(line);
				}
				
				if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
				{
					Vector3D p = (de.getSecondCursor().getPosition());
					
					PickResult pk = Widget.this.getRoot().pick(p.x, p.y, true);
					MTComponent target = pk.getNearestPickResult();
					if ( target instanceof MTListCell ) { target = target.getChildByIndex(0); }
					
					if ( target instanceof view.page.Page )
					{
						System.out.println("create link between " + Widget.this + " and " + target);
						((view.page.Page)target).getModel().addLinked(Widget.this.getModel());
						Widget.this.getModel().addLink(((view.page.Page)target).getModel());
					}
					else
					{
						Widget.this.getModel().addLink(null);
					}
					
					line.destroy();
					line = null;
				}

				return false;
			} 
				
		});
	}
}
