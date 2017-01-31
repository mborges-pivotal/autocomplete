package com.borgescloud.appengine.autocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NgramParserPlus - edge n-gram parser that consider words
 * 
 * This is my initial design, but I decided to walk first, so
 * look at NgramParser for a simpler edge n-gram parser.
 * 
 * @author mborges
 *
 */
public class NgramParserPlus {

	private final static String[] PRODUCTS = { "onion omega 2 board", "onion omega 2 plus" };

	private static final int ST_START_WORD = 1;
	private static final int ST_WORD = 2;

	private static final char SPACE = ' ';
	
	// MAIN
	public static void main2(String[] args) {

		NgramParserPlus p = new NgramParserPlus();

		// Products
		for (int prdIndex = 0; prdIndex < PRODUCTS.length; prdIndex++) {
			p.parseProduct(prdIndex, PRODUCTS[prdIndex].trim());
		}
		
		p.printIndex();

	} // main

	//////////////////////////////////////////////

	private Map<String, List<ProductIndex>> ngramIndex = new HashMap<String, List<ProductIndex>>();

	private long id;

	private void parseProduct(long id, String product) {

		this.id = id;

		int state = ST_START_WORD;
		int wordIndex = 0;

		StringBuffer ngram = new StringBuffer();

		// Product
		for (int i = 0; i < product.length(); i++) {

			char c = product.charAt(i);

			switch (state) {

			case ST_START_WORD:
				if (c != SPACE) {
					state = ST_WORD;
					ngram.append(c);
					addNgram(ngram.toString(), wordIndex);
				}
				break;
			case ST_WORD:
				if (c == SPACE) {
					state = ST_START_WORD;
					wordIndex++;
					ngram.setLength(0);
				} else {
					ngram.append(c);
					addNgram(ngram.toString(), wordIndex);
				}
				break;
			}

		}
		
		addNgram(ngram.toString(), wordIndex);
		
	} // parseProducts

	private void addNgram(String ngram, int wordIndex) {
		
		System.out.printf("%s ", ngram);
		
		List<ProductIndex> productList = ngramIndex.get(ngram);
		if (productList == null) {
			productList = new ArrayList<ProductIndex>();
		}
		productList.add(new ProductIndex(id, wordIndex));
		ngramIndex.put(ngram, productList);
	}
	
	private void printIndex() {
		
		for(Map.Entry<String, List<ProductIndex>> entry: ngramIndex.entrySet()) {
			String ngram = entry.getKey();
			List<ProductIndex> prds = entry.getValue();
			System.out.println(ngram);
			for (ProductIndex p: prds) {
				System.out.printf("%s ", p);
			}
			System.out.println("");
		}
		
	}

	/////////////////////////////////////
	// Inner Classes
	/////////////////////////////////////

	public static class ProductIndex {
		public final long id;
		public final int wordIndex;

		public ProductIndex(long id, int wordIndex) {
			this.id = id;
			this.wordIndex = wordIndex;
		}

		@Override
		public String toString() {
			return "ProductIndex [id=" + id + ", wordIndex=" + wordIndex + "]";
		}		
	}

} // class
