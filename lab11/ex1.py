import asyncio

# functia de calcul
async def suma_gauss(n):
    await asyncio.sleep(0.1)  # simuleaza lucru async - fara asta face worker 1 tot
    return n * (n + 1) // 2


# worker
async def worker(nume, queue):
    while True:
        n = await queue.get()

        if n is None:  # semnal de oprire
            queue.task_done()
            break

        rezultat = await suma_gauss(n)
        print(f"{nume}: S({n}) = {rezultat}")

        queue.task_done()


async def main():
    queue = asyncio.Queue()

    # valori de test
    valori_n = [10, 100, 1000, 50]

    # punem valorile in coada
    for n in valori_n:
        await queue.put(n)

    # cream 4 corutine (workers)
    workers = []
    for i in range(4):
        w = asyncio.create_task(worker(f"Worker-{i+1}", queue))
        workers.append(w)

    # procesare elemente
    await queue.join()

    # workers stop
    for _ in range(4):
        await queue.put(None)

    await asyncio.gather(*workers)


asyncio.run(main())