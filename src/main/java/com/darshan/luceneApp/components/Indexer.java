package com.darshan.luceneApp.components;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;

import com.darshan.luceneApp.interfaces.ILuceneAppConstants;

public class Indexer implements ILuceneAppConstants{

	private IndexWriter writer;
	
	@Autowired
	private ResourceLoader resource;
	
	@SuppressWarnings("deprecation")
	public Indexer(String indexDirectoryPath) throws IOException{
		//this directory will contain indexes
		Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
		
		//create the indexer
		writer = new IndexWriter(indexDirectory,new StandardAnalyzer(Version.LUCENE_36),
				true,IndexWriter.MaxFieldLength.UNLIMITED);
	}
	
	public Indexer(File indexFile) throws IOException{
		//this directory will contain indexes
		Directory indexDirectory = FSDirectory.open(indexFile);
		
		//create the indexer
		writer = new IndexWriter(indexDirectory,new StandardAnalyzer(Version.LUCENE_36),
				true,IndexWriter.MaxFieldLength.UNLIMITED);
	}
	
	public void close() throws CorruptIndexException,IOException{
		writer.close();
	}
	
	private Document getDocument(File file) throws IOException{
		Document document = new Document();
		
		//index file constants
		Field contentField = new Field(CONTENTS, new FileReader(file));
		//index file name
		Field fileNameField = new Field(FILE_NAME, file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		//index file path
		Field filePathField = new Field(FILE_PATH,file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
		
		document.add(contentField);
		document.add(fileNameField);
		document.add(filePathField);
		
		return document;
	}
	
	private void indexFile(File file) throws IOException{
		System.out.println("Indexing "+file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
	}
	
	 public int createIndex(String dataDirPath, FileFilter filter) 
		      throws IOException{
		      //get all files in the data directory
		 
		      File[] files = new File(dataDirPath).listFiles();
		      //System.out.println(dataDirPath+" "+files.toString());
		      for (File file : files) {
		         if(!file.isDirectory()
		            && !file.isHidden()
		            && file.exists()
		            && file.canRead()
		            && filter.accept(file)
		         ){
		            indexFile(file);
		         }
		      }
		      return writer.numDocs();
		   }
}
