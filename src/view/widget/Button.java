package view.widget;

import java.beans.PropertyChangeEvent;

import model.widget.ButtonWidget;

import org.mt4j.MTApplication;
import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.ITextInputListener;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.EditableText;

public class Button extends Widget 
{
	private EditableText texta;
	  
	public Button(PApplet applet, model.widget.Widget m) 
	{
		super(applet, m);
	}	
	
	public Button(view.widget.Button widget, Boolean create_new_model)
	{
		super(widget, create_new_model);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
        String propertyName = e.getPropertyName();
		if ( propertyName == "setContent" ) 
        {
        	this.texta.setText((String) e.getNewValue());       
        }
	}

	protected void initGraphics()
	{
		super.initGraphics();
		Vector3D parentPosition = this.getCenterPointGlobal();
		
		texta = new EditableText(applet, 0, 0, this.model.getWidth(), this.model.getHeight()) 
		{
			@Override
			protected boolean handleGesture(MTGestureEvent e) {
				return Button.super.processGestureEvent(e);
			}

			@Override
			protected void textUpdated(String newUnformatedText) {
				((model.widget.ButtonWidget)Button.this.getModel()).setContent(newUnformatedText);
			}

			@Override
			protected String getUnformatedText() {
				return ((model.widget.ButtonWidget)Button.this.getModel()).getContent();
			}

			@Override
			protected String getFormatedText() {
				return ((model.widget.ButtonWidget)Button.this.getModel()).getContent();
			}
			
		};
	    
		this.addChild(texta);
		texta.setPositionRelativeToParent(parentPosition);
	}
}
