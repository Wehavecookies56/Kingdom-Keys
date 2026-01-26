import json
import os
import re
from decimal import Decimal, ROUND_HALF_UP

MAX_DECIMALS = 3
INLINE_KEYS = {"rotation", "translation", "scale"}

def round_number(value):
    if isinstance(value, float):
        d = Decimal(str(value)).quantize(
            Decimal("0.001"),
            rounding=ROUND_HALF_UP
        )
        # Mantener hasta 3 decimales (no forzar 2)
        return float(d)
    return value

def process_json(obj):
    if isinstance(obj, dict):
        new_obj = {}
        for k, v in obj.items():
            # ❌ eliminar visibility
            if k == "visibility":
                continue
            new_obj[k] = process_json(v)
        return new_obj
    elif isinstance(obj, list):
        return [process_json(v) for v in obj]
    else:
        return round_number(obj)

def inline_transform_arrays(text):
    for key in INLINE_KEYS:
        pattern = rf'"{key}":\s*\[\s*([^\]]+?)\s*\]'
        def repl(match):
            values = re.sub(r"\s+", " ", match.group(1)).strip()
            return f'"{key}": [{values}]'
        text = re.sub(pattern, repl, text, flags=re.MULTILINE)
    return text

def process_file(path):
    try:
        with open(path, "r", encoding="utf-8") as f:
            data = json.load(f)

        new_data = process_json(data)

        if new_data != data:
            text = json.dumps(
                new_data,
                ensure_ascii=False,
                indent=4
            )

            text = inline_transform_arrays(text)

            with open(path, "w", encoding="utf-8") as f:
                f.write(text + "\n")

            print(f"✔ Modificado: {path}")

    except Exception as e:
        print(f"✖ Error en {path}: {e}")

def process_directory(root):
    for root_dir, _, files in os.walk(root):
        for file in files:
            if file.endswith(".json"):
                process_file(os.path.join(root_dir, file))

if __name__ == "__main__":
    folder = input("Ruta a la carpeta de modelos JSON: ").strip()
    process_directory(folder)
    print("✅ Proceso completado")
