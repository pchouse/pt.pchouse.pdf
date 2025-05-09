package pt.pchouse.pdf.api.sign.multicert;

import com.google.gson.Gson;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public class ResponseError
{
      private String  errorCode;
      private String  errorCodeDescription;
      private String  causeMessage;

      public ResponseError(){}

      public String getErrorCode() {
            return errorCode;
      }

      public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
      }

      public String getErrorCodeDescription() {
            return errorCodeDescription;
      }

      public void setErrorCodeDescription(String errorCodeDescription) {
            this.errorCodeDescription = errorCodeDescription;
      }

      public String getCauseMessage() {
            return causeMessage;
      }

      public void setCauseMessage(String causeMessage) {
            this.causeMessage = causeMessage;
      }

      public static ResponseError Parse(String json)
      {
            try {
                  return new Gson().fromJson(json, ResponseError.class);
            }catch (Exception e){
                    return null;
            }
      }
}
