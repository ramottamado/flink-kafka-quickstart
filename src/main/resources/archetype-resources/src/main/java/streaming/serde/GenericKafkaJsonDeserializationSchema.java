package ${package}.streaming.serde;

import java.io.IOException;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Describes how to deserialize byte messages from Kafka into POJO.
 *
 * @param <T> type of POJO the message deserialized into
 */
public class GenericKafkaJsonDeserializationSchema<T> implements KafkaDeserializationSchema<T> {
  private static final long serialVersionUID = 2L;
  private static final Logger LOGGER = LoggerFactory.getLogger(
      GenericKafkaJsonDeserializationSchema.class);

  private final ObjectMapper mapper = new ObjectMapper();
  private final Class<T> type;

  /**
   * Construct new {@link GenericKafkaJsonDeserializationSchema}.
   *
   * @param type class of POJO the message deserialized into
   */
  public GenericKafkaJsonDeserializationSchema(final Class<T> type) {
    this.type = type;
  }

  @Override
  public TypeInformation<T> getProducedType() {
    return TypeInformation.of(type);
  }

  @Override
  public boolean isEndOfStream(final T nextElement) {
    return false;
  }

  @Override
  public T deserialize(final ConsumerRecord<byte[], byte[]> record) throws IOException {
    final ObjectNode node = mapper.createObjectNode();

    try {
      if (record != null) {
        node.set("value", mapper.readValue(record.value(), JsonNode.class));
      }

      final JsonNode dataNode = node.get("value");

      return mapper.treeToValue(dataNode, type);
    } catch (final JsonProcessingException e) {
      LOGGER.error(
          "Cannot process incoming record. Check the POJO or content of the record.", e);

      throw new IOException("Error process incoming record");
    }
  }
}
