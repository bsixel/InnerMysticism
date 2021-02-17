package com.bsixel.mysticism.common.api.math;

import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/*
    Seriously why is none of this baked into Minecraft or Forge???
    All credits to @Mithion
    https://github.com/Mithion/ArsMagica2/blob/master/src/main/java/am2/utility/MathUtilities.java - meaning license for this particular file are as follows from the bottom of
    https://github.com/Mithion/ArsMagica2/
 */
public class MathCrap {

    public static SmartVector bezier(SmartVector s, SmartVector c1, SmartVector c2, SmartVector e, double t){
        if (t < 0 || t > 1.0f){
            throw new InvalidParameterException("t is out of range, with a value of :" + t);
        }
        double one_minus_t = 1 - t;

        SmartVector retValue = new SmartVector(0, 0, 0);
        SmartVector[] terms = new SmartVector[4];
        terms[0] = calcNewVector(one_minus_t * one_minus_t * one_minus_t, s);
        terms[1] = calcNewVector(3 * one_minus_t * one_minus_t * t, c1);
        terms[2] = calcNewVector(3 * one_minus_t * t * t, c2);
        terms[3] = calcNewVector(t * t * t, e);

        for (int i = 0; i < 4; ++i){
            retValue.add(terms[i]);
        }

        return retValue;
    }

    private static SmartVector calcNewVector(double scaler, SmartVector base){
        SmartVector retValue = new SmartVector(base.x, base.y, base.z);
        retValue.scale(scaler);
        return retValue;
    }

    public static BlockPos[] GetHorizontalBlocksInFrontOfCharacter(LivingEntity entity, int numBlocks, int x, int y, int z){
        double speed = 0.1F;

        double factor = Math.PI / 180.0f;

        double sinYawRadians = MathHelper.sin((float) (entity.rotationYaw * factor));
        double cosYawRadians = MathHelper.cos((float) (entity.rotationYaw * factor));

        double cosPitchRadians = MathHelper.cos((float) (entity.rotationPitch * factor));

        double motionZ = cosYawRadians * speed;
        double motionX = -sinYawRadians * speed;

        double curX = x;
        double curY = y;
        double curZ = z;

        double minimum = 0.01f;

        if (Math.abs(motionX) < minimum){
            motionX = 0;
        }
        if (Math.abs(motionZ) < minimum){
            motionZ = 0;
        }

        int lastX = x;
        int lastY = y;
        int lastZ = z;


        BlockPos[] list = new BlockPos[numBlocks];
        list[0] = new BlockPos(x, y, z);
        int count = 1;
        while (count < numBlocks){
            curX += motionX;
            curZ += motionZ;

            //check for deltas
            if ((int)Math.round(curX) != lastX || (int)Math.round(curY) != lastY || (int)Math.round(curZ) != lastZ){
                lastX = (int)Math.round(curX);
                lastY = (int)Math.round(curY);
                lastZ = (int)Math.round(curZ);
                list[count++] = new BlockPos(lastX, lastY, lastZ);
            }
        }
        return list;
    }



    public static BlockPos[] GetBlocksInFrontOfCharacter(LivingEntity entity, int numBlocks, int x, int y, int z){ // x, y, z are coords of initial impact
        double speed = 0.1F;

        double factor = Math.PI / 180.0f;

        double sinYawRadians = MathHelper.sin((float) (entity.rotationYaw * factor));
        double cosYawRadians = MathHelper.cos((float) (entity.rotationYaw * factor));

        double sinPitchRadians = MathHelper.sin((float) (entity.rotationPitch * factor));
        double cosPitchRadians = MathHelper.cos((float) (entity.rotationPitch * factor));

        double motionZ = cosYawRadians * cosPitchRadians * speed;
        double motionX = -sinYawRadians * cosPitchRadians * speed;
        double motionY = -sinPitchRadians * speed;

        double curX = x;
        double curY = y;
        double curZ = z;

        double minimum = 0.01f;

        if (Math.abs(motionX) < minimum){
            motionX = 0;
        }
        if (Math.abs(motionY) < minimum){
            motionY = 0;
        }
        if (Math.abs(motionZ) < minimum){
            motionZ = 0;
        }

        int lastX = x;
        int lastY = y;
        int lastZ = z;


        BlockPos[] list = new BlockPos[numBlocks];
        list[0] = new BlockPos(x, y, z);
        int count = 1;
        while (count < numBlocks){
            curX += motionX;
            curY += motionY;
            curZ += motionZ;

            //check for deltas
            if ((int)Math.round(curX) != lastX || (int)Math.round(curY) != lastY || (int)Math.round(curZ) != lastZ){
                lastX = (int)Math.round(curX);
                lastY = (int)Math.round(curY);
                lastZ = (int)Math.round(curZ);
                list[count++] = new BlockPos(lastX, lastY, lastZ);
            }
        }
        return list;
    }

