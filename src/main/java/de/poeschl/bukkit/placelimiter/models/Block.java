package de.poeschl.bukkit.placelimiter.models;

import org.bukkit.Material;


public class Block {
    private Material material;
    private byte data;

    public Block(Material material) {
        this(material, (byte) -1);
    }

    public Block(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (!material.equals(block.material)) {
            return false;
        }
        return data == (byte) -1 || data == block.data;
    }

    @Override
    public int hashCode() {
        int result = material != null ? material.hashCode() : 0;
        result = 31 * result + (int) data;
        return result;
    }
}
