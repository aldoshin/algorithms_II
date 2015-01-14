/*************************************************************************
 * Compilation: javac ResizeDemo.java Execution: java ResizeDemo input.png
 * columnsToRemove rowsToRemove Dependencies: SeamCarver.java SCUtility.java
 * Picture.java Stopwatch.java StdDraw.java
 * 
 * 
 * Read image from file specified as command line argument. Use SeamCarver to
 * remove number of rows and columns specified as command line arguments. Show
 * the images in StdDraw and print time elapsed to screen.
 * 
 *************************************************************************/

public class ResizeDemo {
	public static void main(String[] args) {
		System.out
				.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");

		Picture inputImg = new Picture("seamCarving/HJocean.png");
		int removeColumns = 700;
		int removeRows = 100;

		System.out.printf("image is %d columns by %d rows\n", inputImg.width(),
				inputImg.height());
		SeamCarver sc = new SeamCarver(inputImg);

		Stopwatch sw = new Stopwatch();

		for (int i = 0; i < removeRows; i++) {
			int[] horizontalSeam = sc.findHorizontalSeam();
			sc.removeHorizontalSeam(horizontalSeam);
		}

		for (int i = 0; i < removeColumns; i++) {
			int[] verticalSeam = sc.findVerticalSeam();
			sc.removeVerticalSeam(verticalSeam);
		}
		Picture outputImg = sc.picture();

		System.out.printf("new image size is %d columns by %d rows\n",
				sc.width(), sc.height());

		System.out.println("Resizing time: " + sw.elapsedTime() + " seconds.");
		inputImg.show();
		outputImg.show();
	}

}