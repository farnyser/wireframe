package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;

import org.mt4j.util.math.Vector3D;

public class Element  implements Serializable,Cloneable
{
	private static final long serialVersionUID = 5913307790282474281L;
	public enum Corner { UPPER_LEFT, UPPER_RIGHT, LOWER_LEFT, LOWER_RIGHT };
	
	protected ArrayList<model.Element> childs;
	protected float width = 600, height = 600;
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
		childs.add(e);
		pcs.firePropertyChange("addElement", null, e);
	}
	
	public void removeElement(model.Element e)
	{
		childs.remove(e);
		pcs.firePropertyChange("removeElement", null, e);
	}

	public float getWidth() 
	{
		return width;
	}

	public float getHeight() 
	{
		return height;
	}

	public void setSize(float f, float g) 
	{
		this.width = f;
		this.height = g;
		pcs.firePropertyChange("setSize", null, new Vector3D(f, g));
	}
	
	public void setSize(float f, float g, Corner resizeStart) 
	{
		this.width = f;
		this.height = g;
		pcs.firePropertyChange("setSize", resizeStart, new Vector3D(f, g));
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
