package view.widget;

import org.mt4j.components.visibleComponents.shapes.MTLine;

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
		super.initGraphics();
		MTLine l1 = new MTLine(applet, 0, 0, this.model.getWidth(), this.model.getHeight());
		MTLine l2 = new MTLine(applet, this.model.getWidth(), 0, 0, this.model.getHeight());
		l1.setPickable(false);
		l2.setPickable(false);
		
		this.addChild(l1);
		this.addChild(l2);
		
		l1.setPositionRelativeToParent(this.getCenterPointGlobal());
		l2.setPositionRelativeToParent(this.getCenterPointGlobal());
	}
}
