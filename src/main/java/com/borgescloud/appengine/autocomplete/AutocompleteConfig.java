package com.borgescloud.appengine.autocomplete;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.borgescloud.appengine.autocomplete.store.LocalStore;
import com.borgescloud.appengine.autocomplete.store.MemcacheStore;
import com.borgescloud.appengine.autocomplete.store.RedisStore;

@Configuration
public class AutocompleteConfig {
	
	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "local", matchIfMissing = true)
    public AutocompleteStore localStoreService() {
        return new LocalStore();
    }

	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "redis", matchIfMissing = true)
    public AutocompleteStore redisStoreService() {
        return new RedisStore();
    }

	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "memcache", matchIfMissing = true)
    public AutocompleteStore memcacheStoreService() {
        return new MemcacheStore();
    }

}
