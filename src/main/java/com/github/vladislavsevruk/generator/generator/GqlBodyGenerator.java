package com.github.vladislavsevruk.generator.generator;

import com.github.vladislavsevruk.generator.param.GqlParameterValue;
import com.github.vladislavsevruk.generator.strategy.input.type.InputTypePickingStrategy;
import com.github.vladislavsevruk.generator.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class body generator with common logic for GraphQL operation.
 */
public class GqlBodyGenerator {

    protected String wrapForRequestBody(String operationBody) {
        return "{\"query\":\"" + StringUtil.escapeQuotes(operationBody) + "\"}";
    }

    protected String wrapForRequestBodyWithInputType(String mutationBody, String variables) {
        return "{\"variables\":" + variables + ",\"query\":\"" + StringUtil.escapeQuotes(mutationBody) + "\"}";
    }

    protected String getSignature(InputTypePickingStrategy inputTypePickingStrategy,
                                Iterable<? extends GqlParameterValue<?>> arguments) {
        String signature = StreamSupport.stream(arguments.spliterator(), false)
                .map(argument -> generateSignature(inputTypePickingStrategy, argument))
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(","));
        return StringUtils.isEmpty(signature) ? "" : "(" + signature + ")";
    }

    protected String generateSignature(InputTypePickingStrategy inputTypePickingStrategy, GqlParameterValue<?> argument) {
        if (argument.getValue() != null && inputTypePickingStrategy.getInputType(argument) != null) {
            return "$" + argument.getName() + ":" + inputTypePickingStrategy.getInputType(argument) + "!";
        } else {
            return "";
        }
    }

    protected String generateGqlArgumentsWithInputType(Iterable<? extends GqlParameterValue<?>> arguments) {
        return "(" + StreamSupport.stream(arguments.spliterator(), false)
                .map(this::generateArgumentMethodValue)
                .collect(Collectors.joining(",")) + ")";
    }

    protected String generateArgumentMethodValue(GqlParameterValue<?> argument) {
        return argument.getName() + ":$" + argument.getName();
    }
}
