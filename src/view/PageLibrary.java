package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;

import processing.core.PApplet;

public class PageLibrary extends Library implements PropertyChangeListener
{
	protected model.Project _project;
	
	public PageLibrary(PApplet applet, float x, float y, float width, float height, model.Project project)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, Element>();
		create_new_model = false;
		
		_project = project;
		
		this.initViewFromModel();
	} 
	
	public void initViewFromModel() {

		for ( model.Page p : _project.getPageList() )
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
	
	public void propertyChange(PropertyChangeEvent ev) {
		
		if(ev.getPropertyName() == view.page.Page.EVENT_DELETE_PAGE) {
			
			// on supprime la page
			view.page.Page pageToDelete = (view.page.Page) ev.getSource();
			_project.removePage((model.Page) pageToDelete.getModel());
			pageToDelete.destroy();

			// on met a jour la PageLibrary
			this.removeAllListElements();
			this.initViewFromModel();
		}
	}
}
