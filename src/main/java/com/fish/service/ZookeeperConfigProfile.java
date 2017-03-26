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
package com.fish.service;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 基本配置
 * 
 */

public class ZookeeperConfigProfile {

	private static final ExponentialBackoffRetry DEFAULT_RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

	/**
	 * zookeeper地址
	 */
	@Getter
	private final String connectStr;

	/**
	 * 项目配置根目录
	 */
	@Getter
	private final String rootNode;

	/**
	 * 重试策略
	 */
	@Getter
	private final RetryPolicy retryPolicy;


	public ZookeeperConfigProfile(final String connectStr, final String rootNode) {
		this(connectStr, rootNode, DEFAULT_RETRY_POLICY);
	}

	public ZookeeperConfigProfile(final String connectStr, final String rootNode, final RetryPolicy retryPolicy) {
		this.connectStr = Preconditions.checkNotNull(connectStr);
		this.rootNode = Preconditions.checkNotNull(rootNode);
		this.retryPolicy = Preconditions.checkNotNull(retryPolicy);
	}



}
