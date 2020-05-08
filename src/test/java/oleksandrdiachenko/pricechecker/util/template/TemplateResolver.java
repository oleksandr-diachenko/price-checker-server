package oleksandrdiachenko.pricechecker.util.template;

import java.util.Map;

public interface TemplateResolver {

    String resolve(String template, Map<String, Object> parameters);
}
