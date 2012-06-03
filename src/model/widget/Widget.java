package model.widget;

import java.util.Vector;

import model.Element;

import org.mt4j.util.math.Vector3D;

public class Widget extends Element
{
	private static final long serialVersionUID = -8215736840679294259L;
	
	protected float x, y, z;
	protected Vector<model.Page> links = new Vector<model.Page>();
	
	public Widget()
	{
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Widget(Vector3D _position)
	{
		this.x = _position.x;
		this.y = _position.y;
		this.z = _position.z;
	}
	
	public Widget(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D getPosition()
	{
		return new Vector3D(x,y,z);
	}
	
	public void setPosition(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.pcs.firePropertyChange("setPosition", null, this);
	}
	
	public void setPosition(Vector3D p)
	{
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
		this.pcs.firePropertyChange("setPosition", null, this);
	}
	
	public void addLink(model.Page target) 
	{
		if ( links.contains(target) == false )
			links.add(target);
	}
}
