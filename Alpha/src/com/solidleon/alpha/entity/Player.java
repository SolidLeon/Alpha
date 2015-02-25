/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.entity;

import com.solidleon.alpha.InGameState;
import com.solidleon.alpha.item.Inventory;
import com.solidleon.alpha.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class Player {
    /**
     * Msg Slot 2 -> TOP MESSAGE, disapears after 1
     * 1 -> Disapears after 0
     * 0 -> Disapears at first
     * A new message added is inserted into slot 2, other messages are shifted.
     */
    public List<String> messages = new ArrayList<String>();
    /** Timer for message displaying */
    private int msgTimer;
    /** players inventory */
    public Inventory inventory;
    /** "equiped" item, to use on designated area */
    public Item selectedItem=null;
    /** ingame reference */
    public InGameState ingame;

    public Player(InGameState ingame) {
        this.ingame = ingame;
        inventory = new Inventory();
    }
    
    public void addMsg(String msg) {
        if (messages.isEmpty()) msgTimer = 1500;
        messages.add(0, msg);
    }

    public void update(int delta) {
        if (!messages.isEmpty()) {
            msgTimer -= delta;
            if (msgTimer <= 0) {
                messages.remove(0);
                msgTimer += 1500;
            }
        }
    }


    public void deselectItem() {
        if (selectedItem != null) {
            inventory.add(0, selectedItem);
            addMsg("Deselect " + selectedItem.getName());
            selectedItem = null; //deselect
        }
    }
    
    public void selectItem(int itemCursor) {
        if (itemCursor < 0 || itemCursor >= inventory.items.size()) return;
        if (selectedItem == null) {
            selectedItem = inventory.items.remove(itemCursor);
            addMsg("Select " + selectedItem.getName());
        }
    }
    
    
}
