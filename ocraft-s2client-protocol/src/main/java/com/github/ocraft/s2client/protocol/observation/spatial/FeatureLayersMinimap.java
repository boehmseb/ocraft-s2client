package com.github.ocraft.s2client.protocol.observation.spatial;

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

import SC2APIProtocol.Spatial;
import com.github.ocraft.s2client.protocol.Strings;

import java.io.Serializable;
import java.util.Objects;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.DataExtractor.tryGet;
import static com.github.ocraft.s2client.protocol.Errors.required;
import static com.github.ocraft.s2client.protocol.Preconditions.require;

public final class FeatureLayersMinimap implements Serializable {

    private static final long serialVersionUID = 5024615413306860431L;

    private final ImageData heightMap;      // uint8. Terrain height. World space units of [-200, 200] encoded into [0, 255].
    private final ImageData visibilityMap;  // uint8. 0=Hidden, 1=Fogged, 2=Visible, 3=FullHidden
    private final ImageData creep;          // 1-bit. Zerg creep.
    private final ImageData camera;         // 1-bit. Area covered by the camera.
    private final ImageData playerId;       // uint8. Participants: [1, 15] Neutral: 16
    private final ImageData playerRelative; // uint8. See "Alliance" enum in raw.proto. Range: [1, 4]
    private final ImageData selected;       // 1-bit. Selected units.


    // Cheat layers, enable with SpatialCameraSetup.allow_cheating_layers
    private final ImageData unitType;       // int32. Unique identifier for type of unit.


    private FeatureLayersMinimap(Spatial.FeatureLayersMinimap sc2ApiFeatureLayersMinimap) {
        heightMap = tryGet(Spatial.FeatureLayersMinimap::getHeightMap, Spatial.FeatureLayersMinimap::hasHeightMap)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("height map"));

        visibilityMap = tryGet(
                Spatial.FeatureLayersMinimap::getVisibilityMap, Spatial.FeatureLayersMinimap::hasVisibilityMap
        ).apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("visibility map"));

        creep = tryGet(Spatial.FeatureLayersMinimap::getCreep, Spatial.FeatureLayersMinimap::hasCreep)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("creep"));

        camera = tryGet(Spatial.FeatureLayersMinimap::getCamera, Spatial.FeatureLayersMinimap::hasCamera)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("camera"));

        playerId = tryGet(Spatial.FeatureLayersMinimap::getPlayerId, Spatial.FeatureLayersMinimap::hasPlayerId)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("player id"));

        unitType = tryGet(Spatial.FeatureLayersMinimap::getUnitType, Spatial.FeatureLayersMinimap::hasUnitType)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElse(nothing());

        selected = tryGet(Spatial.FeatureLayersMinimap::getSelected, Spatial.FeatureLayersMinimap::hasSelected)
                .apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("selected"));

        playerRelative = tryGet(
                Spatial.FeatureLayersMinimap::getPlayerRelative, Spatial.FeatureLayersMinimap::hasPlayerRelative
        ).apply(sc2ApiFeatureLayersMinimap).map(ImageData::from).orElseThrow(required("player relative"));
    }

    public static FeatureLayersMinimap from(Spatial.FeatureLayersMinimap sc2ApiFeatureLayersMinimap) {
        require("sc2api feature layers minimap", sc2ApiFeatureLayersMinimap);
        return new FeatureLayersMinimap(sc2ApiFeatureLayersMinimap);
    }

    /**
     * uint8. Terrain height. World space units of [-200, 200] encoded into [0, 255].
     */
    public ImageData getHeightMap() {
        return heightMap;
    }

    /**
     * uint8. 0=Hidden, 1=Fogged, 2=Visible, 3=FullHidden
     */
    public ImageData getVisibilityMap() {
        return visibilityMap;
    }

    /**
     * 1-bit. Zerg creep.
     */
    public ImageData getCreep() {
        return creep;
    }

    /**
     * 1-bit. Area covered by the camera.
     */
    public ImageData getCamera() {
        return camera;
    }

    /**
     * uint8. Participants: [1, 15] Neutral: 16
     */
    public ImageData getPlayerId() {
        return playerId;
    }

    /**
     * uint8. See "Alliance" enum in raw.proto. Range: [1, 4]
     */
    public ImageData getPlayerRelative() {
        return playerRelative;
    }

    /**
     * 1-bit. Selected units.
     */
    public ImageData getSelected() {
        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureLayersMinimap that = (FeatureLayersMinimap) o;

        if (!heightMap.equals(that.heightMap)) return false;
        if (!visibilityMap.equals(that.visibilityMap)) return false;
        if (!creep.equals(that.creep)) return false;
        if (!camera.equals(that.camera)) return false;
        if (!playerId.equals(that.playerId)) return false;
        if (!playerRelative.equals(that.playerRelative)) return false;
        if (!selected.equals(that.selected)) return false;
        return Objects.equals(unitType, that.unitType);
    }

    @Override
    public int hashCode() {
        int result = heightMap.hashCode();
        result = 31 * result + visibilityMap.hashCode();
        result = 31 * result + creep.hashCode();
        result = 31 * result + camera.hashCode();
        result = 31 * result + playerId.hashCode();
        result = 31 * result + playerRelative.hashCode();
        result = 31 * result + selected.hashCode();
        result = 31 * result + (unitType != null ? unitType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return Strings.toJson(this);
    }
}
