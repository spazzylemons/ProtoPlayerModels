package spazzylemons.protoplayermodels.config

import net.minecraft.client.MinecraftClient
import java.io.File
import java.io.IOException
import java.nio.channels.Channels

data class Settings(
    // true if the protogen model is enabled
    val isEnabled: Boolean,
) {
    fun save() {
        ensureConfigFolder()
        val file = loadConfigFile("config.properties")
        val props = CommentedProperties(file)
        props["enabled"] = "$isEnabled"
        file.writer().use { props.save(it) }
    }
}

private val configFolder = MinecraftClient.getInstance().runDirectory.resolve("config").resolve("protoplayermodels")

private fun ensureConfigFolder() {
    if (!configFolder.exists()) configFolder.mkdirs() || throw IOException("Cannot create config folders")
}

private fun loadConfigFile(name: String): File {
    val file = configFolder.resolve(name)
    if (!file.exists()) {
        val resource = ClassLoader.getSystemClassLoader().getResource(name)!!
        val size = resource.openConnection().contentLength
        // fully transfer the data from the resource to the file
        file.outputStream().channel.use { fileChannel ->
            Channels.newChannel(resource.openStream()).use { resourceChannel ->
                var position = 0L
                while (position < size) {
                    position += fileChannel.transferFrom(resourceChannel, position, Long.MAX_VALUE)
                }
            }
        }
    }
    // file should exist now
    return file
}

fun loadSettings(): Settings {
    ensureConfigFolder()
    val props = CommentedProperties(loadConfigFile("config.properties"))
    return Settings(
        isEnabled = props["enabled"]?.equals("true", ignoreCase = true) ?: true,
    )
}