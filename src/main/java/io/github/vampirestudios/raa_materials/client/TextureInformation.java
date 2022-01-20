package io.github.vampirestudios.raa_materials.client;

import net.minecraft.resources.ResourceLocation;

public record TextureInformation(ResourceLocation oreOverlay, ResourceLocation storageBlock,
                                 ResourceLocation crystalBlock, ResourceLocation buddingCrystalBlock,
                                 ResourceLocation rawMaterialBlock, ResourceLocation crystal,
                                 ResourceLocation ingot, ResourceLocation gem, ResourceLocation rawItem,
                                 ResourceLocation plate, ResourceLocation shard, ResourceLocation gear,
                                 ResourceLocation nugget, ResourceLocation dust, ResourceLocation smallDust,
                                 ResourceLocation swordBlade, ResourceLocation swordHandle,
                                 ResourceLocation pickaxeHead, ResourceLocation pickaxeStick,
                                 ResourceLocation axeHead, ResourceLocation axeStick,
                                 ResourceLocation hoeHead, ResourceLocation hoeStick,
                                 ResourceLocation shovelHead, ResourceLocation shovelStick) {

    public static Builder builder() {
        return new Builder();
    }

    //Blocks
    public ResourceLocation getOreOverlay() {
        return oreOverlay;
    }

    public ResourceLocation getStorageBlock() {
        return storageBlock;
    }

    public ResourceLocation getRawMaterialBlock() {
        return rawMaterialBlock;
    }

    public ResourceLocation getCrystalBlock() {
        return crystalBlock;
    }

    public ResourceLocation getBuddingCrystalBlock() {
        return buddingCrystalBlock;
    }

    public ResourceLocation getCrystal() {
        return crystal;
    }

    //Items
    public ResourceLocation getIngot() {
        return ingot;
    }

    public ResourceLocation getGem() {
        return gem;
    }

    public ResourceLocation getRawItem() {
        return rawItem;
    }

    public ResourceLocation getPlate() {
        return plate;
    }

    public ResourceLocation getShard() {
        return shard;
    }

    public ResourceLocation getGear() {
        return gear;
    }

    public ResourceLocation getNugget() {
        return nugget;
    }

    public ResourceLocation getDust() {
        return dust;
    }

    public ResourceLocation getSmallDust() {
        return smallDust;
    }

    public ResourceLocation getSwordBlade() {
        return swordBlade;
    }

    public ResourceLocation getSwordHandle() {
        return swordHandle;
    }

    public ResourceLocation getPickaxeHead() {
        return pickaxeHead;
    }

    public ResourceLocation getPickaxeStick() {
        return pickaxeStick;
    }

    public ResourceLocation getAxeHead() {
        return axeHead;
    }

    public ResourceLocation getAxeStick() {
        return axeStick;
    }

    public ResourceLocation getHoeHead() {
        return hoeHead;
    }

    public ResourceLocation getHoeStick() {
        return hoeStick;
    }

    public ResourceLocation getShovelHead() {
        return shovelHead;
    }

    public ResourceLocation getShovelStick() {
        return shovelStick;
    }

    public static class Builder {
        //Blocks
        private ResourceLocation oreOverlay;
        private ResourceLocation storageBlock;
        private ResourceLocation rawMaterialBlock;
        private ResourceLocation crystalBlock;
        private ResourceLocation buddingCrystalBlock;
        private ResourceLocation crystal;

        //Items
        private ResourceLocation ingot;
        private ResourceLocation gem;
        private ResourceLocation rawItem;
        private ResourceLocation nugget;
        private ResourceLocation plate;
        private ResourceLocation shard;
        private ResourceLocation dust;
        private ResourceLocation smallDust;
        private ResourceLocation gear;
        private ResourceLocation swordBlade;
        private ResourceLocation swordHandle;
        private ResourceLocation pickaxeHead;
        private ResourceLocation pickaxeStick;
        private ResourceLocation axeHead;
        private ResourceLocation axeStick;
        private ResourceLocation hoeHead;
        private ResourceLocation hoeStick;
        private ResourceLocation shovelHead;
        private ResourceLocation shovelStick;

        //Blocks
        public Builder oreOverlay(ResourceLocation oreOverlay) {
            this.oreOverlay = oreOverlay;
            return this;
        }

        public Builder storageBlock(ResourceLocation storageBlock) {
            this.storageBlock = storageBlock;
            return this;
        }

        public Builder rawMaterialBlock(ResourceLocation rawMaterialBlock) {
            this.rawMaterialBlock = rawMaterialBlock;
            return this;
        }

        public Builder crystalBlock(ResourceLocation crystalBlock) {
            this.crystalBlock = crystalBlock;
            return this;
        }

        public Builder buddingCrystalBlock(ResourceLocation buddingCrystalBlock) {
            this.buddingCrystalBlock = buddingCrystalBlock;
            return this;
        }

        public Builder crystal(ResourceLocation crystal) {
            this.crystal = crystal;
            return this;
        }

        //Items
        public Builder ingot(ResourceLocation ingot) {
            this.ingot = ingot;
            return this;
        }

        public Builder gem(ResourceLocation gem) {
            this.gem = gem;
            return this;
        }

        public Builder rawItem(ResourceLocation rawItem) {
            this.rawItem = rawItem;
            return this;
        }

        public Builder plate(ResourceLocation plate) {
            this.plate = plate;
            return this;
        }

        public Builder shard(ResourceLocation shard) {
            this.shard = shard;
            return this;
        }

        public Builder nugget(ResourceLocation nugget) {
            this.nugget = nugget;
            return this;
        }

        public Builder dust(ResourceLocation dust) {
            this.dust = dust;
            return this;
        }

        public Builder smallDust(ResourceLocation smallDust) {
            this.smallDust = smallDust;
            return this;
        }

        public Builder gear(ResourceLocation gear) {
            this.gear = gear;
            return this;
        }

        public Builder swordBlade(ResourceLocation swordBlade) {
            this.swordBlade = swordBlade;
            return this;
        }

        public Builder swordHandle(ResourceLocation swordHandle) {
            this.swordHandle = swordHandle;
            return this;
        }

        public Builder pickaxeHead(ResourceLocation pickaxeHead) {
            this.pickaxeHead = pickaxeHead;
            return this;
        }

        public Builder pickaxeStick(ResourceLocation pickaxeStick) {
            this.pickaxeStick = pickaxeStick;
            return this;
        }

        public Builder axeHead(ResourceLocation axeHead) {
            this.axeHead = axeHead;
            return this;
        }

        public Builder axeStick(ResourceLocation axeStick) {
            this.axeStick = axeStick;
            return this;
        }

        public Builder hoeHead(ResourceLocation hoeHead) {
            this.hoeHead = hoeHead;
            return this;
        }

        public Builder hoeStick(ResourceLocation hoeStick) {
            this.hoeStick = hoeStick;
            return this;
        }

        public Builder shovelHead(ResourceLocation shovelHead) {
            this.shovelHead = shovelHead;
            return this;
        }

        public Builder shovelStick(ResourceLocation shovelStick) {
            this.shovelStick = shovelStick;
            return this;
        }

        public TextureInformation build() {
            return new TextureInformation(oreOverlay, storageBlock, crystalBlock, buddingCrystalBlock,
                    rawMaterialBlock, crystal, ingot, gem, rawItem, plate, shard, gear, nugget, dust,
                    smallDust, swordBlade, swordHandle, pickaxeHead, pickaxeStick, axeHead, axeStick,
                    hoeHead, hoeStick, shovelHead, shovelStick);
        }

    }

}