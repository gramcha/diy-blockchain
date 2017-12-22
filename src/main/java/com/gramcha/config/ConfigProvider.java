/**
 * @author gramcha
 * 22-Dec-2017 7:03:21 PM
 * 
 */
package com.gramcha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ConfigProvider {
	@Autowired
	private Environment env;

	public Boolean isOrchestrator() {
		if (false == StringUtils.isEmpty(env.getProperty("isOrchestrator"))) {
			return env.getProperty("isOrchestrator").equals("true");
		} else {
			return false;
		}
	}

	public String getPortNumber() {
		return env.getProperty("server.port");
	}
	
	public String getOrchestratorUrl() {
		return env.getProperty("OrchestratorUrl");
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigProvider [isOrchestrator()=");
		builder.append(isOrchestrator());
		builder.append(", getPortNumber()=");
		builder.append(getPortNumber());
		builder.append(", getOrchestratorUrl()=");
		builder.append(getOrchestratorUrl());
		builder.append("]");
		return builder.toString();
	}

}
