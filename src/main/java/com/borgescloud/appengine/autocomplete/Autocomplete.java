package com.borgescloud.appengine.autocomplete;

import com.borgescloud.appengine.autocomplete.AbstractAutocomplete.Response;

public interface Autocomplete {

	Response[] getNgramMatch(String ngram, int max);

}