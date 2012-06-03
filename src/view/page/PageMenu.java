package view.page;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTClipRoundRect;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.EditableText;

public class PageMenu extends MTClipRectangle {
	
	protected EditableText textArea;
	protected PageMenuProperties properties;
	protected MTClipRectangle arrowDown;
	protected PApplet applet;
	protected MTClipRoundRect close;
	
	private boolean animationRunning = false; // for the slide down animation	

	public PageMenu(PApplet applet) 
	{
		super(applet, 0, 0, 0, 400, 25);

		this.applet = applet;
		textArea = new EditableText(applet) 
		{
			@Override
			protected boolean handleGesture(MTGestureEvent e) {
				return PageMenu.this.processGestureEvent(e);
			}

			@Override
			protected void textUpdated(String newUnformatedText) {
				((model.Page)((view.page.Page) PageMenu.this.getParent()).getModel()).setLabel(newUnformatedText);
			}

			@Override
			protected String getUnformatedText() {
				if(PageMenu.this.getParent() != null) {
					return ((model.Page)((view.page.Page) PageMenu.this.getParent()).getModel()).getLabel();
				}
				else return "";
			}

			@Override
			protected String getFormatedText() {
				return getUnformatedText();
			}
			
		};
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
		
		close = new MTClipRoundRect(applet, 0, 0, 0, 20, 20, 10, 10);
		close.setFillColor(MTColor.RED);
		close.setPositionRelativeToParent(new Vector3D(this.getWidth(), 0, 0));
		this.addChild(close);
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		
		// Slide animation
		final int duration = 200;
		MultiPurposeInterpolator defaultInterpolator = new MultiPurposeInterpolator(0, 0, duration, 0.0f, 1.0f, 1);
		final Animation slideAnimation = new Animation("Slide anim", defaultInterpolator, this, 0);
		slideAnimation.addAnimationListener(new IAnimationListener() {
			
			public void processAnimationEvent(AnimationEvent ae) {
				properties.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
					
					// if this is a slide up movement, we hide the properties
					if(ae.getDelta() <= 0) {
						properties.setVisible(false);
					}
				}
			}
		});		
		
		// Slide interaction
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			private Vector3D initialPoint;

			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				if(de.getId() == MTGestureEvent.GESTURE_STARTED) {
					initialPoint = de.getFrom();
					properties.setVisible(true);
					properties.setHeightLocal(properties.getFeedBackHeight());
				}
				else if(de.getId() == MTGestureEvent.GESTURE_UPDATED) {
					
					float delta = de.getTo().subtractLocal(initialPoint).y;
					
					if(delta >= properties.getFeedBackHeight() && delta <= PageMenuProperties.HEIGHT_WHEN_OPENED) {
						properties.setHeightLocal(delta);
					}
				}
				else if(de.getId() == MTGestureEvent.GESTURE_ENDED) {

					initialPoint = de.getTo().subtractLocal(initialPoint);

					if(animationRunning) return false;
					animationRunning = true;
					
					if(initialPoint.y > (PageMenuProperties.HEIGHT_WHEN_OPENED / 3) && (Math.abs(initialPoint.x) <= 100)) {
							properties.getEditablePageName().reloadText();
							PageMenu.this.sendToFront();
							MultiPurposeInterpolator slideDownInterpolator = new MultiPurposeInterpolator(initialPoint.y, PageMenuProperties.HEIGHT_WHEN_OPENED, duration, 0.0f, 1.0f, 1);
							slideAnimation.setInterpolator(slideDownInterpolator);
							slideAnimation.start();
					}
					else {
						if(initialPoint.y <= properties.getFeedBackHeight()) {
							animationRunning = false;
							properties.setVisible(false);
						}
						else {
							MultiPurposeInterpolator slideUpInterpolator = new MultiPurposeInterpolator(initialPoint.y, properties.getFeedBackHeight(), duration, 0.0f, 1.0f, 1);
							slideAnimation.setInterpolator(slideUpInterpolator);
							slideAnimation.start();
						}
					}
				}
				
		        return false;
			}
		});
		
		arrowDown.removeAllGestureEventListeners();
		
		close.removeAllGestureEventListeners(); // supprime les comportements par defaut (drag, zoom, ...)
		close.registerInputProcessor(new TapProcessor(this.applet));
		close.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						if ( view.page.PageMenu.this.getParent() != null )
						{
							view.page.PageMenu.this.getParent().destroy();
						}
					}
		        }
				
		        return false;
			}
		});
	}
	
	public void changePageName(String newLabel) {
		this.textArea.setText(newLabel);
		this.properties.getEditablePageName().reloadText();
		
		this.textArea.setPositionRelativeToParent(this.getCenterPointLocal());		
	}
	
	public void setColor(MTColor c) 
	{
		this.setFillColor(c);
		arrowDown.setFillColor(c);
		properties.setFillColor(c);
	}
	
	public float getHeight() { return this.getHeightXYGlobal(); }
	public float getWidth() { return this.getWidthXYGlobal(); }

	public MTTextArea getTextArea() { return this.textArea; }
}
