package com.borgescloud.appengine.autocomplete.algo;

public interface AutocompleteStore extends Autocomplete {
	
	void addProduct(long id, String product);
	void addNgram(String ngram, long id, String product);
	void stats();

}
