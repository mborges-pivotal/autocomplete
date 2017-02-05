package com.borgescloud.appengine.autocomplete.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component("localstore")
public class LocalStore extends AbstractAutocomplete {
	
	// This structure 
	private Map<Long, String> products = new HashMap<Long, String>();
	private Map<String, List<Long>> ngramIndex = new HashMap<String, List<Long>>();

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

	public void addNgram(String ngram, long id, String product) {
		List<Long> productList = ngramIndex.get(ngram);
		if (productList == null) {
			productList = new ArrayList<Long>();
		}
		productList.add(id);
		ngramIndex.put(ngram, productList);		
		
		products.put(id, product);
	}
	
	// printIndex
	public void stats() {

		//System.out.println("");
		System.out.printf("total ngrams: %d", ngramIndex.size());
		
		long max = 0;
		long avg = 0;
		long min = 0;
		long total = 0;

		for (Map.Entry<String, List<Long>> entry : ngramIndex.entrySet()) {
			List<Long> prds = entry.getValue();
			
			int size = prds.size();
			
			total += size;
			
			if (min == 0) {
				min = size;
			}
			
			max = Math.max(max, size);
			min = Math.min(min, size);			
		}
		
		avg = total / ngramIndex.size();
		
		System.out.printf("\nngram numbers: %d, %d, %d\n", min,avg,max);

	}
		

}
