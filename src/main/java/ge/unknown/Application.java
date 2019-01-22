package ge.unknown;

import ge.unknown.configuration.StorageConfiguration;
import ge.unknown.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletException;

/**
 * Created by MJaniko on 3/3/2017.
 */
@SpringBootApplication()
@EnableConfigurationProperties(StorageConfiguration.class)
@PropertySource("classpath:application.properties")
@EnableScheduling
@Slf4j
public class Application {

    @Autowired
    private StorageService serv;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.init();
        };
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        return new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(Tomcat tomcat) {
                Context root = null;
                try {
                    root = tomcat.addWebapp("/uploads", serv.getRootLocation().toAbsolutePath().toString());
                    root.setAllowCasualMultipartParsing(true);
                } catch (ServletException e) {
                    //e.printStackTrace();
                }
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }
        };
    }
}
