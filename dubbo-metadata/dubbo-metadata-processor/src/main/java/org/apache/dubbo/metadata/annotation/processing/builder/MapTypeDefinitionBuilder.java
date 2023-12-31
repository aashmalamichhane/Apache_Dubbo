/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.metadata.annotation.processing.builder;

import org.apache.dubbo.metadata.definition.model.TypeDefinition;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import java.util.Map;
import java.util.Objects;

/**
 * {@link TypeBuilder} for Java {@link Map}
 *
 * @since 2.7.6
 */
public class MapTypeDefinitionBuilder implements DeclaredTypeDefinitionBuilder {

    @Override
    public boolean accept(ProcessingEnvironment processingEnv, DeclaredType type) {
        Elements elements = processingEnv.getElementUtils();
        TypeElement mapTypeElement = elements.getTypeElement(Map.class.getTypeName());
        TypeMirror mapType = mapTypeElement.asType();
        Types types = processingEnv.getTypeUtils();
        TypeMirror erasedType = types.erasure(type);
        return types.isAssignable(erasedType, mapType);
    }

    @Override
    public TypeDefinition build(
            ProcessingEnvironment processingEnv, DeclaredType type, Map<String, TypeDefinition> typeCache) {
        TypeDefinition typeDefinition = new TypeDefinition(type.toString());
        // Generic Type arguments
        type.getTypeArguments().stream()
                .map(typeArgument -> TypeDefinitionBuilder.build(
                        processingEnv, typeArgument, typeCache)) // build the TypeDefinition from typeArgument
                .filter(Objects::nonNull)
                .map(TypeDefinition::getType)
                .forEach(typeDefinition.getItems()::add); // Add into the declared TypeDefinition
        return typeDefinition;
    }

    @Override
    public int getPriority() {
        return MIN_PRIORITY - 6;
    }
}
