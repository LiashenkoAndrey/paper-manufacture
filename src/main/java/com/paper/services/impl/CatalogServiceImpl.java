package com.paper.services.impl;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import com.paper.domain.Catalog;
import com.paper.services.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static com.google.cloud.translate.Translate.TranslateOption.sourceLanguage;
import static com.google.cloud.translate.Translate.TranslateOption.targetLanguage;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final Translate translate;

    @Override
    public List<Catalog> translateAll(List<Catalog> catalogs) {
        List<String> names = catalogs.stream().map(Catalog::getName).toList();
        String currentLocale = LocaleContextHolder.getLocale().getLanguage();
        String sourceLanguage = Locale.ENGLISH.getLanguage();

        if (!currentLocale.equals(sourceLanguage)) {
            List<Translation> translation = translate.translate(
                    names,
                    sourceLanguage("en"),
                    targetLanguage(LocaleContextHolder.getLocale().getLanguage())
            );
            for (int i = 0; i < catalogs.size(); i++) {
                catalogs.get(i).setName(translation.get(i).getTranslatedText());
            }
        }
        return catalogs;
    }
}
