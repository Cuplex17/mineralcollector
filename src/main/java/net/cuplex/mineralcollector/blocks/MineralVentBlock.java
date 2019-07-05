package net.cuplex.mineralcollector.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MagmaBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MineralVentBlock extends MagmaBlock
{
    public MineralVentBlock(Block.Settings settings)
    {
        super(settings);
    }

    @Override
    public void onScheduledTick(BlockState blockState, World world, BlockPos blockPos, Random random)
    {
        MineralBubbleColumnBlock.update(world, blockPos.up(), false);
    }
}
