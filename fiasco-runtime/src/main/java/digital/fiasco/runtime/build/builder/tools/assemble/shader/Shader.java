package digital.fiasco.runtime.build.builder.tools.assemble.shader;

import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

public class Shader extends BaseTool<Shader, Void>
{
    public Shader(Builder builder)
    {
        super(builder);
    }

    public Shader(Shader that)
    {
        super(that);
    }

    @Override
    public Shader copy()
    {
        return new Shader(this);
    }

    @Override
    public String description()
    {
        return null;
    }

    @Override
    public Void onRun()
    {
        return null;
    }
}
