package view;

import java.util.HashMap;

import org.mt4j.components.visibleComponents.widgets.MTListCell;

import processing.core.PApplet;

public class WidgetLibrary extends Library 
{
	public WidgetLibrary(PApplet applet, float x, float y, float width, float height)
	{
		super(applet, x, y, width, height);
		clones = new HashMap<MTListCell, view.Element>();
		
		view.widget.Widget widget = null;
		model.widget.Widget model = null;
			
		{
			model = new model.widget.Widget();
			model.setSize(300,100);
			widget = new view.widget.Widget(applet, model);
			this.addWidget(applet, widget);
		}
		{
			model = new model.widget.ImgWidget();
			model.setSize(200,200);
			widget = new view.widget.Image(applet, model);
			this.addWidget(applet, widget);
		}
		{
			model = new model.widget.ListWidget();
			model.setSize(200,200);
			widget = new view.widget.List(applet, model);
			this.addWidget(applet, widget);
		}
		{
			model = new model.widget.ButtonWidget();
			model.setSize(100, 30);
			widget = new view.widget.Button(applet, model);
			this.addWidget(applet, widget);
		}
	}
	
	public void addWidget(PApplet applet, view.widget.Widget widget)
	{
		MTListCell cell = new MTListCell(applet, 50, 50);		
		widget.setMinSize(50, 50);
		cell.addChild(widget);
		this.addListElement(cell);
		this.addDragProcessor(cell);
	}
}
