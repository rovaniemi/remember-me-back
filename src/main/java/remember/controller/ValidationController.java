package remember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import remember.errorhandling.ErrorMessage;
import remember.errorhandling.ErrorMessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ValidationController {

    @Autowired
    private MessageSource msgSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorMessage> processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> error = result.getFieldErrors();
        return processFieldError(error);
    }

    private List<ErrorMessage> processFieldError(List<FieldError> error) {
        List<ErrorMessage> messages = new ArrayList<>();
        if (error != null) {
            for (int i = 0; i < error.size(); i++) {
                Locale currentLocale = LocaleContextHolder.getLocale();
                String msg = msgSource.getMessage(error.get(i).getDefaultMessage(), null, currentLocale);
                messages.add(new ErrorMessage(ErrorMessageType.ERROR, msg, error.get(i).getField()));
            }
        }
        return messages;
    }
}