    public static Entity[] GetEntitiesInAngleNearEntity(World world, LivingEntity source, int degrees, int radius, Class<? extends Entity> filterClass, boolean includeSource){

        //argument verification
        if (degrees > 360){
            degrees = 360;
        }
        if (degrees < 5){
            return new Entity[0];
        }

        //get all entities within distance
        AxisAlignedBB boundingBox = new AxisAlignedBB((double)source.getPosition().getX() - radius, source.getPosition().getY() - radius, (double)source.getPosition().getZ() - radius, source.getPosition().getX() + radius, source.getPosition().getY() + radius, source.getPosition().getZ() + radius);
        List<Entity> distanceFilter = world.getEntitiesWithinAABB(filterClass, boundingBox);
        if (!includeSource){
            for (int x = 0; x < distanceFilter.size(); ++x){
                if (distanceFilter.get(x) == source){
                    distanceFilter.remove(x);
                    x--;
                }
            }
        }

        ArrayList<Entity> angleFilter = new ArrayList<>();

        //get the angle that the player is facing and normalize it
        double playerAngle = NormalizeRotation(source.rotationYaw);

        //calculate the min and max angle that the mob can be within the player's view
        double maxAngle = degrees;

        //filter by angle
        for (Entity curEntity : distanceFilter) {
            //get the current entity for the loop
            if (isInFieldOfVision(curEntity, source, maxAngle, maxAngle) && source.canEntityBeSeen(curEntity)) {
                angleFilter.add(curEntity);
            }
        }

        Entity[] array = new Entity[angleFilter.size()];

        for (int t = 0; t < angleFilter.size(); t++){
            array[t] = (Entity)angleFilter.get(t);
        }

        return array;
    }

    public static boolean isInFieldOfVision(Entity e1, LivingEntity e2, double angleX, double angleY) {
        //save Entity 2's original rotation variables
        double rotationYawPrime = e2.rotationYaw;
        double rotationPitchPrime = e2.rotationPitch;
        //make Entity 2 directly face Entity 1
        e2.lookAt(EntityAnchorArgument.Type.EYES, e1.getPositionVec());
        //switch values of prime rotation variables with current rotation variables
        double f = e2.rotationYaw;
        double f2 = e2.rotationPitch;
        e2.rotationYaw = (float) rotationYawPrime;
        e2.rotationPitch = (float) rotationPitchPrime;
        rotationYawPrime = f;
        rotationPitchPrime = f2;
        //assuming field of vision consists of everything within X degrees from rotationYaw and Y degrees from rotationPitch, check if entity 2's current rotationYaw and rotationPitch within this X and Y range
        double X = angleX;
        double Y = angleY;
        double yawFOVMin = e2.rotationYaw - X;
        double yawFOVMax = e2.rotationYaw + X;
        double pitchFOVMin = e2.rotationPitch - Y;
        double pitchFOVMax = e2.rotationPitch + Y;
        boolean flag1 = (yawFOVMin < 0F && (rotationYawPrime >= yawFOVMin + 360F || rotationYawPrime <= yawFOVMax)) || (yawFOVMax >= 360F && (rotationYawPrime <= yawFOVMax - 360F || rotationYawPrime >= yawFOVMin)) || (yawFOVMax < 360F && yawFOVMin >= 0F && rotationYawPrime <= yawFOVMax && rotationYawPrime >= yawFOVMin);
        boolean flag2 = (pitchFOVMin <= -180F && (rotationPitchPrime >= pitchFOVMin + 360F || rotationPitchPrime <= pitchFOVMax)) || (pitchFOVMax > 180F && (rotationPitchPrime <= pitchFOVMax - 360F || rotationPitchPrime >= pitchFOVMin)) || (pitchFOVMax < 180F && pitchFOVMin >= -180F && rotationPitchPrime <= pitchFOVMax && rotationPitchPrime >= pitchFOVMin);
        if (flag1 && flag2 && e2.canEntityBeSeen(e1))
            return true;
        else return false;
    }

