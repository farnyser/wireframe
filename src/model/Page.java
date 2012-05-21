package model;

import java.io.Serializable;

public class Page extends Element implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The scene label
	 */
	private String _label;
	
	public Page(String sceneLabel) {
		_label = sceneLabel;
	}

	public String getLabel() { return _label; }
}
