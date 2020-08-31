# Fantasy Realms scoring app
Android scoring app for the game [FantasyRealms](https://boardgamegeek.com/boardgame/223040/fantasy-realms)

This app is a POC to learn android/kotlin.
Tested only on a Xiaomi red note 9.

Icon made by Freepik from www.flaticon.com

###Main features
- helper to calculate score
- handle french and english (but any language can be implemented by adding new strings.xml file)
- card recognition through camera using [ML Kit](https://developers.google.com/ml-kit/vision/text-recognition/android). Only the title of the card used for recognition.

###Next steps : 
- display hand size limit (7 or 8). Don't want to limit hand size (for testing)
- add detail screen for each hand item (arrow button already exists)
- increase label size about player/score and when selecting cards for manual rules (title containing rules)
- fix layout about card/suit selection on horizontal mode
- Review main screen (color icon), fix layout on horizontal mode
- In parameters menu, propose a testing screen for quick scanning
- learn how to add UI testing in Android
- code refactoring about scanning rules (to select the best card from title)
