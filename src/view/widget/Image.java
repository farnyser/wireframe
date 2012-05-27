package view.widget;

import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class Image extends Widget 
{
	public Image(PApplet applet, model.widget.Widget m) 
	{
		super(applet, m);
	}
	
	public Image(view.widget.Image widget,Boolean create_new_model)
	{
		super(widget,create_new_model);
	}
	
	protected void initGraphics()
	{		
		MTLine l1 = new MTLine(applet, 0, 0, this._model.getWidth(), this._model.getHeight());
		MTLine l2 = new MTLine(applet, this._model.getWidth(), 0, 0, this._model.getHeight());
		l1.setStrokeColor(MTColor.BLACK);
		l2.setStrokeColor(MTColor.BLACK);
		l1.setPickable(false);
		l2.setPickable(false);
		
		this.addChild(l1);
		this.addChild(l2);
		
		l1.setPositionRelativeToParent(this.getCenterPointGlobal());
		l2.setPositionRelativeToParent(this.getCenterPointGlobal());
		
		super.initGraphics();
	}
}
