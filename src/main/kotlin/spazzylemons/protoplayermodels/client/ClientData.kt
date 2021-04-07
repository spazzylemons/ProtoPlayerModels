package spazzylemons.protoplayermodels.client

import net.minecraft.client.MinecraftClient
import spazzylemons.protoplayermodels.render.ProtogenPlayerEntityRenderer

object ClientData {
    var renderer: ProtogenPlayerEntityRenderer? = null

    init {
        // TODO when configuration is added, allow this to be reloaded
        renderer = ProtogenPlayerEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)
    }
}