package br.com.nerdskube.integration.fakeplayer;

import java.util.UUID;

/**
 * Duck typing para block entities cujo FakePlayer é gerenciado pelo NerdKube.
 */
public interface FakePlayerOwnerAccess {
    UUID nerdkube$getOwnerUuid();

    String nerdkube$getOwnerName();

    void nerdkube$setOwner(UUID uuid, String name);
}