    public static double NormalizeRotation(double yawValue){
        if (yawValue < 0){
            while (yawValue < 0){
                yawValue += 360;
            }
        }else if (yawValue > 359){
            while (yawValue > 359){
                yawValue -= 360;
            }
        }
        return yawValue;
    }

    public static SmartVector GetMovementVectorBetweenEntities(Entity from, Entity to){
        SmartVector fromPosition = new SmartVector(from.getPosX(), from.getPosY(), from.getPosZ());
        SmartVector toPosition = new SmartVector(to.getPosX(), to.getPosY(), to.getPosZ());
        SmartVector delta = fromPosition.sub(toPosition);
        delta.normalize();
        return delta;
    }

    public static SmartVector GetMovementVectorBetweenPoints(SmartVector from, SmartVector to){
        SmartVector delta = from.sub(to);
        delta.normalize();
        return delta;
    }

    public static Entity getPointedEntity(World world, LivingEntity looker, double range, double collideRadius){
        Vector3d actualLook = looker.getEyePosition(1.0f).add(looker.getLook(0).x * range, looker.getLook(0).y * range, looker.getLook(0).z * range);
        AxisAlignedBB axisalignedbb = looker.getBoundingBox().expand(looker.getLook(0).scale(range)).grow(collideRadius, collideRadius, collideRadius);
        EntityRayTraceResult res = rayTraceEntities(looker, looker.getEyePosition(0), actualLook, axisalignedbb, Entity::canBeCollidedWith, range);
        return res!= null ? res.getEntity() : null;
    }

    // I think this is just replaced in modern versions with a RayTraceResult, see right above and way at the bottom @rayTraceEntities from ProjectileHelper which is for some reason client side only??? Left for posterity
    /*public static Entity getPointedEntity(World world, LivingEntity entityplayer, double range, double collideRadius, boolean nonCollide){
        Vector3d vec3d = new Vector3d(entityplayer.getPosX(), entityplayer.getPosY() + entityplayer.getEyeHeight(), entityplayer.getPosZ());
        Vector3d vec3d1 = entityplayer.getLookVec();
        Vector3d vec3d2 = vec3d.add(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range);
//        List list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getBoundingBox().addCoord(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range).expand(collideRadius, collideRadius, collideRadius)); // TODO: This one might need fixing
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entityplayer, entityplayer.getBoundingBox().grow(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range).expand(collideRadius, collideRadius, collideRadius));

        double d2 = 0.0D;
        for (Entity entity : list) {
            RayTraceResult res = world.rayTraceBlocks(new RayTraceContext(entityplayer.getPositionVec(), entity.getPositionVec(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, entityplayer));

            if (((entity.canBeCollidedWith()) || ((nonCollide)) && res != null && res.getType() == RayTraceResult.Type.BLOCK)) {
                double f2 = Math.max(0.8F, entity.getCollisionBorderSize());
                AxisAlignedBB axisalignedbb = entity.getBoundingBox().expand(f2, f2, f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                if (axisalignedbb.isVecInside(vec3d)) {
                    if ((0.0D < d2) || (d2 == 0.0D)) {
                        pointedEntity = entity;
                        d2 = 0.0D;
                    }

                } else if (movingobjectposition != null) {
                    double d3 = vec3d.distanceTo(movingobjectposition.hitVec);
                    if ((d3 < d2) || (d2 == 0.0D)) {
                        pointedEntity = entity;
                        d2 = d3;
                    }
                }
                return entity;
            }
        }
        return null;
    }*/

    public static Vector3d extrapolateEntityLook(World par1World, LivingEntity entity, double range){
        double var4 = 1.0F;
        double var5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * var4;
        double var6 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * var4;
        double var7 = entity.prevPosX + (entity.getPosX() - entity.prevPosX) * var4;
        double var9 = entity.prevPosY + (entity.getPosY() - entity.prevPosY) * var4 + 1.6D - entity.getYOffset();
        double var11 = entity.prevPosZ + (entity.getPosZ() - entity.prevPosZ) * var4;
        Vector3d var13 = new Vector3d(var7, var9, var11);
        double var14 = MathHelper.cos((float) (-var6 * 0.017453292F - Math.PI)); // TODO: Figure out what these magic numbers mean
        double var15 = MathHelper.sin((float) (-var6 * 0.017453292F - Math.PI));
        double var16 = -MathHelper.cos((float) (-var5 * 0.017453292F));
        double var17 = MathHelper.sin((float) (-var5 * 0.017453292F));
        double var18 = var15 * var16;
        double var20 = var14 * var16;

        return var13.add(var18 * range, var17 * range, var20 * range);
    }

