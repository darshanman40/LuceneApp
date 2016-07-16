import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.darshan.luceneApp.components.Indexer;
import com.darshan.luceneApp.components.Searcher;
import com.darshan.luceneApp.filters.TextFileFilter;
import com.darshan.luceneApp.interfaces.ILuceneAppConstants;

public class LuceneAppTest implements ILuceneAppConstants{

	   Indexer indexer;
	   Searcher searcher;


	   
	   //File indexFile;// =  resourceLoader.getResource(indexDir).getFile();
	   
	   
	   public static void main(String args[]){
		   
		   LuceneAppTest tester;
	      try {
	    	 
	    	 tester = new LuceneAppTest();
	         //tester.indexFile =  tester.getResourceLoader().getResource(indexDir).getFile();
	         tester.createIndex();
	         tester.search("mohan");
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ParseException e) {
	         e.printStackTrace();
	      }
	   }

	   private void createIndex() throws IOException{
	      indexer = new Indexer(indexDir);
	      int numIndexed;
	      long startTime = System.currentTimeMillis();	
	      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
	      long endTime = System.currentTimeMillis();
	      indexer.close();
	      System.out.println(numIndexed+" File indexed, time taken: "
	         +(endTime-startTime)+" ms");		
	   }

	   private void search(String searchQuery) throws IOException, ParseException{
	      searcher = new Searcher(indexDir);
	      long startTime = System.currentTimeMillis();
	      TopDocs hits = searcher.search(searchQuery);
	      long endTime = System.currentTimeMillis();
	   
	      System.out.println(hits.totalHits +
	         " documents found. Time :" + (endTime - startTime));
	      for(ScoreDoc scoreDoc : hits.scoreDocs) {
	         Document doc = searcher.getDocument(scoreDoc);
	            System.out.println("File: "
	            + doc.get(FILE_PATH));
	      }
	      searcher.close();
	   }

	
}
