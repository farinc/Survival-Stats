package com.farinc.survivalstats.common.items;

import com.farinc.survivalstats.SurvivalStats;
import com.farinc.survivalstats.api.heat.ISink;
import com.farinc.survivalstats.capabilities.PlayerSink;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

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
            playerIn.getCapability(PlayerSink.HeatCapability.HEATEXCHANGER_CAPABILITY).ifPresent((ISink t) -> {
            	playerIn.sendStatusMessage(new StringTextComponent("Heat: "+t.getHotness()), false);
            	//t.transfer(50.0F);
            });
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
