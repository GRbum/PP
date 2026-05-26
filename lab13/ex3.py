from functools import reduce
from itertools import starmap
from operator import add, mul


NUMBERS = [1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8]


def solve(numbers):
    filtered_numbers = list(filter(lambda number: number >= 5, numbers))

    pair_iterator = iter(filtered_numbers)
    pairs = list(zip(pair_iterator, pair_iterator))

    products = list(starmap(mul, pairs))
    total = reduce(add, products, 0)

    return filtered_numbers, pairs, products, total


if __name__ == "__main__":
    filtered_numbers, pairs, products, total = solve(NUMBERS)

    print(f"Eliminarea numerelor < 5: {filtered_numbers}")
    print(f"Gruparea in perechi: {pairs}")
    print(f"Multiplicarea numerelor din perechi: {products}")
    print(f"Sumarea rezultatelor: {total}")
