package fiasco;

public interface LibraryCollection
{
    default Library library(String specifier)
    {
        return null;
    }
}
