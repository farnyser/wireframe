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
		
		for ( int i = 0 ; i < 5 ; i++ )
		{
			MTListCell cell = new MTListCell(applet, 50, 50);
			view.widget.Widget widget = null;
			model.widget.Widget model = new model.widget.Widget();
			
			if ( i < 3 )
			{
				model.setSize((i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
				widget = new view.widget.Widget(applet, model);
			}
			else if ( i == 3 )
			{
				model = new model.widget.ImgWidget();
				model.setSize((i%2==0)?100:200, (i%3==0)?200:(i%3==1)?100:50);
				widget = new view.widget.Image(applet, model);
			}
			else
			{
				model = new model.widget.ButtonWidget();
				model.setSize(100, 30);
				widget = new view.widget.Button(applet, model);
			}
			
			widget.setMinSize(50, 50);
			cell.addChild(widget);
			this.addListElement(cell);
			this.addDragProcessor(cell);
		}
	}
}
