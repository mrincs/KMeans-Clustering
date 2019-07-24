import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Centroids {
	Point keyValue;
	ArrayList<Point> intraClusterPoints;
	int label;
	
	public Centroids(int dimension) {
		keyValue = newRandomPoint(10, dimension);
		intraClusterPoints = new ArrayList<Point>();
		label = 0;
		}
	
	public Point newRandomPoint(int max, int dimension) {
		Point p = new Point();
		Random randomGenerator = new Random();
		for (int i = 0; i < dimension; i++) {
			p.attr[i] = randomGenerator.nextInt(max+1);
		}
		return p;
	}
	
}
