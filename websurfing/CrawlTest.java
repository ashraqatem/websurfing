package websurfing;


import java.util.ArrayList;

public class CrawlTest {
    public static void main(String[] args) {
        //Initializing variables
        SearchEngine se;

        try {
            //Creating search engine
            se = new SearchEngine("singleVert.xml");

            //Marking my single url
            String[] urls = {"siteA"};

            //Crawling over url
            se.crawlAndIndex("siteA");

            //Initializing arrays for proper comparison later on
            String expectedGraph = "[siteA]";

            String[] wordsArr = {"lol", "this", "project", "is", "not", "that", "bad"};
            ArrayList<String> words = new ArrayList<>(se.wordIndex.keySet());

            String expectedLinkToWord = "[siteA]";

            //Checking to see if we got all vertices
//            assertEquals(expectedGraph, se.internet.getVertices().toString(), "Check if you're adding vertices correctly");
            System.out.println(se.internet.getVertices().toString());

            //Checking if word index contains all words (my method only works if you put everything to lowercase in word index)
            boolean wordNotThere = false;

            for (String word : wordsArr) {
                if (!words.contains(word)) {
                    wordNotThere = true;
                    break;
                }
            }

//            assertFalse(wordNotThere, "Check if words are added correctly to index");
            System.out.println(wordNotThere);

            //Checking one word to see if it links to the proper url
//            assertEquals(expectedLinkToWord, se.wordIndex.get("lol").toString(), "Check if you're adding correct URL's");
            System.out.println(se.wordIndex.get("lol").toString());

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
