package ldb.xmas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ChristmasThumbs {
	private static final String albumFormat = "<a href=\"%s\"><img alt=\"\" title=\"click to enlarge\"\n" + 
		"src=\"%s\" style=\"border: 3px solid ;\" vspace=\"5\" hspace=\"5\"></a><br>\n"	;
	private static final String noAlbumFormat = "<a href=\"../FramePic.html?image=%s&amp;caption=\">\n" +
			"<img alt=\"\" title=\"click to enlarge\" src=\"%s\" style=\"border: 3px solid ;\" vspace=\"5\" hspace=\"5\"></a><br>\n";
	
	private static final String imagePath = "singles/%s";
	private static final String albumPath = "albums/%s/index.html";
	private static final String albumThumb = "AlbumThumbs/%s";
	private static final String singleThumb = "SingleThumbs/%s";


	/*
	 * arg[0] -- list of thumbnails for albums (album name matches base of thumbnail file name)
	 * arg[1] -- list of thumbnails for individual pictures (thumbnail and picture have same file name
	 */
	public static void main (String[] args) throws IOException
	{
		if( args.length < 1) {
			System.err.println( "Usage:  ldb.xmas.ChristmasThumbs albumThumbs [singleThumbs]");
			System.exit(1);
		}
		//System.out.println( "Input file = " + args[0]);
		BufferedReader in
		   = new BufferedReader(new FileReader(args[0]));
		
		String line = null;
		
		while( ( line = in.readLine()) != null)
		{
			String thumb = String.format(albumThumb, line);
			String album = String.format( albumPath, line.substring(0, line.lastIndexOf('.')));
			System.out.printf( albumFormat, album, thumb);
		}
		
		if (args.length > 1)
		{
			in = new BufferedReader(new FileReader(args[1]));
			while ((line = in.readLine()) != null)
			{
				String image = String.format(imagePath, line);
				String thumb = String.format(singleThumb, line);
				System.out.printf(noAlbumFormat, image, thumb);
			} 
		}
	}
}
