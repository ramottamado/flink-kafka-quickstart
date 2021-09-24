package ${package}.streaming;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * The {@code StreamPipeline} provides method to create streaming pipeline.
 */
public abstract class StreamPipeline implements StreamingEnvironment {
  /**
   * Construct new {@link StreamingPipeline} with needed parameters.
   *
   * @param params parameters for the streaming environment
   */
  public StreamPipeline(final ParameterTool params) {
  }

  @Override
  public final StreamExecutionEnvironment createPipeline() {
    // Initialize stream execution environment
    final StreamExecutionEnvironment env = createExecutionEnvironment();

    // Do things

    return env;
  }
}
