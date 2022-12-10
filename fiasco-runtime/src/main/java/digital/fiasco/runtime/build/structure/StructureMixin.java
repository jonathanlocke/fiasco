package digital.fiasco.runtime.build.structure;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.mixins.Mixin;

@SuppressWarnings("unused")
public interface StructureMixin extends Structure, Mixin
{
    @Override
    default Folder folder(String key)
    {
        return this.structure().folder(key);
    }

    @Override
    default void folder(String key, Folder folder)
    {
        this.structure().folder(key, folder);
    }

    default BaseStructure structure()
    {
        return this.mixin(StructureMixin.class, BaseStructure::new);
    }
}
