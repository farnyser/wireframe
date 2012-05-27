package view.widget;

import java.beans.PropertyChangeEvent;
import java.util.Vector;

import model.widget.ListWidget;

import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.EditableText;

public class List extends Widget 
{
	private EditableText texta;
	  
	public List(PApplet applet, model.widget.Widget m) 
	{
		super(applet, m);
	}	
	
	public List(view.widget.List widget, Boolean create_new_model)
	{
		super(widget, create_new_model);
	}
	
	public void propertyChange(PropertyChangeEvent e) 
	{
		super.propertyChange(e);
		
        String propertyName = e.getPropertyName();
		if ( propertyName == "setContent" ) 
        {
			texta.reloadText();
        }
	}

	protected void initGraphics()
	{
		super.initGraphics();
		Vector3D parentPosition = this.getCenterPointGlobal();
		
		texta = new EditableText(applet, 0, 0, this._model.getWidth(), this._model.getHeight()) 
		{
			@Override
			protected boolean handleGesture(MTGestureEvent e) 
			{
		        return List.super.processGestureEvent(e);
			}

			@Override
			protected void textUpdated(String newUnformatedText) 
			{
				// update model
				Vector<String> content = new Vector<String>();
				for ( String e : newUnformatedText.split("\n") ) 
					content.add(e);
				
				model.widget.ListWidget m = (ListWidget) _model;
				m.setContent(content);
			}

			@Override
			protected String getUnformatedText() 
			{
				String str = "";
				Vector<String> content = ((model.widget.ListWidget)List.this._model).getContent();
				for ( String e : content)
					str += e + "\n";
					
				return str;
			}

			@Override
			protected String getFormatedText() 
			{
				String str = "";
				
				for ( String element : ((model.widget.ListWidget)List.this._model).getContent() )
					str += "* " + element + "\n";

				return str;
			}
		};
			
		this.addChild(texta);
		texta.setPositionRelativeToParent(parentPosition);
	}

	protected void updateList(Vector<String> content) 
	{
		this.texta.setText("");
		
		for ( String element : content )
			this.texta.appendText("* " + element + "\n");
	}
}
