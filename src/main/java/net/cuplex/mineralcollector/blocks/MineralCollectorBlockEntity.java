package net.cuplex.mineralcollector.blocks;

import net.cuplex.mineralcollector.MineralCollector;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.world.loot.LootSupplier;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextTypes;

import java.util.List;

public class MineralCollectorBlockEntity extends BlockEntity implements Tickable
{
    public MineralCollectorBlockEntity()
    {
        super(MineralCollector.MINERAL_COLLECTOR_BLOCK_ENTITY);
    }

    private int counter = 0;
    private int counterLimit = MineralCollector.mineralCollectorConfig.getProperty("lootGenerationTimeInTicks");

    @Override
    public void tick()
    {
        if(!world.isClient)
        {
            if((world.getBlockState(pos.down()).getBlock() == MineralCollector.MINERAL_BUBBLE_COLUMN_BLOCK) && (world.getBlockState(pos).getFluidState().getLevel() == 8))
            {
                if(counter < counterLimit)
                {
                    counter++;
                }
                else if(counter >= counterLimit)
                {
                    counter = 0;
                    LootContext.Builder lootContextBuilder = (new LootContext.Builder((ServerWorld)this.world));
                    LootSupplier lootSupplier = this.world.getServer().getLootManager().getSupplier(new Identifier("mineralcollector", "mineralcollector/mineral_collector"));
                    List<ItemStack> list = lootSupplier.getDrops(lootContextBuilder.build(LootContextTypes.EMPTY));
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), list.get(0));
                    world.spawnEntity(itemEntity);
                }
            }
            markDirty();
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        super.toTag(tag);

        //Save the current counter
        tag.putInt("counter", counter);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        super.fromTag(tag);
        counter = tag.getInt("counter");
    }
}
