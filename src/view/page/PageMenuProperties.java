package view.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.ConfirmationSlider;
import view.EditableText;

public class PageMenuProperties extends MTClipRectangle implements PropertyChangeListener {
	
	final static public float HEIGHT_WHEN_OPENED = 250;
	
	protected boolean animationRunning = false; // for the slide up animation
	
	protected ConfirmationSlider _sliderConfirm;
	protected EditableText _pageName;
	protected MTClipRectangle _feedback;

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 0);

		_sliderConfirm = new ConfirmationSlider(applet, 200, 20, 15);
		_sliderConfirm.addPropertyChangeListener(ConfirmationSlider.EVENT_CONFIRMATION, this);
		
		_feedback = new MTClipRectangle(applet, 0, this.getHeightXY(TransformSpace.LOCAL), 0, 400, 20);
		
		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		this.setNoStroke(true);
		this.setVisible(false);
		
		IFont font = FontManager.getInstance().createFont(this.getRenderer(), "SansSerif", 12);
		
		MTTextArea pageNameLabel = new MTTextArea(this.getRenderer(), font);
		this.addChild(pageNameLabel);
		pageNameLabel.setAnchor(PositionAnchor.UPPER_LEFT);
		pageNameLabel.setPositionRelativeToParent(new Vector3D(10, 15, 0));
		pageNameLabel.setText("Nom de la page");
		pageNameLabel.setNoFill(true);
		pageNameLabel.setNoStroke(true);
		pageNameLabel.setPickable(false);
		pageNameLabel.setVisible(false);
		
		_pageName = new EditableText(this.getRenderer()) 
		{
			@Override
			protected boolean handleGesture(MTGestureEvent e) {
				return false;
			}

			@Override
			protected void textUpdated(String newUnformatedText) {
				((model.Page)((view.page.Page) PageMenuProperties.this.getParent().getParent()).getModel()).setLabel(newUnformatedText);
			}

			@Override
			protected String getUnformatedText() {
				if(PageMenuProperties.this.getParent() != null) {
					return ((model.Page)((view.page.Page) PageMenuProperties.this.getParent().getParent()).getModel()).getLabel();
				}
				else return "";
			}

			@Override
			protected String getFormatedText() {
				return getUnformatedText();
			}
		};
	    
		this.addChild(_pageName);
		_pageName.setAnchor(PositionAnchor.UPPER_LEFT);
		_pageName.setPositionRelativeToParent(new Vector3D(pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).x + pageNameLabel.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 5, 15, 0));
		_pageName.setFont(font);
		_pageName.setNoStroke(false);
		_pageName.setVisible(false);
		
		MTTextArea deleteLabel = new MTTextArea(this.getRenderer(), font);
		this.addChild(deleteLabel);
		deleteLabel.setAnchor(PositionAnchor.UPPER_LEFT);
		float deleteLineY = pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + pageNameLabel.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + 10;
		deleteLabel.setPositionRelativeToParent(new Vector3D(10, deleteLineY, 0));
		deleteLabel.setText("Suppression de la page");
		deleteLabel.setNoFill(true);
		deleteLabel.setNoStroke(true);
		deleteLabel.setPickable(false);
		deleteLabel.setVisible(false);
		
		this.addChild(_sliderConfirm);
		_sliderConfirm.setPositionRelativeToParent(new Vector3D(deleteLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).x + deleteLabel.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 5, deleteLineY, 0));
		_sliderConfirm.setVisible(false);
		
		this.addChild(_feedback);
		_feedback.setFillColor(MTColor.BLACK);
		_feedback.setAnchor(PositionAnchor.LOWER_LEFT);
		_feedback.setNoStroke(true);
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		
		// Slide animation
		final int duration = 200;
		MultiPurposeInterpolator defaultInterpolator = new MultiPurposeInterpolator(0, 0, duration, 0.0f, 1.0f, 1);
		final Animation slideAnimation = new Animation("Slide anim", defaultInterpolator, this, 0);
		slideAnimation.addAnimationListener(new IAnimationListener() {
			
			public void processAnimationEvent(AnimationEvent ae) {
				PageMenuProperties.this.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
					
					// if this is a slide up movement, we hide the properties
					if(ae.getDelta() <= 0) {
						PageMenuProperties.this.setVisible(false);
					}
				}
			}
		});		
		
		// Slide interaction
		_feedback.removeAllGestureEventListeners();
		_feedback.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			private Vector3D initialPoint;

			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				if(de.getId() == MTGestureEvent.GESTURE_STARTED) {
					initialPoint = de.getFrom();
				}
				else if(de.getId() == MTGestureEvent.GESTURE_UPDATED) {
					
					float delta = initialPoint.y - de.getTo().y;

					if(delta > 0 && delta <= PageMenuProperties.HEIGHT_WHEN_OPENED - PageMenuProperties.this.getFeedBackHeight()) {
						PageMenuProperties.this.setHeightLocal(PageMenuProperties.HEIGHT_WHEN_OPENED - delta);
					}
				}
				else if(de.getId() == MTGestureEvent.GESTURE_ENDED) {

					initialPoint.subtractLocal(de.getTo());

					if(animationRunning) return false;
					animationRunning = true;
					
					if(initialPoint.y > (PageMenuProperties.HEIGHT_WHEN_OPENED / 3) && (Math.abs(initialPoint.x) <= 100)) {
						MultiPurposeInterpolator slideUpInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED - initialPoint.y, PageMenuProperties.this.getFeedBackHeight(), duration, 0.0f, 1.0f, 1);
							slideAnimation.setInterpolator(slideUpInterpolator);
							slideAnimation.start();
					}
					else {
						MultiPurposeInterpolator slideDownInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED - initialPoint.y, PageMenuProperties.HEIGHT_WHEN_OPENED, duration, 0.0f, 1.0f, 1);
						slideAnimation.setInterpolator(slideDownInterpolator);
						slideAnimation.start();
					}
				}
				
		        return false;
			}
		});
	} 
	
	@Override
	public void setHeightLocal(float height) {
		
		super.setHeightLocal(height);

		// on set la barre de feedback au bon Y
		_feedback.setPositionRelativeToParent(new Vector3D(0, this.getHeightXY(TransformSpace.LOCAL), 0));
	}
	
	public float getFeedBackHeight() {
		return _feedback.getHeightXY(TransformSpace.LOCAL);
	}

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		
		if(ev.getPropertyName() == ConfirmationSlider.EVENT_CONFIRMATION) {
			Page pageView = (Page) PageMenuProperties.this.getParent().getParent();
			pageView.getViewNotifier().firePropertyChange(Page.EVENT_DELETE_PAGE, null, null);
		}
	}
	
	public EditableText getEditablePageName() { return _pageName; }
}
