package com.befuzzle.antiquity.render;

import com.befuzzle.antiquity.Antiquity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AncientBellRenderer implements BlockEntityRenderer<BellBlockEntity> {
    public static final ResourceLocation ANCIENT_BELL_RESOURCE_LOCATION = new ResourceLocation(Antiquity.MOD_ID, "textures/entity/ancient_bell/ancient_bell_body");
    //public static final Material ANCIENT_BELL_RESOURCE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, ANCIENT_BELL_TEXTURE);
    private static final String BELL_BODY = "bell_body";
    private final ModelPart bellBody;
    
    public AncientBellRenderer(BlockEntityRendererProvider.Context pContext) {
        this.bellBody = pContext.bakeLayer(ModelLayers.BELL).getChild(BELL_BODY);
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("bell_body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F), PartPose.offset(8.0F, 12.0F, 8.0F));
        partdefinition1.addOrReplaceChild("bell_base", CubeListBuilder.create().texOffs(0, 13).addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F), PartPose.offset(-8.0F, -12.0F, -8.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }
    
    public void render(BellBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        float f = (float)pBlockEntity.ticks + pPartialTick;
        float f1 = 0.0F;
        float f2 = 0.0F;
        if (pBlockEntity.shaking) {
            float f3 = Mth.sin(f / (float)Math.PI) / (4.0F + f / 3.0F);
            if (pBlockEntity.clickDirection == Direction.NORTH) {
               f1 = -f3;
            } else if (pBlockEntity.clickDirection == Direction.SOUTH) {
               f1 = f3;
            } else if (pBlockEntity.clickDirection == Direction.EAST) {
               f2 = -f3;
            } else if (pBlockEntity.clickDirection == Direction.WEST) {
               f2 = f3;
            }
        }
        
        this.bellBody.xRot = f1;
        this.bellBody.zRot = f2;
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entitySolid(ANCIENT_BELL_RESOURCE_LOCATION));
        this.bellBody.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay);
    }
}