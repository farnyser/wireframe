package View;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class Page extends MTClipRectangle implements Cloneable
{
	private float h, w;
	
	public Page(PApplet applet, float x, float y, float width, float height) 
	{
		super(applet, x, y, 0, width, height);
		h = height;
		w = width;
		this.setFillColor(MTColor.WHITE);
	}
	
	public void setMinSize(int _h, int _w)
	{
		Vector3D pos = this.getCenterPointGlobal();
		this.setSizeXYGlobal(_h, _w);
		pos.x = pos.x - (h - _h)/2;
		pos.y = pos.y - (w - _w)/2;
		this.setPositionGlobal(pos);
	}
	
	public void setFullSize()
	{
		this.setSizeXYGlobal(h, w);
	}	
	
	public Object clone() 
	{
		Page new_object = null;
	    try 
	    {
	    	new_object = (Page) super.clone();
	    } 
	    catch(CloneNotSupportedException cnse) 
	    {
	      	cnse.printStackTrace(System.err);
	    }
	    	    
	    return new_object;
	}

}
