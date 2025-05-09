package pt.pchouse.pdf.api.sign.multicert.service;

import retrofit2.Call;
import retrofit2.http.*;

import pt.pchouse.pdf.api.sign.multicert.model.DocumentSignBody;
import java.io.File;
import pt.pchouse.pdf.api.sign.multicert.model.SignOperation;
import pt.pchouse.pdf.api.sign.multicert.model.SignRequestBase64Document;
import pt.pchouse.pdf.api.sign.multicert.model.SignRequestDocument;

import java.util.List;

/**
 * Interface for interacting with the SignStash document signing service.
 * It provides methods for submitting document signing requests using both
 * base64 and binary content approaches.
 *
 * @since 1.0.0
 */
public interface SignStashDocumentSignService
{

  /**
   * Request the digital signing on a set of documents using base64 content (simple integration).
   * 
   * @param body  (required)
   * @return Call&lt;SignOperation&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v0/document/sign/base64/")
  Call<SignOperation> signDocumentBatchBase64(
    @retrofit2.http.Body SignRequestBase64Document body
  );

  /**
   * Request the digital signing on a set of documents using binary content (complex integration).
   * 
   * @param body  (required)
   * @return Call&lt;SignOperation&gt;
   */
  @retrofit2.http.Multipart
  @POST("api/v0/document/sign/")
  Call<SignOperation> signDocumentBatchBinary(
    @retrofit2.http.Body DocumentSignBody body
  );

  /**
   * Request the digital signing on a set of documents using binary content (complex integration).
   * 
   * @param files  (required)
   * @param signRequest  (required)
   * @return Call&lt;SignOperation&gt;
   */
  @retrofit2.http.Multipart
  @POST("api/v0/document/sign/")
  Call<SignOperation> signDocumentBatchBinary(
    @retrofit2.http.Part("files") List<File> files, @retrofit2.http.Part("signRequest") SignRequestDocument signRequest
  );

}
