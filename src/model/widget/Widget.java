package model.widget;

import org.mt4j.util.math.Vector3D;

import model.Element;

public class Widget extends Element
{
	private static final long serialVersionUID = -8215736840679294259L;
	
	protected Vector3D position;
	
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
		this.position = new Vector3D(x,y,z);
		this.pcs.firePropertyChange("setPosition", null, this);
	}
}
