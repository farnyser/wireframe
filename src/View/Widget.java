package View;

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

public class Widget extends MTClipRectangle
{
	public Widget(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, 0, width, height);
		this.setFillColor(MTColor.randomColor());
		
		this.removeAllGestureEventListeners(DragProcessor.class);
		this.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{				
				DragEvent de = (DragEvent)ge;
				Widget.this.translateGlobal(de.getTranslationVect());
				
				if ( de.getId() == MTGestureEvent.GESTURE_ENDED )
				{
					MTComponent c = null;
					Vector3D[] newpos = Widget.this.getBounds().getVectorsGlobal();
					
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

						
						for (Vector3D v : newpos) 
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
						if ( c instanceof Library ) 
						{ 
							System.out.println("Widget dragged to library"); 
							Widget.this.destroy();
						}
						// dragged to page/widget
						else if ( c instanceof Page || c instanceof Widget ) 
						{ 
							System.out.println("Widget dragged to page/widget");
							if ( c.getChildbyID(Widget.this.getID()) == null )
							{
								c.addChild(Widget.this);
							}
						}	
					}
					else if ( Widget.this.getParent() != Widget.this.getRoot() )
					{
						System.out.println("Widget dragged to scene (workspace)");
						Widget.this.getRoot().addChild(Widget.this);
					}

				}
				
				return false;
			}
		});
	}
}
