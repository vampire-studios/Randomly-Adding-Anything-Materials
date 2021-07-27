package io.github.vampirestudios.raa_materials.client;

import net.minecraft.util.Identifier;

public record TextureInformation(Identifier oreOverlay, Identifier storageBlock,
                                 Identifier rawMaterialBlock, Identifier ingot,
                                 Identifier gem, Identifier rawItem,
                                 Identifier plate, Identifier gear,
                                 Identifier nugget, Identifier dust,
                                 Identifier smallDust, Identifier swordBlade,
                                 Identifier swordHandle, Identifier pickaxeHead,
                                 Identifier pickaxeStick, Identifier axeHead,
                                 Identifier axeStick, Identifier hoeHead,
                                 Identifier hoeStick, Identifier shovelHead,
                                 Identifier shovelStick) {

    public static Builder builder() {
        return new Builder();
    }

    //Blocks
    public Identifier getOreOverlay() {
        return oreOverlay;
    }

    public Identifier getStorageBlock() {
        return storageBlock;
    }

    public Identifier getRawMaterialBlock() {
        return rawMaterialBlock;
    }

    //Items
    public Identifier getIngot() {
        return ingot;
    }

    public Identifier getGem() {
        return gem;
    }

    public Identifier getRawItem() {
        return rawItem;
    }

    public Identifier getPlate() {
        return plate;
    }

    public Identifier getGear() {
        return gear;
    }

    public Identifier getNugget() {
        return nugget;
    }

    public Identifier getDust() {
        return dust;
    }

    public Identifier getSmallDust() {
        return smallDust;
    }

    public Identifier getSwordBlade() {
        return swordBlade;
    }

    public Identifier getSwordHandle() {
        return swordHandle;
    }

    public Identifier getPickaxeHead() {
        return pickaxeHead;
    }

    public Identifier getPickaxeStick() {
        return pickaxeStick;
    }

    public Identifier getAxeHead() {
        return axeHead;
    }

    public Identifier getAxeStick() {
        return axeStick;
    }

    public Identifier getHoeHead() {
        return hoeHead;
    }

    public Identifier getHoeStick() {
        return hoeStick;
    }

    public Identifier getShovelHead() {
        return shovelHead;
    }

    public Identifier getShovelStick() {
        return shovelStick;
    }

    public static class Builder {
        //Blocks
        private Identifier oreOverlay;
        private Identifier storageBlock;
        private Identifier rawMaterialBlock;

        //Items
        private Identifier ingot;
        private Identifier gem;
        private Identifier rawItem;
        private Identifier plate;
        private Identifier nugget;
        private Identifier dust;
        private Identifier smallDust;
        private Identifier gear;
        private Identifier swordBlade;
        private Identifier swordHandle;
        private Identifier pickaxeHead;
        private Identifier pickaxeStick;
        private Identifier axeHead;
        private Identifier axeStick;
        private Identifier hoeHead;
        private Identifier hoeStick;
        private Identifier shovelHead;
        private Identifier shovelStick;

        //Blocks
        public Builder oreOverlay(Identifier oreOverlay) {
            this.oreOverlay = oreOverlay;
            return this;
        }

        public Builder storageBlock(Identifier storageBlock) {
            this.storageBlock = storageBlock;
            return this;
        }

        public Builder rawMaterialBlock(Identifier rawMaterialBlock) {
            this.rawMaterialBlock = rawMaterialBlock;
            return this;
        }

        //Items
        public Builder ingot(Identifier ingot) {
            this.ingot = ingot;
            return this;
        }

        public Builder gem(Identifier gem) {
            this.gem = gem;
            return this;
        }

        public Builder rawItem(Identifier rawItem) {
            this.rawItem = rawItem;
            return this;
        }

        public Builder plate(Identifier plate) {
            this.plate = plate;
            return this;
        }

        public Builder nugget(Identifier nugget) {
            this.nugget = nugget;
            return this;
        }

        public Builder dust(Identifier dust) {
            this.dust = dust;
            return this;
        }

        public Builder smallDust(Identifier smallDust) {
            this.smallDust = smallDust;
            return this;
        }

        public Builder gear(Identifier gear) {
            this.gear = gear;
            return this;
        }

        public Builder swordBlade(Identifier swordBlade) {
            this.swordBlade = swordBlade;
            return this;
        }

        public Builder swordHandle(Identifier swordHandle) {
            this.swordHandle = swordHandle;
            return this;
        }

        public Builder pickaxeHead(Identifier pickaxeHead) {
            this.pickaxeHead = pickaxeHead;
            return this;
        }

        public Builder pickaxeStick(Identifier pickaxeStick) {
            this.pickaxeStick = pickaxeStick;
            return this;
        }

        public Builder axeHead(Identifier axeHead) {
            this.axeHead = axeHead;
            return this;
        }

        public Builder axeStick(Identifier axeStick) {
            this.axeStick = axeStick;
            return this;
        }

        public Builder hoeHead(Identifier hoeHead) {
            this.hoeHead = hoeHead;
            return this;
        }

        public Builder hoeStick(Identifier hoeStick) {
            this.hoeStick = hoeStick;
            return this;
        }

        public Builder shovelHead(Identifier shovelHead) {
            this.shovelHead = shovelHead;
            return this;
        }

        public Builder shovelStick(Identifier shovelStick) {
            this.shovelStick = shovelStick;
            return this;
        }

        public TextureInformation build() {
            return new TextureInformation(oreOverlay, storageBlock, rawMaterialBlock,
                    ingot, gem, rawItem, plate, gear, nugget, dust, smallDust,
                    swordBlade, swordHandle, pickaxeHead, pickaxeStick, axeHead,
                    axeStick, hoeHead, hoeStick, shovelHead, shovelStick);
        }

    }

}