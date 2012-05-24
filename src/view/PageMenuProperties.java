package view;

import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class PageMenuProperties extends MTClipRectangle {
	
	protected MTTextArea ta;

	PageMenuProperties(PApplet applet) {
		super(applet, 0, 0, 0, 400, 200);

		ta = new MTTextArea(applet);
		
		initGraphics();
		initGesture();
	}
	
	protected void initGraphics() {
		this.setFillColor(MTColor.GRAY);
		
		ta.setText("Supprimer la page");
		
		this.setVisible(false);
		
	}
	
	protected void initGesture() {	
	}

}
