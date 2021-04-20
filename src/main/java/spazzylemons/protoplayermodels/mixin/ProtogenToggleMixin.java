package spazzylemons.protoplayermodels.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import spazzylemons.protoplayermodels.client.ClientData;

@Environment(EnvType.CLIENT)
@Mixin(SkinOptionsScreen.class)
public class ProtogenToggleMixin extends Screen {
    protected ProtogenToggleMixin() {
        super(null);
    }

    @Inject(
            at = @At("TAIL"),
            method = "init",
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void init(CallbackInfo ci, int i) {
        i += 2;
        ButtonWidget button = new ButtonWidget(
                this.width / 2 - 100,
                this.height / 6 + 24 * (i >> 1),
                200,
                20,
                Text.of("Toggle Protogen Model"),
                this::onClick
        );
        this.addButton(button);
    }

    private void onClick(ButtonWidget button) {
        ClientData.INSTANCE.setEnabled(!ClientData.INSTANCE.isEnabled());
    }
}
