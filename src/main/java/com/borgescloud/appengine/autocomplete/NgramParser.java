package com.borgescloud.appengine.autocomplete;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 * NgramParser - edge n-gram parser
 * 
 * TODO:
 * - This should become a loader
 * - This loader can be implement in MapReduce or with some other parallel processing method
 * 
 * @author mborges
 *
 */

@Component
public class NgramParser implements CommandLineRunner {

	private static final int MIN_NGRAM = 3;
	private static final int MAX_NGRAM = 15;

	// This structure 
	private Map<Long, String> products = new HashMap<Long, String>();
	private Map<String, List<Long>> ngramIndex = new HashMap<String, List<Long>>();
	
	@Value("${autocomplete.load}")
	private Resource loadFile;
	
	// run
	public void run(String... arg0) throws Exception {

		int total = 0;
		
		Reader in = new InputStreamReader(loadFile.getInputStream());
		
		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
		for (CSVRecord record : records) {	
			long recordNumber = record.getRecordNumber();
			String name = record.get(0).trim();
			
			products.put(recordNumber, name);
			parseProduct(recordNumber, name.toLowerCase());
			total += 1;
		}

		System.out.printf("total records: %d\n", total);

		printIndex();
	}

	// parseProduct
	private void parseProduct(long id, String product) {

		StringBuffer ngram = new StringBuffer();

		// Product
		for (int i = 0; i < product.length(); i++) {

			ngram.append(product.charAt(i));

			if (i < (MIN_NGRAM - 1)) {
				continue;
			}
			
			if (i > MAX_NGRAM) {
				break;
			}

			addNgram(ngram.toString(), id);
		}

	} // parseProducts

	// addNgram
	private void addNgram(String ngram, long id) { 

		//System.out.printf("%s, ", ngram);

		List<Long> productList = ngramIndex.get(ngram);
		if (productList == null) {
			productList = new ArrayList<Long>();
		}
		productList.add(id);
		ngramIndex.put(ngram, productList);
	}
	
	public Response[] getNgramMatch(String ngram, int max) {
		Response[] names = new Response[0];
		
		List<Long> productIds = ngramIndex.get(ngram.toLowerCase());
		if (productIds != null) {
			int records = Math.min(max, productIds.size());
			List<Long> ids = productIds.subList(0, records);
			names = new Response[records];
			for (int i=0; i < names.length; i++) {
				long record = ids.get(i);
				names[i] = new Response(record+"",products.get(record));
			}
		}
		return names;
	}

	// printIndex
	private void printIndex() {

		//System.out.println("");
		System.out.printf("total ngrams: %d", ngramIndex.size());
		
		long max = 0;
		long avg = 0;
		long min = 0;
		long total = 0;

		for (Map.Entry<String, List<Long>> entry : ngramIndex.entrySet()) {
			String ngram = entry.getKey();
			List<Long> prds = entry.getValue();
			
			int size = prds.size();
			
			total += size;
			
			if (min == 0) {
				min = size;
			}
			
			max = Math.max(max, size);
			min = Math.min(min, size);
			
			/*
			System.out.println(ngram);
			for (Long p : prds) {
				System.out.printf("%s ", p);
			}
			System.out.println("");
			*/
		}
		
		avg = total / ngramIndex.size();
		
		System.out.printf("\nngram numbers: %d, %d, %d\n", min,avg,max);

	}
	
	// based on the bootcomplete jquery utility
	// http://getwebhelp.com/bootcomplete/
	public static class Response {
		public final String id;
		public final String label;
		
		public Response(String id, String label) {
			this.id = id;
			this.label = label;
		}
	}

} // class
