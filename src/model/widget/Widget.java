package model.widget;

import org.mt4j.util.math.Vector3D;

import model.Element;

public class Widget extends Element
{
	private static final long serialVersionUID = -8215736840679294259L;
	
	protected Vector3D position;
	//protected float width, height, angle;
	
	public Widget()
	{
		this.position = new Vector3D(0,0,0);
	}
	
	public Widget(Vector3D _position)
	{
		this.position = _position;
	}
	
	public Vector3D getPosition()
	{
		return this.position;
	}
	
	public void getPosition(Vector3D _position)
	{
		this.position = _position;
	}

}
