package oleksandrdiachenko.pricechecker.util.template;

import java.util.Map;

public abstract class AbstractTemplateResolver implements TemplateResolver {

    @Override
    public String resolve(String template, Map<String, Object> parameters) {
        String result = template;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry.getValue() instanceof Number) {
                result = result.replace("\"{{" + entry.getKey() + "}}\"", String.valueOf(entry.getValue()));
            }
            result = result.replace("{{" + entry.getKey() + "}}", String.valueOf(entry.getValue()));
        }
        return result;
    }
}
