package com.kropotov.asrd.services.pdf;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

public interface PdfService {

    /**
     * Генерация pdf-файла
     * @param pathAndFileName путь и имя файла
     * @param text текстовое содержимое pdf-файла
     */
    void generatePlainPdf(@NotNull String pathAndFileName,
                          @NotNull String text);

    /**
     * Генерация pdf-файла с пользовательскими размерами страницы
     * @param pathAndFileName путь и имя файла
     * @param text текстовое содержимое pdf-файла
     */
    void generateCustomPageSizePdf(@NotNull String pathAndFileName,
                                   @NotNull String text);

    /**
     * Генерация pdf-файла с текстом после изображения
     * @param imageFileName имя файла с изображением
     * @param pathAndFileName путь и имя файла
     * @param text текстовое содержимое pdf-файла
     */
    void generateTextWithImagePdf(@NotNull String imageFileName,
                                  @NotNull String pathAndFileName,
                                  @NotNull String text);

    /**
     * Генерация pdf-файла с пользовательским шрифтом и цветом шрифта
     * @param pathAndFileName путь и имя файла
     * @param text текстовое содержимое pdf-файла
     */
    void generateCustomFontAndColorPdf(@NotNull String pathAndFileName,
                                       @NotNull String text);

    /**
     * Генерация pdf-файла с пользовательским размером таблицы
     * @param pathAndFileName путь и имя файла
     * @param text текстовое содержимое pdf-файла
     */
    void generateTextWithTablePdf(String pathAndFileName, String text);

}
