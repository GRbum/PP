# file_executor.py
import subprocess
import tempfile
import os
import sys

# ================== CHAIN OF RESPONSIBILITY ==================
class Handler:
    def __init__(self):
        self.next = None

    def set_next(self, handler):
        self.next = handler
        return handler

    def handle(self, content):
        if self.next:
            return self.next.handle(content)
        return None


class PythonHandler(Handler):
    def handle(self, content):
        if "def " in content or "import " in content:
            return "python"
        return super().handle(content)


class JavaHandler(Handler):
    def handle(self, content):
        if "public class" in content:
            return "java"
        return super().handle(content)


class BashHandler(Handler):
    def handle(self, content):
        if "#!/bin/bash" in content or "echo " in content:
            return "bash"
        return super().handle(content)


class KotlinHandler(Handler):
    def handle(self, content):
        if "fun main" in content:
            return "kotlin"
        return super().handle(content)


# ================== COMMAND PATTERN ==================
class Command:
    def execute(self, content):
        pass


class PythonCommand(Command):
    def execute(self, content):
        with tempfile.NamedTemporaryFile(delete=False, suffix=".py") as f:
            f.write(content.encode())
            filename = f.name

        result = subprocess.run(["python", filename], capture_output=True, text=True)
        os.unlink(filename)
        return result.stdout


class BashCommand(Command):
    def execute(self, content):
        with tempfile.NamedTemporaryFile(delete=False, suffix=".sh") as f:
            f.write(content.encode())
            filename = f.name

        result = subprocess.run(["bash", filename], capture_output=True, text=True)
        os.unlink(filename)
        return result.stdout


class JavaCommand(Command):
    def execute(self, content):
        filename = "Main.java"
        with open(filename, "w") as f:
            f.write(content)

        subprocess.run(["javac", filename])
        result = subprocess.run(["java", "Main"], capture_output=True, text=True)

        os.remove(filename)
        os.remove("Main.class")
        return result.stdout


class KotlinCommand(Command):
    def execute(self, content):
        filename = "Main.kt"
        with open(filename, "w") as f:
            f.write(content)

        subprocess.run(["kotlinc", filename, "-include-runtime", "-d", "main.jar"])
        result = subprocess.run(["java", "-jar", "main.jar"], capture_output=True, text=True)

        os.remove(filename)
        os.remove("main.jar")
        return result.stdout


# ================== FACTORY ==================
def get_command(lang):
    if lang == "python":
        return PythonCommand()
    elif lang == "bash":
        return BashCommand()
    elif lang == "java":
        return JavaCommand()
    elif lang == "kotlin":
        return KotlinCommand()
    return None


# ================== MAIN ==================
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Utilizare: python file_executor.py <fisier_fara_extensie>")
        sys.exit(1)

    filepath = sys.argv[1]

    if not os.path.exists(filepath):
        print("Fisierul nu exista!")
        sys.exit(1)

    # citim continutul fisierului
    with open(filepath, "r", encoding="utf-8") as f:
        content = f.read()

    # chain
    chain = PythonHandler()
    chain.set_next(JavaHandler()).set_next(BashHandler()).set_next(KotlinHandler())

    lang = chain.handle(content)

    if not lang:
        print("Nu s-a putut determina limbajul.")
        sys.exit(1)

    print(f"Limbaj detectat: {lang}")

    command = get_command(lang)
    if not command:
        print("Nu exista comanda pentru acest limbaj.")
        sys.exit(1)

    output = command.execute(content)

    print("\n=== OUTPUT ===")
    print(output)