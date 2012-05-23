package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

public class Element  implements Serializable,Cloneable
{
	private static final long serialVersionUID = 5913307790282474281L;
	
	protected ArrayList<model.Element> childs;
	protected int width = 600, height = 600;
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public Element()
	{
		childs = new ArrayList<model.Element>();
	}
	
	public void addListener(PropertyChangeListener listener)
	{
		pcs.addPropertyChangeListener(listener);
	}
	
	public void removeListener(PropertyChangeListener listener)
	{
		pcs.removePropertyChangeListener(listener);
	}
	
	public void resetListener()
	{
		pcs = new PropertyChangeSupport(this);
	}
	
	public void addElement(model.Element e)
	{
		System.out.println("element " + e + " added to " + this);
		childs.add(e);
		pcs.firePropertyChange("addElement", null, e);
	}
	
	public void removeElement(model.Element e)
	{
		System.out.println("element " + e + " removed from " + this);
		childs.remove(e);
		pcs.firePropertyChange("removeElement", null, e);
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
	
	public Object clone()
	{
		try {
			model.Element obj = (model.Element) super.clone();
			obj.pcs = new PropertyChangeSupport(obj);
			obj.childs = new ArrayList<model.Element>();
			
			for( model.Element em : this.childs )
			{
				obj.childs.add((Element) em.clone());
			}

			return obj;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
