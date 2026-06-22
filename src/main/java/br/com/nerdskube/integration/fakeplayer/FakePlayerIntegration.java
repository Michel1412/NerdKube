package br.com.nerdskube.integration.fakeplayer;

import br.com.nerdskube.NerdKube;
import net.neoforged.fml.ModList;

public final class FakePlayerIntegration {
    private FakePlayerIntegration() {}

    public static void init() {
        long managed = FakePlayerOwnerRegistry.ENTRIES.stream()
                .filter(e -> e.handling() == FakePlayerOwnerRegistry.Handling.MANAGED)
                .filter(e -> ModList.get().isLoaded(e.modId()))
                .count();
        long nativeSupport = FakePlayerOwnerRegistry.ENTRIES.stream()
                .filter(e -> e.handling() == FakePlayerOwnerRegistry.Handling.NATIVE)
                .filter(e -> ModList.get().isLoaded(e.modId()))
                .count();

        if (managed > 0 || nativeSupport > 0) {
            NerdKube.LOGGER.info(
                    "FakePlayer ownership — {} bloco(s) gerenciado(s), {} com suporte nativo do mod.",
                    managed,
                    nativeSupport);
        }

        NerdKube.LOGGER.info("FakePlayer progression guard ativo — lasers e automações bloqueados em rituais manuais.");
    }
}
