package view;

import model.ApplicationModel;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import processing.core.PApplet;

public class DuoMenu extends MTRectangle
{
	final static int HANDLE = 25;
	
	public DuoMenu(PApplet applet, int width, int height, ApplicationModel app) 
	{
		super(applet, 0, 0, width, height);
		
		PageLibrary pl = new PageLibrary(applet, 0, 0, width, (height-HANDLE)/2, app);
		WidgetLibrary wl = new WidgetLibrary(applet, 0, (height+HANDLE)/2, width, (height-HANDLE)/2, app);
		
		pl.setVisible(true);
		wl.setVisible(true);
		this.addChild(pl);
		this.addChild(wl);
	}
}
