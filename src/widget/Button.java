package widget;

import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Button extends Widget 
{
	private final static int initial_width = 100; 
	private final static int initial_height = 30;
	
	public Button(PApplet applet, float x, float y) 
	{
		super(applet, x, y, initial_width, initial_height);
		MTTextArea texta = new MTTextArea(applet, 0, 0, initial_width, initial_height);
		texta.setText("Button");
		texta.setPickable(false);
		texta.setFillColor(new MTColor(0,0,0,MTColor.ALPHA_FULL_TRANSPARENCY));

		this.addChild(texta);
	}
}
