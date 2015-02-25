/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.solidleon.alpha.item;

import com.solidleon.alpha.item.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Markus
 */
public class Inventory implements Serializable{
    public List<Item> items = new ArrayList<Item>();
    public boolean dirty = false;

    public Inventory() {
    }
    
    private void dirty() {
        this.dirty = true;
    }
    
    public boolean removeResource(Resource resource, int count) {
        ResourceItem has = findResource(resource);
        if (has == null) return false;
        if (has.count < count) return false;
        has.count -= count;
        if (has.count <= 0) {
            items.remove(has);
        }
        dirty();
        return true;
    }
    
    public boolean add(int idx, Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = (ResourceItem) item;
            ResourceItem has = findResource(ri.res);
            if (has == null) {
                items.add(idx, ri);
            } else {
                has.count += ri.count;
            }
        } else {
            items.add(idx, item);
        }
        dirty();
        return true;
    }
    
    public ResourceItem findResource(Resource resource) {
        for (int i = 0; i< items.size(); i++) {
            Item item = items.get(i);
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                if (ri.res.id == resource.id) {
                    return ri;
                }
            }
        }
        return null;
    }
    
    public int count(Resource resource) {
        ResourceItem has = findResource(resource);
        if (has == null) return 0;
        return has.count;
    }

    public boolean hasResources(Resource resource, int n) {
        return count(resource) >= n;
    }

    public int count(Item item) {
        if (item instanceof ResourceItem) {
            return count(((ResourceItem) item).res);
        } else {
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).matches(item)) count += 1;
            }
            return count;
        }
    }

    public boolean add(Item result) {
        return add(items.size(), result);
    }
    
}
