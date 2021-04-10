package spazzylemons.protoplayermodels.client

import net.minecraft.client.MinecraftClient
import spazzylemons.protoplayermodels.render.ProtogenPlayerEntityRenderer

object ClientData {
    var renderer: ProtogenPlayerEntityRenderer? = null

    init {
        renderer = ProtogenPlayerEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)
    }
}