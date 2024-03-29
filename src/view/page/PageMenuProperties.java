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
	
	final static public int HEIGHT_WHEN_OPENED = 200;
	
	protected boolean animationRunning = false; // for the slide up animation
	
	protected ConfirmationSlider _sliderConfirm;
	protected EditableText _pageName;
	protected MTClipRectangle _feedback;
	protected MTTextArea _pageNameLabel;
	protected MTTextArea _deleteLabel;

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 0);

		_sliderConfirm = new ConfirmationSlider(applet, 200, 20, 15);
		_sliderConfirm.addPropertyChangeListener(ConfirmationSlider.EVENT_CONFIRMATION, this);
		
		IFont font = FontManager.getInstance().createFont(this.getRenderer(), "SansSerif", 12);
		_pageNameLabel = new MTTextArea(this.getRenderer(), font);
		_deleteLabel = new MTTextArea(this.getRenderer(), font);
		
		_feedback = new MTClipRectangle(applet, 0, this.getHeightXY(TransformSpace.LOCAL), 0, 400, 20);
		
		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		this.setNoStroke(true);
		this.setVisible(false);
		
		
		this.addChild(_pageNameLabel);
		_pageNameLabel.setAnchor(PositionAnchor.UPPER_LEFT);
		_pageNameLabel.setPositionRelativeToParent(new Vector3D(10, 15, 0));
		_pageNameLabel.setText("Nom de la page");
		_pageNameLabel.setNoFill(true);
		_pageNameLabel.setNoStroke(true);
		_pageNameLabel.setPickable(false);
		
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
		_pageName.setPositionRelativeToParent(new Vector3D(_pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).x + _pageNameLabel.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 5, 15, 0));
		_pageName.setFont(_pageNameLabel.getFont());
		_pageName.setNoStroke(false);
		
		this.addChild(_deleteLabel);
		_deleteLabel.setAnchor(PositionAnchor.UPPER_LEFT);
		float deleteLineY = _pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + _pageNameLabel.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + 10;
		_deleteLabel.setPositionRelativeToParent(new Vector3D(10, deleteLineY, 0));
		_deleteLabel.setText("Suppression de la page");
		_deleteLabel.setNoFill(true);
		_deleteLabel.setNoStroke(true);
		_deleteLabel.setPickable(false);
		
		this.addChild(_sliderConfirm);
		_sliderConfirm.setPositionRelativeToParent(new Vector3D(_deleteLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).x + _deleteLabel.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 5, deleteLineY, 0));
		
		this.addChild(_feedback);
		_feedback.setAnchor(PositionAnchor.LOWER_LEFT);
		_feedback.setNoStroke(true);
	}
	
	public void setFillColor(MTColor c) {
		super.setFillColor(c);
		
		_feedback.setFillColor(new MTColor(c.getR() - 50, c.getG() - 50, c.getB() - 50, c.getAlpha()));
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
			private Vector3D expectedDirection;

			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				if(de.getId() == MTGestureEvent.GESTURE_STARTED)
				{
					initialPoint = de.getFrom();
					expectedDirection = PageMenuProperties.this.getCenterPointGlobal().getSubtracted(de.getFrom()).normalizeLocal();
				}
				else if(de.getId() == MTGestureEvent.GESTURE_UPDATED)
				{
					// vecteur direction
					Vector3D d = de.getTo().getSubtracted(initialPoint);
					float delta = (float) Math.sqrt(Math.pow((expectedDirection.x * d.x),2) + Math.pow((expectedDirection.y * d.y),2));
					float angle = (float) Math.abs(180.0/3.14957 * Vector3D.angleBetween(d, expectedDirection));

					if(delta > 0 && delta <= PageMenuProperties.HEIGHT_WHEN_OPENED - PageMenuProperties.this.getFeedBackHeight() && angle < 90) {
						PageMenuProperties.this.setHeightLocal(PageMenuProperties.HEIGHT_WHEN_OPENED - delta);
					}
				}
				else if(de.getId() == MTGestureEvent.GESTURE_ENDED) 
				{
					// vecteur direction
					Vector3D d = de.getTo().getSubtracted(initialPoint);
					float delta = (float) Math.sqrt(Math.pow((expectedDirection.x * d.x),2) + Math.pow((expectedDirection.y * d.y),2));
					float angle = (float) Math.abs(180.0/3.14957 * Vector3D.angleBetween(d, expectedDirection));

					if(animationRunning) return false;
					animationRunning = true;

					if(delta > (PageMenuProperties.HEIGHT_WHEN_OPENED / 3) && angle <= 90) 
					{
						if(delta > PageMenuProperties.HEIGHT_WHEN_OPENED) {
							animationRunning = false;
							PageMenuProperties.this.setVisible(false);
						}
						else {
							MultiPurposeInterpolator slideUpInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED - delta, PageMenuProperties.this.getFeedBackHeight(), duration, 0.0f, 1.0f, 1);
							slideAnimation.setInterpolator(slideUpInterpolator);
							slideAnimation.start();
						}
					}
					else {
						if(delta > 0 && delta < PageMenuProperties.HEIGHT_WHEN_OPENED - PageMenuProperties.this.getFeedBackHeight()) {
							MultiPurposeInterpolator slideDownInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED - delta, PageMenuProperties.HEIGHT_WHEN_OPENED, duration, 0.0f, 1.0f, 1);
							slideAnimation.setInterpolator(slideDownInterpolator);
							slideAnimation.start();
						}
						else {
							animationRunning = false;
						}
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
		
		// on affiche ou non les composants
		_pageNameLabel.setVisible(_pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + _pageNameLabel.getHeightXY(TransformSpace.LOCAL) <= height);
		_deleteLabel.setVisible(_deleteLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + _deleteLabel.getHeightXY(TransformSpace.LOCAL) <= height);
		_pageName.setVisible(_pageName.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + _pageName.getHeightXY(TransformSpace.LOCAL) <= height);
		_sliderConfirm.setVisible(_sliderConfirm.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + _sliderConfirm.getHeightXY(TransformSpace.LOCAL) <= height);
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
