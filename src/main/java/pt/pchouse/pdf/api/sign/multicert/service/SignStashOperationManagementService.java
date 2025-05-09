package pt.pchouse.pdf.api.sign.multicert.service;


import retrofit2.Call;
import retrofit2.http.*;

import okhttp3.ResponseBody;

import pt.pchouse.pdf.api.sign.multicert.model.BaseOperation;
import pt.pchouse.pdf.api.sign.multicert.model.SignOperation;

import java.util.List;

/**
 * The SignStashOperationManagementService interface provides methods for managing signing operations
 * through various endpoints. It supports fetching operation details, querying the status of operations,
 * retrieving document content, and resending delivery emails.
 *
 * @since 1.0.0
 */
public interface SignStashOperationManagementService
{
  /**
   * Request the id of an operation with the document external request used by the client service.
   * Request the id of an operation with the document external request used by the client service.  This operation is particularly useful in case of a signDocument infrastructure failure (e.g. Timeout, Connection errors) where is not clear if the server has processed the request has been not able to retrieve the response to the client. With this operation and the getStatus, the client service can be sure if the request is processed or if it needs to be resent.
   * @param externalReference External reference to be queried (required)
   * @return Call&lt;Long&gt;
   */
  @GET("api/v0/operation/id/withDocumentExternalReference/{externalReference}")
  Call<Long> getOperationIdByDocumentExternalReference(
    @retrofit2.http.Path("externalReference") String externalReference
  );

  /**
   * Request the detail of a sign operation.
   * Request the detail of a sign operation. All operation detail with related document content will be retrieved.
   * @param operationId Operation id to be queried (required)
   * @param includeContent States if the files content should be returned (in base64 format) in the response. For better performance should be set as false (no documents content in response), and using the document detail APIs to retrieve the contents.\&quot;) (required)
   * @return Call&lt;SignOperation&gt;
   */
  @GET("api/v0/operation/sign/detail/{operationId}/contentInResponse/{includeContent}")
  Call<SignOperation> getSignOperationDetail(
    @retrofit2.http.Path("operationId") Long operationId, @retrofit2.http.Path("includeContent") Boolean includeContent
  );

  /**
   * Request the content of the documents related to a sign operation.
   * Request the content of the documents related to a sign operation. All documents will be retrieved, packed into a zip file and returned.Zip file will include the name of the documents according to the external reference supplied upon the signature request respecting the format: &lt;externalRef&gt;.&lt;documentName&gt;
   * @param operationId Operation id for which the documents content will be retrieved (required)
   * @return Call&lt;File&gt;
   */
  @GET("api/v0/operation/sign/detail/{operationId}/documents")
  Call<ResponseBody> getSignOperationDocumentsContent(
    @retrofit2.http.Path("operationId") Long operationId
  );

  /**
   * Request the status of an operation.
   * 
   * @param operationId Operation id to be queried (required)
   * @return Call&lt;BaseOperation&gt;
   */
  @GET("api/v0/operation/status/{operationId}")
  Call<BaseOperation> getStatus(
    @retrofit2.http.Path("operationId") Long operationId
  );

  /**
   * Request the delivery email re-send for a given operation id and collection of person receiver emails.
   * 
   * @param body  (required)
   * @param operationId The operation ID. (required)
   * @param attachDocuments Allows you to control whether the documents should be attached to the email or not. (required)
   * @return Call&lt;Void&gt;
   */
  @SuppressWarnings("rawtypes")
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/v0/delivery/mail/resend/{operationId}/{attachDocuments}")
  Call<Void> resendDeliveryMails(
    @retrofit2.http.Body List body, @retrofit2.http.Path("operationId") Long operationId, @retrofit2.http.Path("attachDocuments") Boolean attachDocuments
  );

}
