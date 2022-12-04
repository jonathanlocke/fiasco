package fiasco.plugins;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;

import java.util.regex.Pattern;

/**
 * Matches resources using a simplified pattern matching syntax.
 * <p>
 * <b>Matching Syntax</b>
 * <ul>
 *     <li>* - matches zero or more filename characters</li>
 *     <li>** - matches zero or more folders</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
public class FilePattern implements Matcher<ResourcePathed>
{
    public static FilePattern parse(String simplifiedPattern)
    {
        final var files = "\0";
        final var characters = "\1";

        // Substitute \0 for /**/ and \1 for *,
        simplifiedPattern = simplifiedPattern
                .replaceAll("[^/]\\*\\*[$/]", files)
                .replaceAll("\\*", characters);

        // quote characters around \0 and \1,
        var matcher = Pattern.compile("(.*)([\0\1])").matcher(simplifiedPattern);
        simplifiedPattern = matcher.replaceAll("Q$1E$2");
        matcher = Pattern.compile("([\0\1])(.*)$").matcher(simplifiedPattern);
        simplifiedPattern = matcher.replaceAll("Q$1E$2");

        // then build the final pattern by substituting regexps for \0 and \1
        var pattern = simplifiedPattern
                .replaceAll(files, ".*")
                .replaceAll(characters, "[^/]*");

        return new FilePattern(Pattern.compile(pattern));
    }

    private final Pattern pattern;

    private FilePattern(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public boolean matches(ResourcePathed file)
    {
        var path = file.path().join("/");
        return pattern.matcher(path).matches();
    }
}
