import re
from pprint import pprint

import more_itertools


TEXT = """
Le-am scris cu unghia pe tencuială
Pe un părete de firidă goală,
Pe întuneric, în singurătate,
Cu puterile neajutate
Nici de taurul, nici de leul, nici de vulturul
Care au lucrat împrejurul
Lui Luca, lui Marcu și lui Ioan.
"""


extract_words = lambda text: re.findall(r"[A-Za-z]+", text.lower())
map_word = lambda word: (word[0], word)


def sort_words_by_first_letter(text):
    mapped_pairs = map(map_word, extract_words(text))
    grouped_words = more_itertools.map_reduce(
        mapped_pairs,
        lambda pair: pair[0],
        lambda pair: pair[1],
        lambda words: sorted(words),
    )

    return [(letter, grouped_words[letter]) for letter in sorted(grouped_words)]


if __name__ == "__main__":
    pprint(sort_words_by_first_letter(TEXT))
