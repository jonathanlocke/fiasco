package digital.fiasco.runtime.build.settings;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.builder.Builder;

/**
 * The name of a profile which can be used to enable or disable tools from the command line. For details, see
 * {@link Builder}.
 *
 * @author Jonathan Locke
 * @see Build
 * @see Builder
 */
public record BuildProfile(String name)
{
}
