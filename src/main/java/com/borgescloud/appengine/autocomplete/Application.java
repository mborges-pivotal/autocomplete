/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.borgescloud.appengine.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.borgescloud.appengine.autocomplete.algo.Autocomplete;
import com.borgescloud.appengine.autocomplete.algo.AbstractAutocomplete.Response;

@SpringBootApplication
@RestController
public class Application {
	
	@Autowired
	@Qualifier("localstore")
	private Autocomplete store;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/////////////////////////////////
	// RestController
	/////////////////////////////////
	
	@RequestMapping("/autocomplete")
	public Response[] autocomplete(String query, @RequestParam(value="max", required=false, defaultValue="3") int max) {
		return store.getNgramMatch(query, max);
	}

	/**
	 * <a href=
	 * "https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
	 * App Engine health checking</a> requires responding with 200 to
	 * {@code /_ah/health}.
	 */
	@RequestMapping("/_ah/health")
	public String healthy() {
		// Message body required though ignored
		return "Still surviving.";
	}

}
