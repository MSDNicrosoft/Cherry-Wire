package top.catowncraft.cherrywire.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class CherryWireMixinPlugin implements IMixinConfigPlugin {

    private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

    // RedStoneWireBlock
    private static final String TARGET_CLASS_INTERMEDIARY_NAME = "net.minecraft.class_2457";

    // COLORS
    private static final String TARGET_FIELD_INTERMEDIARY_NAME = "field_24466";

    private static final String TARGET_CLASS_NAME = resolver.mapClassName("intermediary",
            TARGET_CLASS_INTERMEDIARY_NAME);

    private static final String TARGET_FIELD_NAME = resolver.mapFieldName("intermediary",
            TARGET_CLASS_INTERMEDIARY_NAME, TARGET_FIELD_INTERMEDIARY_NAME, null);

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        if (!TARGET_CLASS_NAME.equals(targetClassName)) {
            return;
        }

        final FieldNode field = targetClass.fields.stream()
                .filter(fieldNode -> TARGET_FIELD_NAME.equals(fieldNode.name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find field " + TARGET_FIELD_NAME));

        final String matchedName = "generateColors$" + getName(field.desc);
        final String matchedDesc = "()" + field.desc;

        final ListIterator<AbstractInsnNode> iter = targetClass.methods.stream()
                .filter(methodNode -> "<clinit>".equals(methodNode.name) && "()V".equals(methodNode.desc))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Could not find <clinit> method"))
                .instructions.iterator();

        while (iter.hasNext()) {
            final AbstractInsnNode insnNode = iter.next();
            if (insnNode instanceof FieldInsnNode && insnNode.getOpcode() == Opcodes.PUTSTATIC
                    && TARGET_FIELD_NAME.equals(((FieldInsnNode) insnNode).name)
            ) {
                iter.previous();
                iter.add(new InsnNode(Opcodes.POP));
                iter.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        targetClass.name,
                        matchedName,
                        matchedDesc
                ));
                break;
            }
        }
    }

    private static String getName(String desc) {
        final String replaced = desc.replace(";", "").replace("[", "");
        return replaced.substring(replaced.lastIndexOf("/") + 1);
    }
}