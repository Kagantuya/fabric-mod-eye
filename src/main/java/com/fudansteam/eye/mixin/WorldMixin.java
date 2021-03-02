package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-03-01 11:46:58
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(World.class)
public class WorldMixin {
    
    /**
     * 解决实体原有发光效果被强行抹除，只允许更新在此集合中的实体为不发光
     */
    private final Set<Integer> eyeGlowingEntityIdSet = new HashSet<>();
    
    @Inject(method = "close", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        // 此处需要遍历集合将所有被标记实体全部取消发光，但出于未知原因没法取消，因此会在下一次进入地图后对应实体发光效果无法消除
        eyeGlowingEntityIdSet.clear();
    }
    
    @Inject(method = "tickEntity", at = @At("RETURN"))
    private void onTickEntity(Consumer<Entity> tickConsumer, Entity entity, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity player = minecraftClient.player;
        int entityId = entity.getEntityId();
        long now = Util.getMeasuringTimeMs();
        
        // 玩家不为空且实体不为当前玩家，同时二者距离在指定半径内
        if (player != null && entityId != player.getEntityId() && entity.isInRange(player, EyeConfig.distance)) {
            // 校验是否发光是为了防止原有发光效果的实体刚好在此模组标记的实体集合中，造成实体原有发光效果被强行抹除
            if (EyeConfig.superEye && !entity.isGlowing()) {
                entity.setGlowing(true);
                eyeGlowingEntityIdSet.add(entityId);
            }
            // 提示实体信息同时跟踪此实体，若之前未标记过实体或标记实体消失了，或接下来有新的实体更靠近玩家或仍为此实体则再次更新实体
            Entity preEntity = null;
            if (minecraftClient.world != null) {
                preEntity = minecraftClient.world.getEntityById(Eye.preClosestEntityId);
            }
            if (Eye.preClosestEntityId == -1 || preEntity == null || entity.distanceTo(player) < preEntity.distanceTo(player) || entityId == Eye.preClosestEntityId) {
                Eye.preClosestEntityId = entityId;
                String tip = getTip(player, entity);
                
                // 作用同上方：为了防止实体原有发光效果被意外强行抹除，同时也避免了集合重复添加已存在的实体
                if (!entity.isGlowing()) {
                    entity.setGlowing(true);
                    eyeGlowingEntityIdSet.add(entityId);
                }
                if (entity instanceof HostileEntity || entity instanceof AmbientEntity) {
                    Eye.tips.put(EyeConfig.TERRIBLE, tip);
                    updateTipTimes(EyeConfig.TERRIBLE, now);
                } else {
                    Eye.tips.put(EyeConfig.OTHER, tip);
                    updateTipTimes(EyeConfig.OTHER, now);
                }
            } else if (!EyeConfig.superEye && eyeGlowingEntityIdSet.contains(entityId)) {
                entity.setGlowing(false);
                eyeGlowingEntityIdSet.remove(entityId);
            }
        } else {
            if (eyeGlowingEntityIdSet.contains(entityId)) {
                entity.setGlowing(false);
                eyeGlowingEntityIdSet.remove(entityId);
            }
            // 当前未检测到实体则判断是否可移除过期提示
            for (String tipType : Eye.tipTimes.keySet()) {
                if (now - Eye.tipTimes.get(tipType) >= EyeConfig.ALL_TIME) {
                    Eye.tips.remove(tipType);
                    Eye.tipTimes.remove(tipType);
                }
            }
        }
    }
    
    /**
     * 更新提示触发时间
     *
     * @param tipType 提示种类
     * @param now     当前时间
     */
    private void updateTipTimes(String tipType, long now) {
        Long preTime = Eye.tipTimes.get(tipType);
        if (preTime == null || now - preTime < EyeConfig.ALL_TIME) {
            Eye.tipTimes.put(tipType, now);
        }
    }
    
    /**
     * 获取提示
     *
     * @param player 玩家
     * @param entity 实体
     * @return 提示
     */
    private String getTip(ClientPlayerEntity player, Entity entity) {
        double leftOrRight = getLeftOrRight(player, entity);
        double upOrDown = player.getY() - entity.getY();
        String uod = upOrDown < 0.0D ? " ↑ " : upOrDown == 0 ? " " : " ↓ ";
        String dir = leftOrRight < 0.0D ? " <" + uod : leftOrRight == 0 ? "" : uod + "> ";
        return (leftOrRight < 0.0D ? dir : " ") + (leftOrRight == 0 ? uod : "") +
                entity.getName().getString() + " : " + (int) entity.distanceTo(player) +
                (leftOrRight > 0.0D ? dir : " ") + (leftOrRight == 0 ? uod : "");
    }
    
    /**
     * 获取实体相对玩家左右方位
     *
     * @param player 玩家
     * @param entity 实体
     * @return 方位
     */
    private double getLeftOrRight(ClientPlayerEntity player, Entity entity) {
        Vec3d vec3d = new Vec3d(player.getX(), player.getEyeY(), player.getZ());
        Vec3d vec3d2 = (new Vec3d(0.0D, 0.0D, -1.0D)).rotateX(-player.pitch * 0.017453292F).rotateY(-player.yaw * 0.017453292F);
        Vec3d vec3d3 = (new Vec3d(0.0D, 1.0D, 0.0D)).rotateX(-player.pitch * 0.017453292F).rotateY(-player.yaw * 0.017453292F);
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3);
        Vec3d vec3d5 = entity.getPos().subtract(vec3d).normalize();
        if (-vec3d2.dotProduct(vec3d5) <= 0.5D) {
            return -vec3d4.dotProduct(vec3d5);
        }
        return 0;
    }
    
}
