# KMeans-Clustering
Implementations of K Means algorithm from scratch in Java

Organization of code:
* [Points](https://github.com/mrincs/KMeans-Clustering/blob/master/Point.java) defines superclass where a point is structured in higher dimensional space.
* [Centroids](https://github.com/mrincs/KMeans-Clustering/blob/master/Centroids.java) extends from Point to represent centroid of each cluster.
* [K Means Clustering](https://github.com/mrincs/KMeans-Clustering/blob/master/KLMeansClustering.java) implements the main unsupervised algorithm that iteratively tries to classify given points to individual clusters and update the cluster means.
