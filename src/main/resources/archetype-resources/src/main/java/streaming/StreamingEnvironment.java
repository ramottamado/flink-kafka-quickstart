package ${package}.streaming;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;

/**
 * The {@code StreamingEnvironment} provides an interface to define streams and methods to read them
 * and provides method to create streaming environment.
 */
public interface StreamingEnvironment {
  /**
   * Method to initialize Flink stream execution environment.
   *
   * @return Flink {@link StreamExecutionEnvironment}
   */
  StreamExecutionEnvironment createExecutionEnvironment();

  /**
   * Method to create streaming pipeline.
   *
   * @return Flink {@link StreamExecutionEnvironment}
   */
  StreamExecutionEnvironment createPipeline();

  /**
   * Method to write stream to sink.
   *
   * @param <T>    type of stream
   * @param stream stream to write to sink
   * @return the {@link DataStreamSink} for stream
   */
  <T> DataStreamSink<T> writeStream(DataStream<T> stream);

  /**
   * Method to provide way to read stream from Kafka, deserialized into POJO type according to
   * message schema.
   *
   * @param <T>               Kafka stream POJO type
   * @param env               Flink {@link StreamExecutionEnvironment}
   * @param topic             Kafka topic for the stream
   * @param valueDeserializer the {@link KafkaDeserializationSchema} for the POJO
   * @return the {@link DataStream} of the Kafka stream
   */
   <T> DataStreamSource<T> readStream(
      StreamExecutionEnvironment env,
      String topic,
      KafkaDeserializationSchema<T> valueDeserializer);
}
