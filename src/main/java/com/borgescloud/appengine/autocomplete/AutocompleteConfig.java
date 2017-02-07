package com.borgescloud.appengine.autocomplete;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.borgescloud.appengine.autocomplete.store.LocalStore;
import com.borgescloud.appengine.autocomplete.store.MemcacheStore;
import com.borgescloud.appengine.autocomplete.store.RedisStore;

@Configuration
public class AutocompleteConfig {
	
	private Log log = LogFactory.getLog(AutocompleteConfig.class);
	
	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "local", matchIfMissing = true)
    public AutocompleteStore localStoreService() {
		log.info("using 'local' store.");
        return new LocalStore();
    }

	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "redis", matchIfMissing = true)
    public AutocompleteStore redisStoreService() {
		log.info("using 'redis' store.");
        return new RedisStore();
    }

	@Bean
    @ConditionalOnProperty(name = "autocomplete.store.name", havingValue = "memcache", matchIfMissing = true)
    public AutocompleteStore memcacheStoreService() {
		log.info("using 'memcache' store.");
        return new MemcacheStore();
    }

}
