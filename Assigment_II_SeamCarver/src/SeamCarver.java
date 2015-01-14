import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeamCarver {
	private static final double BORDER_ENERGY = 3 * (255 * 255);
	private Picture picture;

	private double[][] weights;
	private double[] distTo;
	private int[] edgeTo;

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		this.picture = new Picture(picture);
		calculateWeights();
	}

	private void calculateWeights() {
		this.weights = new double[this.width()][this.height()];
		for (int col = 0; col < width(); col++) {
			for (int row = 0; row < height(); row++) {
				double pixelEnergy = energy(col, row);
				weights[col][row] = pixelEnergy;
			}
		}
	}

	private double[][] transposeEnergyMatrix(double[][] eMatrix) {
		double[][] result = new double[eMatrix[0].length][eMatrix.length];
		for (int i = 0; i < eMatrix.length; i++) {
			for (int j = 0; j < eMatrix[i].length; j++)
				result[j][i] = eMatrix[i][j];
		}
		return result;
	}

	// current picture
	public Picture picture() {
		return this.picture;
	}

	// width of current picture
	public int width() {
		return this.picture.width();
	}

	// height of current picture
	public int height() {
		return this.picture.height();
	}

	/**
	 * The energy of pixel (x, y) is Ax2(x, y) + Ay2(x, y), where the square of
	 * the x-gradient Ax2(x, y) = Rx(x, y)2 + Gx(x, y)2 + Bx(x, y)2 energy of
	 * pixel at column x and row y.
	 * 
	 * We define the energy of pixels at the border of the image to be 2552 +
	 * 2552 + 2552 = 195075.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double energy(int x, int y) {
		if (!isValidPictureIndex(x, y)) {
			throw new IndexOutOfBoundsException();
		}
		if (isBorderPixel(x, y)) {
			return BORDER_ENERGY;
		} else
			return gradientX(x, y) + gradientY(x, y);
	}

	private boolean isValidPictureIndex(int x, int y) {
		return x >= 0 && y >= 0 && x < width() && y < height();
	}

	/**
	 * Checks if the pixel is at the border of the image
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isBorderPixel(int x, int y) {
		return (x == 0 || x == (width() - 1))
				|| (y == 0 || y == (height() - 1));
	}

	private double gradientX(int x, int y) {
		Color pixelPrev = this.picture.get(x + 1, y);
		Color pixelFord = this.picture.get(x - 1, y);
		return calculateDualGradientEnergy(pixelPrev, pixelFord);
	}

	private double gradientY(int x, int y) {
		Color pixelUp = this.picture.get(x, y + 1);
		Color pixelDown = this.picture.get(x, y - 1);
		return calculateDualGradientEnergy(pixelUp, pixelDown);
	}

	/**
	 * where the central differences Rx(x, y), Gx(x, y), and Bx(x, y) are the
	 * absolute value in differences of red, green, and blue components between
	 * pixel (x + 1, y) and pixel (x - 1, y)
	 * 
	 * @param pixelUp
	 * @param pixelDown
	 * @return
	 */
	private double calculateDualGradientEnergy(Color pixelUp, Color pixelDown) {
		return square(pixelDown.getRed() - pixelUp.getRed())
				+ square(pixelDown.getGreen() - pixelUp.getGreen())
				+ square(pixelDown.getBlue() - pixelUp.getBlue());
	}

	private double square(double number) {
		return number * number;
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		return findShortestSeam(transposeEnergyMatrix(weights));
	}

	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		return findShortestSeam(this.weights);
	}

	/**
	 * Initializes arrays needed and call relax and find the shortest seam
	 * 
	 * @param weightsMatrix
	 * @return
	 */
	private int[] findShortestSeam(double[][] weightsMatrix) {
		int width = weightsMatrix.length;
		int height = weightsMatrix[0].length;
		this.distTo = new double[width * height];
		this.edgeTo = new int[width * height];
		for (int col = 0; col < width; col++) {
			for (int row = 0; row < height; row++) {
				distTo[node(col, row, width)] = (row == 0) ? 0
						: Double.POSITIVE_INFINITY;
			}
		}
		int size = width * height;
		for (int i = 0; i < size; i++) {
			relax(i, adjPixel(i, height, width), weightsMatrix, width);
		}
		return minSeam(height, width);
	}

	/**
	 * Finds the pixel with minimum distance in the last row, then build the
	 * ancestral path to that node
	 * 
	 * @return
	 */
	private int[] minSeam(int height, int width) {
		double minBottom = Double.POSITIVE_INFINITY;
		int bottomPixel = node(0, height - 1, width);
		for (int pixel = 0; pixel < width - 1; pixel++) {
			int node = node(pixel, height - 1, width);
			if (distTo[node] < minBottom) {
				minBottom = distTo[node];
				bottomPixel = node;
			}
		}

		int[] path = new int[height];
		int row = height;
		for (int pixel = bottomPixel; row > 0; pixel = edgeTo[pixel]) {
			path[row - 1] = col(pixel, width);
			row--;
		}
		return path;
	}

	/**
	 * Return an array with the 3 adjacent pixels of i (1d to 2d representation)
	 * 
	 * @param i
	 * @return
	 */
	private Integer[] adjPixel(int i, int height, int width) {
		List<Integer> adj = new ArrayList<>();
		int column = col(i, width);
		int row = row(i, width);
		if (row < height - 1) {
			if (column > 0) {
				adj.add(node(column - 1, row + 1, width));
			}
			adj.add(node(column, row + 1, width));
			if (column < width - 1) {
				adj.add(node(column + 1, row + 1, width));
			}
		}
		return adj.toArray(new Integer[] {});
	}

	private void relax(int pixelIndex, Integer[] adjPixel,
			double[][] weightsMatrix, int width) {
		for (int pixel : adjPixel) {
			int column = col(pixel, width);
			int row = row(pixel, width);
			if (distTo[pixel] > distTo[pixelIndex] + weightsMatrix[column][row]) {
				distTo[pixel] = distTo[pixelIndex] + weightsMatrix[column][row];
				edgeTo[pixel] = pixelIndex;
			}
		}
	}

	private int node(int col, int row, int width) {
		return row * width + col;
	}

	private int col(int node, int width) {
		return node % width;
	}

	private int row(int node, int width) {
		return node / width;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam.length != this.width()) {
			throw new IndexOutOfBoundsException();
		}
		Picture cut = new Picture(width(), height() - 1);
		for (int column = 0; column < width(); column++) {
			for (int row = 0; row < height(); row++) {
				if (row < seam[column]) {
					cut.set(column, row, this.picture.get(column, row));
				} else if (row > seam[column]) {
					cut.set(column, row - 1, this.picture.get(column, row));
				}
			}
		}
		this.picture = cut;
		calculateWeights();
	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam.length != this.height()) {
			throw new IndexOutOfBoundsException();
		}
		Picture cut = new Picture(width() - 1, height());
		for (int row = 0; row < height(); row++) {
			for (int column = 0; column < width(); column++) {
				if (column < seam[row]) {
					cut.set(column, row, this.picture.get(column, row));
				} else if (column > seam[row]) {
					cut.set(column - 1, row, this.picture.get(column, row));
				}
			}
		}
		this.picture = cut;
		calculateWeights();
	}
}