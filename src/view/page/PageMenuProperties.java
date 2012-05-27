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
	
	final static public float HEIGHT_WHEN_OPENED = 200;
	
	protected boolean animationRunning = false; // for the slide up animation
	
	protected ConfirmationSlider _sliderConfirm;
	protected EditableText _pageName;

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 0);

		_sliderConfirm = new ConfirmationSlider(applet, 200, 20, 15);
		_sliderConfirm.addPropertyChangeListener(ConfirmationSlider.EVENT_CONFIRMATION, this);
		
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
		
		MTTextArea deleteLabel = new MTTextArea(this.getRenderer(), font);
		this.addChild(deleteLabel);
		deleteLabel.setAnchor(PositionAnchor.UPPER_LEFT);
		float deleteLineY = pageNameLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).y + pageNameLabel.getHeightXY(TransformSpace.RELATIVE_TO_PARENT) + 10;
		deleteLabel.setPositionRelativeToParent(new Vector3D(10, deleteLineY, 0));
		deleteLabel.setText("Suppression de la page");
		deleteLabel.setNoFill(true);
		deleteLabel.setNoStroke(true);
		deleteLabel.setPickable(false);
		
		this.addChild(_sliderConfirm);
		_sliderConfirm.setPositionRelativeToParent(new Vector3D(deleteLabel.getPosition(TransformSpace.RELATIVE_TO_PARENT).x + deleteLabel.getWidthXY(TransformSpace.RELATIVE_TO_PARENT) + 5, deleteLineY, 0));
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		
		// Slide up animation
		final int duration = 200;
		MultiPurposeInterpolator slideUpInterpolator = new MultiPurposeInterpolator(PageMenuProperties.HEIGHT_WHEN_OPENED, 0, duration, 0.0f, 1.0f, 1);
		final Animation slideUpAnimation = new Animation("Slide up anim", slideUpInterpolator, this, 0);
		slideUpAnimation.addAnimationListener(new IAnimationListener() {
			
			public void processAnimationEvent(AnimationEvent ae) {
				PageMenuProperties.this.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
					PageMenuProperties.this.setVisible(false);
				}
			}
		});
		
		// Slide up interaction
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			private Vector3D initialPoint;
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				
				if(de.getId() == MTGestureEvent.GESTURE_STARTED) {
					initialPoint = de.getFrom();
				}
				
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					initialPoint.subtractLocal(de.getTo());

					if(initialPoint.y > 100 && (Math.abs(initialPoint.x) <= 100)) {
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

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		
		if(ev.getPropertyName() == ConfirmationSlider.EVENT_CONFIRMATION) {
			Page pageView = (Page) PageMenuProperties.this.getParent().getParent();
			pageView.getViewNotifier().firePropertyChange(Page.EVENT_DELETE_PAGE, null, null);
		}
	}
	
	public EditableText getEditablePageName() { return _pageName; }
}
