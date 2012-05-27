package view.widget;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.Library;

public class Widget extends view.Element
{
	// fake position
	protected float fx = -1, fy = -1;
	
	protected boolean GRID_ENABLED = true;
	protected int GRID_SPACING = 20;
	
	public Widget(PApplet a, model.widget.Widget m) 
	{
		super(a, m.getPosition().x, m.getPosition().y, m);
	}
	
	public Widget(Widget widget, Boolean create_new_model) 
	{
		super(widget, create_new_model);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
        String propertyName = e.getPropertyName();
		if ( propertyName == "setPosition" ) 
        {
			model.widget.Widget wm = (model.widget.Widget) e.getNewValue();
        	this.setPositionRelativeToParent(wm.getPosition());
        }
	}
	
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
	
	protected MTComponent getMTcomponent()
	{
		MTComponent c = null;
		Vector3D[] newposshape = this.getBounds().getVectorsGlobal();
		
		Vector<MTComponent> mtcs = Widget.this.getMTcomponents();
		mtcs.remove(Widget.this);
		
		for ( int i = mtcs.size()-1 ; i >= 0 ; i-- )
		{
			boolean inside = true;
			MTComponent p = mtcs.get(i);
			if ( p.getBounds() == null ) { continue; }

			Vector3D[] v_p = p.getBounds().getVectorsGlobal();

			
			for (Vector3D v : newposshape) 
			{
				if (!ToolsGeometry.isPolygonContainsPoint(v_p, v))
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
		
		for ( model.Element e : model.getElements() )
		{
			view.Element w = view.Element.newInstance(applet, e);
			this.addChild(w);
			if ( e instanceof model.widget.Widget ) 
			{ 
				w.setPositionRelativeToParent(((model.widget.Widget)e).getPosition()); 
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
					
					if ( fx == -1 || fy == -1 ) 
					{ 
						fx = Widget.this.getCenterPointRelativeToParent().x; 
						fy = Widget.this.getCenterPointRelativeToParent().y; 
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
							if ( de.getId() == MTGestureEvent.GESTURE_ENDED && c instanceof Library ) 
							{ 
								System.out.println("Widget dragged to library"); 
	
								// remove from parent (model)
								if ( Widget.this.getParent() instanceof view.Element ){ view.Element cc = (view.Element)  Widget.this.getParent(); cc.getModel().removeElement(Widget.this.model); }
	
								Widget.this.destroy();
							}
							// dragged to page/widget
							else if ( c instanceof view.Element ) 
							{ 
								if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
								{
									if ( c.getChildbyID(Widget.this.getID()) == null )
									{
										System.out.println("Widget dragged to page/widget");
										
										// remove from parent (model)
										if ( Widget.this.getParent() instanceof view.Element ){ view.Element cc = (view.Element)  Widget.this.getParent(); cc.getModel().removeElement(Widget.this.model); }
										
										// add to new parent (model)
										{ view.Element cc = (view.Element) c; cc.getModel().addElement(Widget.this.model); }
										
										c.addChild(Widget.this);
										destroy = true;
										Widget.this.setPositionGlobal(newpos);
										fx = Widget.this.getCenterPointRelativeToParent().x; 
										fy = Widget.this.getCenterPointRelativeToParent().y; 
									}
	
									// update coordinate in the model
	//								System.out.println("-- update coordinate..");
									model.widget.Widget mw = (model.widget.Widget) Widget.this.model;
									mw.setPosition(Widget.this.getCenterPointRelativeToParent().x - model.getWidth()/2, Widget.this.getCenterPointRelativeToParent().y - model.getHeight()/2, 0);
							}
								
								// grid
								if ( GRID_ENABLED )
								{
									float x = fx - Widget.this.model.getWidth()/2;
									float y = fy - Widget.this.model.getHeight()/2;
									
									if ( x%GRID_SPACING <= (GRID_SPACING/2) ) 
										x = (x-x%GRID_SPACING); 
									else if ( x%GRID_SPACING > GRID_SPACING-(GRID_SPACING/2) ) 
										x = (x-x%GRID_SPACING+GRID_SPACING); 
									if ( y%GRID_SPACING <= (GRID_SPACING/2) ) 
										y = (y-y%GRID_SPACING); 
									else if ( y%GRID_SPACING > GRID_SPACING-(GRID_SPACING/2) ) 
										y = (y-y%GRID_SPACING+GRID_SPACING);
									
	//								System.out.println("snap to x=" + x + ", y = " + y);
									x += Widget.this.model.getWidth()/2;
									y += Widget.this.model.getHeight()/2;
									
									Widget.this.setPositionRelativeToParent(new Vector3D(x,y,0));
									model.widget.Widget mw = (model.widget.Widget) Widget.this.model;
									mw.setPosition(x,y,0);
								}
							}	
						}
						// dragged to scene
						else if ( de.getId() == MTGestureEvent.GESTURE_ENDED && Widget.this.getParent() != Widget.this.getRoot() )
						{
							System.out.println("Widget dragged to scene (workspace)");
							Object o = Widget.this.getParent();
							
							Widget.this.setPositionGlobal(newpos);
							fx = -1;
							
							// remove from parent (model)
							if ( o instanceof view.Element ){ view.Element cc = (view.Element)  o; cc.getModel().removeElement(Widget.this.model); }
						}
	
					}
				}
				else if (Widget.this.dragType == DragType.RESIZE) 
				{
					Widget.this.model.setSize(Widget.this.model.getWidth() + de.getTranslationVect().x, Widget.this.model.getHeight() + de.getTranslationVect().y);
					
					if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
					{
						System.out.println("end of resize");
						Widget.this.dragType = DragType.MOVE;
					}
				}
				
				if ( destroy ) { Widget.this.removeFromParent(); }
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
		            Widget.this.dragType = DragType.RESIZE;
		        }
		        return false;
		    }    
		});
	}
}
