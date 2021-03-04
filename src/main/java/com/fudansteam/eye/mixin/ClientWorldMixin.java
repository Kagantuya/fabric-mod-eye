package com.fudansteam.eye.mixin;

import com.fudansteam.eye.Eye;
import com.fudansteam.eye.config.EyeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author : 箱子
 * Description : description
 * Created by 箱子 on 2021-03-01 11:46:58
 * Copyright 2021 HDU_IES. All rights reserved.
 */
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    
    /**
     * 解决实体原有发光效果被强行抹除，只允许更新在此集合中的实体为不发光；同时保存被标记的实体，在退出世界时取消发光效果
     */
    private final Set<Integer> eyeGlowingEntityIdSet = new HashSet<>();
    private final Map<String, Integer> preClosestEntityIdMap = new HashMap<>();
    
    @Inject(method = "disconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        Eye.tips.clear();
        Eye.tipTimes.clear();
        preClosestEntityIdMap.clear();
        Eye.shouldWarn = false;
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world != null) {
            for (int entityId : eyeGlowingEntityIdSet) {
                Entity entity = world.getEntityById(entityId);
                if (entity != null) {
                    entity.setGlowing(false);
                }
            }
        }
        eyeGlowingEntityIdSet.clear();
    }
    
    @Inject(method = "tickEntity", at = @At("RETURN"))
    private void onTickEntity(Entity entity, CallbackInfo ci) {
        //不是超级眼则不检测非生物，同时取消超级眼中被标记的非生物实体发光
        if (!EyeConfig.superEye && !(entity instanceof LivingEntity)) {
            if (eyeGlowingEntityIdSet.contains(entity.getEntityId())) {
                entity.setGlowing(false);
            }
            return;
        }
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (((int) entity.distanceTo(player)) == 0) {
            return;
        }
        // 玩家不为空且实体不为当前玩家，同时二者距离在指定半径内
        if (player != null && entity.getEntityId() != player.getEntityId() && entity.isInRange(player, EyeConfig.distance)) {
            insideEye(entity, player);
        } else {
            outsideEye(entity);
        }
    }
    
    /**
     * 在检测距离之外
     *
     * @param entity 实体
     */
    private void outsideEye(Entity entity) {
        int entityId = entity.getEntityId();
        if (eyeGlowingEntityIdSet.contains(entityId)) {
            entity.setGlowing(false);
            eyeGlowingEntityIdSet.remove(entityId);
        }
        // 当前未检测到实体则判断是否可移除过期提示
        for (String tipType : Eye.tipTimes.keySet()) {
            if (Util.getMeasuringTimeMs() - Eye.tipTimes.get(tipType) >= EyeConfig.DISAPPEAR_TIME) {
                Eye.tips.remove(tipType);
                Eye.tipTimes.remove(tipType);
            }
        }
    }
    
    /**
     * 在检测距离之内
     *
     * @param entity 实体
     * @param player 玩家
     */
    private void insideEye(Entity entity, ClientPlayerEntity player) {
        int entityId = entity.getEntityId();
        // 校验是否发光是为了防止原有发光效果的实体刚好在此模组标记的实体集合中，造成实体原有发光效果被强行抹除
        if (EyeConfig.superEye && !entity.isGlowing()) {
            entity.setGlowing(true);
            eyeGlowingEntityIdSet.add(entityId);
        }
        if (entity instanceof HostileEntity || entity instanceof AmbientEntity || entity instanceof SlimeEntity) {
            shouldUpdate(EyeConfig.TERRIBLE, entity, player);
        } else {
            shouldUpdate(EyeConfig.OTHER, entity, player);
        }
    }
    
    /**
     * 是否应该更新
     *
     * @param tipType 提示种类
     * @param entity  实体
     * @param player  玩家
     */
    private void shouldUpdate(String tipType, Entity entity, ClientPlayerEntity player) {
        int entityId = entity.getEntityId();
        // 提示实体信息同时跟踪此实体，若之前未标记过实体或标记实体消失了，或接下来有新的实体更靠近玩家或仍为此实体则再次更新实体
        Integer preTerribleEntityId = preClosestEntityIdMap.get(tipType);
        Entity preTerribleEntity = null;
        if (MinecraftClient.getInstance().world != null && preTerribleEntityId != null) {
            preTerribleEntity = MinecraftClient.getInstance().world.getEntityById(preTerribleEntityId);
        }
        if (preTerribleEntityId == null || preTerribleEntity == null ||
                entity.distanceTo(player) < preTerribleEntity.distanceTo(player) || entityId == preTerribleEntityId) {
            // 为了防止实体原有发光效果被意外强行抹除，同时也避免了集合重复添加已存在的实体
            if (!entity.isGlowing()) {
                entity.setGlowing(true);
                eyeGlowingEntityIdSet.add(entityId);
            }
            Eye.tips.put(tipType, getTip(player, entity));
            preClosestEntityIdMap.put(tipType, entityId);
            updateTipTimes(tipType);
            if (tipType.equals(EyeConfig.TERRIBLE)) {
                Eye.shouldWarn = entity.distanceTo(player) <= EyeConfig.warnDistance;
            }
        } else if (!EyeConfig.superEye && eyeGlowingEntityIdSet.contains(entityId)) {
            entity.setGlowing(false);
            eyeGlowingEntityIdSet.remove(entityId);
        }
    }
    
    /**
     * 更新提示触发时间
     *
     * @param tipType 提示种类
     */
    private void updateTipTimes(String tipType) {
        Long preTime = Eye.tipTimes.get(tipType);
        if (preTime == null || Util.getMeasuringTimeMs() - preTime < EyeConfig.DISAPPEAR_TIME) {
            Eye.tipTimes.put(tipType, Util.getMeasuringTimeMs());
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
        return (leftOrRight < 0.0D ? dir : " ") +
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
        Vec3d vec3d2 = (new Vec3d(0.0D, 0.0D, -1.0D))
                .rotateX(-player.pitch * 0.017453292F)
                .rotateY(-player.yaw * 0.017453292F);
        Vec3d vec3d3 = (new Vec3d(0.0D, 1.0D, 0.0D))
                .rotateX(-player.pitch * 0.017453292F)
                .rotateY(-player.yaw * 0.017453292F);
        Vec3d vec3d4 = vec3d2.crossProduct(vec3d3);
        Vec3d vec3d5 = entity.getPos().subtract(vec3d).normalize();
        return -vec3d2.dotProduct(vec3d5) <= 0.5D ? -vec3d4.dotProduct(vec3d5) : 0;
    }
    
}
