package com.discreet.datamasking.autodetect;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTranslationsLoaderTest {
    ColumnTranslationsLoader loader = new ColumnTranslationsLoader();

    @Test
    public void testBirthDateColumns() {
        String content = """
                Ukrainian: дата народження (data narodzhennya)
                Slovenian: datum rojstva
                Haitian Creole: dat nesans
                German: Geburtsdatum
                French: date de naissance
                Chinese (Simplified): 出生日期 (chūshēng rìqī)
                English: birth date""";
        Set<String> actualTranslations = loader.parseColumnTraslationsContent(content);
        assertEquals(9, actualTranslations.size());

        assertTrue(actualTranslations.containsAll(List.of(
                "дата_народження", "data_narodzhennya",
                "datum_rojstva", "dat_nesans", "Geburtsdatum",
                "date_de_naissance", "出生日期", "chūshēng_rìqī", "birth_date"
        )));
    }
}