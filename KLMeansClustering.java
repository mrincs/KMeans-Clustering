import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;




public class KLMeansClustering {

	public static double euclideanDistance(Point p1, Point p2, int dimension) {
		double distance = 0;
		
		for (int i = 0; i < dimension; i++) {
			distance = distance + (p1.attr[i] - p2.attr[i]) * (p1.attr[i] - p2.attr[i]);
		}
		distance = Math.sqrt(distance); 
		return distance;
	}
	
	public double[] generateRandomNumbers(int dimension, int maxNumber) {
		double[] point = new double[dimension];
		Random randomGenerator = new Random();
		for (int i = 0; i < dimension; i++) {
			point[i] = randomGenerator.nextInt(maxNumber);
		}
		return point;
	}
	
	public static Point calculateMean(Centroids centroid, int dimension) {
		double[] attrSum = new double[dimension];
		Point newCentroid = new Point();
		for (Point p : centroid.intraClusterPoints) {
			for (int i = 0; i < dimension; i++) {
				attrSum[i] += p.attr[i];
			}
		}
		for (int i = 0; i < dimension; i++) {
			newCentroid.attr[i] = attrSum[i]/centroid.intraClusterPoints.size();
		}
		return newCentroid;
	}
	
	public static int indexOfMaxElement(double[] arr, int length) {
		int index = 0;
		double max = Integer.MIN_VALUE;
		for (int i = 0; i < length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				index = i;
			}
		}
		return index;
	}
	
	public static int selectBestCentroidforPoints(Point point, Centroids[] centroids, int dimension) {
		int numOfCentroids = centroids.length;
		double distance, minDistance = Integer.MAX_VALUE;
		int selectedCentroid = 0;
		
		for ( int i = 0; i < numOfCentroids; i++) {
			distance = euclideanDistance(point, centroids[i].keyValue, dimension);
			if (distance < minDistance) {
				minDistance = distance;
				selectedCentroid = i;
			}
		}
		return selectedCentroid;
	}
	
	public static int[] selectBestLCentroidforPoints(Point point, Centroids[] centroids, int dimension, int numAssociations) {
		int numOfCentroids = centroids.length;
		double[] distance = new double[numOfCentroids];
		double[] minDistances = new double[numAssociations];
		Arrays.fill(minDistances, Integer.MAX_VALUE);
		int[] selectedCentroids = new int[numAssociations];
		Arrays.fill(selectedCentroids, Integer.MAX_VALUE);
		

		
		for ( int i = 0; i < numOfCentroids; i++) {
			distance[i] = euclideanDistance(point, centroids[i].keyValue, dimension);
		}

		for ( int i = 0; i < numOfCentroids; i++) {
			for (int j = 0; j < numAssociations; j++) {
				int replaceIndex  = indexOfMaxElement(minDistances, numAssociations);
				if (distance[i] < minDistances[replaceIndex]) {
					minDistances[replaceIndex] = distance[i];
					selectedCentroids[replaceIndex] = i;
					break;
				}
			}
		}

		return selectedCentroids;
	}
	
	@SuppressWarnings("resource")
	public static Point[] readData(int numPoints, int dimension) throws FileNotFoundException {
		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new FileReader(new File("medicalDataCleanedWithLabels.csv")));
        String line = "";
        Point[] points = new Point[numPoints];

        try {
        	int point_idx = 0;
			while ((line = bufferedReader.readLine()) != null) { 
				double[] attrValues = new double[dimension+1];
		        String[] value = line.split(","); 
			    for (int i = 0; i < dimension+1; i++) { 
			    	attrValues[i] = Integer.parseInt(value[i]);
			    }
			    Point p =new Point();
			    p.setAllAttributes(attrValues, dimension+1);
			    points[point_idx] = p;		
			    point_idx++;
			}
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return points;
    }
	
	public static void showPoint(Point p, int dimension) {
		for (int j = 0; j < dimension; j++) {
			System.out.printf(p.attr[j]+",");
		}
		System.out.println();
	}
	
	public static void showCentroid(Centroids c, int dimension) {
		for (int j = 0; j < dimension; j++) {
			System.out.print(c.keyValue.attr[j]+",");
		}
		System.out.println();
	}
	
	public static void showAllPoints(Point[] p, int numPoints, int dimension) {
		for (int i = 0; i < numPoints; i++) {
			System.out.print(i+":");
			for (int j = 0; j < dimension+1; j++) {
				System.out.print(p[i].attr[j]+",");
			}
			System.out.println();
		}
	}
	
	public static void showAllCentroids(Centroids[] centroids, int numCentroids, int dimension) {
		for (int i = 0; i < numCentroids; i++) {
			System.out.print(i+":");
			Point p = centroids[i].keyValue;
			for (int j = 0; j < dimension; j++) {
				System.out.print(p.attr[j]+",");
			}
			System.out.println();
		}
	}
	
	public static void showAllCentroidsSets(Centroids[] centroids, int numCentroids, int dimension) {
		for (int i = 0; i < numCentroids; i++) {
			System.out.print(i+":");
			showCentroid(centroids[i], dimension);
			for (Point p : centroids[i].intraClusterPoints) {
				System.out.print("    ");
				showPoint(p, dimension+1);
			}
		}
	}
	
	public static void includePointtoCentroid(Centroids centroid, Point point) {
		centroid.intraClusterPoints.add(point);
	}
	
	public static double calculateAllCentroidPairValues(Centroids[] centroids, int numCentroids, int dimension) {
		double sum = 0;
		for (int i = 0; i < numCentroids; i++) {
			for (int j = 0; j < numCentroids; j++) {
				sum = sum + euclideanDistance(centroids[i].keyValue, centroids[j].keyValue, dimension);
			}
		}
		return sum;
	}
	
	@SuppressWarnings("resource")
	public static int[] verifyLabels(int numPoints, int dimension) throws FileNotFoundException {
		BufferedReader bufferedReader = null;
		// This medicalDataCleaned has been generated using R with labels as its last attributes, V11 
		bufferedReader = new BufferedReader(new FileReader(new File("medicalDataCleanedWithLabels.csv")));
        String line = "";
        int[] labels = new int[numPoints];
        int i;

        try {
        	int point_idx = 0;
			while ((line = bufferedReader.readLine()) != null) { 
		        String[] value = line.split(","); 
			    labels[point_idx] = Integer.parseInt(value[dimension]);		
			    point_idx++;
			}
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return labels;
		
	}

	
	public static int findLabels(Point p, int dimension) {
		return (int)p.attr[dimension]; // Last attribute is its label
	}
	
	
	/* Performance measurement function */
	public static double performanceMeasurePPV(Centroids[] centroids, int numCentroids, int dimension) {
		int truePositives = 0;
		int falsePositives = 0;
		int label;
		int checkLabels;
		for (int i = 0; i < numCentroids; i++ ) {
			int num2Labels = 0;
			int num4Labels = 0;
			ArrayList<Point> points = centroids[i].intraClusterPoints;
			for (Point p: points) {
				checkLabels = findLabels(p, dimension);
				if (checkLabels == 2) {
					num2Labels += 1;
				}
				else {
					num4Labels += 1;
				}
			}
			if (num2Labels > num4Labels) {
				centroids[i].label = 2;
			}
			else {
				centroids[i].label = 4;
			}
		}
		
		for (int i = 0; i < numCentroids; i++) {
			label = centroids[i].label;
			ArrayList<Point> points = centroids[i].intraClusterPoints;
			for(Point p: points) {
				checkLabels = findLabels(p, dimension);
				if (checkLabels == label) {
					truePositives += 1;
				}
				else {
					falsePositives += 1;
				}
			}	
		}
		//System.out.println("<TP: "+truePositives+", FP: "+ falsePositives+">");
		double PPV = 1.0 * (truePositives) / (truePositives+falsePositives);
		return PPV;
	}
	
	public static Centroids[] KLMeans(Point[] points, int numPoints, int numCentroids/* k */, int numAssociations/* l */, int dimension, int maxIter, double threshold) {
		
		Centroids[] centroids = new Centroids[numCentroids];
		for (int j = 0; j < numCentroids; j++) {
			centroids[j] = new Centroids(dimension);
		}
		//showAllCentroids(centroids, numCentroids, dimension);
		
		double newAllCentroidPairValues = Integer.MAX_VALUE;
		double allCentroidPairValues;
		double difference;
		
		if (numCentroids < numAssociations) {
			System.out.println("Cannot perform KL means");
			return centroids;
		}
	
		//Iterate Until threshold conditions are met
		int iter = 0;
		//System.out.println("Convergence");
		do {
			for(int j = 0; j < numCentroids; j++) {
				centroids[j].intraClusterPoints = new ArrayList<Point>();
			}
			iter = iter + 1;
			allCentroidPairValues = newAllCentroidPairValues;
			for (int i = 0; i < numPoints; i++) {
				Point p = points[i];
				int[] selectedCentroids = selectBestLCentroidforPoints(p, centroids, dimension, numAssociations);
				for (int k = 0; k < numAssociations; k++) {
					includePointtoCentroid(centroids[selectedCentroids[k]], p);
				}
			}
			//showAllCentroidsSets(centroids, numCentroids, dimension);
			
			for(int j = 0; j < numCentroids; j++) {
				centroids[j].keyValue = calculateMean(centroids[j], dimension);
			}
			
			newAllCentroidPairValues = calculateAllCentroidPairValues(centroids, numCentroids, dimension);
			difference = Math.abs(newAllCentroidPairValues-allCentroidPairValues);

			
			if (difference < (threshold * allCentroidPairValues))
				break;
			else {
				//System.out.println(iter+":<"+allCentroidPairValues+","+newAllCentroidPairValues+ difference+">");
			}
			
		} while(iter < maxIter);
		
		//System.out.println("\nCluster blocks");
		//showAllCentroidsSets(centroids, numCentroids, dimension);
		
		/*
		int[] labels = null;
		//Labels access
		try {
			labels = verifyLabels(numPoints, dimension);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		double PPV = performanceMeasurePPV(centroids, numCentroids, dimension);
		//System.out.println("k = "+numCentroids+ ",PPV: "+PPV);
		return centroids;
		
	}
	
	
	public static void crossValidation(Point[] points, int numPoints, int numCentroids, int numAssociations, int dimension, int maxIter, double threshold) {
		int numOfPartitions = 10;
		int partitionSize = numPoints/numOfPartitions;
		double PPVSum = 0; 
		double[] PPV = new double[numOfPartitions];
		
		System.out.println("L = "+numAssociations);
		System.out.println("----------------------");
		System.out.println("Test               PPV Result");
		for (int partitionIdx = 0; partitionIdx < numOfPartitions; partitionIdx++) {
			Point[] testPoints = new Point[partitionSize];
			Point[] trainPoints = new Point[partitionSize * 9+1];
			int trainPoint_idx = 0, testPoint_idx = 0;
		
			int startIdx = partitionIdx*partitionSize;
			int endIdx = startIdx + partitionSize-1;
			for (int i = 0; i < (partitionSize * numOfPartitions); i++) {
				if (i >= startIdx && i < endIdx) {
					testPoints[testPoint_idx] = points[i];
					testPoint_idx += 1;
				} else {
					trainPoints[trainPoint_idx] = points[i];
					trainPoint_idx += 1;
				}
			}
		
			//Train the data 
			Centroids[] centroids = KLMeans(trainPoints, partitionSize * 9, numCentroids, numAssociations, dimension, maxIter, threshold);
			//Test the data
			int TP = 0, FP = 0;
			for(int i = 0; i < partitionSize-1; i++) {
				int selectedCentroid = selectBestCentroidforPoints(testPoints[i], centroids, dimension);
				if (centroids[selectedCentroid].label == findLabels(testPoints[i], dimension)) {
					TP += 1;
				}
				else {
					FP += 1;
				}
			}
			PPV[partitionIdx] = 1.0 * TP / (TP + FP);
			PPVSum += PPV[partitionIdx];
			System.out.println(startIdx+"-"+endIdx+"                 "+PPV[partitionIdx]);
			}
		
		PPVSum =  (double) PPVSum / numOfPartitions;
		System.out.println("Average PPV: "+ PPVSum);
	}
	

	public static void main(String[] args) {
		
		int numPoints = 683;
		int dimensionInclusingTarget = 10;
		int dimension = dimensionInclusingTarget-1;
		Point[] points = new Point[numPoints];
		
		//Load data
		try {
			points = readData(numPoints, dimension);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Display points
		//showAllPoints(points, numPoints, dimension);
		//KLMeans(points, numPoints, 8, 2, dimension, 10, 0.0001);
		crossValidation(points, numPoints, 8/*k*/, 7/*l*/, dimension, 1000, 0.0001);
	}
}
