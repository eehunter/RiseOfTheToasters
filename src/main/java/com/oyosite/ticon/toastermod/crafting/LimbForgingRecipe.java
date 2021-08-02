package com.oyosite.ticon.toastermod.crafting;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.Predicate;

public record LimbForgingRecipe(Identifier id, Ingredient limb, Ingredient addition, LimbForgingPredicate limbPredicate, ItemStack result) implements Recipe<Inventory> {

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.limb.test(inventory.getStack(0)) && this.addition.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return null;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return null;
    }

    @Override
    public Identifier getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public interface LimbForgingPredicate extends Predicate<ItemStack>{
        default LimbForgingPredicate fromJson(JsonObject json) {
            return s->true;
        }
    }
}
