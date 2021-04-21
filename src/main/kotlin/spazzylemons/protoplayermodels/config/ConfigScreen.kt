package spazzylemons.protoplayermodels.config

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ScreenTexts
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import spazzylemons.protoplayermodels.model.ClientData

class ConfigScreen(private val parent: Screen) : Screen(Text.of("ProtoPlayerModels configuration")) {
    override fun init() {
        var i = 0

        /** Add a button */
        fun button(text: Text, action: ButtonWidget.PressAction) =
            addButton(ButtonWidget(width / 2 - 100, height / 6 + 24 * i++, 200, 20, text, action))

        /** Add a section break */
        fun sectionBreak() = ++i

        // Toggle whether model is enabled
        button(createIsEnabledText()) {
            ClientData.settings = ClientData.settings.copy(isEnabled = !ClientData.settings.isEnabled)
            ClientData.settings.save()
            it.message = createIsEnabledText()
        }

        sectionBreak()

        // Close screen
        button(ScreenTexts.DONE) {
            client?.openScreen(parent)
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawCenteredText(matrices, textRenderer, title, width / 2, 20, 16777215)
        super.render(matrices, mouseX, mouseY, delta)
    }
}

private fun createIsEnabledText() =
    ScreenTexts.composeToggleText(Text.of("Enable protogen model"), ClientData.settings.isEnabled)