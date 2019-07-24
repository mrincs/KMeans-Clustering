import java.util.Random;

public class Point {
	double[] attr;
	int dimension = 10;
	
	public Point() {
		attr = new double[dimension];
		for (int i = 0; i < dimension; i++) {
			attr[i] = 0;
		}
	}
	
	public void setAttribute(int position, double value) {
		this.attr[position] = value;
	}
	
	
	public void setAllAttributes(double[] arr, int len) {
		Point p = new Point();
		for (int i =0; i < len; i++) {
			this.attr[i] = arr[i];
		}
	}
	

}
