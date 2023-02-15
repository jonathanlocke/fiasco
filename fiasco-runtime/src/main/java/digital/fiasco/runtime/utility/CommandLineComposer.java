package digital.fiasco.runtime.utility;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.interfaces.object.Copyable;

import java.util.Collection;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;

public class CommandLineComposer implements Copyable<CommandLineComposer>
{
    private final StringList arguments;

    public CommandLineComposer(CommandLineComposer that)
    {
        this.arguments = that.arguments.copy();
    }

    public CommandLineComposer()
    {
        this.arguments = stringList();
    }

    public StringList asStringList()
    {
        return arguments;
    }

    @Override
    public CommandLineComposer copy()
    {
        return new CommandLineComposer(this);
    }

    public CommandLineComposer withArgument(Object object)
    {
        return withArgument(object, ",");
    }

    public CommandLineComposer withArgument(Object object, String separator)
    {
        var strings = toStringList(object);
        if (strings != null)
        {
            return mutated(it -> it.arguments.add(strings.join(separator)));
        }
        return this;
    }

    public CommandLineComposer withArguments(Collection<?> object)
    {
        return withArgument(object, ",");
    }

    public CommandLineComposer withBooleanFlag(String name)
    {
        return withArgument(name);
    }

    public CommandLineComposer withOneArgumentSwitch(String name, Object object)
    {
        return withOneArgumentSwitch(name, object, ",");
    }

    public CommandLineComposer withOneArgumentSwitch(String name, Object object, String separator)
    {
        return withOneArgumentSwitch(name, object, separator, "=");
    }

    public CommandLineComposer withOneArgumentSwitch(String name, Object object, String equals, String separator)
    {
        var strings = toStringList(object);
        if (strings != null)
        {
            return mutated(it -> it.arguments.add(name + equals + strings.join(separator)));
        }
        return this;
    }

    public CommandLineComposer withTwoArgumentSwitch(String name, Object object)
    {
        return withTwoArgumentSwitch(name, object, ",");
    }

    public CommandLineComposer withTwoArgumentSwitch(String name, Object object, String separator)
    {
        var strings = toStringList(object);
        if (strings != null)
        {
            return mutated(it ->
            {
                it.arguments.add(name);
                it.arguments.add(strings.join(separator));
            });
        }
        return this;
    }

    private StringList toStringList(Object object)
    {
        if (object == null)
        {
            return null;
        }
        if (object instanceof Collection<?> collection)
        {
            if (collection.isEmpty())
            {
                return null;
            }
            else
            {
                return stringList(collection);
            }
        }
        return stringList(object.toString());
    }
}
