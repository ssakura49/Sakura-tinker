#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float Time;
uniform vec2 ScreenSize;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord1;
in vec4 normal;
in vec4 texProj0;

out vec4 fragColor;
uint hash21( uvec2 p )
{
    p *= uvec2(73333,7777);
    p ^= (uvec2(3333777777)>>(p>>28));
    uint n = p.x*p.y;
    return n^(n>>15);
}

float hash( uvec2 p )
{
    // we only need the top 24 bits to be good really
    uint h = hash21( p );

    // straight to float, see https://iquilezles.org/articles/sfrand/
    // return uintBitsToFloat((h>>9)|0x3f800000u)-1.0;

    return float(h)*(1.0/float(0xffffffffU));
}
void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    uvec2 p = uvec2(texCoord0 * ScreenSize * 19800.) + 1980U *1080U*uint(Time);

    float f = hash(p);
    float m = color.a;
    fragColor = linear_fog(vec4(color.rgb*(1.-m+f*m), color.a), vertexDistance, FogStart, FogEnd, FogColor);
}
