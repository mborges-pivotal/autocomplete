package com.borgescloud.appengine.autocomplete;

import com.borgescloud.appengine.autocomplete.AbstractAutocomplete.Stats;

public interface AutocompleteStore extends Autocomplete {
	
	void addProduct(String id, String product);
	void addNgram(String ngram, String id, String product);
	Stats stats();

}
