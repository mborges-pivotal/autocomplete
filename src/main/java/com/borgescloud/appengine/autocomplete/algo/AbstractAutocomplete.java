package com.borgescloud.appengine.autocomplete.algo;

public abstract class AbstractAutocomplete implements AutocompleteStore {

	private static final int MIN_NGRAM = 3;
	private static final int MAX_NGRAM = 15;

	// addProduct
	public void addProduct(long id, String product) {
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

	// Inner Class used by Autocomplete
	// based on the bootcomplete jquery utility
	public static class Response {
		public final String id;
		public final String label;

		public Response(String id, String label) {
			this.id = id;
			this.label = label;
		}
	}

}