    public static SmartVector getLook(Entity source, double f){
        double var2;
        double var3;
        double var4;
        double var5;

        if (f == 1.0F){
            var2 = MathHelper.cos((float) (-source.rotationYaw * 0.017453292F - Math.PI));
            var3 = MathHelper.sin((float) (-source.rotationYaw * 0.017453292F - Math.PI));
            var4 = -MathHelper.cos(-source.rotationPitch * 0.017453292F);
            var5 = MathHelper.sin(-source.rotationPitch * 0.017453292F);
            return new SmartVector(var3 * var4, var5, var2 * var4);
        }else{
            var2 = source.prevRotationPitch + (source.rotationPitch - source.prevRotationPitch) * f;
            var3 = source.prevRotationYaw + (source.rotationYaw - source.prevRotationYaw) * f;
            var4 = MathHelper.cos((float) (-var3 * 0.017453292F - Math.PI));
            var5 = MathHelper.sin((float) (-var3 * 0.017453292F - Math.PI));
            double var6 = -MathHelper.cos((float) (-var2 * 0.017453292F));
            double var7 = MathHelper.sin((float) (-var2 * 0.017453292F));
            return new SmartVector(var5 * var6, var7, var4 * var6);
        }
    }

    public static int getDistanceToGround(LivingEntity ent, World world){
        int yCoord = (int)(ent.getPosY());
        int distance = 0;

        while (distance < 20){ // TODO: If this isn't working properly revert to Mithion's OR-less version, I'm probably missing some clever reasoning
            if (world.isAirBlock(new BlockPos((int) Math.floor(ent.getPosX()), yCoord, (int) Math.floor(ent.getPosZ())))
                    || world.isAirBlock(new BlockPos((int) Math.ceil(ent.getPosX()), yCoord, (int) Math.floor(ent.getPosZ())))
                    || world.isAirBlock(new BlockPos((int) Math.floor(ent.getPosX()), yCoord, (int) Math.ceil(ent.getPosZ())))
                    || world.isAirBlock(new BlockPos((int) Math.ceil(ent.getPosX()), yCoord, (int) Math.ceil(ent.getPosZ())))) {
                break;
            }
            distance++;
            yCoord--;
        }

        return distance;
    }

    public static double[] colorIntToFloats(int color){
        double[] colors = new double[3];
        colors[0] = ((color >> 16) & 0xFF) / 255.0F;
        colors[1] = ((color >> 8) & 0xFF) / 255.0F;
        colors[2] = ((color) & 0xFF) / 255.0F;

        return colors;
    }

    public static int colorDoublesToInt(double r, double g, double b) {
        return ((int)(r * 255) << 16) + ((int)(g * 255) << 8) + ((int)(b * 255));
    }

    // Someone's used to JS? Might not need these
    public static int[] push(int[] original, int value){
        int[] newArr = new int[original.length + 1];
        for (int i = 0; i < original.length; ++i)
            newArr[i] = original[i];
        newArr[newArr.length - 1] = value;
        return newArr;
    }

    public static int[] splice(int[] arr, int index){
        if (arr.length <= 1)
            return arr;
        int[] newArr = new int[arr.length - 1];
        int count = 0;
        for (int i = 0; i < arr.length; ++i){
            if (i == index)
                continue;
            newArr[count++] = arr[i];
        }
        return newArr;
    }

    // Why the heck does Minecraft only have projectile ray tracing client side?? I sure don't know. Here's a copy.
    public static EntityRayTraceResult rayTraceEntities(Entity shooter, Vector3d startVec, Vector3d endVec, AxisAlignedBB boundingBox, Predicate<Entity> filter, double distance) {
        World world = shooter.world;
        double d0 = distance;
        Entity entity = null;
        Vector3d vector3d = null;

        for(Entity entity1 : world.getEntitiesInAABBexcluding(shooter, boundingBox, filter)) {
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)entity1.getCollisionBorderSize());
            Optional<Vector3d> optional = axisalignedbb.rayTrace(startVec, endVec);
            if (axisalignedbb.contains(startVec)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vector3d = optional.orElse(startVec);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vector3d vector3d1 = optional.get();
                double d1 = startVec.squareDistanceTo(vector3d1);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getLowestRidingEntity() == shooter.getLowestRidingEntity() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vector3d = vector3d1;
                        }
                    } else {
                        entity = entity1;
                        vector3d = vector3d1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityRayTraceResult(entity, vector3d);
    }

}
