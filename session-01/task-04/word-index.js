const LINES_PRE_PAGE = 45;
const STOP_FREQUENCY_LIMIT = 100;

const fs = require('fs');

const filePath = process.argv[2];

function lineIndexToPage(lineIndex) {
    return Math.floor(lineIndex / LINES_PRE_PAGE) + 1;
}

try {
    const data = fs.readFileSync(filePath, 'utf8');

    const lines = data.split('\n');

    const wordsToOccurrences = new Map();
    for (let i = 0; i < lines.length; i++) {
        const page = lineIndexToPage(i);

        const words = lines[i].split(' ');

        for (const word of words) {
            if (word) {
                const occurrences = wordsToOccurrences.get(word) || [];

                if (!occurrences.includes(page)) {
                    occurrences.push(page);
                }

                wordsToOccurrences.set(word, occurrences);
            }
        }
    }

    const sortedKeys = Array.from(wordsToOccurrences.keys()).sort();

    for (const key of sortedKeys) {
        const occurrences = wordsToOccurrences.get(key);

        if (occurrences.length <= STOP_FREQUENCY_LIMIT) {
            console.log(`${key} ${occurrences.join(', ')}`);
        }
    }

} catch (err) {
    console.error(err);
}

export {lineIndexToPage}
