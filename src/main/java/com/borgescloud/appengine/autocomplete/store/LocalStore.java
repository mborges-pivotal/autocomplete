package com.borgescloud.appengine.autocomplete.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.borgescloud.appengine.autocomplete.AbstractAutocomplete;

public class LocalStore extends AbstractAutocomplete {
	
	// This structure 
	private Map<String, String> products = new HashMap<String, String>();
	private Map<String, List<String>> ngramIndex = new HashMap<String, List<String>>();

	public Response[] getNgramMatch(String ngram, int max) {
		Response[] names = new Response[0];
		
		List<String> productIds = ngramIndex.get(ngram.toLowerCase());
		if (productIds != null) {
			int records = Math.min(max, productIds.size());
			List<String> ids = productIds.subList(0, records);
			names = new Response[records];
			for (int i=0; i < names.length; i++) {
				String record = ids.get(i);
				names[i] = new Response(record,products.get(record));
			}
		}
		return names;
	}

	public void addNgram(String ngram, String id, String product) {
		List<String> productList = ngramIndex.get(ngram);
		if (productList == null) {
			productList = new ArrayList<String>();
		}
		productList.add(id);
		ngramIndex.put(ngram, productList);		
		
		products.put(id, product);
	}
	
	// printIndex
	public Stats stats() {

		int max = 0;
		int avg = 0;
		int min = 0;
		int total = 0;

		for (Map.Entry<String, List<String>> entry : ngramIndex.entrySet()) {
			List<String> prds = entry.getValue();
			
			int size = prds.size();
			
			total += size;
			
			if (min == 0) {
				min = size;
			}
			
			max = Math.max(max, size);
			min = Math.min(min, size);			
		}
		
		avg = total / ngramIndex.size();
		
		return new Stats(products.size(), ngramIndex.size(), min, avg, max);
	}
		

}
