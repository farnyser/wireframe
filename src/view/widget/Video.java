package view.widget;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;

public class Video extends Widget 
{
	public Video(PApplet applet, model.widget.Widget model) 
	{
		super(applet, model);
	}
	
	public Video(view.widget.Video widget,Boolean create_new_model)
	{
		super(widget,create_new_model);
	}
	
	@Override
	protected void initGraphics()
	{
		final int PADDING = 10, TIMELINE = 5, BUTTON = 25, PLAY = 5;
		
		MTRectangle screen = new MTRectangle(applet, this._model.getWidth() - 2*PADDING, this._model.getHeight() - 3*PADDING - BUTTON);
		MTRectangle button = new MTRectangle(applet, BUTTON, BUTTON);
		MTRectangle timeline = new MTRectangle(applet, this._model.getWidth() - 3*PADDING - BUTTON, TIMELINE);
		Vertex[] v = new Vertex[]{
	       		new Vertex(0,0,0,   0,0,0,255),
	       		new Vertex((float) (3*PLAY), (float) (1.5*PLAY),0, 0,0,0,255),
	       		new Vertex(0,3*PLAY,0, 0,0,0,255),
		};
		MTPolygon play = new MTPolygon(applet, v);

		this.addChild(screen);
		screen.setAnchor(PositionAnchor.CENTER);
		screen.setPositionRelativeToParent(new Vector3D(this.getCenterPointLocal().x, (float) (this.getCenterPointLocal().y - 1.5*PADDING)));
		screen.setStrokeColor(MTColor.BLACK);
		
		this.addChild(button);
		button.setAnchor(PositionAnchor.UPPER_LEFT);
		button.setPositionRelativeToParent(new Vector3D(
				this.getCenterPointLocal().x - this.getWidthXY(TransformSpace.LOCAL)/2 + PADDING, 
				this.getCenterPointLocal().y + this.getHeightXY(TransformSpace.LOCAL)/2 - BUTTON - PADDING)
		);
		button.setStrokeColor(MTColor.BLACK);
		
		button.addChild(play);
		play.setPositionRelativeToParent(button.getCenterPointLocal());
		
		this.addChild(timeline);
		timeline.setAnchor(PositionAnchor.UPPER_LEFT);
		timeline.setPositionRelativeToParent(new Vector3D(2*PADDING+BUTTON, screen.getHeightXY(TransformSpace.LOCAL) + 2*PADDING + BUTTON/2 - TIMELINE/2));
		timeline.setPositionRelativeToParent(new Vector3D(
				this.getCenterPointLocal().x - this.getWidthXY(TransformSpace.LOCAL)/2 + BUTTON + 2 * PADDING, 
				this.getCenterPointLocal().y + this.getHeightXY(TransformSpace.LOCAL)/2 - BUTTON/2 - TIMELINE - PADDING)
		);
		timeline.setStrokeColor(MTColor.BLACK);
		
		
		this.composite(timeline);
		this.composite(play);
		this.composite(screen);
		this.composite(button);

		super.initGraphics();
	}
	
	void composite(MTComponent m)
	{
		m.removeAllGestureEventListeners();
		
		// drag event passed to other widget
		m.addGestureListener(DragProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return Video.this.processGestureEvent(ge);
			}
		});
		// scale event passed to other widget
		m.addGestureListener(ScaleProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return Video.this.processGestureEvent(ge);
			}
		});
		// rotate event passed to other widget
		m.addGestureListener(RotateProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return Video.this.processGestureEvent(ge);
			}
		});
		// tap&hold event passed to other widget
		m.registerInputProcessor(new TapAndHoldProcessor((MTApplication)applet,1000));
		m.addGestureListener(TapAndHoldProcessor.class,new TapAndHoldVisualizer((MTApplication)applet, this));
		m.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
		        return Video.this.processGestureEvent(ge);
			}
		});
	}
}
