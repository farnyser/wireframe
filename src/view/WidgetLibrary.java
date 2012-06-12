package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;

import processing.core.PApplet;

public class WidgetLibrary extends Library implements PropertyChangeListener
{
	private static int SPACING = 20;
	private model.ApplicationModel _application;
	
	public WidgetLibrary(PApplet applet, float x, float y, float width, float height, model.ApplicationModel app)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, view.Element>();
		this._application = app;
		
		this._application.addListener(this);
		this._application.getCurrentProject().addListener(this);
		this.init();
	}
	
	protected void init()
	{
		this.removeAllListElements();
		
		view.widget.Widget widget = null;
		model.widget.Widget model = null;
			
		{
			model = new model.widget.Widget();
			model.setSize(300,100);
			widget = new view.widget.Widget(this.getRenderer(), model);
			this.addWidget(this.getRenderer(), widget);
		}
		{
			model = new model.widget.ImgWidget();
			model.setSize(200,200);
			widget = new view.widget.Image(this.getRenderer(), model);
			this.addWidget(this.getRenderer(), widget);
		}		
		{
			model = new model.widget.VideoWidget();
			model.setSize(200,200);
			widget = new view.widget.Video(this.getRenderer(), model);
			this.addWidget(this.getRenderer(), widget);
		}
		{
			model = new model.widget.ListWidget();
			model.setSize(200,200);
			widget = new view.widget.List(this.getRenderer(), model);
			this.addWidget(this.getRenderer(), widget);
		}
		{
			model = new model.widget.ButtonWidget();
			model.setSize(100, 30);
			widget = new view.widget.Button(this.getRenderer(), model);
			this.addWidget(this.getRenderer(), widget);
		}
		
		if ( this._application != null && this._application.getCurrentProject() != null && this._application.getCurrentProject().getWidgetList() != null )
		{
			for ( model.widget.PersonalWidget pw : this._application.getCurrentProject().getWidgetList() )
			{
				view.Element w = Element.newInstance(this.getRenderer(), pw);
				this.addWidget(this.getRenderer(), (view.widget.Widget)w);
			}
		}
	}
	
	protected void addWidget(PApplet applet, view.widget.Widget widget)
	{
		MTListCell cell = new MTListCell(applet, this.getWidthXYGlobal(), this.getWidthXYGlobal() - SPACING);		
		cell.setNoFill(true);
		cell.setNoStroke(true);
		widget.setMinSize(this.getWidthXYGlobal() - SPACING, this.getWidthXYGlobal() - SPACING);
		cell.addChild(widget);
		widget.setPositionRelativeToParent(cell.getCenterPointLocal());
		this.addListElement(cell);
		this.addDragProcessor(cell);
	}

	public void addWidget(model.widget.PersonalWidget widget)
	{
		view.Element w = Element.newInstance(this.getRenderer(), widget);
		this.addWidget(this.getRenderer(), (view.widget.Widget)w);
		this._application.getCurrentProject().addWidget(widget);
	}

	@Override
	public void propertyChange(PropertyChangeEvent ev) 
	{
		if(ev.getPropertyName() == "newProject") 
		{
			this.init();
		}		
	}
}
