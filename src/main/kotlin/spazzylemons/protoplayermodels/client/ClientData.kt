package spazzylemons.protoplayermodels.client

import net.minecraft.client.MinecraftClient
import spazzylemons.protoplayermodels.render.ProtogenPlayerEntityRenderer

object ClientData {
    var renderer: ProtogenPlayerEntityRenderer? = null
    var isEnabled = true

    init {
        renderer = ProtogenPlayerEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)
    }
}