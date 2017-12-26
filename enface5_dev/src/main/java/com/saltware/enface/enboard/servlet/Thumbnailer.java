package com.saltware.enface.enboard.servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Thumbnailer {
    private Image inImage;
	private String thumb;
	private int maxX;
    private int maxY;
    private int margin;

    // 생성자
    public Thumbnailer(String orig, String thumb, int xx, int yy) {
        initLocal(orig, thumb, xx, yy, 0);
    }

    // 원격이미지 생성자
    public Thumbnailer(URL remotePath, String thumb, int xx, int yy) {
        initRemote(remotePath, thumb, xx, yy, 0);
    }

    // 생성자
    public Thumbnailer(String orig, String thumb, int xx, int yy, int margin) {
        initLocal(orig, thumb, xx, yy, margin);
    }

    // 원격이미지 생성자
    public Thumbnailer(URL remotePath, String thumb, int xx, int yy, int margin) {
        initRemote(remotePath, thumb, xx, yy, margin);
    }

    public void initLocal(String orig, String thumb, int xx, int yy, int margin) {
		//long stime = System.currentTimeMillis();
        this.inImage = new ImageIcon(orig).getImage();
		//System.out.println("loaded:"+(System.currentTimeMillis()-stime));

		this.thumb = thumb;
        this.maxX = xx;
        this.maxY = yy;
        this.margin = margin;
    }

    public void initRemote(URL remotePath, String thumb, int xx, int yy, int margin) {
        this.inImage = new ImageIcon(remotePath).getImage();
        this.thumb = thumb;
        this.maxX = xx;
        this.maxY = yy;
        this.margin = margin;
    }

    /********************************************************************************************************
     * 작은 이미지를 만든다. maxX/maxY 대비 이미지의 가로/세로의 축소 비율이 틀리면 축소 비율이 더 큰 축의 비율로 축소하고,
     * 가로/세로 비율이 원본 이미지의 가로/세로 비율이 유지되도록 축소한다.
     * 또한, 이미지의 사이즈가 maxX/maxY 보다 작으면 확대하지 않는다.
     ********************************************************************************************************/
	public void createThumbnail() throws Exception {
		
			//long stime = System.currentTimeMillis();

			// Determine the scale.
            double scaleX = (double) (maxX - margin * 2) / inImage.getWidth(null);
            double scaleY = (double) (maxY - margin * 2) / inImage.getHeight(null);
            double scale = scaleX;
			if (scaleX > scaleY) {
				scale = scaleY;
			}

			// Determine size of new image.
			// One of them should equal maxDim.
			int scaledW = (int) (scale * inImage.getWidth(null));
			int scaledH = (int) (scale * inImage.getHeight(null));

            // Create an image buffer in which to paint on.
			BufferedImage outImage = null;
			if (scale < 1.0d)
				outImage = new BufferedImage (scaledW, scaledH, BufferedImage.TYPE_INT_RGB);
			else
				outImage = new BufferedImage (inImage.getWidth(null), inImage.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// Set the scale.
			AffineTransform tx = new AffineTransform();

			// If the image is smaller than the desired image size, don't bother scaling.
			if (scale < 1.0d) {
				tx.scale(scale, scale);
			} else {
				tx.scale(1, 1);
			}
            AffineTransform toCenterAt = new AffineTransform();
            int startx = 0;
            int starty = 0;
            toCenterAt.translate (startx, starty);
            toCenterAt.concatenate (tx);

            // Paint image.
			Graphics2D g2d = outImage.createGraphics();
            RenderingHints qualityHints = new RenderingHints (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put (RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints (qualityHints);
            //g2d.fillRect (0, 0, scaledW, scaledH);
            //g2d.fillRect (0, 0, maxX, maxY);
            g2d.fillRect (0, 0, inImage.getWidth(null), inImage.getHeight(null));
			g2d.drawImage (inImage, toCenterAt, null);

			g2d.dispose();
			/*
			// JPEG-encode the image and write to file.
			OutputStream os = new FileOutputStream (thumb);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
	        
//			JPEGEncodeParam  param = encoder.getDefaultJPEGEncodeParam (outImage);
//			float quality = 10/100.0f;
//			param.setQuality(quality, false);

 			encoder.encode(outImage);
			os.close();
			//System.out.println("finished:"+(System.currentTimeMillis()-stime));
			 */
			File file = new File( thumb);
			ImageIO.write(outImage, "jpeg", file);
	}
	
    /********************************************************************************************************
     * 작은 이미지를 만든다. maxX/maxY 대비 이미지의 가로/세로의 축소 비율이 틀리면 축소 비율이 더 큰 축의 비율로 축소하고,
     * 가로/세로 비율이 원본 이미지의 가로/세로 비율이 유지되도록 축소하며, 작은 이미지의 크기는 maxX/maxY로 하고 축소비율의 차이로 인해
     * 남는 영역은 이미지 내에 마진 영역으로 포함시킨다. 또한, 이미지의 사이즈가 maxX/maxY 보다 작으면 확대한다.
     ********************************************************************************************************/
	public void createThumbnail_notUse() throws Exception {
		
			long stime = System.currentTimeMillis();

			// Determine the scale.
            double scaleX = (double) (maxX - margin * 2) / inImage.getWidth(null);
            double scaleY = (double) (maxY - margin * 2) / inImage.getHeight(null);
            double scale = scaleX;
			if (scaleX > scaleY) {
				scale = scaleY;
			}

			// Determine size of new image.
			// One of them should equal maxDim.
			int scaledW = (int) (scale * inImage.getWidth(null));
			int scaledH = (int) (scale * inImage.getHeight(null));

            // Create an image buffer in which to paint on.
			BufferedImage outImage =
				new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_RGB);

			// Set the scale.
			AffineTransform tx = new AffineTransform();

			// If the image is smaller than the desired image size,
			// don't bother scaling.
			// if (scale < 1.0d) {
				tx.scale(scale, scale);
			// }
            AffineTransform toCenterAt = new AffineTransform();
            int startx = (maxX - scaledW) / 2 ;
            int starty = (maxY - scaledH) / 2 ;
            toCenterAt.translate(startx, starty);
            toCenterAt.concatenate(tx);

            // Paint image.
			Graphics2D g2d = outImage.createGraphics();
            RenderingHints qualityHints;
            qualityHints = new
            RenderingHints(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints(qualityHints);
            g2d.fillRect(0,0,maxX,maxY);
			g2d.drawImage(inImage, toCenterAt, null);

			g2d.dispose();
			/*	
			// JPEG-encode the image and write to file.
			OutputStream os = new FileOutputStream(thumb);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
	        
//			JPEGEncodeParam  param = encoder.getDefaultJPEGEncodeParam (outImage);
//			float quality = 10/100.0f;
//			param.setQuality(quality, false);

 			encoder.encode(outImage);
			os.close();
			*/
			File file = new File( thumb);
			ImageIO.write(outImage, "jpeg", file);
	}
}
