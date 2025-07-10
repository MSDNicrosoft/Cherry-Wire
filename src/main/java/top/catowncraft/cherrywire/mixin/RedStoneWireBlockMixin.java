package top.catowncraft.cherrywire.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(RedStoneWireBlock.class)
public class RedStoneWireBlockMixin {
    @Unique
    private static float[] color$r;
    @Unique
    private static float[] color$g;
    @Unique
    private static float[] color$b;

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void onClInit(CallbackInfo ci) {
        color$r = new float[]{0.44F, 0.78F, 0.84F, 0.90F, 0.94F, 0.98F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F, 1.00F};
        color$g = new float[]{0.07F, 0.16F, 0.24F, 0.27F, 0.31F, 0.33F, 0.37F, 0.39F, 0.43F, 0.47F, 0.53F, 0.55F, 0.59F, 0.63F, 0.63F, 0.65F};
        color$b = new float[]{0.14F, 0.27F, 0.37F, 0.41F, 0.47F, 0.49F, 0.53F, 0.53F, 0.55F, 0.59F, 0.63F, 0.65F, 0.67F, 0.69F, 0.71F, 0.71F};
    }

    @Unique
    private static Vec3[] generateColors$Vec3() {
        Vec3[] value = new Vec3[16];
        for (int i = 0; i < 16; i++) {
            value[i] = new Vec3(color$r[i], color$g[i], color$b[i]);
        }
        return value;
    }

    @Unique
    private static Vector3f[] generateColors$Vector3f() {
        Vector3f[] value = new Vector3f[16];
        for (int i = 0; i < 16; i++) {
            value[i] = new Vector3f(color$r[i], color$g[i], color$b[i]);
        }
        return value;
    }

    @Unique
    private static int[] generateColors$I() {
        int[] value = new int[16];
        for (int i = 0; i < 16; i++) {
            value[i] = 0xFF000000 | ((int) (color$r[i] * 255.0F)) << 16 | ((int) (color$g[i] * 255.0F)) << 8 | ((int) (color$b[i] * 255.0F));
        }
        return value;
    }
}