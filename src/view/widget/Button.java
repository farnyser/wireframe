package view.widget;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class Button extends Widget 
{
	private MTTextArea texta;
	private MTKeyboard keyboard = null;
	  
	public Button(PApplet applet, model.widget.Widget m) 
	{
		super(applet, m);
	}	
	
	public Button(view.widget.Button widget, Boolean create_new_model)
	{
		super(widget, create_new_model);
		texta.setText(widget.texta.getText());
	}

	protected void initGraphics()
	{
		super.initGraphics();
		Vector3D parentPosition = this.getCenterPointGlobal();
		
		texta = new MTTextArea(applet, 0, 0, this.model.getWidth(), this.model.getHeight());
		texta.setText("Button");
		texta.setPickable(true);
		texta.setFillColor(new MTColor(0,0,0,MTColor.ALPHA_FULL_TRANSPARENCY));
		
	    // Add Tap listener to evoke Keyboard
		texta.removeAllGestureEventListeners(DragProcessor.class);
		texta.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		tp.setEnableDoubleTap(true);
		texta.registerInputProcessor(tp);
		texta.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.DOUBLE_TAPPED) 
					{
						System.out.println("DOUBLE_TAPPED");
						
			            if (keyboard == null) 
			            {
							keyboard = new MTKeyboard(applet);
							Button.this.addChild(keyboard);
							keyboard.addTextInputListener(texta);
							keyboard.addStateChangeListener(
						      StateChange.COMPONENT_DESTROYED,
						      new StateChangeListener() {
								
								@Override
								public void stateChanged(StateChangeEvent arg0) {
									texta.setEnableCaret(false);
									keyboard = null;
								}
							});
							
							texta.setEnableCaret(true);
			            }
					}
		        }
				
		        return false;
			}
		});
		texta.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return Button.super.processGestureEvent(ge);
			}
		});
	    
		this.addChild(texta);
//		texta.setPositionRelativeToParent(new Vector3D(this.model.getWidth()/2,this.model.getHeight()/2,0));
		texta.setPositionRelativeToParent(parentPosition);
	}
}
