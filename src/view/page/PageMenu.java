package view.page;

import org.mt4j.components.TransformSpace;
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

public class PageMenu extends MTClipRectangle {
	
	protected MTTextArea textArea;
	protected PageMenuProperties properties;
	protected MTClipRectangle arrowDown;
	protected PApplet applet;
	
	private boolean animationRunning = false; // for the slide down animation	

	PageMenu(PApplet applet) {
		super(applet, 0, 0, 0, 400, 25);

		this.applet = applet;
		textArea = new MTTextArea(applet);
		properties = new PageMenuProperties(applet);
		arrowDown = new MTClipRectangle(applet, 0, 0, 0, 10, 10);

		initGraphics();
		initGesture();
	}
	 
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		this.setNoStroke(true);

		textArea.setFillColor(new MTColor(0,0,0,MTColor.ALPHA_FULL_TRANSPARENCY));
		textArea.setNoStroke(true);
		textArea.setHeightXYGlobal(25);
		this.addChild(textArea);
		
		this.addChild(properties);
		properties.setAnchor(PositionAnchor.UPPER_LEFT);
		properties.setPositionRelativeToParent(new Vector3D(0, this.getHeight(), 0));
		
		this.addChild(arrowDown);
		arrowDown.setFillColor(MTColor.GRAY);
		arrowDown.setNoStroke(true);
		arrowDown.setPositionRelativeToParent(new Vector3D(this.getWidth() / 2, this.getHeight(), 0));
		arrowDown.rotateZ(arrowDown.getCenterPointLocal(), 45, TransformSpace.LOCAL);
		
		textArea.sendToFront();
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		
		// Slide down animation
		final int duration = 200;
		MultiPurposeInterpolator slideDownInterpolator = new MultiPurposeInterpolator(0, PageMenuProperties.HEIGHT_WHEN_OPENED, duration, 0.0f, 1.0f, 1);
		final Animation slideDownAnimation = new Animation("Slide down anim", slideDownInterpolator, this.properties, 0);
		slideDownAnimation.addAnimationListener(new IAnimationListener() {
			
			public void processAnimationEvent(AnimationEvent ae) {
				PageMenu.this.properties.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_STARTED) {
					PageMenu.this.properties.setVisible(true);
				}
				else if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
				}
			}
		});
		
		// Slide Down interaction
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					Vector3D move = de.getTo();
					move.subtractLocal(PageMenu.this.getCenterPointGlobal());
					
					if(move.y > 150 && (Math.abs(move.x) <= 100)) {
					
						if (!animationRunning){
							animationRunning = true;
							properties.getEditablePageName().reloadText();
							slideDownAnimation.start();
						}
					}
				}
				
		        return false;
			}
		});
		
		arrowDown.removeAllGestureEventListeners();
		
		textArea.removeAllGestureEventListeners();
		textArea.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return PageMenu.this.processGestureEvent(ge);
			}
		});
	}
	
	public void changePageName(String newLabel) {
		this.textArea.setText(newLabel);
		this.properties.getEditablePageName().reloadText();
	}
	
	public float getHeight() { return this.getHeightXYGlobal(); }
	public float getWidth() { return this.getWidthXYGlobal(); }

	public MTTextArea getTextArea() { return this.textArea; }
}
