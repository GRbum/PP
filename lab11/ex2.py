import subprocess

comanda = input("Introdu comanda: ")

# split la comenzi dintre pipe
comenzi = [cmd.strip() for cmd in comanda.split("|")]

procese = []
prev_stdout = None

#bucla principala - trece prin fiecare comanda
#stdin - input ul la comanda
#stdout - output ul la comanda
for i, cmd in enumerate(comenzi):
    if i == 0:
        p = subprocess.Popen(
            ["bash", "-c", cmd],
            stdout=subprocess.PIPE
        )
    else:
        p = subprocess.Popen(
            ["bash", "-c", cmd],
            stdin=prev_stdout,
            stdout=subprocess.PIPE
        )
        prev_stdout.close()

    prev_stdout = p.stdout
    #pun ultimul proces
    procese.append(p)

output, _ = procese[-1].communicate()
print(output.decode())