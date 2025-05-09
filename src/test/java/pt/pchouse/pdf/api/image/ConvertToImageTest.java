package pt.pchouse.pdf.api.image;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@SpringBootTest
public class ConvertToImageTest
{

    @Autowired
    protected ApplicationContext appContext;

    @Test
    void TestConvertToImageNoSignature() throws Exception
    {
        var path = appContext.getResource("classpath:convert-img/").getFile().toPath();

        try (var list = Files.list(path)) {
            list.filter(p -> p.getFileName().toString().startsWith("page-no-signature"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        var convertToImage = appContext.getBean(ConvertToImage.class);
        var pdfBase64 = appContext.getResource("classpath:convert-img/test.pdf")
                .getInputStream()
                .readAllBytes();

        Assertions.assertNotNull(pdfBase64);

        var response = appContext.getBean(PdfToImageResponse.class);
        convertToImage.convert(response, Base64.getEncoder().encodeToString(pdfBase64), 72);

        Assertions.assertFalse(response.getImages().isEmpty());
        Assertions.assertNull(response.getSignature());

        for (int i = 0; i < response.getImages().size(); i++) {
            var imageBase64 = response.getImages().get(i);
            var imageBytes = Base64.getDecoder().decode(imageBase64);
            var imagePath = path.resolve("page-no-signature-" + i + ".png");
            Files.write(imagePath, imageBytes);
        }
    }

    @Test
    void TestConvertToImageSignature() throws Exception
    {
        var path = appContext.getResource("classpath:convert-img/").getFile().toPath();

        try (var list = Files.list(path)) {
            list.filter(p -> p.getFileName().toString().startsWith("page-signed"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        var convertToImage = appContext.getBean(ConvertToImage.class);
        var pdfBase64 = appContext.getResource("classpath:convert-img/test_signed.pdf")
                .getInputStream()
                .readAllBytes();

        Assertions.assertNotNull(pdfBase64);

        var response = appContext.getBean(PdfToImageResponse.class);
        convertToImage.convert(response, Base64.getEncoder().encodeToString(pdfBase64), 72);

        Assertions.assertFalse(response.getImages().isEmpty());
        Assertions.assertNotNull(response.getSignature());

        var signature = response.getSignature();
        Assertions.assertEquals("Lisbon", signature.getLocation());
        Assertions.assertEquals("Test API", signature.getReason());

        for (int i = 0; i < response.getImages().size(); i++) {
            var imageBase64 = response.getImages().get(i);
            var imageBytes = Base64.getDecoder().decode(imageBase64);
            var imagePath = path.resolve("page-signed-" + i + ".png");
            Files.write(imagePath, imageBytes);
        }
    }

    @Test
    void TestConvertToImageMultiPage() throws Exception
    {
        var path = appContext.getResource("classpath:convert-img/").getFile().toPath();

        try (var list = Files.list(path)) {
            list.filter(p -> p.getFileName().toString().startsWith("page-multi"))
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        var convertToImage = appContext.getBean(ConvertToImage.class);
        var pdfBase64 = appContext.getResource("classpath:convert-img/test_multi_page.pdf")
                .getInputStream()
                .readAllBytes();

        Assertions.assertNotNull(pdfBase64);

        var response = appContext.getBean(PdfToImageResponse.class);
        convertToImage.convert(response, Base64.getEncoder().encodeToString(pdfBase64), 72);

        Assertions.assertTrue(response.getImages().size() > 1);
        Assertions.assertNull(response.getSignature());

        for (int i = 0; i < response.getImages().size(); i++) {
            var imageBase64 = response.getImages().get(i);
            var imageBytes = Base64.getDecoder().decode(imageBase64);
            var imagePath = path.resolve("page-multi-" + i + ".png");
            Files.write(imagePath, imageBytes);
        }

    }


}
