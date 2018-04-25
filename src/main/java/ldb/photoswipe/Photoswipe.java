package ldb.photoswipe;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;

public class Photoswipe
{
	enum PsDirectory {
		OriginalImages(null, null), 
		Images(1200, 900),
		Thumbnails(240, 180);

		private Integer width;
		private Integer height;

		PsDirectory( Integer width, Integer height){
			this.width = width;
			this.height = height;
		};
	}

	// htmlTemplate
    //  $1 -- path to image file
    //  $2 -- image width
    //  $3 -- image height
    //  $4 -- path to thumbnail

	private final String htmlTemplate = "\t<a href=\"%s\" data-size=\"%dx%d\" class=\"demo-gallery__img--main\">\n" +
            "\t\t<img src=\"%s\" alt=\"\" />\n\t</a>\n";
	private final Path baseDirectory;

	public Photoswipe(String baseDir) {
		baseDirectory = Paths.get(baseDir);

		for( PsDirectory dir : PsDirectory.values()) {
			if( !Files.isDirectory(formDirectory(dir))) {
				System.err.println("Required directory " + formDirectory(dir) + " not found");
				System.exit(1);
			}
		}
	}


	private Path formDirectory( PsDirectory dir) {
		return Paths.get(baseDirectory.toString(), dir.name());
	}

	private Path formImageFilePath( PsDirectory dir, Path fileName) {
		return Paths.get(formDirectory(dir).toString(), fileName.toString());
	}

	public Collection<Path>getOriginalImages() throws IOException{
		Collection<Path>out = new ArrayList<>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(formDirectory(PsDirectory.OriginalImages))) {
			stream.forEach(out::add);
		}

		return out;
	}

	private void scaleImage( Path originalImageFile, PsDirectory dir) throws IOException {
		BufferedImage image = ImageIO.read(originalImageFile.toFile());
		if(image == null) {
			System.err.println( "File " + originalImageFile.getFileName() + " not an image:  skipping");
			return;
		}

		double widthScale = image.getWidth() > dir.width ? (double)dir.width / (double)image.getWidth() : 1.0;
		double heightScale = image.getHeight() > dir.height ? (double)dir.height / (double)image.getHeight() : 1.0;
		double scale = widthScale < heightScale ? widthScale : heightScale;

		ImageResizer.resize(image, formImageFilePath(dir, originalImageFile.getFileName()), scale);

	}

	private void genHtml(Path originalImageFile) throws IOException {
	    Path pathToImageFile = formImageFilePath(PsDirectory.Images, originalImageFile.getFileName());
	    BufferedImage image = ImageIO.read(pathToImageFile.toFile());
	    pathToImageFile = baseDirectory.relativize(pathToImageFile);
	    int width = image.getWidth();
	    int height = image.getHeight();
	    Path pathToThumbNail = baseDirectory.relativize(formImageFilePath(PsDirectory.Thumbnails, originalImageFile.getFileName()));

	    System.out.printf(htmlTemplate, pathToImageFile, width, height, pathToThumbNail);
    }
	public static void main( String[] args) throws IOException {
		if( args.length < 1) {
			System.err.println( "Usage:  photoswipe baseDirectory");
			System.exit(1);
		}

		Photoswipe photoswipe = new Photoswipe(args[0]);
		
		for( Path originalImage : photoswipe.getOriginalImages()) {
			photoswipe.scaleImage(originalImage, PsDirectory.Images);
			photoswipe.scaleImage(originalImage, PsDirectory.Thumbnails);
			photoswipe.genHtml(originalImage);
		}
	}
}
