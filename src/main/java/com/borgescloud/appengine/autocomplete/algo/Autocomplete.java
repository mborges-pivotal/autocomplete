package com.borgescloud.appengine.autocomplete.algo;

import com.borgescloud.appengine.autocomplete.algo.AbstractAutocomplete.Response;

public interface Autocomplete {

	Response[] getNgramMatch(String ngram, int max);

}