package menu;

import model.ApplicationModel;

import org.mt4j.components.StateChange;
import org.mt4j.components.StateChangeEvent;
import org.mt4j.components.StateChangeListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimation;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.ani.AniAnimation;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class NewProjectMenu extends MTRectangle {
	
	protected PApplet applet;
	protected float posx, posy, h, w, size;
	private MTTextArea texta;
	private MTTextArea label;
	private MTKeyboard keyboard = null;
	private MTRectangle ok, cancel;
	private ApplicationModel model;
	

	public NewProjectMenu(PApplet a, ApplicationModel m, float x, float y, float width, float height) 
	{
		super(a, x, y, 0, width, height);
		applet = a;
		posx = x;
		posy = y;
		w = width; 
		h = height;
		size = width;
		model = m;
		
		initMenu();
	}
	
	private void initMenu()
	{
		this.setCssForceDisable(true);
		this.setFillColor(new MTColor(154,192,205));
		this.setNoStroke(true);

		this.setMovablePosition();
		this.setLabel();
		this.setNameTextArea();
		this.setCancelIcon();
		this.setOKIcon();
		
	}	
	
	private void setMovablePosition()
	{
		float minx = 16000, maxx = -16000, miny = 16000, maxy = -16000;
		
		// Position in the grid

		this.setPositionRelativeToParent(new Vector3D(posx, posy));
		//Determine Min/Max-Positions
		//We have to use the childrens relative-to parent vertices for the calculation of this component's local vertices
		Vertex[] unTransformedCopy = Vertex.getDeepVertexArrayCopy(this.getGeometryInfo().getVertices());
		//transform the copied vertices and save them in the vertices array
		Vertex[] verticesRelParent = Vertex.transFormArray(this.getLocalMatrix(), unTransformedCopy);
			for (Vertex v: verticesRelParent) {
					if (v.x < minx) minx = v.x;
					if (v.x > maxx) maxx = v.x;
					if (v.y < miny) miny = v.y;
					if (v.y > maxy) maxy = v.y;
			}
			
		MTColor fill = this.getFillColor();
				//Set Vertices to include all children
		this.setVertices(new Vertex[] {
				new Vertex(minx,miny, 0, fill.getR(), fill.getG(), fill.getB(), fill.getAlpha()), 
				new Vertex(maxx,miny, 0, fill.getR(), fill.getG(), fill.getB(), fill.getAlpha()), 
				new Vertex(maxx,maxy, 0, fill.getR(), fill.getG(), fill.getB(), fill.getAlpha()), 
				new Vertex(minx,maxy, 0, fill.getR(), fill.getG(), fill.getB(), fill.getAlpha()),
				new Vertex(minx,miny, 0, fill.getR(), fill.getG(), fill.getB(), fill.getAlpha())});
	}
	
	private void setLabel()
	{
		label = new MTTextArea(applet, 0, 0, this.w, this.h/4);
		label.setText("Enter the new project name :");
		label.setNoStroke(true);
		label.setPickable(false);
		label.setFillColor(new MTColor(154,192,205));
		this.addChild(label);
		label.setPositionRelativeToParent(new Vector3D(posx,posy-h/2));
	}
	
	private void setNameTextArea()
	{
		texta = new MTTextArea(applet, 0, 0, this.w, this.h/2);
		texta.setNoStroke(true);
		texta.setPickable(true);
		this.addChild(texta);
		texta.setPositionRelativeToParent(new Vector3D(posx, posy));
		
	    // Add Tap listener to evoke Keyboard
		texta.removeAllGestureEventListeners(DragProcessor.class);
		texta.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		texta.registerInputProcessor(tp);
		texta.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				System.out.println("tapped");
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						System.out.println("TAPPED");
						
			            if (keyboard == null) 
			            {
							keyboard = new MTKeyboard(applet);
							NewProjectMenu.this.addChild(keyboard);
							keyboard.setPositionRelativeToParent(new Vector3D(posx, (float) (1.5*posy)));
							keyboard.addTextInputListener(texta);
							keyboard.addStateChangeListener(
						      StateChange.COMPONENT_DESTROYED,
						      new StateChangeListener() {
								
								@Override
								public void stateChanged(StateChangeEvent arg0) {
									texta.setEnableCaret(false);
									
								}
							});
							
							texta.setEnableCaret(true);
			            }
					}
		        }
				
		        return false;
			}
		});
	}
	
	private void setCancelIcon()
	{
		int s = (int)w/8;
		PImage cancelImage = applet.loadImage("data/Cancel.jpg");;
		cancelImage = cropImage(cancelImage, s ,true);
		
		cancel = new MTRectangle(applet, 0, 0, s, s);
		cancel.setTexture(cancelImage);
		cancel.setNoStroke(true);
		cancel.setChildClip(new Clip(cancel));
		cancel.setPickable(true);
		this.addChild(cancel);
		cancel.setPositionRelativeToParent(new Vector3D(posx+w/2+s/2,posy-h/2));
		
		cancel.removeAllGestureEventListeners(TapProcessor.class);
		cancel.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		cancel.registerInputProcessor(tp);
		cancel.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						System.out.println("Cancel");
						close();
					}
		        }
				
		        return false;
			}
		});
	}
	
	private void setOKIcon()
	{
		int s = (int)w/8;
		PImage OKImage = applet.loadImage("data/OK.jpg");;
		OKImage = cropImage(OKImage, s ,true);
		
		ok = new MTRectangle(applet, 0, 0, s, s);
		ok.setTexture(OKImage);
		ok.setNoStroke(true);
		ok.setChildClip(new Clip(ok));
		ok.setPickable(true);
		this.addChild(ok);
		ok.setPositionRelativeToParent(new Vector3D(posx+w/2+s/2,posy+h/2));
		
		ok.removeAllGestureEventListeners(TapProcessor.class);
		ok.setGestureAllowance(TapProcessor.class, true);
		TapProcessor tp = new TapProcessor(this.applet);
		ok.registerInputProcessor(tp);
		ok.addGestureListener(TapProcessor.class, new IGestureEventListener()
		{
			public boolean processGestureEvent(MTGestureEvent ge) 
			{
				if (ge instanceof TapEvent) 
				{
					TapEvent te = (TapEvent) ge;
					if (te.getTapID() == TapEvent.TAPPED) 
					{
						System.out.println("OK");
						setData();//Model
						close();
					}
		        }
				
		        return false;
			}
		});
	}
	
	private PImage cropImage(PImage image, int size, boolean resize) {
		PImage workingCopy;
		try {
			workingCopy= (PImage) image.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not supported!");
			workingCopy = image;
		}
		
		
		
		//Crops (and resizes) an Image to fit into the suqare
		PImage returnImage = applet.createImage(size, size, PConstants.RGB);
		
		//Resize Image
		if (resize || workingCopy.width < size || workingCopy.height < size) {
			if (workingCopy.width < workingCopy.height) {
				workingCopy.resize(
						size,
						(int) ((float) workingCopy.height / ((float) workingCopy.width / (float) size)));
			} else {
				workingCopy.resize(
						(int) ((float) workingCopy.width / ((float) workingCopy.height / (float) size)),
						size);
			}

		}
		
		// Crop Starting Points
		int x = (workingCopy.width / 2) - (size / 2);
		int y = (workingCopy.height / 2) - (size / 2);
		
		// Bugfixing: Don't Allow Out-of-Bounds coordinates
		if (x + size > workingCopy.width)
			x = workingCopy.width - size;
		if (x < 0)
			x = 0;
		if (x + size > workingCopy.width)
			size = workingCopy.width - x;
		if (y + size > workingCopy.height)
			x = workingCopy.height - size;
		if (y < 0)
			y = 0;
		if (y + size > workingCopy.height)
			size = workingCopy.height - y;
		
		//Crop Image
		returnImage.copy(workingCopy, x, y, size, size, 0, 0, size, size);

		return returnImage;
	}

	private void close(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
//		IAnimation closeAnim = new Animation("Window Fade", new MultiPurposeInterpolator(width, 1, 350, 0.2f, 0.5f, 1), this);
		IAnimation closeAnim = new AniAnimation(width, 1, 350, AniAnimation.SINE_IN, this);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
//				float delta = ae.getAnimation().getInterpolator().getCurrentStepDelta();
				switch (ae.getId()) {
				case AnimationEvent.ANIMATION_STARTED:
				case AnimationEvent.ANIMATION_UPDATED:
					float currentVal = ae.getAnimation().getValue();
					setWidthXYRelativeToParent(currentVal);
					rotateZ(getCenterPointRelativeToParent(), -ae.getDelta()*0.3f);
					break;
				case AnimationEvent.ANIMATION_ENDED:
					setVisible(false);
					destroy();
					break;	
				default:
					break;
				}//switch
			}//processanimation
		});
		closeAnim.start();
	}
	
	private void setData(){
		
		 model.createProject(texta.getText());
	}
}

