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
package org.apache.dubbo.spring.boot.actuate.endpoint.metadata;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.ReferenceBean;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.stereotype.Component;

/**
 * {@link DubboReference} Metadata
 *
 * @since 2.7.0
 */
@Component
public class DubboReferencesMetadata extends AbstractDubboMetadata {

    public Map<String, Map<String, Object>> references() {

        Map<String, Map<String, Object>> referencesMetadata = new HashMap<>();

        ReferenceAnnotationBeanPostProcessor beanPostProcessor = getReferenceAnnotationBeanPostProcessor();

        referencesMetadata.putAll(buildReferencesMetadata(beanPostProcessor.getInjectedFieldReferenceBeanMap()));
        referencesMetadata.putAll(buildReferencesMetadata(beanPostProcessor.getInjectedMethodReferenceBeanMap()));

        return referencesMetadata;
    }

    private Map<String, Map<String, Object>> buildReferencesMetadata(
            Map<InjectionMetadata.InjectedElement, ReferenceBean<?>> injectedElementReferenceBeanMap) {
        Map<String, Map<String, Object>> referencesMetadata = new HashMap<>();

        for (Map.Entry<InjectionMetadata.InjectedElement, ReferenceBean<?>> entry :
                injectedElementReferenceBeanMap.entrySet()) {

            InjectionMetadata.InjectedElement injectedElement = entry.getKey();

            ReferenceBean<?> referenceBean = entry.getValue();
            ReferenceConfig referenceConfig = referenceBean.getReferenceConfig();

            Map<String, Object> beanMetadata = null;
            if (referenceConfig != null) {
                beanMetadata = resolveBeanMetadata(referenceConfig);
                // beanMetadata.put("invoker", resolveBeanMetadata(referenceBean.get()));
            } else {
                // referenceBean is not initialized
                beanMetadata = new LinkedHashMap<>();
            }

            referencesMetadata.put(String.valueOf(injectedElement.getMember()), beanMetadata);
        }

        return referencesMetadata;
    }
}
