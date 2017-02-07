package com.borgescloud.appengine.autocomplete.store;

import com.borgescloud.appengine.autocomplete.AbstractAutocomplete;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcacheStore extends AbstractAutocomplete {
	
	private static final String SPLIT_CHAR = "|";

	private MemcacheService ngramIndex = MemcacheServiceFactory.getMemcacheService("ngramIndex");
	private MemcacheService products = MemcacheServiceFactory.getMemcacheService("products");

	public void addNgram(String ngram, String id, String product) {
		String productList = (String) ngramIndex.get(ngram);
		if (productList == null) {
			productList = product;
		} else {
			// not worry about optimization, let compiler do it or check later
			productList += SPLIT_CHAR + product;
		}
		ngramIndex.put(ngram, productList);
		products.put(id, product);
	}

	public Stats stats() {
		throw new Error("not implemented!");
	}

	public Response[] getNgramMatch(String ngram, int max) {
		Response[] names = new Response[0];

		String productIds = (String) ngramIndex.get(ngram.toLowerCase());
		if (productIds != null) {
			
			String[] ids = productIds.split(SPLIT_CHAR);
			
			int records = Math.min(max, ids.length);
			names = new Response[records];
			for (int i = 0; i < names.length; i++) {
				String record = ids[i];
				names[i] = new Response(record, (String)products.get(record));
			}
		}
		return names;
	}

}
