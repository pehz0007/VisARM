/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class EditorTheme {
    private final String name;
    private String bkgColour;
    private String normHighColour;
    private String texecHighColour;
    private String fexecHighColour;
    private String destHighColour;
    private String linkHighColour;
    private String hoverHighColour;

    public EditorTheme(String name, String bkgColour, String normHighColour, String texecHighColour, String fexecHighColour, String destHighColour, String linkHighColour, String hoverHighColour) {
        this.name = name;
        this.bkgColour = bkgColour;
        this.normHighColour = normHighColour;
        this.texecHighColour = texecHighColour;
        this.fexecHighColour = fexecHighColour;
        this.destHighColour = destHighColour;
        this.linkHighColour = linkHighColour;
        this.hoverHighColour = hoverHighColour;
    }

    public String getName() {
        return this.name;
    }

    public String getBkgColour() {
        return this.bkgColour;
    }

    public String getNormHighColour() {
        return this.normHighColour;
    }

    public String getTexecHighColour() {
        return this.texecHighColour;
    }

    public String getFexecHighColour() {
        return this.fexecHighColour;
    }

    public String getDestHighColour() {
        return this.destHighColour;
    }

    public String getLinkHighColour() {
        return this.linkHighColour;
    }

    public String getHoverHighColour() {
        return this.hoverHighColour;
    }
}

