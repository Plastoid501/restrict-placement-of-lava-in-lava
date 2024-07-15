package net.plastoid501.rpll.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        BlockPos blockPos = hitResult.getBlockPos();
        Direction direction = hitResult.getSide();
        BlockPos blockPos2 = blockPos.offset(direction);

        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.LAVA_BUCKET)) {
            if (player.clientWorld.getBlockState(blockPos2).getFluidState().isOf(Fluids.LAVA)) {
                cir.setReturnValue(TypedActionResult.fail(itemStack).getResult());
                cir.cancel();
                player.sendMessage(Text.of("Restrict Placement of Lava in Lava"), true);
            }
        }
    }


}
