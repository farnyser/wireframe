package view.widget;

import model.widget.PersonalWidget;
import processing.core.PApplet;
import processing.core.PImage;

public class PerWidget extends Widget 
{
	public PerWidget(PApplet applet, model.widget.Widget model) 
	{
		super(applet, model);
	}
	
	public PerWidget(view.widget.PerWidget widget,Boolean create_new_model)
	{
		super(widget,create_new_model);
	}
	
	@Override
	protected void initGraphics()
	{
		super.initGraphics();
		PImage p = applet.loadImage(this.getModel().getTexture());
		this.setTexture(p);
	}
	
	public void setImage(String imagePath)
	{
		this.getModel().setTexture(imagePath);
		PImage p = applet.loadImage(this.getModel().getTexture());
		this.setTexture(p);
	}
	
	public model.widget.PersonalWidget getModel()
	{
		return (PersonalWidget) _model;
	}
}
