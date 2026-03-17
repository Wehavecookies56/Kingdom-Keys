from PIL import Image
import numpy as np

# ==========================
# CONFIG
# ==========================

INPUT_IMAGE = "hp_outline.png"

THICKNESSES = [14]  # grosores que quieres generar

# ==========================
# LOAD IMAGE
# ==========================

img = Image.open(INPUT_IMAGE).convert("RGBA")
data = np.array(img)

alpha = data[:, :, 3] > 0
h, w = alpha.shape

# ==========================
# DETECT EDGE PIXELS
# ==========================

edges = []

for y in range(h):
    for x in range(w):
        if not alpha[y, x]:
            continue

        for dy in (-1,0,1):
            for dx in (-1,0,1):

                ny = y + dy
                nx = x + dx

                if ny < 0 or ny >= h or nx < 0 or nx >= w:
                    edges.append((y,x))
                    break

                if not alpha[ny, nx]:
                    edges.append((y,x))
                    break
            else:
                continue
            break

edges = np.array(edges)

# ==========================
# DISTANCE FIELD
# ==========================

dist = np.full((h,w), np.inf)

for y in range(h):
    for x in range(w):

        if not alpha[y,x]:
            continue

        dy = edges[:,0] - y
        dx = edges[:,1] - x

        d = np.sqrt(dx*dx + dy*dy)

        dist[y,x] = d.min()

# ==========================
# GENERATE TEXTURES
# ==========================

for t in THICKNESSES:

    new_data = data.copy()

    mask = dist <= t

    new_data[~mask] = [0,0,0,0]

    out = Image.fromarray(new_data)
    out.save(f"hp_fill_{t}px.png")

print("Done!")
