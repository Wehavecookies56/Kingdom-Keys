from PIL import Image
import math

# ===========================
# CONFIGURATION
# ===========================

WIDTH  = 1024
HEIGHT = 256

RADIUS = 120
THICKNESS = 10
ARC_DEGREES = 270

# Arc center at upper-right area
ARC_CENTER = (WIDTH - RADIUS, RADIUS)

inner_r = RADIUS - THICKNESS
outer_r = RADIUS

# The straight bar must have exactly the same thickness
RECT_HEIGHT = THICKNESS
RECT_TOP = HEIGHT - RECT_HEIGHT
RECT_BOT = HEIGHT

# Compute union point (where arc reaches intensity 128)
angle_rad = math.radians(ARC_DEGREES)
union_dx = math.cos(angle_rad) * inner_r
union_dy = math.sin(angle_rad) * inner_r

UNION_X = int(ARC_CENTER[0] + union_dx)
UNION_Y = int(ARC_CENTER[1] + union_dy)

# Straight bar starts exactly at union X coordinate
RECT_START_X = UNION_X
RECT_END_X = 0

# ===========================
# IMAGE CREATION (TRANSPARENT)
# ===========================

# "LA": 8-bit grayscale + alpha channel
img = Image.new("LA", (WIDTH, HEIGHT), (0, 0))
pixels = img.load()

cx, cy = ARC_CENTER

for y in range(HEIGHT):
    for x in range(WIDTH):

        dx = x - cx
        dy = y - cy
        dist = math.hypot(dx, dy)

        # ---------------------------------------
        # 1) DONUT ARC (horizontal + vertical flip)
        # ---------------------------------------
        if inner_r <= dist <= outer_r:

            dy_flip = -dy
            dx_flip = -dx

            angle = math.degrees(math.atan2(dy_flip, dx_flip))
            if angle < 0:
                angle += 360

            if 0 <= angle <= ARC_DEGREES:
                intensity = int((angle / ARC_DEGREES) * 128)
                pixels[x, y] = (intensity, 255)
                continue

        # ---------------------------------------
        # 2) STRAIGHT BAR (aligned + connected)
        # ---------------------------------------
        if RECT_TOP <= y < RECT_BOT and RECT_END_X <= x <= RECT_START_X:

            t = (RECT_START_X - x) / (RECT_START_X - RECT_END_X)
            intensity = int(128 + t * 127)
            pixels[x, y] = (min(255, intensity), 255)
            continue

        # Transparent background
        pixels[x, y] = (0, 0)

img.save("aligned_bar_transparent.png")
print("Generated: aligned_bar_transparent.png")
