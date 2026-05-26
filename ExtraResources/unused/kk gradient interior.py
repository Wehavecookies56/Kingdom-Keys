from PIL import Image
import math

# ===========================
# CONFIGURATION
# ===========================

WIDTH  = 1024
HEIGHT = 256

RADIUS = 120
THICKNESS = 40
ARC_DEGREES = 270

# Radial gradient colors (inner → outer)  ---- NOW RGB
INNER_COLOR = (255, 255, 255)    # ejemplo verde puro
OUTER_COLOR = (150, 215, 40)   # ejemplo verde-amarillento

# Arc center at upper-right
ARC_CENTER = (WIDTH - RADIUS, RADIUS)

inner_r = RADIUS - THICKNESS
outer_r = RADIUS

# Straight bar same thickness
RECT_HEIGHT = THICKNESS
RECT_TOP = HEIGHT - RECT_HEIGHT
RECT_BOT = HEIGHT

# Compute union X
angle_rad = math.radians(ARC_DEGREES)
union_dx = math.cos(angle_rad) * inner_r
union_dy = math.sin(angle_rad) * inner_r

UNION_X = int(ARC_CENTER[0] + union_dx)
RECT_START_X = UNION_X
RECT_END_X = 0

# ===========================
# IMAGE (RGBA)
# ===========================
img = Image.new("RGBA", (WIDTH, HEIGHT), (0, 0, 0, 0))
px = img.load()

cx, cy = ARC_CENTER

def lerp_color(inner, outer, t):
    """Interpolate between 2 RGB tuples."""
    return (
        int(inner[0] + t * (outer[0] - inner[0])),
        int(inner[1] + t * (outer[1] - inner[1])),
        int(inner[2] + t * (outer[2] - inner[2])),
    )

for y in range(HEIGHT):
    for x in range(WIDTH):

        dx = x - cx
        dy = y - cy
        dist = math.hypot(dx, dy)

        # ---------------------------
        # 1) ARC — PURE RADIAL GRADIENT
        # ---------------------------
        if inner_r <= dist <= outer_r:

            dy_flip = -dy
            dx_flip = -dx

            angle = math.degrees(math.atan2(dy_flip, dx_flip))
            if angle < 0:
                angle += 360

            if 0 <= angle <= ARC_DEGREES:

                t = (dist - inner_r) / (outer_r - inner_r)
                color = lerp_color(INNER_COLOR, OUTER_COLOR, t)

                px[x, y] = (*color, 255)
                continue

        # ---------------------------
        # 2) STRAIGHT BAR — SAME RADIAL FEEL
        # ---------------------------
        if RECT_TOP <= y < RECT_BOT and RECT_END_X <= x <= RECT_START_X:

            bar_pos = RECT_BOT - y         # 0→top, THICKNESS→bottom
            dist_t = bar_pos / THICKNESS   # 0→inner, 1→outer

            color = lerp_color(INNER_COLOR, OUTER_COLOR, dist_t)

            px[x, y] = (*color, 255)
            continue

        # Transparent pixel
        px[x, y] = (0, 0, 0, 0)

img.save("radial_rgb_bar.png")
print("Generated: radial_rgb_bar.png")
