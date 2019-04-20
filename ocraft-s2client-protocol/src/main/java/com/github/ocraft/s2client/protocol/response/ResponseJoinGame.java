package com.github.ocraft.s2client.protocol.response;

/*-
 * #%L
 * ocraft-s2client-protocol
 * %%
 * Copyright (C) 2017 - 2018 Ocraft Project
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import SC2APIProtocol.Sc2Api;
import com.github.ocraft.s2client.protocol.Strings;
import com.github.ocraft.s2client.protocol.game.GameStatus;

import java.util.Objects;
import java.util.Optional;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Preconditions.isSet;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class ResponseJoinGame extends Response {

    private static final long serialVersionUID = 286015951447691895L;

    private final ResponseJoinGame.Error error;
    private final String errorDetails;
    private final int playerId;

    public enum Error {
        MISSING_PARTICIPATION,
        INVALID_OBSERVED_PLAYER_ID,
        MISSING_OPTIONS,
        MISSING_PORTS,
        GAME_FULL,
        LAUNCH_ERROR,
        FEATURE_UNSUPPORTED,
        NO_SPACE_FOR_USER,
        MAP_DOES_NOT_EXIST,
        CANNOT_OPEN_MAP,
        CHECKSUM_ERROR,
        NETWORK_ERROR,
        OTHER_ERROR;

        public static Error from(Sc2Api.ResponseJoinGame.Error sc2ApiResponseJoinGameError) {
            require("sc2api response join game error", sc2ApiResponseJoinGameError);
            switch (sc2ApiResponseJoinGameError) {
                case MissingParticipation:
                    return MISSING_PARTICIPATION;
                case InvalidObservedPlayerId:
                    return INVALID_OBSERVED_PLAYER_ID;
                case MissingOptions:
                    return MISSING_OPTIONS;
                case MissingPorts:
                    return MISSING_PORTS;
                case GameFull:
                    return GAME_FULL;
                case LaunchError:
                    return LAUNCH_ERROR;
                case FeatureUnsupported:
                    return FEATURE_UNSUPPORTED;
                case NoSpaceForUser:
                    return NO_SPACE_FOR_USER;
                case MapDoesNotExist:
                    return MAP_DOES_NOT_EXIST;
                case CannotOpenMap:
                    return CANNOT_OPEN_MAP;
                case ChecksumError:
                    return CHECKSUM_ERROR;
                case NetworkError:
                    return NETWORK_ERROR;
                case OtherError:
                    return OTHER_ERROR;
                default:
                    throw new AssertionError("unknown sc2api response join game error: " + sc2ApiResponseJoinGameError);
            }
        }
    }

    private ResponseJoinGame(Sc2Api.ResponseJoinGame sc2ApiResponseJoinGame, Sc2Api.Status sc2ApiStatus) {
        super(ResponseType.JOIN_GAME, GameStatus.from(sc2ApiStatus));

        this.playerId = sc2ApiResponseJoinGame.getPlayerId();

        this.error = tryGet(
                Sc2Api.ResponseJoinGame::getError, Sc2Api.ResponseJoinGame::hasError
        ).apply(sc2ApiResponseJoinGame).map(ResponseJoinGame.Error::from).orElse(nothing());

        this.errorDetails = tryGet(
                Sc2Api.ResponseJoinGame::getErrorDetails, Sc2Api.ResponseJoinGame::hasErrorDetails
        ).apply(sc2ApiResponseJoinGame).orElse(nothing());
    }

    public static ResponseJoinGame from(Sc2Api.Response sc2ApiResponse) {
        if (!hasJoinGameResponse(sc2ApiResponse)) {
            throw new IllegalArgumentException("provided argument doesn't have join game response");
        }

        return new ResponseJoinGame(sc2ApiResponse.getJoinGame(), sc2ApiResponse.getStatus());
    }

    private static boolean hasJoinGameResponse(Sc2Api.Response sc2ApiResponse) {
        return isSet(sc2ApiResponse) && sc2ApiResponse.hasJoinGame();
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(error);
    }

    public Optional<String> getErrorDetails() {
        return Optional.ofNullable(errorDetails);
    }

    public Integer getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResponseJoinGame)) return false;
        if (!super.equals(o)) return false;

        ResponseJoinGame that = (ResponseJoinGame) o;

        return that.canEqual(this) &&
                playerId == that.playerId &&
                error == that.error &&
                (Objects.equals(errorDetails, that.errorDetails));
    }

    @Override
    public boolean canEqual(Object other) {
        return other instanceof ResponseJoinGame;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (errorDetails != null ? errorDetails.hashCode() : 0);
        result = 31 * result + playerId;
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
