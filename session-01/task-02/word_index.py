#!/usr/bin/env python

# Program Description:
#
# Word Index is a program that takes a plain text file as input and
# outputs all the words contained in it
# sorted alphabetically along with the page numbers on which they occur.
# The program assumes that a page is a
# sequence of 45 lines, each line has max 80 characters, and there is no
# hyphenation. Additionally, Word Index
# must ignore all words that occur more than 100 times.

####################################
# Free Flow, a.k.a. No Style
####################################

import sys, string

# Global "constants"
LINES_PER_PAGE = 45
MAX_SIZE_LINE = 80
STOP_FREQUENCY_LIMIT = 100


def lineIndexToPage(lineIndex):
    return lineIndex / LINES_PER_PAGE + 1


# Defining a main method makes testing easier
def main(file_path):
    file = open(file_path)

    wordsToOccurrences = {}
    lineIndex = 0
    for line in file:
        line = line.replace('\n', '')
        page = lineIndexToPage(lineIndex)
        words = line.split(' ')

        for word in words:
            if word:
                if word in wordsToOccurrences:
                    occurrences = wordsToOccurrences[word]
                else:
                    occurrences = []

                if page not in occurrences:
                    occurrences.append(page)

                wordsToOccurrences[word] = occurrences

    words = sorted(wordsToOccurrences.keys())

    for word in words:
        occurrences = wordsToOccurrences[word]

        if len(occurrences) <= STOP_FREQUENCY_LIMIT:
            print(word, wordsToOccurrences[word])



if __name__ == "__main__":
    main(sys.argv[1])
