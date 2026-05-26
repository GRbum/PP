from functools import reduce
import re


DEFAULT_NUMBERS = [1, 4, 6, 7, 13, 17, 22, 53, 59, 61, 75, 97]


is_prime = lambda number: number > 1 and reduce(
    lambda valid, divisor: valid and number % divisor != 0,
    range(2, int(number**0.5) + 1),
    True,
)


class FunctionalAutomaton:
    state0 = staticmethod(lambda numbers: list(filter(is_prime, numbers)))
    state1 = staticmethod(lambda numbers: list(filter(lambda number: number % 2 != 0, numbers)))
    state2 = staticmethod(lambda numbers: list(filter(lambda number: number <= 50, numbers)))
    stop = staticmethod(lambda numbers: (print(f"STOP: {numbers}"), numbers)[1])

    def __init__(self, numbers):
        self.numbers = numbers

    run = lambda self: reduce(
        lambda current_numbers, state: state(current_numbers),
        (self.state0, self.state1, self.state2, self.stop),
        self.numbers,
    )


read_numbers = lambda text: list(map(int, re.findall(r"-?\d+", text)))


if __name__ == "__main__":
    text = input("Numere separate prin spatiu: ").replace("\ufeff", "").strip()
    numbers = read_numbers(text) or DEFAULT_NUMBERS
    FunctionalAutomaton(numbers).run()
