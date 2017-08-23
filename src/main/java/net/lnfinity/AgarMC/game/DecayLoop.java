package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;

/*
 * This file is part of AgarMC.
 *
 * AgarMC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgarMC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgarMC.  If not, see <http://www.gnu.org/licenses/>.
 */
public class DecayLoop implements Runnable {

    @Override
    public void run() {
        for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
            if(!player.isPlaying()) continue;

            for(PlayerCell playerCell : player.getCells()) {
                if(playerCell.getMass() < 500) continue;

                playerCell.increaseMass((int) - Math.floor(playerCell.getMass() * 0.002));
            }
        }
    }

}