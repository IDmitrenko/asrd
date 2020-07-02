package com.kropotov.asrd.services.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kropotov.asrd.exceptions.PdfWriteException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@Slf4j
@Api(value = "PdfService", description = "Набор методов для работы с pdf-файлами" )
public class PdfServiceImpl implements PdfService {

    @Override
    @ApiOperation(value = "Генерация простого pdf-файла с текстом", response = String.class)
    public void generatePlainPdf(String pathAndFileName, String text) {
        writeFile(new Document(), pathAndFileName, text);
    }

    @Override
    @ApiOperation(value = "Генерация pdf-файла с текстом и пользовательскими параметрами страницы", response = String.class)
    public void generateCustomPageSizePdf(String pathAndFileName, String text) {
        Rectangle rectangle = new Rectangle(216f, 720f);
        Document document = new Document(rectangle, 36f, 7f, 108f, 180f);
        writeFile(document, pathAndFileName, text);
    }

    @Override
    @ApiOperation(value = "Генерация простого pdf-файла с текстом и пользовательскими параметрами шрифта", response = String.class)
    public void generateCustomFontAndColorPdf(String pathAndFileName, String text) {
        final Document document = new Document();
        try (FileOutputStream stream = new FileOutputStream((pathAndFileName))) {
            PdfWriter.getInstance(document, stream);
        } catch (DocumentException | IOException ex) {
            log.error("Ошибка получения экземпляра документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка получения экземпляра документа " + pathAndFileName, ex);
        }
        document.open();
        try {
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            document.add(new Paragraph(text, font));

            Font font2 = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.RED);
            Chunk chunk = new Chunk(text, font2);
            document.add(chunk);
        } catch (DocumentException ex) {
            log.error("Ошибка при модификации документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка при модификации документа " + pathAndFileName, ex);
        }
        document.close();
    }

    @Override
    @ApiOperation(value = "Генерация простого pdf-файла с изображением и текстом под ним", response = String.class)
    public void generateTextWithImagePdf(String imageFileName, String pathAndFileName, String text) {
        Path path = null;
        try {
            path = Paths.get(new ClassPathResource(imageFileName).getURI());
        } catch (IOException ex) {
            log.error("Ошибка обработки файла изображения {}.", imageFileName);
            throw new PdfWriteException("Ошибка обработки файла изображения " + imageFileName, ex);

        }
        final Document document = new Document();
        try (FileOutputStream stream = new FileOutputStream((pathAndFileName))) {
            PdfWriter.getInstance(document, stream);
        } catch (DocumentException | IOException ex) {
            log.error("Ошибка получения экземпляра документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка получения экземпляра документа " + pathAndFileName, ex);
        }
        document.open();
        Image img = null;
        try {
            img = Image.getInstance(path.toAbsolutePath().toString());
        } catch (BadElementException | IOException ex) {
            log.error("Ошибка при обработке вставки изображения.");
            throw new PdfWriteException("Ошибка при обработке вставки изображения " + path.toAbsolutePath().toString(), ex);
        }
        try {
            document.add(img);
            document.add(new Paragraph(text));
        } catch (DocumentException ex) {
            log.error("Ошибка при модификации документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка при модификации документа " + pathAndFileName, ex);
        }
        document.close();
    }

    @Override
    @ApiOperation(value = "Генерация простого pdf-файла с таблицей и текстом под ней", response = String.class)
    public void generateTextWithTablePdf(String pathAndFileName, String text) {
        final Document document = new Document();
        try (FileOutputStream stream = new FileOutputStream((pathAndFileName))) {
            PdfWriter.getInstance(document, stream);
        } catch (DocumentException | IOException ex) {
            log.error("Ошибка получения экземпляра документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка получения экземпляра документа " + pathAndFileName, ex);
        }
        document.open();
        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table);
        try {
            addCustomRows(table);
        } catch (URISyntaxException | BadElementException | IOException ex) {
            log.error("Ошибка при создании пользовательских строк.");
            throw new PdfWriteException("Ошибка при создании пользовательских строк документа " + pathAndFileName, ex);
        }
        try {
            document.add(table);
            document.add(new Paragraph(text));
        } catch (DocumentException ex) {
            log.error("Ошибка при модификации документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка при модификации документа " + pathAndFileName, ex);
        }
        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Header 1", "Header 2", "Header 3")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("labelstatus.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }

    private void writeFile(Document document, String pathAndFileName, String text) {
        try (FileOutputStream stream = new FileOutputStream((pathAndFileName))) {
            PdfWriter.getInstance(document, stream);
        } catch (DocumentException | IOException ex) {
            log.error("Ошибка получения экземпляра документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка получения экземпляра документа " + pathAndFileName, ex);
        }
        document.open();
        try {
            document.add(new Paragraph(text));
        } catch (DocumentException ex) {
            log.error("Ошибка при модификации документа {}.", pathAndFileName);
            throw new PdfWriteException("Ошибка при модификации документа " + pathAndFileName, ex);
        }
        document.close();
    }
}
