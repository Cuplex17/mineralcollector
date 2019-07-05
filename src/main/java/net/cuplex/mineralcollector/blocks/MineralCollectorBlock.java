package net.cuplex.mineralcollector.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MineralCollectorBlock extends Block implements BlockEntityProvider
{
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public MineralCollectorBlock(Settings settings)
    {
        super(settings);
    }

    @Override
    public FluidState getFluidState(BlockState blockState)
    {
        return (Boolean)blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }

    @Override
    public BlockState  getPlacementState(ItemPlacementContext itemPlacementContext)
    {
        FluidState fluidState = itemPlacementContext.getWorld().getFluidState(itemPlacementContext.getBlockPos());
        boolean waterLog = fluidState.matches(FluidTags.WATER) && fluidState.getLevel() == 8;
        return super.getPlacementState(itemPlacementContext).with(WATERLOGGED, waterLog);
    }

    protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1)
    {
        stateFactory$Builder_1.add(WATERLOGGED);
    }

    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onEntityCollision(BlockState blockState_1, World world_1, BlockPos blockPos_1, Entity entity_1) {
        super.onEntityCollision(blockState_1, world_1, blockPos_1, entity_1);
        if (entity_1 instanceof BoatEntity) {
            world_1.breakBlock(new BlockPos(blockPos_1), true);
        }

    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext_1) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView)
    {
        return new MineralCollectorBlockEntity();
    }
}
