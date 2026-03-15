from PIL import Image
import math

SIZE = 17
CENTER = SIZE // 2
MAX_DIST = 8  # radio máximo

img = Image.new("RGBA", (SIZE, SIZE))
pixels = img.load()

for y in range(SIZE):
    for x in range(SIZE):
        dx = x - CENTER
        dy = y - CENTER
        dist = math.sqrt(dx*dx + dy*dy)

        if dist > MAX_DIST:
            pixels[x, y] = (0,0,0,0)
            continue

        t = dist / MAX_DIST  # 0 centro, 1 borde

        # color gradiente amarillo -> naranja
        r = 255
        g = int(255 - (115 * t))   # 255 -> 140
        b = int(0)

        # alpha cae en los últimos 8 px
        alpha = int(255 * (1 - t))

        pixels[x, y] = (r, g, b, alpha)

img.save("bright_dot.png")
