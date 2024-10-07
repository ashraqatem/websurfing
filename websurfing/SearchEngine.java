package websurfing;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, ArrayList of Strings)
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}

	/*
	 * This does an exploration of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 *
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : Add code here
//      brainstorming steps:
//		Initialize a queue to perform  traversal.
//		Add the starting URL vertex to the graph and marks it as visited.
//      enque the starting URL onto the q.
//		While the s is not empty:
//		deq a page.
//		Update the web graph by adding appropriate edges.
//		Update the word index for each word on the page.
//		Mark neighboring pages as visited and en-q them onto the queue.

		//stack addFirst and removeFirst make a queue to be used for  traversal
		ArrayList<String> q = new ArrayList<>();

		//add our current url to the internet
		internet.addVertex(url);
		internet.setVisited(url,true);

		//add our current url to the queue to start the traversal
		q.addFirst(url);

		while (!q.isEmpty()){
			//the first element in the queue is our currentUrl used for first traversal
			String currentUrl = q.removeLast();

			//get all the links from current link so edges can be updated
			ArrayList<String> currentUrlLinks = parser.getLinks(currentUrl);

			for (String link: currentUrlLinks){
				//add the vertex before adding an edge if the vertex is not found addedge will return
				internet.addVertex(link);
				internet.addEdge(currentUrl, link);

				//add edge if it is succsefuly added then mark it as visited and add to queque
				if  (!internet.getVisited(link)){
					internet.setVisited(link,true);
					q.addFirst(link);
				}
			}

			ArrayList<String> currentUrlContent = parser.getContent(currentUrl);

			for (String word : currentUrlContent){
				word = word.toLowerCase();

				//check if word in our search engine add as a key
				if (!wordIndex.containsKey(word)){
					wordIndex.put(word, new ArrayList<>());
				}

				//if word is in the search engine check if the current url is associted with the word
				//case where more than one url have the same word
				if (!wordIndex.get(word).contains(currentUrl)){
					wordIndex.get(word).add(currentUrl);
				}
			}

		}
	}

	/*
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex().
	 * To implement this method, refer to the algorithm described in the
	 * assignment pdf.
	 *
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		ArrayList<String> vertices = internet.getVertices();

		//initializing list for the ranks in the previous iteration
		ArrayList<Double> previousIter = new ArrayList<>();

		//Start by initializing pr(vi) to 1 for all 0 ≤ i ≤ N
		// this is also the ranks in the currentIter
		ArrayList<Double> currentIter = new ArrayList<>();

		for (String vertex : vertices) {
			currentIter.add(1.0);
		}

		//double converge = ; //how do we calculate convergence what is pr^k in (prk−1(vi) − prk(vi))
		//convergence is the difference between the current rank and the previous rank

		boolean converge = false;

		while (!converge) {
			converge = true;

			//now our current ranks become our previous ranks
			previousIter = currentIter;

			//this returns a WHOLE new array
			currentIter = computeRanks(vertices);

			//int maxConver = vertices.size();
			//int counter = 0;

			//loop for convergence
			for (int i = 0; i < vertices.size(); i++){
				double prevRank = previousIter.get(i);
				double currRank = currentIter.get(i);
				double difference = Math.abs(prevRank - currRank);

				//if one of the pairs is not converged then the whole thing has not converged
				if (difference >= epsilon){
					converge = false;
				}
			}

			//must make sure that all vertices have reached convergence
			//once converged update internet with
			for (int v = 0; v < vertices.size(); v++) {
				internet.setPageRank(vertices.get(v), currentIter.get(v));
			}
		}
	}



	// for (String vertex : vertices) {
	// 	double pageRank = internet.getPageRank(vertex);
	// 	System.out.println("Vertex: " + vertex + ", Page Rank: " + pageRank);
	// }



	/*HELPER METHOD FOR ASSIGNPAGERANKS
	 * The method takes as input an ArrayList<String> representing the urls in the web graph
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls.
	 * Note that the double in the output list is matched to the url in the input list using
	 * their position in the list.
	 *
	 * This method will probably fit in about 20 lines.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here

		//move to assignpage
//		//Start by initializing pr(vi) to 1 for all 0 ≤ i ≤ N
//		ArrayList<Double> initialRanks = new ArrayList<>();
//		for (String vertex : vertices) {
//			initialRanks.add(1.0);
//		}

		ArrayList<Double> ranks = new ArrayList<>();

		//damping factor
		double d = 0.5;

		for (String vertex : vertices){
//			double prw = internet.getPageRank(vertex);
//			int outw = internet.getOutDegree(vertex);

			double newRank = 0;

			//ArrayList<String> neighbors = internet.getNeighbors(vertex);

			for (String page : internet.getEdgesInto(vertex)){

				int outw = internet.getOutDegree(page);
				double prw = internet.getPageRank(page);

				newRank += (prw / outw);
			}
			double finalNewRank = (1 - d) + d * newRank;
			ranks.add(finalNewRank);
		}
		return ranks;
	}


	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 *
	 * This method will probably fit in about 10-15 lines.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		ArrayList<String> searchResult = new ArrayList<>();
		query = query.toLowerCase();

		if (wordIndex.containsKey(query)){
			ArrayList<String> UrlsWithQuery = wordIndex.get(query);

			HashMap<String, Double> urlRanks = new HashMap<>();

			for (String url : UrlsWithQuery){
				double rank = internet.getPageRank(url);
				urlRanks.put(url, rank);
			}

			ArrayList<String> sortedUrl = Sorting.slowSort(urlRanks);

			searchResult.addAll(sortedUrl);

		}
		return searchResult;
	}
}
