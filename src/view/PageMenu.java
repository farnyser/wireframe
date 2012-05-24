package view;

import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class PageMenu extends MTClipRectangle {
	
	protected MTTextArea textArea;
	protected PageMenuProperties properties;

	PageMenu(PApplet applet) {
		super(applet, 0, 0, 0, 400, 25);

		textArea = new MTTextArea(applet);
		properties = new PageMenuProperties(applet);

		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);

		textArea.setFillColor(new MTColor(0,0,0,MTColor.ALPHA_FULL_TRANSPARENCY));
		textArea.setNoStroke(true);
		textArea.setHeightXYGlobal(25);
		this.addChild(textArea);
		
		this.addChild(properties);
		properties.setAnchor(PositionAnchor.UPPER_LEFT);
		properties.setPositionRelativeToParent(new Vector3D(0, this.getHeight(), 0));
	}
	
	protected void initGesture() {
		this.removeAllGestureEventListeners();
		
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					Vector3D move = de.getTo();
					move.subtractLocal(PageMenu.this.getCenterPointGlobal());
					
					if(move.y > 50 && (Math.abs(move.x) <= 30)) {
						PageMenu.this.properties.setVisible(true);
					}

				}
				
		        return false;
			}
		});		
		
		textArea.removeAllGestureEventListeners();
		textArea.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return PageMenu.this.processGestureEvent(ge);
			}
		});		
	}
	
	public float getHeight() { return this.getHeightXYGlobal(); }
	public MTTextArea getTextArea() { return this.textArea; }
}
