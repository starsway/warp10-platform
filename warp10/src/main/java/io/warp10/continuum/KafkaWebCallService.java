//
//   Copyright 2016  Cityzen Data
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.continuum;

import io.warp10.WarpConfig;
import io.warp10.crypto.CryptoUtils;
import io.warp10.crypto.KeyStore;
import io.warp10.script.thrift.data.WebCallRequest;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;

import com.google.common.base.Preconditions;

public class KafkaWebCallService {  

  public static final String WEBCALL_KAFKA_ZKCONNECT = "webcall.kafka.zkconnect";
  public static final String WEBCALL_KAFKA_BROKERLIST = "webcall.kafka.brokerlist";
  public static final String WEBCALL_KAFKA_TOPIC = "webcall.kafka.topic";
  public static final String WEBCALL_KAFKA_AES = "webcall.kafka.aes";
  public static final String WEBCALL_KAFKA_MAC = "webcall.kafka.mac";
  
  private static boolean initialized = false;
  
  private static Producer<byte[],byte[]> producer;
  
  private static String topic;
  
  private static byte[] aesKey = null;
      
  private static byte[] siphashKey = null;
  
  public static synchronized boolean offer(WebCallRequest request) {    
    try {
      //
      // Initialize service if not done yet
      //
      
      if (!initialized) {
        initialize();
      }
    
      TSerializer serializer = new TSerializer(new TCompactProtocol.Factory());
      
      byte[] value = serializer.serialize(request);
      
      //
      // Wrap data if AES key is defined
      //
      
      if (null != aesKey) {
        value = CryptoUtils.wrap(aesKey, value);               
      }
      
      //
      // Compute MAC if the SipHash key is defined
      //
      
      if (null != siphashKey) {
        value = CryptoUtils.addMAC(siphashKey, value);
      }

      KeyedMessage<byte[], byte[]> message = new KeyedMessage<byte[], byte[]>(topic, value);
      
      producer.send(message);
      
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  
  /**
   * Extract the required keys if they exist
   * 
   * @param keystore
   */
  public static void initKeys(KeyStore keystore, Properties props) {
    if (props.containsKey(WEBCALL_KAFKA_AES)) {
      byte[] key = keystore.decodeKey(props.getProperty(WEBCALL_KAFKA_AES));
      Preconditions.checkArgument((16 == key.length || 24 == key.length || 32 == key.length), WEBCALL_KAFKA_AES + " MUST be 128, 192 or 256 bits long.");
      keystore.setKey(KeyStore.AES_KAFKA_WEBCALL, key);
      aesKey = key;
    }
    if (props.containsKey(WEBCALL_KAFKA_MAC)) {
      byte[] key = keystore.decodeKey(props.getProperty(WEBCALL_KAFKA_MAC));
      Preconditions.checkArgument((16 == key.length), WEBCALL_KAFKA_MAC + " MUST be 128 bits long.");
      keystore.setKey(KeyStore.SIPHASH_KAFKA_WEBCALL, key);
      siphashKey = key;
    }    
  }
  
  private static void initialize() {
    Properties props = WarpConfig.getProperties();
    
    if (null == props.getProperty(WEBCALL_KAFKA_ZKCONNECT)) {
      throw new RuntimeException(WEBCALL_KAFKA_ZKCONNECT + " was not specified in the configuration.");
    }

    if (null == props.getProperty(WEBCALL_KAFKA_BROKERLIST)) {
      throw new RuntimeException(WEBCALL_KAFKA_BROKERLIST + " was not specified in the configuration.");
    }

    if (null == props.getProperty(WEBCALL_KAFKA_TOPIC)) {
      throw new RuntimeException(WEBCALL_KAFKA_TOPIC + " was not specified in the configuration.");
    }

    Properties properties = new Properties();
    // @see http://kafka.apache.org/documentation.html#producerconfigs
    properties.setProperty("zookeeper.connect", props.getProperty(WEBCALL_KAFKA_ZKCONNECT));
    properties.setProperty("metadata.broker.list", props.getProperty(WEBCALL_KAFKA_BROKERLIST));
    properties.setProperty("request.required.acks", "-1");
    properties.setProperty("producer.type","sync");
    properties.setProperty("serializer.class", "kafka.serializer.DefaultEncoder");
    properties.setProperty("partitioner.class", io.warp10.continuum.KafkaPartitioner.class.getName());
    
    ProducerConfig config = new ProducerConfig(properties);
    producer = new Producer<byte[], byte[]>(config);

    topic = props.getProperty(WEBCALL_KAFKA_TOPIC);
    
    initialized = true;
  }  
}
