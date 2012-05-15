package widget;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;

import processing.core.PApplet;
import view.Page;

public class Button extends Widget 
{
	private final static int initial_width = 100; 
	private final static int initial_height = 30;
	private MTTextArea texta;
	private MTKeyboard keyboard = null;
	  
	public Button(PApplet applet, float x, float y) 
	{
		super(applet, x, y, initial_width, initial_height);
	}	
	
	public Button(widget.Button widget)
	{
		super(widget);
		texta.setText(widget.texta.getText());
	}

	protected void initGraphics()
	{
		super.initGraphics();
		texta = new MTTextArea(applet, 0, 0, this.w, this.h);
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
				System.out.println("tapped");
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
	}
}
