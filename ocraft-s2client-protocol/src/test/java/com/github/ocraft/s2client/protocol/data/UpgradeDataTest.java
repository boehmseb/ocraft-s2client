package com.github.ocraft.s2client.protocol.data;

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

import SC2APIProtocol.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.ocraft.s2client.protocol.Constants.nothing;
import static com.github.ocraft.s2client.protocol.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UpgradeDataTest {
    @Test
    void throwsExceptionWhenSc2ApiUpgradeDataIsNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UpgradeData.from(nothing()))
                .withMessage("sc2api upgrade data is required");
    }

    @Test
    void convertsAllFieldsFromSc2ApiUnit() {
        assertThatAllFieldsAreConverted(UpgradeData.from(sc2ApiUpgradeData()));
    }

    private void assertThatAllFieldsAreConverted(UpgradeData unit) {
        assertThat(unit.getUpgrade()).as("upgrade: id").isNotNull();
        assertThat(unit.getName()).as("upgrade: name").isEqualTo(UPGRADE_NAME);
        assertThat(unit.getMineralCost()).as("upgrade: mineral cost").hasValue(UPGRADE_MINERAL_COST);
        assertThat(unit.getVespeneCost()).as("upgrade: vespene cost").hasValue(UPGRADE_VESPENE_COST);
        assertThat(unit.getResearchTime()).as("upgrade: research time").hasValue(UPGRADE_RESEARCH_TIME);
        assertThat(unit.getAbility()).as("upgrade: ability").isNotEmpty();
    }

    @Test
    void throwsExceptionWhenUpgradeIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UpgradeData.from(without(
                        () -> sc2ApiUpgradeData().toBuilder(),
                        Data.UpgradeData.Builder::clearUpgradeId).build()))
                .withMessage("upgrade is required");
    }

    @Test
    void throwsExceptionWhenNameIsNotProvided() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> UpgradeData.from(without(
                        () -> sc2ApiUpgradeData().toBuilder(),
                        Data.UpgradeData.Builder::clearName).build()))
                .withMessage("name is required");
    }

    @Test
    void fulfillsEqualsContract() {
        EqualsVerifier.forClass(UpgradeData.class).withNonnullFields("upgrade", "name").verify();
    }
}
