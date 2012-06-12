package view.widget;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class PerWidget extends Widget {
	
	private MTRectangle widget;
	
	public PerWidget(PApplet applet, model.widget.Widget model, String path) 
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
		
		widget = new MTRectangle(applet, this._model.getWidth(), this._model.getHeight());
		this.addChild(widget);
		widget.setAnchor(PositionAnchor.CENTER);
		widget.setPositionRelativeToParent(new Vector3D(this.getCenterPointLocal().x, (float) (this.getCenterPointLocal().y )));
	
		super.initGraphics();
	}
	
	public void setImage(String imagePath){
		System.out.println("imagePath :" + imagePath);
		PImage p = applet.loadImage(imagePath);
		widget.setTexture(p);
	}
	
}
