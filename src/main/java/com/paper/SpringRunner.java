package com.paper;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SpringRunner {


    /**
     * //        Detection detection = translate.detect("Hola");
     * //        String detectedLanguage = detection.getLanguage();
     * //        Translation translation = translate.translate(
     * //                "World",
     * //                com.google.cloud.translate.Translate.TranslateOption.sourceLanguage("en"),
     * //                com.google.cloud.translate.Translate.TranslateOption.targetLanguage(detectedLanguage));
     * //
     * //        System.out.printf("Hola %s%n", translation.getTranslatedText());
     */
    @Bean
    public Translate translate() {
        System.setProperty("GOOGLE_API_KEY", "AIzaSyDLZ6wuEcXjA8TT6g4nrL1teKOxSm6P4zM");
        return TranslateOptions.getDefaultInstance().getService();
    }



    public static void main(String[] args) {
        SpringApplication.run(SpringRunner.class);
    }
}
