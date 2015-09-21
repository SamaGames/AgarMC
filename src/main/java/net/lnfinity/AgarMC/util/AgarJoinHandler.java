package net.lnfinity.AgarMC.util;

import java.util.Set;
import java.util.UUID;

import net.samagames.api.games.Game;
import net.samagames.api.games.IGameManager;
import net.samagames.api.games.Status;
import net.samagames.api.network.IJoinHandler;
import net.samagames.api.network.JoinResponse;
import net.samagames.api.network.ResponseType;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AgarJoinHandler implements IJoinHandler {
    private final IGameManager api;

    public AgarJoinHandler(IGameManager api) {
        this.api = api;
    }

    @Override
    public void finishJoin(Player player) {
        if (this.api.getGame() != null) {
            this.api.getGame().handleLogin(player);
            this.api.refreshArena();
        }
    }

	@Override
    public JoinResponse requestJoin(UUID player, JoinResponse response) {
        if (this.api.getGame() != null) {
            Game game = this.api.getGame();
            Pair<Boolean, String> gameResponse = game.canJoinGame(player, false);
            if (!((Boolean)gameResponse.getKey()).booleanValue()) {
                response.disallow((String)gameResponse.getValue());
                return response;
            }
            response.allow();
            if (game.getStatus() == Status.STARTING) {
                response.disallow(ResponseType.DENY_NOT_READY);
            } else if (game.getConnectedPlayers() >= this.api.getGameProperties().getMaxSlots()) {
                response.disallow(ResponseType.DENY_FULL);
            }
        }
        return response;
    }

    @Override
    public JoinResponse requestPartyJoin(UUID partyLeader, Set<UUID> partyMembers, JoinResponse response) {
        if (this.api.getGame() != null) {
            Game game = this.api.getGame();
            Pair<Boolean, String> gameResponse = game.canPartyJoinGame(partyMembers);
            if (!((Boolean)gameResponse.getKey()).booleanValue()) {
                response.disallow((String)gameResponse.getValue());
                return response;
            }
            response.allow();
            if (game.getStatus() == Status.STARTING) {
                response.disallow(ResponseType.DENY_NOT_READY);
            } else if (game.getConnectedPlayers() >= this.api.getGameProperties().getMaxSlots()) {
                response.disallow(ResponseType.DENY_FULL);
            }
        }
        return response;
    }

    @Override
    public void onModerationJoin(Player player) {
        this.api.getGame().handleModeratorLogin(player);
    }

    @Override
    public void onLogout(Player player) {
        this.api.onPlayerDisconnect(player);
    }
}

