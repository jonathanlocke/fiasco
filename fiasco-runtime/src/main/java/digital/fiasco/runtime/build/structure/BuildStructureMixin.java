package digital.fiasco.runtime.build.structure;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.mixins.Mixin;

@SuppressWarnings("unused")
public interface BuildStructureMixin extends
        BuildStructure,
        Mixin
{
    @Override
    default Folder folder(String key)
    {
        return structure().folder(key);
    }

    @Override
    default void folder(String key, Folder folder)
    {
        structure().folder(key, folder);
    }

    default BaseBuildStructure structure()
    {
        return mixin(BuildStructureMixin.class, BaseBuildStructure::new);
    }
}
