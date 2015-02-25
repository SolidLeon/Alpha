/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.game.world.tile.Tile;

/**
 *
 * @author markusmannel
 */
public interface Placeable {
    boolean canPlaceOn(Tile t);
}
