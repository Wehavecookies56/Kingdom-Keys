#version 150

uniform sampler2D FillTex;
uniform sampler2D MaskTex;

uniform float Fill;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    vec4 color = texture(FillTex, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    float maskAlpha = texture(MaskTex, texCoord0).a;

    if (texCoord0.x > Fill) {
        fragColor = vec4(0.0, 0.0, 0.0, 0.0);
    } else {
        fragColor = vec4(color.rgb, color.a * maskAlpha);
    }
}