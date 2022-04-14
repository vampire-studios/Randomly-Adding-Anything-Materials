package io.github.vampirestudios.raa_materials.client;

import net.minecraft.resources.ResourceLocation;

public record TextureInformation(ResourceLocation oreOverlay, ResourceLocation storageBlock,
                                 ResourceLocation exposedStorageBlock, ResourceLocation wornStorageBlock,
                                 ResourceLocation weatheredStorageBlock, ResourceLocation oxidizedStorageBlock,
                                 ResourceLocation crystalBlock, ResourceLocation buddingCrystalBlock,
                                 ResourceLocation rawMaterialBlock, ResourceLocation crystal,
                                 ResourceLocation ingot, ResourceLocation gem, ResourceLocation rawItem,
                                 ResourceLocation plate, ResourceLocation shard, ResourceLocation gear,
                                 ResourceLocation nugget, ResourceLocation dust, ResourceLocation smallDust,
                                 ResourceLocation crushedOre, ResourceLocation swordBlade, ResourceLocation swordHandle,
                                 ResourceLocation pickaxeHead, ResourceLocation pickaxeStick, ResourceLocation axeHead,
                                 ResourceLocation axeStick, ResourceLocation hoeHead, ResourceLocation hoeStick,
                                 ResourceLocation shovelHead, ResourceLocation shovelStick, ResourceLocation stoneBricks,
                                 ResourceLocation stoneFrame, ResourceLocation stoneTiles, ResourceLocation stoneCobbled,
                                 ResourceLocation stoneChiseled) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        //Blocks
        private ResourceLocation oreOverlay;
        private ResourceLocation storageBlock;
        private ResourceLocation exposedStorageBlock;
        private ResourceLocation wornStorageBlock;
        private ResourceLocation weatheredStorageBlock;
        private ResourceLocation oxidizedStorageBlock;
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
        private ResourceLocation crushedOre;
        private ResourceLocation stoneBricks;
        private ResourceLocation stoneFrame;
        private ResourceLocation stoneTiles;
        private ResourceLocation stoneCobbled;
        private ResourceLocation stoneChiseled;

        //Blocks
        public Builder oreOverlay(ResourceLocation oreOverlay) {
            this.oreOverlay = oreOverlay;
            return this;
        }

        public Builder storageBlock(ResourceLocation storageBlock) {
            this.storageBlock = storageBlock;
            return this;
        }

        public Builder exposedStorageBlock(ResourceLocation exposedStorageBlock) {
            this.exposedStorageBlock = exposedStorageBlock;
            return this;
        }

        public Builder wornStorageBlock(ResourceLocation wornStorageBlock) {
            this.wornStorageBlock = wornStorageBlock;
            return this;
        }

        public Builder weatheredStorageBlock(ResourceLocation weatheredStorageBlock) {
            this.weatheredStorageBlock = weatheredStorageBlock;
            return this;
        }

        public Builder oxidizedStorageBlock(ResourceLocation oxidizedStorageBlock) {
            this.oxidizedStorageBlock = oxidizedStorageBlock;
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

        public Builder crushedOre(ResourceLocation crushedOre) {
            this.crushedOre = crushedOre;
            return this;
        }

        public Builder stoneBricks(ResourceLocation stoneBricks) {
            this.stoneBricks = stoneBricks;
            return this;
        }

        public Builder stoneFrame(ResourceLocation stoneFrame) {
            this.stoneFrame = stoneFrame;
            return this;
        }

        public Builder stoneTiles(ResourceLocation stoneTiles) {
            this.stoneTiles = stoneTiles;
            return this;
        }

        public Builder stoneCobbled(ResourceLocation stoneCobbled) {
            this.stoneCobbled = stoneCobbled;
            return this;
        }

        public Builder stoneChiseled(ResourceLocation stoneChiseled) {
            this.stoneChiseled = stoneChiseled;
            return this;
        }

        public TextureInformation build() {
            return new TextureInformation(oreOverlay, storageBlock, exposedStorageBlock, wornStorageBlock,
                    weatheredStorageBlock, oxidizedStorageBlock, crystalBlock, buddingCrystalBlock,
                    rawMaterialBlock, crystal, ingot, gem, rawItem, plate, shard, gear, nugget, dust,
                    smallDust, crushedOre, swordBlade, swordHandle, pickaxeHead, pickaxeStick, axeHead,
                    axeStick, hoeHead, hoeStick, shovelHead, shovelStick, stoneBricks, stoneFrame,
                    stoneTiles, stoneCobbled, stoneChiseled);
        }

    }

}