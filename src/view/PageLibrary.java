package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import model.Page;

import org.mt4j.components.visibleComponents.widgets.MTListCell;

import processing.core.PApplet;

public class PageLibrary extends Library implements PropertyChangeListener
{
	protected model.ApplicationModel _application;
	private static int SPACING = 20;
	
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
			MTListCell cell = new MTListCell(this.getRenderer(), this.getWidthXYGlobal(), this.getWidthXYGlobal() - SPACING);
			cell.setNoFill(true);
			cell.setNoStroke(true);
			view.page.Page page = new view.page.Page(this.getRenderer(), p);
			page.addListener(this);
			page.setMinSize(this.getWidthXYGlobal() - SPACING, this.getWidthXYGlobal() - SPACING);
			cell.addChild(page);
			page.setPositionRelativeToParent(cell.getCenterPointLocal());
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
		else if(ev.getPropertyName() == "duplicatedPage") 
		{
			System.out.println("duplicate");
			this._application.getCurrentProject().addPage((Page) ev.getNewValue());
		}
		else if(ev.getPropertyName() == "newProject") 
		{
			((model.Project)ev.getNewValue()).addListener(this);
			this.removeAllListElements();
			this.initViewFromModel();
		}
	}
}
