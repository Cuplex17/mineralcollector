package net.cuplex.mineralcollector.world;

import net.cuplex.mineralcollector.MineralCollector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableIntBoundingBox;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

import java.util.List;
import java.util.Random;

public class MineralVentGenerator
{
    public static final Identifier id = new Identifier("mineralcollector", "mineral_vent");

    public static void addParts(StructureManager structureManager, BlockPos blockPos, BlockRotation rotation, List<StructurePiece> list_1, Random random, DefaultFeatureConfig defaultFeatureConfig)
    {
        list_1.add(new MineralVentGenerator.Piece(structureManager, id, blockPos, rotation));
    }

    public static class Piece extends SimpleStructurePiece {
        //private final BlockRotation rotation;
        private final Identifier identifier;

        public Piece(StructureManager structureManager_1, CompoundTag compoundTag_1) {
            super(MineralCollector.MINERAL_VENT_PIECE_TYPE, compoundTag_1);

            this.identifier = new Identifier(compoundTag_1.getString("Template"));
            //this.rotation = BlockRotation.valueOf(compoundTag_1.getString("Rot"));

            this.setStructureData(structureManager_1);
        }

        public Piece(StructureManager structureManager, Identifier identifier, BlockPos pos, BlockRotation rotation) {
            super(MineralCollector.MINERAL_VENT_PIECE_TYPE, 0);

            //this.rotation = rotation;
            this.identifier = identifier;
            this.pos = pos;

            this.setStructureData(structureManager);
        }

        public void setStructureData(StructureManager structureManager)
        {
            Structure structure_1 = structureManager.getStructureOrBlank(this.identifier);
            StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            //Rotation didn't work
            //StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        protected void handleMetadata(String s, BlockPos blockPos, IWorld iWorld, Random random, MutableIntBoundingBox mutableIntBoundingBox) {

        }

        @Override
        public boolean generate(IWorld iWorld_1, Random random_1, MutableIntBoundingBox mutableIntBoundingBox_1, ChunkPos chunkPos_1) {
            int yHeight = iWorld_1.getTop(Heightmap.Type.OCEAN_FLOOR_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
            this.pos = this.pos.add(0, yHeight - 4, 0);
            return super.generate(iWorld_1, random_1, mutableIntBoundingBox_1, chunkPos_1);
        }
    }
}
