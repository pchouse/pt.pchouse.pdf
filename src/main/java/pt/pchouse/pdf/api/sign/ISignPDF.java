package pt.pchouse.pdf.api.sign;

import org.springframework.context.ApplicationContext;

import java.util.concurrent.CompletableFuture;

public interface ISignPDF {

    String signPDF(String pdfBase64, ApplicationContext appContext) throws Exception;

    CompletableFuture<String> signPDFAsync(String pdfBase64, ApplicationContext appContext);

}
