import os

def process_obj_file(path):
    try:
        with open(path, "r", encoding="utf-8") as f:
            lines = f.readlines()

        new_lines = []
        for line in lines:
            stripped = line.lstrip()
            # ❌ eliminar solo comentarios
            if stripped.startswith("#"):
                continue
            new_lines.append(line)

        if new_lines != lines:
            with open(path, "w", encoding="utf-8") as f:
                f.writelines(new_lines)
            print(f"✔ Modificado: {path}")

    except Exception as e:
        print(f"✖ Error en {path}: {e}")

def process_directory(root):
    for root_dir, _, files in os.walk(root):
        for file in files:
            if file.lower().endswith((".obj", ".mtl")):
                process_obj_file(os.path.join(root_dir, file))

if __name__ == "__main__":
    folder = input("Ruta a la carpeta de archivos OBJ: ").strip()
    process_directory(folder)
    print("✅ Proceso completado")
