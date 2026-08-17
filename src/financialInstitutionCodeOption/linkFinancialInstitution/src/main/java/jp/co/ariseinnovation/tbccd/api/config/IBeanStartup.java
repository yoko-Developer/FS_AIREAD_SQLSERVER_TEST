package jp.co.ariseinnovation.tbccd.api.config;

import org.springframework.context.ApplicationContext;

public interface IBeanStartup {
	public void startup(ApplicationContext appContext);
}
