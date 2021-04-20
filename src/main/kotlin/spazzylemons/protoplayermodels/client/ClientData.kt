package spazzylemons.protoplayermodels.client

import net.minecraft.client.MinecraftClient
import spazzylemons.protoplayermodels.config.loadSettings
import spazzylemons.protoplayermodels.render.ProtogenPlayerEntityRenderer

object ClientData {
    var renderer: ProtogenPlayerEntityRenderer? = null
    var settings = loadSettings()

    init {
        renderer = ProtogenPlayerEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)
    }
}