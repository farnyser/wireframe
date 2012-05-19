package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Element implements Serializable,Cloneable
{
	private static final long serialVersionUID = 5913307790282474281L;
	
	protected ArrayList<model.Element> childs;

	protected int width = 600, height = 600;

	public Element()
	{
		childs = new ArrayList<model.Element>();
	}
	
	public void addElement(model.Element e)
	{
		System.out.println("element added to " + this);
		childs.add(e);
	}
	
	public void removeElement(model.Element e)
	{
		System.out.println("element removed from " + this);
		childs.remove(e);
	}

	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}

	public void setSize(int width, int height) 
	{
		this.width = width;
		this.height = height;
	}

	public ArrayList<model.Element> getElements() 
	{
		return childs;
	}
}
