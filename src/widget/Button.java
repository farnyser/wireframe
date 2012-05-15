package widget;

import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Button extends Widget 
{
	private final static int initial_width = 100; 
	private final static int initial_height = 30;
	private MTTextArea texta;
	
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
		texta.setPickable(false);
		texta.setFillColor(new MTColor(0,0,0,MTColor.ALPHA_FULL_TRANSPARENCY));
		this.addChild(texta);
	}
}
