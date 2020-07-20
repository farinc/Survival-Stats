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

public class TestHeatItem extends Item {

	public TestHeatItem(Properties properties) {
		super(properties);
        setRegistryName(SurvivalStats.MODID, "testheatitem");
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote)
            playerIn.getCapability(BasicHeatExchanger.HeatCapability.HEATEXCHANGER_CAPABILITY).orElseThrow(
            new NonNullConsumer<IHeatExchanger>() {

				@Override
				public void accept(IHeatExchanger t) {
					playerIn.sendStatusMessage(new StringTextComponent("Heat: "+t.getTemperature()), false);
					t.inputHeat(50);
				}
            });
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
