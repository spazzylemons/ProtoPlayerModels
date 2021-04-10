package spazzylemons.protoplayermodels.render

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.PlayerEntityRenderer
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier

class ProtogenPlayerEntityRenderer(dispatcher: EntityRenderDispatcher) : PlayerEntityRenderer(dispatcher, false) {
    init {
        model = ProtogenPlayerEntityModel(0.0F)
    }

    override fun getTexture(abstractClientPlayerEntity: AbstractClientPlayerEntity?) = TEXTURE_IDENTIFIER

    companion object {
        val TEXTURE_IDENTIFIER = Identifier("protoplayermodels", "proto")

        init {
            // NativeImage.read will close the resource for us.
            val nativeImage = NativeImage.read(this::class.java.classLoader.getResourceAsStream("proto.png"))
            val texture = NativeImageBackedTexture(nativeImage)
            MinecraftClient.getInstance().textureManager.registerTexture(TEXTURE_IDENTIFIER, texture)
        }
    }
}