package com.borgescloud.appengine.autocomplete.loader;

import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.borgescloud.appengine.autocomplete.AbstractAutocomplete.Stats;
import com.borgescloud.appengine.autocomplete.AutocompleteStore;

/**
 * ProductLoader
 * 
 * @author mborges
 *
 */

@Component
public class ProductLoader implements CommandLineRunner {
	
	private Log log = LogFactory.getLog(ProductLoader.class);

	@Value("${autocomplete.load}")
	private Resource loadFile;
	
	@Autowired
	private AutocompleteStore store;
	
	// run
	public void run(String... args) throws Exception {
		
		if (args.length > 0) {
			log.info("seeding cache...");
			load();
		} else {
			return;
		}
		
		for(String arg: args) {
			log.info(arg);
		}
		
	}
	
	public Stats load() throws Exception {
		Reader in = new InputStreamReader(loadFile.getInputStream());
		
		int total = 0;
		
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {	
			String name = record.get(0).trim(); // dvd_title
			String upc = record.get(11).trim(); // upc
		
			total++;
			
			store.addProduct(upc, name);
			
			if (total % 100 == 0) {
				log.info(String.format("loaded %d records...", total));
			}
		}
		
		if (in != null) {
			in.close();
		}
		
		log.info(store.stats());
		
		return store.stats();
	}

} // class
