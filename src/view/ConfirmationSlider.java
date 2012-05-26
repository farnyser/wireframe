package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
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

public class ConfirmationSlider extends MTRectangle {
	
	final static public String EVENT_CONFIRMATION = "ConfirmationSlider.event_confirmation"; 
	
	protected MTEllipse _knob;
	protected MTRectangle _trail;
	protected MTRectangle _confirmationZone;
	protected float _confirmationZoneRatio;
	
	protected boolean animationRunning = false;
	
	protected PropertyChangeSupport propertyChangeSupport;

	ConfirmationSlider(PApplet applet, float width, float height, float confirmationZoneRatio) {
		super(applet, 0, 0, width, height);
		
		_confirmationZoneRatio = confirmationZoneRatio;
		_knob = new MTEllipse(applet, new Vector3D(0, 0, 0), height / 2, height / 2);
		_trail = new MTRectangle(applet, 0, 0, 0, height);
		_confirmationZone = new MTRectangle(applet, 0, 0, 0, height);
		
		propertyChangeSupport = new PropertyChangeSupport(this);

		initGraphics();
		initGesture();
	}
	
	public void initGraphics() {
		this.setFillColor(new MTColor(150, 150, 150, 255));
		this.setAnchor(PositionAnchor.UPPER_LEFT);	

		// The confirmation zone (the zone where is fire the vent)
		this.addChild(_confirmationZone);
		_confirmationZone.setAnchor(PositionAnchor.UPPER_LEFT);
		_confirmationZone.setFillColor(MTColor.RED);
		float confirmationZoneWidth = this.getWidthXY(TransformSpace.LOCAL) * _confirmationZoneRatio / 100;
		float confirmationZoneStart = this.getPosition(TransformSpace.LOCAL).x + this.getWidthXY(TransformSpace.LOCAL) - confirmationZoneWidth;
		_confirmationZone.setWidthLocal(confirmationZoneWidth);
		_confirmationZone.setPositionRelativeToParent(new Vector3D(confirmationZoneStart, 0, 0));
		
		// The trail that appears when the knob is sliding to the right
		this.addChild(_trail);
		_trail.setAnchor(PositionAnchor.UPPER_LEFT);
		_trail.setFillColor(MTColor.GREEN);

		// the button
		this.addChild(_knob);
        _knob.setFillColor(new MTColor(140, 140, 140, 255));
        _knob.setPositionRelativeToParent(new Vector3D(0, 10, 0));
        
	}
	
	public void initGesture() {
		
		this.removeAllGestureEventListeners();
		_trail.removeAllGestureEventListeners();
		_confirmationZone.removeAllGestureEventListeners();
		
		_knob.removeAllGestureEventListeners();
			
		_knob.addGestureListener(DragProcessor.class, new IGestureEventListener() {
			//@Override
			public boolean processGestureEvent(MTGestureEvent ge) {

				if(animationRunning == true) return false;
				
				DragEvent de = (DragEvent)ge;
				Vector3D dir = new Vector3D(de.getTranslationVect());
				//Transform the global direction vector into knob local coordiante space
				dir.transformDirectionVector(_knob.getGlobalInverseMatrix());
				
				_knob.translate(new Vector3D(dir.x,0,0), TransformSpace.LOCAL);

				float outerWidthLocal = ConfirmationSlider.this.getWidthXY(TransformSpace.LOCAL);
				float outerXMin = ConfirmationSlider.this.getPosition(TransformSpace.LOCAL).x;
				float outerXMax = outerXMin + outerWidthLocal;
				float confirmZoneStart = _confirmationZone.getPosition(TransformSpace.RELATIVE_TO_PARENT).x;
				
				Vector3D knobCenterRelToParent = _knob.getCenterPointRelativeToParent();
				
				// Set the trail
				_trail.setWidthLocal(knobCenterRelToParent.x);

				//Cap the movement at both ends of the slider
				if(knobCenterRelToParent.x > outerXMax) {
					Vector3D pos = new Vector3D(outerXMax, 10, 0);
					_knob.setPositionRelativeToParent(pos);
				}
				else if(knobCenterRelToParent.x < outerXMin) {
					Vector3D pos = new Vector3D(outerXMin, 10, 0);
					_knob.setPositionRelativeToParent(pos);
				}
				
				if(knobCenterRelToParent.x > confirmZoneStart) {
					_knob.setSizeXYRelativeToParent(25, 25);
				}
				else {
					float knobSize = ConfirmationSlider.this.getHeightXY(TransformSpace.LOCAL);
					_knob.setSizeXYRelativeToParent(knobSize,knobSize);
				}
				
				if(de.getId() == DragEvent.GESTURE_ENDED) {
					
					// fire the event if we are in the confirmation area
					if(knobCenterRelToParent.x > confirmZoneStart) {
						
						if(propertyChangeSupport.hasListeners(ConfirmationSlider.EVENT_CONFIRMATION)) {
							propertyChangeSupport.firePropertyChange(ConfirmationSlider.EVENT_CONFIRMATION, null, null);
						}
					}
					else {
						// Slide left animation
						final int duration = 500;
						MultiPurposeInterpolator slideLeftInterpolator = new MultiPurposeInterpolator(knobCenterRelToParent.x, 0, duration, 0.0f, 1.0f, 1);
						final Animation slideLeftAnimation = new Animation("Slide left anim", slideLeftInterpolator, this, 0);
						slideLeftAnimation.addAnimationListener(new IAnimationListener() {
							
							public void processAnimationEvent(AnimationEvent ae) {
								_trail.setWidthLocal(ae.getValue());
								_knob.setPositionRelativeToParent(new Vector3D(ae.getValue(), 10, 0));
								
								if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
									animationRunning = false;
								}
							}
						});

						animationRunning = true;
						slideLeftAnimation.start();
					}
					
				}

				return false;
			}
		});
	}
	
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}
}
