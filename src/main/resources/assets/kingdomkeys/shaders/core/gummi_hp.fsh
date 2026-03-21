#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform float HealthPercentage;

uniform float RedStart;
uniform float RedEnd;
uniform vec4 Colour;

in vec2 texCoord0;
out vec4 fragColor;

void main() {
    float maskValue = texture(Sampler1, texCoord0).r;

    if (RedEnd > RedStart) {
        if (maskValue >= RedStart && maskValue <= RedEnd) {
            fragColor = texture(Sampler0, texCoord0) * Colour;
            return;
        }
    }

    if (HealthPercentage <= 0.0) {
        discard;
    }

    if (maskValue <= HealthPercentage) {
        fragColor = texture(Sampler0, texCoord0) * Colour;
    } else {
        discard;
    }
}