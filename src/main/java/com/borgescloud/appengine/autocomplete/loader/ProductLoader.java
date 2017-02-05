package com.borgescloud.appengine.autocomplete.loader;

import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.borgescloud.appengine.autocomplete.algo.AutocompleteStore;

/**
 * ProductLoader
 * 
 * @author mborges
 *
 */

@Component
public class ProductLoader implements CommandLineRunner {

	@Value("${autocomplete.load}")
	private Resource loadFile;
	
	@Autowired
	@Qualifier("localstore")
	private AutocompleteStore store;
	
	// run
	public void run(String... arg0) throws Exception {

		int total = 0;
		
		Reader in = new InputStreamReader(loadFile.getInputStream());
		
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {	
			long recordNumber = record.getRecordNumber();
			String name = record.get(0).trim();
			
			store.addProduct(recordNumber, name);
			total += 1;
		}

		System.out.printf("total records: %d\n", total);

		store.stats();
	}

} // class
