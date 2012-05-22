package menu;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
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

public class ExistProjectMenu extends MTList{
	
	private AbstractMTApplication app;
	
	/** The font. */
	private IFont font;
	
	/** The preferred icon height. */
	private int preferredIconHeight;
	
	/** The gap between icon and reflection. */
	private int gapBetweenIconAndReflection;
	
	/** The display height of reflection. */
	private float displayHeightOfReflection;
	
	/** The list width. */
	private float listWidth;
	
	/** The list height. */
	private int listHeight;
	
	/** The preferred icon width. */
	private int preferredIconWidth;
	
	public ExistProjectMenu(PApplet applet, float x, float y, float width, int height, float padY) {
		super(applet, x, y, width, height, padY);
		
		this.app = (AbstractMTApplication) applet;
		
		preferredIconWidth = 256;
		preferredIconHeight = 192;
		gapBetweenIconAndReflection = 9;
		displayHeightOfReflection = preferredIconHeight * 0.6f;
		
		listWidth = width;
		listHeight = height;
		
		this.setFillColor(new MTColor(150,150,150,200));
		this.setNoFill(true);
		this.setNoStroke(true);
		
		font = FontManager.getInstance().createFont(app, "SansSerif", 18, MTColor.WHITE);
		
		this.addScene("Project 1", cropImage(app.loadImage("data/Penguins.jpg"),preferredIconWidth,preferredIconHeight));
		this.addScene("Project 2", cropImage(app.loadImage("data/Koala.jpg"),preferredIconWidth,preferredIconHeight));
		this.addScene("Project 3", cropImage(app.loadImage("data/Hydrangeas.jpg"),preferredIconWidth,preferredIconHeight));
		this.addScene("Project 4", cropImage(app.loadImage("data/Lighthouse.jpg"),preferredIconWidth,preferredIconHeight));
		this.addScene("Project 5", cropImage(app.loadImage("data/Tulips.jpg"),preferredIconWidth,preferredIconHeight));
		
		this.rotateZ(this.getCenterPointLocal(), -90, TransformSpace.LOCAL);
		this.setPositionGlobal(new Vector3D(app.width/2f, app.height/2f));
	
	}
	
	public void addScene(String title, PImage icon){

		PImage reflection = this.getReflection(app, icon);
		
		float border = 1;
		float bothBorders = 2*border;
		float topShift = 30;
		float reflectionDistanceFromImage = topShift + gapBetweenIconAndReflection; //Gap width between image and reflection
		
		float listCellWidth = listWidth;		
		float realListCellWidth = listCellWidth - bothBorders;

		float listCellHeight = preferredIconWidth ;
		
		MTListCell cell = new MTListCell(app , realListCellWidth, listCellHeight);
		cell.setNoFill(true);
		cell.setNoStroke(true);
		
		Vertex[] vertices = new Vertex[]{
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
				new Vertex(realListCellWidth-topShift, 				 listCellHeight -border,	0, 1,0),
				new Vertex(realListCellWidth-topShift - icon.height, listCellHeight -border,	0, 1,1),
				new Vertex(realListCellWidth-topShift - icon.height,	border,		  		0, 0,1),
				new Vertex(realListCellWidth-topShift, 				border,		  		0, 0,0),
		};
		MTPolygon p = new MTPolygon(app, vertices);
		p.setTexture(icon);
		p.setStrokeColor(new MTColor(80,80,80, 255));
		
		Vertex[] verticesRef = new Vertex[]{
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 				 					border,	0, 	0,0),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage,						listCellHeight -border,	0, 	1,0),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage, 	listCellHeight -border,	0, 	1,1),
				new Vertex(listCellWidth - icon.height - reflection.height - reflectionDistanceFromImage,				border,	0, 	0,1),
				new Vertex(listCellWidth - icon.height - reflectionDistanceFromImage, 									border,	0, 	0,0),
		};
		MTPolygon pRef = new MTPolygon(app, verticesRef);
		pRef.setTexture(reflection);
		pRef.setNoStroke(true);
		
		cell.setName(title);
		cell.addChild(p);
		cell.addChild(pRef);
		
		this.addListElement(cell);
		this.addTapProcessor(cell);

		
		///Add scene title
		MTTextArea text = new MTTextArea(app, font);
		text.setFillColor(new MTColor(150,150,250,200));
		text.setNoFill(true);
		text.setNoStroke(true);
		text.setText(title);
		text.rotateZ(text.getCenterPointLocal(), 90, TransformSpace.LOCAL);
		cell.addChild(text);
		
		text.setPositionRelativeToParent(cell.getCenterPointLocal());
		text.translate(new Vector3D(realListCellWidth*0.5f - text.getHeightXY(TransformSpace.LOCAL)*0.5f, 0));
		///
	}

	private PImage cropImage(PImage image, int w, int h) {
		PImage workingCopy;
		try {
			workingCopy= (PImage) image.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Cloning not supported!");
			workingCopy = image;
		}

		//Crops (and resizes) an Image to fit into the suqare
		PImage returnImage = app.createImage(w, h, PConstants.RGB);
		workingCopy.resize(w,h);
		
		// Crop Starting Points
		int x = (workingCopy.width / 2) - (w / 2);
		int y = (workingCopy.height / 2) - (h / 2);
		
		// Bugfixing: Don't Allow Out-of-Bounds coordinates
		if (x + w > workingCopy.width)
			x = workingCopy.width - w;
		if (x < 0)
			x = 0;
		if (x + w > workingCopy.width)
			w = workingCopy.width - x;
		if (y + h > workingCopy.height)
			x = workingCopy.height - h;
		if (y < 0)
			y = 0;
		if (y + h > workingCopy.height)
			h = workingCopy.height - y;
		
		//Crop Image
		returnImage.copy(workingCopy, x, y, w, h, 0, 0, w, h);

		return returnImage;
	}
	
	private PImage getReflection(PApplet pa, PImage image) {
		int width =  image.width; 
		int height = image.height;
		
		PImage copyOfImage = pa.createImage(image.width, image.height, PApplet.ARGB);
		image.loadPixels();
		copyOfImage.loadPixels();
		   
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int imageIndex = y*image.width+x;
				int currR = (image.pixels[imageIndex] >> 16) & 0xFF;
			    int currG = (image.pixels[imageIndex] >> 8) & 0xFF;
			    int currB = image.pixels[imageIndex] & 0xFF;
			    
			    int col = image.pixels[imageIndex];
			    float alpha = pa.alpha(col);
			    
			    int reflectImageIndex = (image.height-y-1) * image.width+x;

			    if (alpha <= 0.0f){
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , 0.0f); 
			    }else{
			    	copyOfImage.pixels[reflectImageIndex] = pa.color(currR , currG , currB , Math.round(y*y*y * (0.00003f) - 60)); //WORKS	
			    }
			}
		} 
		copyOfImage.updatePixels();
		return copyOfImage;
	}
	
	private void addTapProcessor(MTListCell cell){
		cell.registerInputProcessor(new TapProcessor(app, 15));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					
					System.out.println("Selected :" + te.getTarget().getName());
					setData();//Model
					close();
				}
				return false;
			}
		});
	}
	
	private void setData(){
		
	}
	
	private void close(){
		float width = this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT);
		IAnimation closeAnim = new AniAnimation(width, 1, 350, AniAnimation.SINE_IN, this);
		closeAnim.addAnimationListener(new IAnimationListener(){
			public void processAnimationEvent(AnimationEvent ae) {
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
	
	
}
