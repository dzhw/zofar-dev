package eu.dzhw.zofar.management.utils.images;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.images.svg.ImageTracer;
import sun.awt.image.BufferedImageGraphicsConfig;

public class ImageConverter {

	/** The Constant INSTANCE. */
	private static final ImageConverter INSTANCE = new ImageConverter();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(ImageConverter.class);

	/**
	 * Instantiates a new Converter.
	 */
	private ImageConverter() {
		super();
	}

	/**
	 * Gets the single instance of StringUtils.
	 * 
	 * @return single instance of StringUtils
	 */
	public static synchronized ImageConverter getInstance() {
		return INSTANCE;
	}

	public String toAscii(final BufferedImage image, final char character) {
		final int height = image.getHeight();
		final int width = image.getWidth();
		StringBuffer back = new StringBuffer();
		for (int y = 0; y < height; y++) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
				sb.append(image.getRGB(x, y) == -16777216 ? " " : character);
			}

			if (sb.toString().trim().isEmpty()) {
				continue;
			}

			back.append(sb + "\n");
		}
		return back.toString();

	}

	public List<String> toAsciiList(final BufferedImage image, final char character) {
		final int height = image.getHeight();
		final int width = image.getWidth();
		List<String> back = new ArrayList<String>();
		for (int y = 0; y < height; y++) {
			StringBuilder sb = new StringBuilder();
			for (int x = 0; x < width; x++) {
				sb.append(image.getRGB(x, y) == -16777216 ? " " : character);
			}

			if (sb.toString().trim().isEmpty()) {
				continue;
			}

			back.add(sb.toString());
		}
		return back;
	}

	public BufferedImage scale(final BufferedImage original, final double width, final double height) {
		double scaleXFactor = width / original.getWidth();
		double scaleYFactor = height / original.getHeight();

		AffineTransform tx = AffineTransform.getScaleInstance(scaleXFactor, scaleYFactor);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage back = new BufferedImage(original.getWidth(null), original.getHeight(null), original.getType());
		op.filter(original, back);
		return back;

	}

	public BufferedImage scaleMaxOld(final BufferedImage original, final double maxHeight, final double maxWidth) throws IOException {
		double imageWidth = original.getWidth();
		double imageHeight = original.getHeight();

		double scaleX = (double) maxWidth / imageWidth;
		double scaleY = (double) maxHeight / imageHeight;

		double scale = Math.min(scaleX, scaleY);

		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BICUBIC);

		int imageWidthInt = (int) Math.ceil(original.getWidth() * scale);
		int imageHeightInt = (int) Math.ceil(original.getHeight() * scale);
		
		BufferedImage resizedImage = scaleOp.filter(original, new BufferedImage(imageWidthInt, imageHeightInt, original.getType()));

		return resizedImage;
	}
	
	public File scaleMaxWeb(final File originalFile, final double maxHeight, final double maxWidth) throws IOException {
//		BufferedImage original = ImageIO.read(originalFile);
//		double imageWidth = original.getWidth();
//		double imageHeight = original.getHeight();

//		double scaleX = (double) maxWidth / imageWidth;
//		double scaleY = (double) maxHeight / imageHeight;

//		double scale = Math.min(scaleX, scaleY);
//		int imageWidthInt = (int) Math.ceil(original.getWidth() * scale);
//		int imageHeightInt = (int) Math.ceil(original.getHeight() * scale);
		
		Tinify.setKey("oZ_RfUr_irxTotTyjDEnHoWLgQpcjwtj");
		
		Source source = Tinify.fromFile(originalFile.getAbsolutePath());
		Options options = new Options()
		    .with("method", "fit")
		    .with("width", maxWidth)
		    .with("height", maxHeight);
		Source resized = source.resize(options);
		File back = FileClient.getInstance().createTempFile(originalFile.getName(), System.currentTimeMillis()+"");
		resized.toFile(back.getAbsolutePath());
		
		
//		BufferedImage resizedImage = scaleOp.filter(original, new BufferedImage(imageWidthInt, imageHeightInt, original.getType()));

		return back;
	}
	
