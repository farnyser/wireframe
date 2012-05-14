package View;

import java.util.Vector;

import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTSceneMenu;
import org.mt4j.components.visibleComponents.widgets.MTSceneWindow;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.sceneManagement.transition.BlendTransition;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.ToolsGeometry;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class Library extends MTList
{
	public Library(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, width, height, 10);
		
		this.setFillColor(MTColor.TEAL);
		this.setPickable(true);
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			Widget widget = new Widget(applet, 0, 0, (i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
			widget.setMinSize(50, 50);
			cell.addChild(widget);
			this.addListElement(cell);
			this.addDragProcessor(cell);
		}
	}
	
	private void addDragProcessor(MTListCell cell)
	{
		cell.addGestureListener(DragProcessor.class, new IGestureEventListener() 
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent)ge;
				MTListCell target = (MTListCell) ge.getTarget();
				Widget w = (Widget) target.getChildByIndex(0);
				Widget nw = (Widget) w.clone();
				nw.setFullSize();
				nw.processGestureEvent(ge);
				return false;
			}
		});
	}
}
