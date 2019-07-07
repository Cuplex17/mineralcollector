package net.cuplex.mineralcollector;

import net.cuplex.mineralcollector.blocks.MineralBubbleColumnBlock;
import net.cuplex.mineralcollector.blocks.MineralCollectorBlock;
import net.cuplex.mineralcollector.blocks.MineralCollectorBlockEntity;
import net.cuplex.mineralcollector.blocks.MineralVentBlock;
import net.cuplex.mineralcollector.config.MineralCollectorConfig;
import net.cuplex.mineralcollector.world.MineralVentFeature;
import net.cuplex.mineralcollector.world.MineralVentGenerator;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.OceanBiome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class MineralCollector implements ModInitializer
{
    //New Blocks
    public static final MineralCollectorBlock MINERAL_COLLECTOR_BLOCK = new MineralCollectorBlock(FabricBlockSettings
            .of(Material.WOOD)
            .drops(new Identifier("mineralcollector", "blocks/mineral_collector"))
            .hardness(0.1f)
            .resistance(0.1f)
            .sounds(BlockSoundGroup.WOOD)
            .build());
    public static final MineralVentBlock MINERAL_VENT_BLOCK = new MineralVentBlock(FabricBlockSettings
            .of(Material.STONE)
            .lightLevel(5)
            .dropsNothing()
            .build());
    public static final MineralBubbleColumnBlock MINERAL_BUBBLE_COLUMN_BLOCK = new MineralBubbleColumnBlock(FabricBlockSettings
            .of(Material.BUBBLE_COLUMN)
            .build());

    //New Block Entity Type
    public static BlockEntityType<MineralCollectorBlockEntity> MINERAL_COLLECTOR_BLOCK_ENTITY;

    //Item Groups (Creative tabs)
    public static final ItemGroup MINERALCOLLECTOR_GROUP = FabricItemGroupBuilder.create(
            new Identifier("mineralcollector", "general"))
            .icon(() -> new ItemStack(MineralCollector.MINERAL_COLLECTOR_BLOCK))
            .build();

    //New Structures

    public static final StructurePieceType MINERAL_VENT_PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "mineral_vent_piece", MineralVentGenerator.Piece::new);
    public static final StructureFeature<DefaultFeatureConfig> MINERAL_VENT_FEATURE = Registry.register(Registry.FEATURE, "mineral_vent_feature", new MineralVentFeature());
    public static final StructureFeature<?> MINERAL_VENT = Registry.register(Registry.STRUCTURE_FEATURE, "mineral_vent", MINERAL_VENT_FEATURE);

    //Mod Config
    public static MineralCollectorConfig mineralCollectorConfig = new MineralCollectorConfig();

    @Override
    public void onInitialize()
    {
        System.out.println("Mineral Collector Initialising");

        //Register New Blocks and Items
        Registry.register(Registry.BLOCK, new Identifier("mineralcollector", "mineral_collector"), MINERAL_COLLECTOR_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("mineralcollector", "mineral_collector"), new BlockItem(MINERAL_COLLECTOR_BLOCK, new Item.Settings().group(MineralCollector.MINERALCOLLECTOR_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier("mineralcollector", "mineral_vent"), MINERAL_VENT_BLOCK);
        Registry.register(Registry.ITEM, new Identifier("mineralcollector", "mineral_vent"), new BlockItem(MINERAL_VENT_BLOCK, new Item.Settings().group(MineralCollector.MINERALCOLLECTOR_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier("mineralcollector", "mineral_bubble_column"), MINERAL_BUBBLE_COLUMN_BLOCK);

        //Register Block Entity Type
        MINERAL_COLLECTOR_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, "mineralcollector:mineral_collector", BlockEntityType.Builder.create(MineralCollectorBlockEntity::new, MINERAL_COLLECTOR_BLOCK).build(null));

        //Register structures
        Feature.STRUCTURES.put("Mineral Vent", MINERAL_VENT_FEATURE);

        for(Biome biome : Registry.BIOME)
        {
            if(biome.getCategory() == Biome.Category.OCEAN)
            {
                biome.addStructureFeature(MINERAL_VENT_FEATURE, new DefaultFeatureConfig());
                biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Biome.configureFeature(MINERAL_VENT_FEATURE, new DefaultFeatureConfig(), Decorator.CHANCE_PASSTHROUGH, new ChanceDecoratorConfig(2)));
            }
        }

        //Register configs
        mineralCollectorConfig.loadConfig();
    }
}
