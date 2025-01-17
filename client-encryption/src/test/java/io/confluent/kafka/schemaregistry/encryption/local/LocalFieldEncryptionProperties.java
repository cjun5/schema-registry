/*
 * Copyright 2023 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.confluent.kafka.schemaregistry.encryption.local;

import static io.confluent.kafka.schemaregistry.encryption.local.LocalFieldEncryptionExecutor.LOCAL_OLD_SECRETS;
import static io.confluent.kafka.schemaregistry.encryption.local.LocalFieldEncryptionExecutor.LOCAL_SECRET;

import io.confluent.kafka.schemaregistry.encryption.FieldEncryptionProperties;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalFieldEncryptionProperties extends FieldEncryptionProperties {

  public LocalFieldEncryptionProperties(List<String> ruleNames) {
    super(ruleNames);
  }

  @Override
  public String getKeyId() {
    return "";
  }

  @Override
  public Map<String, Object> getClientPropertiesWithoutKey()
      throws Exception {
    List<String> ruleNames = getRuleNames();
    Map<String, Object> props = new HashMap<>();
    props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "mock://");
    props.put(AbstractKafkaSchemaSerDeConfig.AUTO_REGISTER_SCHEMAS, "false");
    props.put(AbstractKafkaSchemaSerDeConfig.USE_LATEST_VERSION, "true");
    props.put(AbstractKafkaSchemaSerDeConfig.LATEST_CACHE_TTL, "60");
    props.put(AbstractKafkaSchemaSerDeConfig.RULE_EXECUTORS, String.join(",", ruleNames));
    for (String ruleName : ruleNames) {
      props.put(AbstractKafkaSchemaSerDeConfig.RULE_EXECUTORS + "." + ruleName + ".class",
          LocalFieldEncryptionExecutor.class.getName());
      props.put(AbstractKafkaSchemaSerDeConfig.RULE_EXECUTORS + "." + ruleName
          + ".param." + LOCAL_SECRET, "mysecret");
      props.put(AbstractKafkaSchemaSerDeConfig.RULE_EXECUTORS + "." + ruleName
          + ".param." + LOCAL_OLD_SECRETS, "old1, old2");
    }
    return props;
  }
}

