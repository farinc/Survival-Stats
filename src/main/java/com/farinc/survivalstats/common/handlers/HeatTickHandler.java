package com.farinc.survivalstats.common.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import com.farinc.survivalstats.SurvivalStats;
import com.farinc.survivalstats.api.heat.ISink;
import com.farinc.survivalstats.capabilities.PlayerSink;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class HeatTickHandler {

	public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

	/*
	* A testing map of hot and cold blocks
	*/
	private HashMap<String, Float> map;
	
	/*
	 * The number of ticks before another check. TODO: configurable
	 */
	private int ticksPerCycle = 80;
	
	/*
	 * Counts ticks between checks, TODO: configurable
	 */
	private int tickCount = 0;
	
	/*
	 * The cubic area about the player, TODO: configurable
	 */
	private int size = 3;
	
	/*
	 * player's position in terms of block integers 
	 */
	private int x;
	private int y;
	private int z;

	/**
	 * Running variables per cycle.
	 */
	private float netEffect;
	private float intensity;
	private float netX;
	private float netY;
	private float netZ;
	private float distanceSq;
	private ArrayList<BlockPos> sourceList = new ArrayList<BlockPos>();

	/*
	* Extra variables
	*/
	private PlayerEntity player;
	private World world;

	public HeatTickHandler() {
		//TODO: Config setting stuff here

		map = new HashMap<String, Float>();
		map.put("Block{minecraft:ice}", -0.5F);
		map.put("Block{minecraft:packed_ice}", -1F);
		map.put("Block{minecraft:blue_ice}", -1.5F);

		map.put("Block{minecraft:fire}", 1.0F);
		map.put("Block{minecraft:lava}", 3.0F);
	}

	/**
	 * TODO: Move to API class
	 * 
	 * Determines if a block is a source or not. 
	 * There are really two options, 
	 * 1) the block/tile is on the master list of blocks/tiles that are registered without implementation.
	 * 		a) TODO: Done through IMC
	 * 		b) TODO: Done through config
	 * 2) TODO: the tile implements ISource
	 * @param pos
	 * @return
	 */
	public boolean isSourceBlock(BlockPos pos){
		String block = world.getBlockState(pos).getBlock().toString();
		return this.map.containsKey(block);
	}
	
	@SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) 
    {
    	//There is a start and end phase. TODO: does it matter which phase?
    	if(event.phase == Phase.START) {
    		tickCount++;
    		if(tickCount >= ticksPerCycle) {
    			tickCount = 0;
    			
    			/**
    			 * Here we determine player position, then find every block around this position in a cubic radius of "size"
    			 **/
    			this.player = event.player;
    			this.world = player.world;
    			this.x = (int) Math.floor(player.getPosX());
    			this.y = (int) Math.floor(player.getPosY());
				this.z = (int) Math.floor(player.getPosZ());
				
				//This effectively iterates through the bound given. With the individual BlockPos, the blocks can be determined.
    			BlockPos.getAllInBoxMutable(x - size, y - size, z - size, x + size, y + size, z + size).forEach((BlockPos pos) -> {
					String block = world.getBlockState(pos).getBlock().toString();
					
					if(this.isSourceBlock(pos)){
						this.intensity = this.map.get(block);						
						this.sourceList.add(pos);

						//Calculates the weighted average of the for this source's position and intensity. At the end, the collective average will be determined.
						this.netEffect += this.intensity;
						this.netX += pos.getX()*this.intensity;
						this.netY += pos.getY()*this.intensity;
						this.netZ += pos.getZ()*this.intensity;
					}
				});

				//Skip if no source was found.
				if(!this.sourceList.isEmpty()){

					//Uses inverse square distance for the heat transfer to the heat capability

					//Find distance from the player to the calculated mean "center of intensity" coordinate.
					this.distanceSq = (float) player.getDistanceSq(this.netX/this.netEffect, this.netY/this.netEffect, this.netZ/this.netEffect);
					
					player.getCapability(PlayerSink.HeatCapability.HEATEXCHANGER_CAPABILITY).ifPresent((ISink t) -> {
						t.transfer(this.netEffect/this.distanceSq);
					});

					//Reset running variables
					this.sourceList.clear();
					this.netEffect = this.netX = this.netY = this.netZ = 0.0F;
				}
			}
    	}
	}
}
