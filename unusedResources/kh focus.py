from PIL import Image
import math

# ===========================
# CONFIG
# ===========================

SCALE = 2

WIDTH  = 256 * SCALE
HEIGHT = 256 * SCALE
RADIUS = 100 * SCALE
THICKNESS = 150 * SCALE
ARC_DEGREES = 270     # arc length (90° gradient)


# Direction where the BLACK part starts:
# "left", "right", "up", "down"
BLACK_DIRECTION = "left"

# ===========================
# INTERNAL SETTINGS
# ===========================

direction_offsets = {
    "right":   0,
    "up":      90,
    "left":    180,
    "down":    270
}

ANGLE_OFFSET = direction_offsets.get(BLACK_DIRECTION, 180)

CX = WIDTH // 2
CY = HEIGHT // 2

inner_r = RADIUS - THICKNESS
outer_r = RADIUS

# Create transparent image (RGBA)
img = Image.new("RGBA", (WIDTH, HEIGHT), (0, 0, 0, 0))
px = img.load()

# ===========================
# RENDER
# ===========================

for y in range(HEIGHT):
    for x in range(WIDTH):

        dx = x - CX
        dy = CY - y
        dist = math.hypot(dx, dy)

        # Only render pixels within the ring thickness
        if not (inner_r <= dist <= outer_r):
            continue  # remain fully transparent

        # Compute angle in standard math orientation
        angle = math.degrees(math.atan2(dy, dx))
        if angle < 0:
            angle += 360

        # Rotate the black–starting direction
        angle = (angle - ANGLE_OFFSET) % 360

        # Ignore pixels outside the arc sweep
        if angle > ARC_DEGREES:
            continue

        # Convert angle to grayscale (0 = black, ARC_DEGREES = white)
        t = angle / ARC_DEGREES
        intensity = int(t * 255)

        # Write pixel (grayscale, fully opaque)
        px[x, y] = (intensity, intensity, intensity, 255)

# ===========================
# SAVE
# ===========================

img.save("arc_gradient_transparent.png")
print("Generated: arc_gradient_transparent.png")
