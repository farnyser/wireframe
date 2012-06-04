package view.page;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTClipRectangle;
import org.mt4j.components.visibleComponents.widgets.MTClipRoundRect;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import view.EditableText;

public class PageMenu extends MTClipRectangle 
{
	final public static int HEIGHT = 25;
	final public static int ARROW_HEIGHT = 10;
	final public static int CLOSE_HEIGHT = 20;
	final public static int PROPERTIES_HEIGHT = PageMenuProperties.HEIGHT_WHEN_OPENED;
	final public static int TOTAL_HEIGHT = HEIGHT+ARROW_HEIGHT+CLOSE_HEIGHT+PROPERTIES_HEIGHT;
	
	protected EditableText textArea;
	protected PageMenuProperties properties;
	protected MTClipRectangle topbar;
	protected MTClipRectangle arrowDown;
	protected MTClipRoundRect close;
	protected PApplet applet;
	
	private boolean animationRunning = false; // for the slide down animation	

	public PageMenu(PApplet applet) 
	{
		super(applet, 0, 0, 0, 400, PageMenu.TOTAL_HEIGHT);

		this.applet = applet;
		textArea = new EditableText(applet) 
		{
			@Override
			protected boolean handleGesture(MTGestureEvent e) 
			{
				return PageMenu.this.topbar.processGestureEvent(e);
			}

			@Override
			protected void textUpdated(String newUnformatedText) 
			{
				((model.Page)((view.page.Page) PageMenu.this.getParent()).getModel()).setLabel(newUnformatedText);
			}

			@Override
			protected String getUnformatedText() 
			{
				if(PageMenu.this.getParent() != null) {
					return ((model.Page)((view.page.Page) PageMenu.this.getParent()).getModel()).getLabel();
				}
				else return "";
			}

			@Override
			protected String getFormatedText() 
			{
				return getUnformatedText();
			}
			
		};
		properties = new PageMenuProperties(applet);
		arrowDown = new MTClipRectangle(applet, 0, 0, 0, ARROW_HEIGHT, ARROW_HEIGHT);
		topbar = new MTClipRectangle(applet, 0, 0, 0, 400, PageMenu.HEIGHT);

		initGraphics();
		initGesture();
	}
	 
	protected void initGraphics() 
	{
		this.setNoFill(true);
		this.setNoStroke(true);
		
		topbar.setNoStroke(true);
		topbar.setFillColor(MTColor.GRAY);
		this.addChild(topbar);
		
		textArea.setNoStroke(true);
		textArea.setHeightXYGlobal(PageMenu.HEIGHT);
		this.addChild(textArea);
		
		this.addChild(properties);
		properties.setAnchor(PositionAnchor.UPPER_LEFT);
		properties.setPositionRelativeToParent(new Vector3D(0, PageMenu.HEIGHT, 0));
		
		this.addChild(arrowDown);
		arrowDown.setFillColor(MTColor.GRAY);
		arrowDown.setNoStroke(true);
		arrowDown.setPositionRelativeToParent(new Vector3D(this.getWidth() / 2, PageMenu.HEIGHT, 0));
		arrowDown.rotateZ(arrowDown.getCenterPointLocal(), 45, TransformSpace.LOCAL);
		
		textArea.sendToFront();
		
		close = new MTClipRoundRect(applet, 0, 0, 0, CLOSE_HEIGHT, CLOSE_HEIGHT, CLOSE_HEIGHT/2, CLOSE_HEIGHT/2);
		close.setFillColor(MTColor.RED);
		close.setPositionRelativeToParent(new Vector3D(this.getWidth(), 0, 0));
		this.addChild(close);
	}
	
	protected void initGesture() {

		this.removeAllGestureEventListeners();
		topbar.removeAllGestureEventListeners();
		
		// Slide down animation
		final int duration = 200;
		MultiPurposeInterpolator slideDownInterpolator = new MultiPurposeInterpolator(0, PageMenuProperties.HEIGHT_WHEN_OPENED, duration, 0.0f, 1.0f, 1);
		final Animation slideDownAnimation = new Animation("Slide down anim", slideDownInterpolator, this.properties, 0);
		slideDownAnimation.addAnimationListener(new IAnimationListener()
		{
			public void processAnimationEvent(AnimationEvent ae) {
				PageMenu.this.properties.setHeightLocal(ae.getValue());
				
				if(ae.getId() == AnimationEvent.ANIMATION_STARTED) {
					PageMenu.this.properties.setVisible(true);
				}
				else if(ae.getId() == AnimationEvent.ANIMATION_ENDED) {
					animationRunning = false;
				}
			}
		});
		
		// Slide Down interaction
		topbar.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				DragEvent de = (DragEvent) ge;
				
				if(de.getId() == MTGestureEvent.GESTURE_ENDED) {
					
					Vector3D move = de.getTo();
					move.subtractLocal(PageMenu.this.topbar.getCenterPointGlobal());
					
					if(move.y > 150 && (Math.abs(move.x) <= 100)) {
					
						if (!animationRunning){
							animationRunning = true;
							properties.getEditablePageName().reloadText();
							PageMenu.this.sendToFront();
							slideDownAnimation.start();
						}
					}
				}
				
		        return false;
			}
		});
		
		arrowDown.removeAllGestureEventListeners();
		
		close.removeAllGestureEventListeners(); // supprime les comportements par defaut (drag, zoom, ...)
		close.registerInputProcessor(new TapProcessor(this.applet));
		close.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						if ( view.page.PageMenu.this.getParent() != null )
						{
							view.page.PageMenu.this.getParent().destroy();
						}
					}
		        }
				
		        return false;
			}
		});
	}
	
	public void changePageName(String newLabel) 
	{
		this.textArea.setText(newLabel);
		this.properties.getEditablePageName().reloadText();
		
		this.textArea.setPositionRelativeToParent(new Vector3D(this.getCenterPointLocal().x, HEIGHT/2));		
	}
	
	public void setColor(MTColor c) 
	{
		topbar.setFillColor(c);
		arrowDown.setFillColor(c);
		properties.setFillColor(c);
	}
	
	public float getHeight() { return this.getHeightXYGlobal(); }
	public float getWidth() { return this.getWidthXYGlobal(); }
}
