package ldb.photoswipe;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;
 
/**
 * This program demonstrates how to resize an image.
 *
 * @author www.codejava.net
 *
 */
public class ImageResizer {
 
    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImagePath Path of the original image
     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    public static void resize(BufferedImage inputImage,
            Path outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
//        File inputFile = new File(inputImagePath);
//        BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        // extracts extension of output file
        String formatName = outputImagePath.toString().substring(
        		outputImagePath.toString().lastIndexOf(".") + 1);
 
        // writes to output file
//        System.out.printf("Re-sizing %s to %dx%d\n", outputImagePath, scaledWidth, scaledHeight );
        ImageIO.write(outputImage, formatName, outputImagePath.toFile());
    }
 
    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputImage BufferedImage of the original image
     * @param outputImagePath Path to save the resized image
     * @param scale a double number specifies scale factor of the output image
     * over the input image.
     * @throws IOException
     */
    public static void resize(BufferedImage inputImage,
            Path outputImagePath, double scale) throws IOException {
//        File inputFile = new File(inputImagePath);
//        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * scale);
        int scaledHeight = (int) (inputImage.getHeight() * scale);
        resize(inputImage, outputImagePath, scaledWidth, scaledHeight);
    }

}
