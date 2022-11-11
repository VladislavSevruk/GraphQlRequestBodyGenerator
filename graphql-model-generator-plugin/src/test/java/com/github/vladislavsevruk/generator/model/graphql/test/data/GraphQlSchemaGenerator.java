/*
 * MIT License
 *
 * Copyright (c) 2022 Uladzislau Seuruk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.vladislavsevruk.generator.model.graphql.test.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQlSchemaGenerator {

    private static final String COMPLEX_ENUM_TYPE_NAME = "ComplexEnumType";
    private static final String COMPLEX_INPUT_NAME = "ComplexInput";
    private static final String COMPLEX_TYPE_NAME = "ComplexType";
    private static final String MULTI_UNION_NAME = "multi_union";
    private static final String SIMPLE_ENUM_TYPE_NAME = "simple_enum_type";
    private static final String SIMPLE_INPUT_NAME = "simple_input";
    private static final String SIMPLE_TYPE_NAME = "simple_type";
    private static final String SINGLE_UNION_NAME = "SingleUnion";
    private static final Map<String, String> TYPES_MAP = initTypesMap();
    private final List<String> inputsUsed = new ArrayList<>();
    private final StringBuilder typesBuilder = new StringBuilder();
    private final List<String> typesUsed = new ArrayList<>();

    public static String getComplexEnumType() {
        return getType(COMPLEX_ENUM_TYPE_NAME);
    }

    public static String getComplexInput() {
        return getType(COMPLEX_INPUT_NAME);
    }

    public static String getComplexType() {
        return getType(COMPLEX_TYPE_NAME);
    }

    public static String getMultiUnion() {
        return getType(MULTI_UNION_NAME);
    }

    public static String getSimpleEnumType() {
        return getType(SIMPLE_ENUM_TYPE_NAME);
    }

    public static String getSimpleInput() {
        return getType(SIMPLE_INPUT_NAME);
    }

    public static String getSimpleType() {
        return getType(SIMPLE_TYPE_NAME);
    }

    public static String getSingleUnion() {
        return getType(SINGLE_UNION_NAME);
    }

    public GraphQlSchemaGenerator addComplexEnum() {
        return addType(COMPLEX_ENUM_TYPE_NAME);
    }

    public GraphQlSchemaGenerator addComplexInput() {
        return addInput(COMPLEX_INPUT_NAME).addSimpleInput().addComplexEnum();
    }

    public GraphQlSchemaGenerator addComplexType() {
        return addType(COMPLEX_TYPE_NAME).addSimpleType().addSimpleEnum().addSingleUnion();
    }

    public GraphQlSchemaGenerator addMultiUnion() {
        return addType(MULTI_UNION_NAME).addComplexType();
    }

    public GraphQlSchemaGenerator addSimpleEnum() {
        return addType(SIMPLE_ENUM_TYPE_NAME);
    }

    public GraphQlSchemaGenerator addSimpleInput() {
        return addInput(SIMPLE_INPUT_NAME).addSimpleType();
    }

    public GraphQlSchemaGenerator addSimpleType() {
        return addType(SIMPLE_TYPE_NAME);
    }

    public GraphQlSchemaGenerator addSingleUnion() {
        return addType(SINGLE_UNION_NAME).addSimpleType();
    }

    public String build() {
        StringBuilder builder = new StringBuilder("\n");
        if (!typesUsed.isEmpty()) {
            builder.append("type Query {\n");
            for (int i = 0; i < typesUsed.size(); ++i) {
                builder.append("  testQuery").append(i).append("(id: ID!): ").append(typesUsed.get(i)).append("\n");
            }
            builder.append("}\n\n");
        }
        if (!inputsUsed.isEmpty()) {
            builder.append("type Mutation {\n");
            for (int i = 0; i < inputsUsed.size(); ++i) {
                builder.append("  testMutation").append(i).append("(input: ").append(inputsUsed.get(i)).append("!): ")
                        .append(SIMPLE_TYPE_NAME).append("!").append("\n");
            }
            builder.append("}\n\n");
        }
        return builder.append(typesBuilder).toString();
    }

    private static String getType(String typeName) {
        return TYPES_MAP.get(typeName).replaceAll("\\n\\s*", "\n").replaceAll("#[^\\n]*\\n", "\n")
                .replaceAll("\\s*@\\s*directive(\\s*\\([^)]*\\))?", "").replaceAll("\\s*?\\n", "\n");
    }

    private static Map<String, String> initTypesMap() {
        Map<String, String> typesMap = new HashMap<>();
        typesMap.put(SIMPLE_TYPE_NAME, "type " + SIMPLE_TYPE_NAME
                + "{\n  id:ID!\n  values : [ String ! ] ! ,values_2:[Int] # comment\nval_ue3: Boolean!,#comment\n  value4: Float}");
        typesMap.put(SIMPLE_INPUT_NAME, "input " + SIMPLE_INPUT_NAME
                + "{\n  id:ID!\n  values : [ String ! ] ! ,values_2:[Int] # comment\nval_ue3: Boolean!,#comment\n  value4: Float}");
        typesMap.put(COMPLEX_TYPE_NAME,
                "type " + COMPLEX_TYPE_NAME + "{\n  value:" + SIMPLE_TYPE_NAME + " @directive # comment\n  values : [ "
                        + SIMPLE_TYPE_NAME + " ! ] ! ,values2:[" + SIMPLE_TYPE_NAME
                        + "]@directive(param: true)#comment\nvalue3: " + SIMPLE_ENUM_TYPE_NAME + "\n value4:"
                        + SINGLE_UNION_NAME + "!\n  value5 : [" + SINGLE_UNION_NAME + "]}");
        typesMap.put(COMPLEX_INPUT_NAME, "input " + COMPLEX_INPUT_NAME + "{\n  value:" + SIMPLE_INPUT_NAME
                + " @directive # comment\n  values : [ " + SIMPLE_INPUT_NAME + " ! ] ! ,values2:[" + SIMPLE_INPUT_NAME
                + "]@directive(param:true)#comment\nvalue3: " + COMPLEX_ENUM_TYPE_NAME + "}");
        typesMap.put(SIMPLE_ENUM_TYPE_NAME,
                "enum " + SIMPLE_ENUM_TYPE_NAME + "{\n  VALUE1\n  VALUE2,value3\nVALUE_4,\n  VALUE5}");
        typesMap.put(COMPLEX_ENUM_TYPE_NAME, "enum " + COMPLEX_ENUM_TYPE_NAME
                + "@directive(param:false){\n  VALUE1@directive()#comment\n  VALUE2,value3 @directive # comment\nVALUE_4,\n  VALUE5}");
        typesMap.put(SINGLE_UNION_NAME, "union " + SINGLE_UNION_NAME + " = " + SIMPLE_TYPE_NAME);
        typesMap.put(MULTI_UNION_NAME,
                "union " + MULTI_UNION_NAME + " @ directive( param1: \"value\" , param2 : true ) = " + SIMPLE_TYPE_NAME
                        + "\n|" + COMPLEX_TYPE_NAME);
        return typesMap;
    }

    private GraphQlSchemaGenerator addInput(String typeName) {
        if (inputsUsed.contains(typeName)) {
            return this;
        }
        inputsUsed.add(typeName);
        return appendType(typeName);
    }

    private GraphQlSchemaGenerator addType(String typeName) {
        if (typesUsed.contains(typeName)) {
            return this;
        }
        typesUsed.add(typeName);
        return appendType(typeName);
    }

    private GraphQlSchemaGenerator appendType(String typeName) {
        typesBuilder.append(TYPES_MAP.get(typeName)).append("\n\n");
        return this;
    }
}
