package com.borgescloud.appengine.autocomplete;

public abstract class AbstractAutocomplete implements AutocompleteStore {

	private static final int MIN_NGRAM = 3;
	private static final int MAX_NGRAM = 15;

	// addProduct
	public void addProduct(String id, String product) {
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
			addNgram(ngram.toString().toLowerCase(), id, product);
		}
	} // addProduct

	/////////////////////////////////////
	// Inner Class used by Autocomplete
	/////////////////////////////////////
	
	// based on the bootcomplete jquery utility
	public static class Response {
		public final String id;
		public final String label;

		public Response(String id, String label) {
			this.id = id;
			this.label = label;
		}
	}
	
	// Basic stats on the ngram indexes
	public static class Stats {
		public final int records;
		public final int ngrams;
		public final int min;
		public final int avg;
		public final int max;
		
		public Stats(int records, int ngrams, int min, int avg, int max) {
			this.records = records;
			this.ngrams = ngrams;
			this.min = min;
			this.avg = avg;
			this.max = max;
		}

		@Override
		public String toString() {
			return "Stats [records=" + records + ", ngrams=" + ngrams + ", min=" + min + ", avg=" + avg + ", max=" + max
					+ "]";
		}		
	}

}