//	public BufferedImage scaleMax1(final BufferedImage original, final double maxHeight, final double maxWidth) {
//		double imageWidth = original.getWidth();
//		double imageHeight = original.getHeight();
//
//		double scaleX = (double) maxWidth / imageWidth;
//		double scaleY = (double) maxHeight / imageHeight;
//
//		double scale = Math.min(scaleX, scaleY);
//		
//		int imageWidthInt = (int) Math.ceil(original.getWidth() * scale);
//		int imageHeightInt = (int) Math.ceil(original.getHeight() * scale);
//
//		AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
//		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
//
//		BufferedImage back = new BufferedImage(imageWidthInt, imageHeightInt, original.getType());
//		op.filter(original, back);
//		return back;
//
//	}

	public BufferedImage scaleMax(final BufferedImage original, final double maxHeight, final double maxWidth) throws IOException {
		double imageWidth = original.getWidth();
		double imageHeight = original.getHeight();

		double scaleX = (double) maxWidth / imageWidth;
		double scaleY = (double) maxHeight / imageHeight;

		double scale = Math.min(scaleX, scaleY);

		int imageWidthInt = (int) Math.ceil(original.getWidth() * scale);
		int imageHeightInt = (int) Math.ceil(original.getHeight() * scale);

		BufferedImage resizedImage = new BufferedImage(imageWidthInt, imageHeightInt, original.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(original.getScaledInstance(imageWidthInt, imageHeightInt, Image.SCALE_AREA_AVERAGING), 0, 0, imageWidthInt, imageHeightInt, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		return resizedImage;
		
//		return getScaledInstance(original,imageWidthInt, imageHeightInt,RenderingHints.VALUE_INTERPOLATION_BICUBIC,true);
	}
	
	public BufferedImage scaleTrick(final BufferedImage original, final double maxHeight, final double maxWidth) throws IOException {

		return this.resizeTrick(original, (int)maxWidth, (int)maxHeight);
		
	}

	private BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 1.25;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}

			if (higherQuality && h > targetHeight) {
				h /= 1.25;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();

			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}
	
	public Document raster2svg(final BufferedImage original){
		
		// Options
		HashMap<String,Float> options = new HashMap<String,Float>();

		// Tracing
		options.put("ltres",1f);
		options.put("qtres",1f);
		options.put("pathomit",8f);

		// Color quantization
		options.put("colorsampling",1f); // 1f means true ; 0f means false: starting with generated palette
		options.put("numberofcolors",16f);
		options.put("mincolorratio",0.02f);
		options.put("colorquantcycles",3f);

		// SVG rendering
		options.put("scale",1f);
		options.put("simplifytolerance",0f);
		options.put("roundcoords",1f); // 1f means rounded to 1 decimal places, like 7.3 ; 3f means rounded to 3 places, like 7.356 ; etc.
		options.put("lcpr",0f);
		options.put("qcpr",0f);
		options.put("desc",1f); // 1f means true ; 0f means false: SVG descriptions deactivated
		options.put("viewbox",0f); // 1f means true ; 0f means false: fixed width and height

		// Selective Gauss Blur
		options.put("blurradius",0f); // 0f means deactivated; 1f .. 5f : blur with this radius
		options.put("blurdelta",20f); // smaller than this RGB difference will be blurred

		// Palette
		// This is an example of a grayscale palette
		// please note that signed byte values [ -128 .. 127 ] will be converted to [ 0 .. 255 ] in the getsvgstring function
		byte[][] palette = new byte[8][4];
		for(int colorcnt=0; colorcnt < 8; colorcnt++){
		    palette[colorcnt][0] = (byte)( -128 + colorcnt * 32); // R
		    palette[colorcnt][1] = (byte)( -128 + colorcnt * 32); // G
		    palette[colorcnt][2] = (byte)( -128 + colorcnt * 32); // B
		    palette[colorcnt][3] = (byte)255;             // A
		}
		
		try {
			final String out = ImageTracer.getInstance().imageToSVG(original,options,palette);
			try {
			    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			    Document doc = db.parse(new InputSource(new StringReader(out)));
			    return doc;
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e2) {
				e2.printStackTrace();
			} catch (ParserConfigurationException e3) {
				e3.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		return null;
	}
	
	public Document raster2svgSimple(final BufferedImage original){
		int width = original.getWidth();
		int height = original.getHeight();
		final String preamble = "<?xml version=\"1.0\" standalone=\"no\"?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n  \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n<svg xmlns=\"http://www.w3.org/2000/svg\"\n     version=\"1.1\" width=\""+width+"px\" height=\""+height+"px\">\n";
		final  String end = "</svg>";
		
		StringBuffer midSVG = new StringBuffer();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) { 
				Color color = new Color(original.getRGB(x, y), true);
//				System.out.println(x+","+y+" = "+color);
				
				if ((color.getAlpha() == 0) == false) {
					midSVG.append("    <rect x=\""+x+"px\" y=\""+y+"px\" width=\"1px\" height=\"1px\" fill=\""+ "#"+Integer.toHexString(color.getRGB()).substring(2) +"\"/>\n");
				}
			}
		}
		
		final String out = preamble + midSVG.toString() + end;
		
		System.out.println("out : "+out);
		
		try {
		    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    Document doc = db.parse(new InputSource(new StringReader(out)));
		    return doc;
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e2) {
			e2.printStackTrace();
		} catch (ParserConfigurationException e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public BufferedImage rotate(final BufferedImage original, final double degree) {
		double x = original.getWidth() / 2;
		double y = original.getHeight() / 2;
		double f = 6.284 / 4;

		AffineTransform tx = AffineTransform.getRotateInstance(f, x, y);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage back = new BufferedImage(original.getWidth(null), original.getHeight(null), original.getType());
		op.filter(original, back);
		return back;
	}

	public BufferedImage crop(BufferedImage original, double width, double height) {
		double x = original.getWidth() / 2 - width / 2;
		double y = original.getHeight() / 2 - height / 2;

		BufferedImage clipping = new BufferedImage((int) width, (int) height, original.getType());
		Graphics2D area = (Graphics2D) clipping.getGraphics().create();
		area.drawImage(original, 0, 0, clipping.getWidth(), clipping.getHeight(), (int) x, (int) y, (int) (x + clipping.getWidth()), (int) (y + clipping.getHeight()), null);
		area.dispose();

		return clipping;
	}

	public BufferedImage fromImage(final Image img) {
		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		return bi;
	}
	
	//Found on web at https://componenthouse.com/2008/02/08/high-quality-image-resize-with-java/

	private static BufferedImage resize(BufferedImage image, int width, int height) {
		int type = image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}

	private static BufferedImage resizeTrick(BufferedImage original, int maxWidth, int maxHeight) {
//		double imageWidth = original.getWidth();
//		double imageHeight = original.getHeight();
//
//		double scaleX = (double) maxWidth / imageWidth;
//		double scaleY = (double) maxHeight / imageHeight;
//
//		double scale = Math.min(scaleX, scaleY);
//
//		int imageWidthInt = (int) Math.ceil(original.getWidth() * scale);
//		int imageHeightInt = (int) Math.ceil(original.getHeight() * scale);
//		
//		
//		
		original = createCompatibleImage(original);
		original = resize(original, 100, 100);
		original = blurImage(original);
		return resize(original, maxWidth, maxHeight);
	}

	public static BufferedImage blurImage(BufferedImage image) {
		float ninth = 1.0f/9.0f;
		float[] blurKernel = {
				ninth, ninth, ninth,
				ninth, ninth, ninth,
				ninth, ninth, ninth
		};

		Map<RenderingHints.Key, Object> map = new HashMap<RenderingHints.Key, Object>();
		map.put(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		map.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		map.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		RenderingHints hints = new RenderingHints(map);
		BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
		return op.filter(image, null);
	}

	private static BufferedImage createCompatibleImage(BufferedImage image) {
		GraphicsConfiguration gc = BufferedImageGraphicsConfig.getConfig(image);
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		Graphics2D g2 = result.createGraphics();
		g2.drawRenderedImage(image, null);
		g2.dispose();
		return result;
	}
}
