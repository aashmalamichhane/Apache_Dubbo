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
package org.apache.dubbo.config.spring.beans.factory.config;

import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.BeanMetadataElement;

/**
 * Configurable the {@link BeanMetadataAttributeAccessor#setSource(Object) source} for {@link BeanMetadataElement}
 *
 * @since 2.7.5
 */
public interface ConfigurableSourceBeanMetadataElement {

    /**
     * Set the source into the specified {@link BeanMetadataElement}
     *
     * @param beanMetadataElement {@link BeanMetadataElement} instance
     */
    default void setSource(BeanMetadataElement beanMetadataElement) {
        if (beanMetadataElement instanceof BeanMetadataAttributeAccessor) {
            ((BeanMetadataAttributeAccessor) beanMetadataElement).setSource(this);
        }
    }
}
