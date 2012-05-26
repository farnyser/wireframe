package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class PageMenuProperties extends MTClipRectangle {
	
	protected MTTextArea ta;

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 200);

		ta = new MTTextArea(applet);
		
		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		
		ta.setText("Supprimer la page");
		
		this.setVisible(false);
		
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();		
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					Vector3D move = PageMenuProperties.this.getCenterPointGlobal();
					move.subtractLocal(de.getTo());
					System.out.println(move.y);
					if(move.y > 100 && (Math.abs(move.x) <= 100)) {
						PageMenuProperties.this.setVisible(false);
					}

				}
				
		        return false;
			}
		});		
	}

}
