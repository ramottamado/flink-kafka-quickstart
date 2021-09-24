package ${package}.streaming.serde;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides {@link GenericKafkaJsonSerializationSchema} for Flink to serialize POJO to Kafka JSON message.
 */
public class GenericKafkaJsonSerializationSchema<T> implements KafkaSerializationSchema<T> {
  private static final long serialVersionUID = -2L;
  private static final Logger LOGGER = LoggerFactory.getLogger(
      GenericKafkaJsonSerializationSchema.class);

  private final ObjectMapper mapper = new ObjectMapper();
  private final String topic;

  /**
   * Construct new {@link GenericKafkaJsonSerializationSchema}.
   *
   * @param topic name of Kafka topic
   */
  public GenericKafkaJsonSerializationSchema(final String topic) {
    this.topic = topic;

    mapper.enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
  }

  @Override
  public ProducerRecord<byte[], byte[]> serialize(final T element, final Long timestamp) {
    byte[] message = null;

    try {
      message = mapper.writeValueAsBytes(element);
    } catch (final JsonProcessingException e) {
      LOGGER.warn("Cannot process incoming POJO to Kafka.", e);
    }

    return new ProducerRecord<>(topic, message);
  }
}
