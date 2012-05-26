package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class PageMenuProperties extends MTClipRectangle {
	
	protected MTTextArea ta;
	
	final static public float HEIGHT_WHEN_OPENED = 200;
	
	private boolean animationRunning = false; // for the slide up animation	

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 0);

		ta = new MTTextArea(applet);
		
		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		
		ta.setText("Supprimer la page");
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		
		final int duration = 200;
		MultiPurposeInterpolator slideUpInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED, 0, duration, 0.0f, 1.0f, 1);
		final Animation slideUpAnimation = new Animation("Slide up anim", slideUpInterpolator, this, 0);
		slideUpAnimation.addAnimationListener(new IAnimationListener() {
			
			public void processAnimationEvent(AnimationEvent ae) {
				PageMenuProperties.this.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
				}
			}
		});
		
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					Vector3D move = PageMenuProperties.this.getCenterPointGlobal();
					move.subtractLocal(de.getTo());
					if(move.y > 100 && (Math.abs(move.x) <= 100)) {
						if (!animationRunning){
							animationRunning = true;
							slideUpAnimation.start();
						}
					}

				}
				
		        return false;
			}
		});		
	}

}
