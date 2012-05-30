package menu;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.css.style.CSSFont;
import org.mt4j.components.css.util.CSSFontManager;
import org.mt4j.components.css.util.CSSKeywords.CSSFontWeight;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.IFont;

import processing.core.PApplet;

public class MessageBox extends MTRectangle{
	
	private MTTextArea message;

	public MessageBox(PApplet pApplet, float x, float y, float width , float height) {
		
		super(pApplet, x, y, width, height);
		this.setFillColor(MTColor.BLACK);
		this.setStrokeColor(MTColor.WHITE);
		this.sendToFront();
		
	    //font format
		CSSFont cf = this.getCssHelper().getVirtualStyleSheet().getCssfont().clone();
		cf.setFontsize(14);
		cf.setWeight(CSSFontWeight.BOLD);

		CSSFontManager cfm = new CSSFontManager((AbstractMTApplication) pApplet);
		IFont font = cfm.selectFont(cf);
		
		message = new MTTextArea(pApplet, 0, 0, width, height/2);
		message.setNoStroke(true);
		message.setNoFill(true);
		message.setFont(font);
		message.setFontColor(MTColor.WHITE);
		message.setPickable(false);
		this.addChild(message);
		message.setPositionRelativeToParent(this.getCenterPointLocal());
		
		this.removeAllGestureEventListeners(TapProcessor.class);
		this.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(pApplet);
		this.registerInputProcessor(tp);
		this.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						MessageBox.this.destroy();
					}
		        }
				
		        return false;
			}
		});
		
	}
	
	public void setMessage(String m)
	{
		MessageBox.this.message.setText(m);
	}
	
}
