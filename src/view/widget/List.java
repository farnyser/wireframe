package view.widget;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import model.widget.ListWidget;

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
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class List extends Widget 
{
	private MTTextArea texta;
	private MTKeyboard keyboard = null;
	  
	public List(PApplet applet, model.widget.Widget m) 
	{
		super(applet, m);
	}	
	
	public List(view.widget.List widget, Boolean create_new_model)
	{
		super(widget, create_new_model);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
        String propertyName = e.getPropertyName();
		if ( propertyName == "setContent" ) 
        {
			this.updateList((Vector<String> ) e.getNewValue());
        }
	}

	protected void initGraphics()
	{
		super.initGraphics();
		Vector3D parentPosition = this.getCenterPointGlobal();
		
		texta = new MTTextArea(applet, 0, 0, this.model.getWidth(), this.model.getHeight());
		texta.setPickable(true);
		texta.setNoFill(true);
		texta.setNoStroke(true);
		model.widget.ListWidget m = (ListWidget) model;
		this.updateList(m.getContent());
		
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
			            if (keyboard == null) 
			            {
							keyboard = new MTKeyboard(applet);
							List.this.addChild(keyboard);
							keyboard.addTextInputListener(new ITextInputListener()
							{
								@Override
								public void appendCharByUnicode(String arg0) {
									texta.appendCharByUnicode(arg0);
									model.widget.ListWidget m = (ListWidget) model;
//									m.setContent(texta.getText());
								}

								@Override
								public void appendText(String arg0) {
									texta.appendText(arg0);
									model.widget.ListWidget m = (ListWidget) model;
//									m.setContent(texta.getText());
								}

								@Override
								public void clear() {
									texta.clear();
									model.widget.ListWidget m = (ListWidget) model;
//									m.setContent(texta.getText());
								}

								@Override
								public void removeLastCharacter() {
									texta.removeLastCharacter();
									model.widget.ListWidget m = (ListWidget) model;
//									m.setContent(texta.getText());
								}

								@Override
								public void setText(String arg0) {
									texta.setText(arg0);
									model.widget.ListWidget m = (ListWidget) model;
//									m.setContent(texta.getText());
								}
								
							});
							keyboard.addStateChangeListener(
						      StateChange.COMPONENT_DESTROYED,
						      new StateChangeListener() {
								
								@Override
								public void stateChanged(StateChangeEvent arg0) {
									texta.setEnableCaret(false);
									List.this.updateList(((model.widget.ListWidget)List.this.model).getContent());
									keyboard = null;
								}
							});
							
							texta.setEnableCaret(true);
							texta.setText(((model.widget.ListWidget)List.this.model).getContent().toString());
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
		        return List.super.processGestureEvent(ge);
			}
		});
		texta.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		texta.addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer((MTApplication)applet, this));
		texta.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent the) 
			{
		        return List.super.processGestureEvent(the);
			}
		});
	    
		this.addChild(texta);
		texta.setPositionRelativeToParent(parentPosition);
	}

	protected void updateList(Vector<String> content) 
	{
		this.texta.setText("");
		
		for ( String element : content )
			this.texta.appendText("* " + element + "\n");
	}
}
