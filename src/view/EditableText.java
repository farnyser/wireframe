package view;

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
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public abstract class EditableText extends MTTextArea
{
	protected PApplet applet;
	private MTKeyboard keyboard = null;
	
	public EditableText(PApplet pApplet) 
	{
		super(pApplet);
		applet = pApplet;
		initGraphics();
		initGesture();
	}

	public EditableText(PApplet pApplet, float x, float y, float w, float h) 
	{
		super(pApplet,x,y,w,h);
		applet = pApplet;
		initGraphics();
		initGesture();
	}
	
	public void destroy()
	{
		if ( keyboard != null )
			keyboard.destroy();
		super.destroy();
	}
	
	public void reloadText()
	{
		if ( keyboard == null )
			this.setText( this.getFormatedText() );
	}
	
	protected abstract boolean handleGesture(MTGestureEvent e);
	protected abstract void textUpdated(String newUnformatedText);
	protected abstract String getUnformatedText();
	protected abstract String getFormatedText();

	private void initGesture()
	{
		this.setPickable(true);
		this.removeAllGestureEventListeners();

	    // Add Tap listener to evoke Keyboard
		this.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		tp.setEnableDoubleTap(true);
		this.registerInputProcessor(tp);
		this.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.DOUBLE_TAPPED) 
					{
			            if (keyboard == null) 
			            {
							keyboard = new MTKeyboard(applet);
							EditableText.this.getRoot().addChild(keyboard);
							keyboard.setPositionGlobal(te.getLocationOnScreen().addLocal(new Vector3D(0,150)));
							keyboard.setHardwareInputEnabled(true);
							keyboard.sendToFront();
							keyboard.addTextInputListener(new ITextInputListener()
							{
								@Override
								public void appendCharByUnicode(String arg0) {
									EditableText.this.appendCharByUnicode(arg0);
									EditableText.this.textUpdated(EditableText.this.getText());
								}

								@Override
								public void appendText(String arg0) {
									EditableText.this.appendText(arg0);
									EditableText.this.textUpdated(EditableText.this.getText());
								}

								@Override
								public void clear() {
									EditableText.this.clear();
									EditableText.this.textUpdated(EditableText.this.getText());
								}

								@Override
								public void removeLastCharacter() {
									EditableText.this.removeLastCharacter();
									EditableText.this.textUpdated(EditableText.this.getText());
								}

								@Override
								public void setText(String arg0) {
									EditableText.this.setText(arg0);
									EditableText.this.textUpdated(EditableText.this.getText());
								}
								
							});
							keyboard.addStateChangeListener(
						      StateChange.COMPONENT_DESTROYED,
						      new StateChangeListener() {
								
								@Override
								public void stateChanged(StateChangeEvent arg0) {
									EditableText.this.setText(EditableText.this.getFormatedText());
									EditableText.this.setEnableCaret(false);
									keyboard = null;
								}
							});
							
							EditableText.this.setText(EditableText.this.getUnformatedText());
							EditableText.this.setEnableCaret(true);
			            }
					}
		        }
				
		        return false;
			}
		});
		
		// drag event passed to other widget
		this.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return EditableText.this.handleGesture(ge);
			}
		});
		// scale event passed to other widget
		this.addGestureListener(ScaleProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return EditableText.this.handleGesture(ge);
			}
		});
		// rotate event passed to other widget
		this.addGestureListener(RotateProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return EditableText.this.handleGesture(ge);
			}
		});
		// tap&hold event passed to other widget
		this.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		this.addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer((MTApplication)applet, this));
		this.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent the) 
			{
		        return EditableText.this.handleGesture(the);
			}
		});
	}
	
	private void initGraphics() 
	{
		this.setNoFill(true);
		this.setNoStroke(true);
		this.setText(this.getFormatedText());
	}
}
