#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float HealthPercentage;
uniform vec4 ColorModulator;

in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 colour = texture(Sampler0, texCoord0);
    float maskValue = texture(Sampler1, texCoord0).r;
    if (HealthPercentage == 0.0) {
        discard;
    }
    if (HealthPercentage >= maskValue) {
        fragColor = colour;
    } else {
        discard;
    }
}