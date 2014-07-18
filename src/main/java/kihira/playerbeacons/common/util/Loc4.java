package kihira.playerbeacons.common.util;

public class Loc4 {

    private int dimID;
    private double posX;
    private double posY;
    private double posZ;

    public Loc4(int dimID, double posX, double posY, double posZ) {
        this.dimID = dimID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public int getDimID() {
        return this.dimID;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loc4 loc4 = (Loc4) o;

        return dimID == loc4.dimID && Double.compare(loc4.posX, posX) == 0 && Double.compare(loc4.posY, posY) == 0 &&
                Double.compare(loc4.posZ, posZ) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = dimID;
        temp = Double.doubleToLongBits(posX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(posZ);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
