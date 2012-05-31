package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class DeleteEffect extends MTClipRectangle
{
	public DeleteEffect(PApplet applet, MTClipRectangle parent, float w, float h) 
	{
		super(applet, 0, 0, 0, w, h);
		parent.addChild(this);
		
		MTColor fc = new MTColor(255,0,0,50);
		MTColor sc = new MTColor(255,0,0);
		this.setFillColor(fc);
		this.setStrokeColor(sc);

		MTClipRectangle im = new MTClipRectangle(applet, this.getWidthXYGlobal()-25, this.getHeightXYGlobal()-25, 0, 25, 25);
		im.setTexture(applet.loadImage("data/Trash.png"));
		im.setNoFill(true);
		im.setNoStroke(true);
		this.addChild(im);
		
		this.setPositionRelativeToParent(parent.getCenterPointLocal());
		this.setVisible(false);
	}
}
