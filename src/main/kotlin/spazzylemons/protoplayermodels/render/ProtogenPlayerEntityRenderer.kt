package spazzylemons.protoplayermodels.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import spazzylemons.protoplayermodels.textureIdentifier

class ProtogenPlayerEntityRenderer(dispatcher: EntityRenderDispatcher) : PlayerEntityRenderer(dispatcher) {
    init {
        model = ProtogenPlayerEntityModel(0.0F)
    }

    override fun getTexture(abstractClientPlayerEntity: AbstractClientPlayerEntity?) = textureIdentifier

    companion object {
        init {
            // NativeImage.read will close the resource for us.
            val nativeImage = NativeImage.read(this::class.java.classLoader.getResourceAsStream("proto.png"))
            val texture = NativeImageBackedTexture(nativeImage)
            MinecraftClient.getInstance().textureManager.registerTexture(textureIdentifier, texture)
        }
    }
}