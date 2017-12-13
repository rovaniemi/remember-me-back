package remember.configuration;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private final static String API_BASE_PATH = "api/v01";

    @Bean
    public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrationsAdapter() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {
                    @Override
                    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mappingInfo) {
                        Class<?> beanType = method.getDeclaringClass();
                        if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
                            PatternsRequestCondition apiPattern = new PatternsRequestCondition(API_BASE_PATH)
                                    .combine(mappingInfo.getPatternsCondition());

                            mappingInfo = new RequestMappingInfo(mappingInfo.getName(), apiPattern,
                                    mappingInfo.getMethodsCondition(), mappingInfo.getParamsCondition(),
                                    mappingInfo.getHeadersCondition(), mappingInfo.getConsumesCondition(),
                                    mappingInfo.getProducesCondition(), mappingInfo.getCustomCondition());
                        }

                        super.registerHandlerMethod(handler, method, mappingInfo);
                    }
                };
            }
        };
    }
}