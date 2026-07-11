package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

    public class MeldingRecipeBuilder extends ModelFile {
        private ResourceLocation ingredient1;
        private ResourceLocation ingredient2;

        private ResourceLocation output;
        private int quantity;

        private ResourceLocation bonusOutput;
        private int bonusQuantity;

        private int bonusChance = 0;

        private int cost;
        private int tier;

        public MeldingRecipeBuilder(Object o) {
            super((ResourceLocation) o);
        }

        private MeldingRecipeBuilder self() {
            return this;
        }

        public MeldingRecipeBuilder ingredient1(Supplier<Item> item) {
            Preconditions.checkNotNull(item);
            this.ingredient1 = BuiltInRegistries.ITEM.getKey(item.get());
            return self();
        }

        public MeldingRecipeBuilder ingredient2(Supplier<Item> item) {
            Preconditions.checkNotNull(item);
            this.ingredient2 = BuiltInRegistries.ITEM.getKey(item.get());
            return self();
        }

        public MeldingRecipeBuilder output(Supplier<Item> item) {
            return output(item,1);
        }
        public MeldingRecipeBuilder output(Supplier<Item> item, int quantity) {
            Preconditions.checkNotNull(item);
            this.output = BuiltInRegistries.ITEM.getKey(item.get());
            this.quantity = quantity;
            return self();
        }

        public MeldingRecipeBuilder bonusOutput(Supplier<Item> item, int quantity, int chance) {
            Preconditions.checkNotNull(item);
            this.bonusOutput = BuiltInRegistries.ITEM.getKey(item.get());
            this.bonusQuantity = quantity;
            this.bonusChance = chance;
            return self();
        }

        public MeldingRecipeBuilder addCost(int cost) {
            this.cost = cost;
            return self();
        }

        public MeldingRecipeBuilder addTier(int tier) {
            this.tier = tier;
            return self();
        }

        @Override
        protected boolean exists() {
            return true;
        }

        @VisibleForTesting
        public JsonObject toJson() {
            JsonObject root = new JsonObject();

            root.addProperty("cost", cost);
            root.addProperty("ingredient1", ingredient1.toString());
            root.addProperty("ingredient2", ingredient2.toString());

            JsonObject outputObj = new JsonObject();
            outputObj.addProperty("type", "item");
            outputObj.addProperty("item", output.toString());
            outputObj.addProperty("quantity", quantity);

            root.add("output", outputObj);

            JsonObject output2Obj = new JsonObject();
            if (bonusOutput != null) {
                output2Obj.addProperty("item", bonusOutput.toString());
                output2Obj.addProperty("quantity", bonusQuantity);
                output2Obj.addProperty("chance", bonusChance);
                root.add("output2", output2Obj);
            }

            root.addProperty("tier", tier);

            return root;
        }
    }