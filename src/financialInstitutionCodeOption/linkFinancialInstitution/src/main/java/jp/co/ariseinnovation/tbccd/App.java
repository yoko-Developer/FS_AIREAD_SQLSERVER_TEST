package jp.co.ariseinnovation.tbccd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Main App */
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
})
public class App {
    private final static Logger log = LoggerFactory.getLogger(App.class);
	public static void main(String[] args) {
		try {
			SpringApplication.run(App.class, args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
		}
	}
}
