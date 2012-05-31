package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.util.MTColor;

import processing.core.PApplet;

public class PageLibrary extends Library implements PropertyChangeListener
{
	protected model.ApplicationModel _application;
	
	public PageLibrary(PApplet applet, float x, float y, float width, float height, model.ApplicationModel app)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, Element>();
		create_new_model = false;
		_application = app;
		
		_application.addListener(this);
		_application.getCurrentProject().addListener(this);
		
		this.initViewFromModel();
	} 
	
	public void initViewFromModel() 
	{
		for ( model.Page p : _application.getCurrentProject().getPageList() )
		{
			MTListCell cell = new MTListCell(this.getRenderer(), 50, 50);
			view.page.Page page = new view.page.Page(this.getRenderer(), 0, 0, p);
			page.addListener(this);
			page.setMinSize(50, 50);
			cell.addChild(page);
			this.addListElement(cell);
			this.addDragProcessor(cell);
		}
	}
	
	public void propertyChange(PropertyChangeEvent ev) 
	{	
		if(ev.getPropertyName() == view.page.Page.EVENT_DELETE_PAGE) 
		{	
			// on supprime la page
			view.page.Page pageToDelete = (view.page.Page) ev.getSource();
			_application.getCurrentProject().removePage((model.Page) pageToDelete.getModel());

			// on met a jour la PageLibrary
			this.removeAllListElements();
			this.initViewFromModel();
		}
		else if(ev.getPropertyName() == "addPage") 
		{
			this.removeAllListElements();
			this.initViewFromModel();
		}
		else if(ev.getPropertyName() == "newProject") 
		{
			((model.Project)ev.getNewValue()).addListener(this);
			this.removeAllListElements();
			this.initViewFromModel();
		}
	}
}
