package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Page extends Element implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The scene label
	 */
	private String _label;
	
	/**
	 * The scene widgets' list
	 */
	private ArrayList<Element> _widgets;
	
	public Page(String sceneLabel) {
		_label = sceneLabel;
		
		_widgets = new ArrayList<Element>();
	}
	
	public void addElement(Element widget) {
		_widgets.add(widget);
	}
	
	public String getLabel() { return _label; }
	
	public ArrayList<Element> getWidgets() { return _widgets; }
}
