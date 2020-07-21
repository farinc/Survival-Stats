package com.farinc.survivalstats.common.items;

import com.farinc.survivalstats.SurvivalStats;
import com.farinc.survivalstats.capabilities.BasicHeatExchanger;
import com.farinc.survivalstats.capabilities.IHeatExchanger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.common.util.NonNullSupplier;

/**
 * A test item for the heat capability. Displays heat and adds heat to the player.
 * @author farinc
 *
 */

public class TestHeatItem extends Item {

	public TestHeatItem() {
		super(new Item.Properties().maxStackSize(1));
        setRegistryName(SurvivalStats.MODID, "testheatitem");
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote)
            playerIn.getCapability(BasicHeatExchanger.HeatCapability.HEATEXCHANGER_CAPABILITY).ifPresent((IHeatExchanger t) -> {
            	playerIn.sendStatusMessage(new StringTextComponent("Heat: "+t.getInternalEnergy()), false);
            	t.inputHeat(50.0F);
            });
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
