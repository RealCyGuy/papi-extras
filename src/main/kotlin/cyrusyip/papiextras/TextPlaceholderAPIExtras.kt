package cyrusyip.papiextras

import eu.pb4.placeholders.api.PlaceholderResult
import eu.pb4.placeholders.api.Placeholders
import net.fabricmc.api.ModInitializer
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import org.slf4j.LoggerFactory

object TextPlaceholderAPIExtras : ModInitializer {
    private val logger = LoggerFactory.getLogger("papi-extras")

    override fun onInitialize() {
        Placeholders.register(Identifier.of("extras", "biome")) { ctx, args ->
            if (!ctx.hasEntity())
                return@register PlaceholderResult.invalid("No entity!")

            val entity = ctx.entity!!
            val biomeEntry = entity.world.getBiome(entity.blockPos)
            val biomeId = entity.world.registryManager.getOrThrow(RegistryKeys.BIOME).getId(biomeEntry.value())
            val words = biomeId?.path.toString().split("_")
            PlaceholderResult.value(words.joinToString(" ") {
                it.lowercase().replaceFirstChar { char -> char.titlecase() }
            })
        }

        Placeholders.register(Identifier.of("extras", "completed_advancements")) { ctx, args ->
            if (!ctx.hasPlayer())
                return@register PlaceholderResult.invalid("No player!")

            val player = ctx.player!!
            var completed = 0
            for (advancement in player.server.advancementLoader.advancements) {
                if (!advancement.value.display.isEmpty && player.advancementTracker.getProgress(advancement).isDone()) {
                    completed++
                }
            }

            PlaceholderResult.value(completed.toString())
        }
    }
}