package widget;

import java.util.Vector;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;



import processing.core.PApplet;
import view.Library;
import view.Page;

public class Widget extends MTClipRectangle
{
	protected PApplet applet;
	
	// real size
	protected float h, w;
	
	// miniature size
	protected float mh, mw;
	
	// fake position
	protected float fx = -1, fy = -1;
	
	protected boolean GRID_ENABLED = true;
	
	public Widget(PApplet a, float x, float y, float width, float height) 
	{
		super(a, x, y, 0, width, height);
		applet = a;
		w = width; 
		h = height;
		mh = h;
		mw = w;
		initGesture();
		initGraphics();
	}
	
	public Widget(Widget widget) 
	{
		super(widget.applet, 0, 0, 0, widget.w, widget.h);
		applet = widget.applet;
		h = widget.h; 
		w = widget.w;
		mh = widget.mh; 
		mw = widget.mw;
		initGesture();
		initGraphics();
		this.setPositionGlobal(new Vector3D(widget.getCenterPointGlobal().x, widget.getCenterPointGlobal().y, 0));
		this.setMinSize(mw, mh);
		this.setFillColor(widget.getFillColor());
	}
	
	protected void initGraphics()
	{
		this.setFillColor(MTColor.randomColor());
	}
	
	private void initGesture()
	{
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				
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
					MTComponent c = null;
					Vector3D[] newposshape = Widget.this.getBounds().getVectorsGlobal();
					Vector3D newpos = Widget.this.getCenterPointGlobal();
					
					Vector<MTComponent> mtcs = new Vector<MTComponent>();
					mtcs.add(Widget.this.getRoot());
					
					for ( int i = 0; i < mtcs.size() ; i++ )
					{
						for ( int j = 0 ; j < mtcs.get(i).getChildCount() ; j++ )
						{
							mtcs.add( mtcs.get(i).getChildByIndex(j) );
						}
					}
					
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
					
					
					if ( c != null )
					{
						// dragged to library
						if ( de.getId() == MTGestureEvent.GESTURE_ENDED && c instanceof Library ) 
						{ 
							System.out.println("Widget dragged to library"); 
							Widget.this.destroy();
						}
						// dragged to page/widget
						else if ( c instanceof Page || c instanceof Widget ) 
						{ 
							if ( de.getId() == MTGestureEvent.GESTURE_ENDED && c.getChildbyID(Widget.this.getID()) == null )
							{
								System.out.println("Widget dragged to page/widget");
								c.addChild(Widget.this);
								Widget.this.setPositionGlobal(newpos);
								fx = Widget.this.getCenterPointRelativeToParent().x; 
								fy = Widget.this.getCenterPointRelativeToParent().y; 
							}
							
							// grid
							if ( GRID_ENABLED )
							{
								int grid = 40;
								int spacing = 20;
								
								float x = fx - Widget.this.w/2;
								float y = fy - Widget.this.h/2;
								
//								System.out.println("x%grid = " + (int)(x%grid) );
								
								if ( x%grid <= spacing ) { x = (x-x%grid); System.out.println("snap to x=" + x); }
								else if ( x%grid > grid-spacing ) { x = (x-x%grid+grid); System.out.println("snap to x=" + x);  }
								if ( y%grid <= spacing ) { y = (y-y%grid); System.out.println("snap to y=" + y);  }
								else if ( y%grid > grid-spacing ) { y = (y-y%grid+grid); System.out.println("snap to y=" + y);  }
								
								x += Widget.this.w/2;
								y += Widget.this.h/2;
								
								Widget.this.setPositionRelativeToParent(new Vector3D(x,y,0));
							}
						}	
					}
					else if ( de.getId() == MTGestureEvent.GESTURE_ENDED && Widget.this.getParent() != Widget.this.getRoot() )
					{
						System.out.println("Widget dragged to scene (workspace)");
						Widget.this.getRoot().addChild(Widget.this);
						Widget.this.setPositionGlobal(newpos);
						fx = -1;
					}

				}
				
				return false;
			}
		});
	}
	
	public void setMinSize(float _w, float _h)
	{
		this.mh = _h;
		this.mw = _w;
		
		Vector3D pos = this.getCenterPointGlobal();
		this.setSizeXYGlobal(this.mw, this.mh);
		pos.x = pos.x - (this.w - this.mw)/2;
		pos.y = pos.y - (this.h - this.mh)/2;
		this.setPositionGlobal(pos);
	}
	
	public void setFullSize()
	{
		this.setSizeXYGlobal(w,h);
	}
}
