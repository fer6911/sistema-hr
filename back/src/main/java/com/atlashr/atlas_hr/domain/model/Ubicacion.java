package com.atlashr.atlas_hr.domain.model;

public class Ubicacion {

    private final String latitud;
    private final String longitud;
    private final String displayName;

    private Ubicacion(Builder builder) {
        this.latitud = builder.latitud;
        this.longitud = builder.longitud;
        this.displayName = builder.displayName;
    }

    public String getLatitud() { return latitud; }
    public String getLongitud() { return longitud; }
    public String getDisplayName() { return displayName; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String latitud;
        private String longitud;
        private String displayName;

        public Builder latitud(String latitud) { this.latitud = latitud; return this; }
        public Builder longitud(String longitud) { this.longitud = longitud; return this; }
        public Builder displayName(String displayName) { this.displayName = displayName; return this; }
        public Ubicacion build() { return new Ubicacion(this); }
    }
}
