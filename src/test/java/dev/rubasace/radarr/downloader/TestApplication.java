package dev.rubasace.radarr.downloader;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = RadarrDownloaderApplication.class))
@SpringBootApplication
public class TestApplication {
}
