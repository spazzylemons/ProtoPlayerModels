package spazzylemons.protoplayermodels.render

import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.PlayerEntityRenderer

class ProtogenPlayerEntityRenderer(dispatcher: EntityRenderDispatcher) : PlayerEntityRenderer(dispatcher, false) {
    init {
        model = ProtogenPlayerEntityModel(0.0F)
    }
}