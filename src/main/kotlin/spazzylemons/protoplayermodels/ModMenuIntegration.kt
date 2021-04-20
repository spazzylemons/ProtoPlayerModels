package spazzylemons.protoplayermodels

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import spazzylemons.protoplayermodels.config.ConfigScreen

@Suppress("unused") // needed as we'll never use this ourselves
class ModMenuIntegration : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory { ConfigScreen(it) }
}