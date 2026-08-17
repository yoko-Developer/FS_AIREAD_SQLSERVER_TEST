package jp.co.ariseinnovation.tbccd.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AppSetting {
    @Value("${app.isValid}")
    private boolean isValid;
    @Value("${app.matchFile}")
    private String matchFile;
    static AppSetting current;

    public final static AppSetting current() {
        return current;
    }
}
