package model.widget;


public class WidgetFactory {

	public static final int WIDGET_TEXT = 1;
	public static final int WIDGET_IMG = 2;
	public static final int WIDGET_BUTTON = 3;
	
	/**
	 * Returns a new instance of the requests widget 
	 * @param widgetType
	 * @return a widget instance of the specified type
	 */
	public static Widget getWidget(int widgetType) {
		
		switch(widgetType) {
			case WidgetFactory.WIDGET_TEXT: return new TextWidget();
			case WidgetFactory.WIDGET_IMG: return new ImgWidget();
			case WidgetFactory.WIDGET_BUTTON: return new ButtonWidget();
			default: throw new RuntimeException("Widget type "+widgetType+" does not exist.");
		}		
	}
}
