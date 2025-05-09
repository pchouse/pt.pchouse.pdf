package pt.pchouse.pdf.api.image;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Base64;

@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class ConvertToImage
{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Convert PDF to image, one per page
     *
     * @since 1.0.0
     */
    public ConvertToImage()
    {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Convert PDF to image, one per page
     *
     * @param response  The response object to be filled with the images
     * @param pdfBase64 The PDF file in base64 format
     * @param dpi       The DPI (dots per inch) to be used for the conversion
     * @throws IOException If an error occurs while reading the PDF file
     */
    public void convert(PdfToImageResponse response, String pdfBase64, float dpi) throws IOException {
        var pdfBytes            = Base64.getDecoder().decode(pdfBase64);
        var inputStream         = new ByteArrayInputStream(pdfBytes);
        var bufferedInputStream = new org.apache.pdfbox.io.RandomAccessReadBuffer(inputStream);
        var parser              = new PDFParser(bufferedInputStream);
        var document            = parser.parse();
        var pdf                 = new PDFRenderer(document);

        for (var n = 0; n < document.getNumberOfPages(); n++) {
            var image                 = pdf.renderImageWithDPI(n, dpi, ImageType.RGB);
            var byteArrayOutputStream = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "png", byteArrayOutputStream);
            var imageBytes  = byteArrayOutputStream.toByteArray();
            var imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            response.getImages().add(imageBase64);
        }

        var signature = document.getLastSignatureDictionary();

        if (signature != null) {
            var pdfSignature = new PdfSignature();
            pdfSignature.setName(signature.getName());
            pdfSignature.setLocation(signature.getLocation());
            pdfSignature.setReason(signature.getReason());
            pdfSignature.setContactInfo(signature.getContactInfo());
            pdfSignature.setSignDate(signature.getSignDate());
            response.setSignature(pdfSignature);
        }
    }

}
