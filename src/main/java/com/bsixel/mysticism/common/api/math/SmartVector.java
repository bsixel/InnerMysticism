package com.bsixel.mysticism.common.api.math;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

/*
 * All credits for this go to @Mithion
 * https://github.com/Mithion/ArsMagica2/blob/master/src/main/java/am2/utility/MathUtilities.java
 */
public class SmartVector {
    public double x;
    public double y;
    public double z;

    public SmartVector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SmartVector(TileEntity tile){
        this.x = (tile.getPos().getX() );
        this.y = (tile.getPos().getY());
        this.z = (tile.getPos().getZ());
    }

    public SmartVector(Vector3d vec){
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public SmartVector(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public SmartVector(SmartVector a, SmartVector b){
        this.x = a.x - b.x;                    // Get the X value of our new vector
        this.y = a.y - b.y;                    // Get the Y value of our new vector
        this.z = a.z - b.z;                    // Get the Z value of our new vector
    }

    public SmartVector(Entity entity){
        this(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }

    public SmartVector add(SmartVector vec){
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public SmartVector sub(SmartVector vec){
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public SmartVector scale(double scale){
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        return this;
    }

    public SmartVector scale(double scalex, double scaley, double scalez){
        this.x *= scalex;
        this.y *= scaley;
        this.z *= scalez;
        return this;
    }

    public SmartVector modulo(double divisor){
        this.x %= divisor;
        this.y %= divisor;
        this.z %= divisor;
        return this;
    }

    public SmartVector normalize(){
        double length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public BlockPos toPos() {
        return new BlockPos(toVec3D());
    }

    public double length(){
        return (double)Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double lengthPow2(){
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public SmartVector copy(){
        return new SmartVector(this.x, this.y, this.z);
    }

    public static SmartVector crossProduct(SmartVector vec1, SmartVector vec2){
        return new SmartVector(vec1.y * vec2.z - vec1.z * vec2.y, vec1.z * vec2.x - vec1.x * vec2.z, vec1.x * vec2.y - vec1.y * vec2.x);
    }

    public static SmartVector xCrossProduct(SmartVector vec){
        return new SmartVector(0.0D, vec.z, -vec.y);
    }

    public static SmartVector zCrossProduct(SmartVector vec){
        return new SmartVector(-vec.y, vec.x, 0.0D);
    }

    public static double dotProduct(SmartVector vec1, SmartVector vec2){
        return (vec1.x * vec2.x) + (vec1.y * vec2.y) + (vec1.z * vec2.z);
    }

    public static double angle(SmartVector vec1, SmartVector vec2){
        return anglePreNorm(vec1.copy().normalize(), vec2.copy().normalize());
    }

    public static double anglePreNorm(SmartVector vec1, SmartVector vec2){
        return (double)Math.acos(dotProduct(vec1, vec2));
    }

    public static SmartVector zero(){
        return new SmartVector(0, 0, 0);
    }

    @Override
    public String toString(){
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    public Vector3d toVec3D(){
        return new Vector3d(this.x, this.y, this.z);
    }

    public static SmartVector getPerpendicular(SmartVector vec){
        if (vec.z == 0.0F){
            return zCrossProduct(vec);
        }
        return xCrossProduct(vec);
    }

    public boolean isZero(){
        return (this.x == 0.0F) && (this.y == 0.0F) && (this.z == 0.0F);
    }

    public boolean isWithinRange(double min, double max){
        return (this.x >= min && x <= max) && (this.y >= min && this.y <= max) && (this.z >= min && this.z <= max);
    }

    public double distanceTo(SmartVector target){
        double var2 = target.x - this.x;
        double var4 = target.y - this.y;
        double var6 = target.z - this.z;
        return MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public double distanceSqTo(SmartVector target){
        double var2 = target.x - this.x;
        double var4 = target.y - this.y;
        double var6 = target.z - this.z;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }


    /**
     * Rounds all values in this Vector to their integral floors
     */
    public void floorToI(){
        this.x = Math.floor(this.x);
        this.y = Math.floor(this.y);
        this.z = Math.floor(this.z);
    }

    /**
     * Rounds all values in this Vector to their integral logical roundings
     */
    public void roundToI(){
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        this.z = Math.round(this.z);
    }

    /**
     * Rounds all values in this Vector to their integral ceilings
     */
    public void ceilToI(){
        this.x = Math.ceil(this.x);
        this.y = Math.ceil(this.y);
        this.z = Math.ceil(this.z);
    }

    /**
     * Writes the vector3 to the specified compound - note that the way this function works, only one vector3 can be written to a compound at a time.
     * Use sub-compounds if you need more!
     */
    public void writeToNBT(CompoundNBT compound){
        compound.putDouble("Vec3_x", x);
        compound.putDouble("Vec3_y", y);
        compound.putDouble("Vec3_z", z);
    }

    public static SmartVector readFromNBT(CompoundNBT compound){
        return new SmartVector(compound.getDouble("Vec3_x"), compound.getDouble("Vec3_y"), compound.getDouble("Vec3_z"));
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof SmartVector){
            SmartVector comp = (SmartVector)obj;
            return (comp.x == this.x && comp.y == this.y && comp.z == this.z);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return (int)(x + y + z);
    }
}
