package model.widget;

import model.Element;
import quicktime.qd3d.math.Vector3D;

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
	
	public Vector3D getPosition()
	{
		return this.position;
	}
	
	public void getPosition(Vector3D _position)
	{
		this.position = _position;
	}
}