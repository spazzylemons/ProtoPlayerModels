package spazzylemons.protoplayermodels.render

import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.entity.LivingEntity
import spazzylemons.protoplayermodels.mixin.CuboidsAccessorMixin
import spazzylemons.protoplayermodels.model.OBJLoader
import spazzylemons.protoplayermodels.model.QuadModel

class ProtogenPlayerEntityModel<T : LivingEntity>(scale: Float) : PlayerEntityModel<T>(scale, false) {
    init {
        replace(head, helmet, headModel)
        replace(leftArm, leftSleeve, leftArmModel)
        replace(rightArm, rightSleeve, rightArmModel)
        replace(torso, jacket, torsoModel)
        replace(leftLeg, leftPantLeg, leftLegModel)
        replace(rightLeg, rightPantLeg, rightLegModel)
    }

    private fun replace(inner: ModelPart, outer: ModelPart, quadModel: QuadModel) {
        // empty the inner part
        (inner as CuboidsAccessorMixin).cuboids.clear()
        // empty the outer part
        (outer as CuboidsAccessorMixin).cuboids.clear()
        // create custom model part
        val newPart = CustomModelPart(this, quadModel)
        // place the part as a child of the inner part
        inner.addChild(newPart)
    }

    // temporary?
    companion object {
        val headModel = OBJLoader.load("head.obj")!!
        val leftArmModel = OBJLoader.load("left_arm.obj")!!
        val rightArmModel = OBJLoader.load("right_arm.obj")!!
        val torsoModel = OBJLoader.load("torso.obj")!!
        val leftLegModel = OBJLoader.load("left_leg.obj")!!
        val rightLegModel = OBJLoader.load("right_leg.obj")!!
    }
}