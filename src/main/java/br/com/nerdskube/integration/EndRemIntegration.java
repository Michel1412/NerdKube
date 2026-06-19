package br.com.nerdskube.integration;

import br.com.nerdskube.NerdKube;

/**
 * Ponto de extensão para features que dependem do End Remastered.
 * O mod carrega sem este mod; integrações só rodam quando {@code endrem} está presente.
 */
public final class EndRemIntegration {
    public static final String MOD_ID = "endrem";

    private EndRemIntegration() {}

    public static void init() {
        NerdKube.LOGGER.info("End Remastered detectado — integração NerdKube pronta para endgame.");
    }
}
