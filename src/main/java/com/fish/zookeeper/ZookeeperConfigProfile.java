/**
 * Copyright 1999-2014 dangdang.com.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fish.zookeeper;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基本配置
 * 
 */
public class ZookeeperConfigProfile {

	static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConfigProfile.class);

	private static final ExponentialBackoffRetry DEFAULT_RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

	/**
	 * zookeeper地址
	 */
	@Getter
	@Setter
	private final String connectStr;

	@Getter
	@Setter
	private final String node;

	/**
	 * 重试策略
	 */
	@Getter
	@Setter
	private final RetryPolicy retryPolicy;


	public ZookeeperConfigProfile(final String connectStr, final String node) {
		this.connectStr = Preconditions.checkNotNull(connectStr);
		this.node = Preconditions.checkNotNull(node);
		this.retryPolicy = DEFAULT_RETRY_POLICY;
	}

}
