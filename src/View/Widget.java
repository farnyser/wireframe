package View;

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
				
				Vector3D[] v = Widget.this.getBounds().getVectorsGlobal();
				
				boolean dragged_to_scene = true;
				for ( int i = 0 ; i < Widget.this.getRoot().getChildCount() ; i++ )
				{
					boolean inside = true;
					MTComponent parent = Widget.this.getRoot().getChildByIndex(i);
					if ( parent.getBounds() == null ) { break; }
					Vector3D[] v_into = parent.getBounds().getVectorsGlobal();

					for (Vector3D vector3d : v) 
					{
						if (!ToolsGeometry.isPolygonContainsPoint(v_into, vector3d))
						{
							inside = false;
							break;
						}
					}
					
					if ( inside )
					{
						// dragged to library
						if ( parent instanceof Library ) 
						{ 
							System.out.println("Widget dragged to library"); 
							//Widget.this.removeFromParent();
						}
						
						// dragged to page
						else if ( parent instanceof Page ) 
						{ 
							System.out.println("Widget dragged to page");
							if ( parent.getChildbyID(Widget.this.getID()) == null )
							{
								parent.addChild(Widget.this);
							}
						}	
						
						dragged_to_scene = false;
						break;
					}
				}
				// dragged to scene (workspace)
				if ( dragged_to_scene ) 
				{
					if ( Widget.this.getParent() != Widget.this.getRoot() )
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
