package model.widget;

import java.util.Vector;

import model.Element;

import org.mt4j.util.math.Vector3D;

public class Widget extends Element
{
	private static final long serialVersionUID = -8215736840679294259L;
	
	protected Vector3D position;
	protected Vector<model.Page> links = new Vector<model.Page>();
	
	public Widget()
	{
		this.position = new Vector3D(0,0,0);
	}
	
	public Widget(Vector3D _position)
	{
		this.position = _position;
	}
	
	public Widget(float x, float y, float z)
	{
		this.position = new Vector3D(x,y,z);
	}
	
	public Vector3D getPosition()
	{
		return this.position;
	}
	
	public void getPosition(Vector3D _position)
	{
		this.position = _position;
	}

	public void setPosition(float x, float y, float z)
	{
		System.out.println("=== " + this + "setPos old " + this.position);
		this.position = new Vector3D(x,y,z);
		System.out.println("=== " + this + "setPos to " + this.position);
		this.pcs.firePropertyChange("setPosition", null, this);
	}
	
	public void setPosition(Vector3D p)
	{
		System.out.println("=== " + this + "setPos old " + this.position);
		this.position = p;
		System.out.println("=== " + this + "setPos to " + this.position);
		this.pcs.firePropertyChange("setPosition", null, this);
	}
	
	public void addLink(model.Page target) 
	{
		if ( links.contains(target) == false )
			links.add(target);
	}
}
