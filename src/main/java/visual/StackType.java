/*
 * Decompiled with CFR 0.152.
 */
package visual;

public enum StackType {
    FD,
    FA,
    ED,
    EA;


    public static StackType fromString(String s, boolean store) {
        switch (s) {
            case "FD": {
                return FD;
            }
            case "FA": {
                return FA;
            }
            case "ED": {
                return ED;
            }
            case "EA": {
                return EA;
            }
            case "DB": {
                return store ? FD : EA;
            }
            case "IB": {
                return store ? FA : ED;
            }
            case "DA": {
                return store ? ED : FA;
            }
            case "IA": {
                return store ? EA : FD;
            }
        }
        return FD;
    }
}

